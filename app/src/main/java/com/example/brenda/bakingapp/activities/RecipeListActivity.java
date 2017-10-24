package com.example.brenda.bakingapp.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.brenda.bakingapp.AppController;
import com.example.brenda.bakingapp.R;
import com.example.brenda.bakingapp.adapters.RecipeListAdapter;
import com.example.brenda.bakingapp.data.SelectedIngredientPreference;
import com.example.brenda.bakingapp.models.Ingredients;
import com.example.brenda.bakingapp.models.Recipes;
import com.example.brenda.bakingapp.utils.NetworkUtils;
import com.example.brenda.bakingapp.utils.RecipeParser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brenda on 10/23/17.
 */

public class RecipeListActivity extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONArray>,RecipeListAdapter.RecipeItemTouchListener {
    private RecyclerView mRecipeListRecyclerView;
    private RecipeListAdapter mRecipeListAdapter;
    private JsonArrayRequest mArrayRequest;
    private ArrayList<Recipes> mRecipeList;

    TextView mErrorMessageTextView;
    ProgressBar mLoadingIndicator;

    private static final String KEY_RECIPES = "recipes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        mRecipeListRecyclerView = (RecyclerView) findViewById(R.id.rv_recipe_list);
        mRecipeListRecyclerView.setHasFixedSize(true);

        mErrorMessageTextView = (TextView) findViewById(R.id.tv_error_message);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);


        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_RECIPES)) {
            mRecipeList = savedInstanceState.getParcelableArrayList(KEY_RECIPES);
            showRecipe();

        } else {
            boolean userIsConnectedToTheInternet = NetworkUtils.isNetworkAvailable(getApplicationContext());

            if(!userIsConnectedToTheInternet) {
                showError(getString(R.string.no_internet_connection_error_message));
            }else {
                makeNetworkRequestForRecipes();
            }
        }

    }

    private void makeNetworkRequestForRecipes() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        String recipeListUrl = NetworkUtils.getRecipeListUrl();

        mArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                recipeListUrl
                , null, this, this
        );

        AppController.getInstance().addToRequestQueue(mArrayRequest);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        boolean dataIsAvailable = mRecipeList != null;
        if (dataIsAvailable) {
            outState.putParcelableArrayList(KEY_RECIPES, mRecipeList);
        }
    }

    private void showError(String errorMessage){
        mLoadingIndicator.setVisibility(View.GONE);
        mRecipeListRecyclerView.setVisibility(View.GONE);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
        mErrorMessageTextView.setText(errorMessage);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mArrayRequest != null) {
            mArrayRequest.cancel();
        }
    }

    @Override
    public void onResponse(JSONArray response) {
        mRecipeList = RecipeParser.parseRecipe(response);
        mLoadingIndicator.setVisibility(View.GONE);
        mErrorMessageTextView.setVisibility(View.GONE);
        mRecipeListRecyclerView.setVisibility(View.VISIBLE);
        showRecipe();


    }

    private void showRecipe() {
        mRecipeListRecyclerView.setLayoutManager(getLayoutManager());
        mRecipeListAdapter = new RecipeListAdapter(getApplicationContext(), mRecipeList);
        mRecipeListAdapter.setRecipeOnTouchListener(this);
        mRecipeListRecyclerView.setAdapter(mRecipeListAdapter);
    }

    private RecyclerView.LayoutManager getLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        boolean isTablet = findViewById(R.id.fl_tablet_container) != null;
        int currentOrientation = getResources().getConfiguration().orientation;
        boolean isLandScape = currentOrientation == Configuration.ORIENTATION_LANDSCAPE;

        if (isTablet || isLandScape) {
            int columnSpan = 2;
            return new GridLayoutManager(getApplicationContext(), columnSpan);
        }
        return  linearLayoutManager;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        String networkError = getString(R.string.network_error_message);
        showError(networkError);
    }


    @Override
    public void onRecipeItemTouched(Recipes touchedRecipe) {
        Intent intent = new Intent(RecipeListActivity.this, RecipeDetailsActivity.class);
        intent.putExtra(RecipeDetailsActivity.KEY_RECIPE, touchedRecipe);
        saveTouchedRecipeIngredient(touchedRecipe.getIngredients());
        startActivity(intent);
    }

    private void saveTouchedRecipeIngredient(List<Ingredients> ingredients){
        String formattedIngredient;
        int quantity;
        String measure;
        String ingredientDetails;
        String finalFormattedString = "";

        for (Ingredients ingredient : ingredients){
            formattedIngredient  = this.getApplicationContext().getString(R.string.bullet);
            quantity = ingredient.getQuantity();
            measure = ingredient.getMeasure();
            ingredientDetails = ingredient.getIngredient();
            formattedIngredient += " " + ingredientDetails + " ("+ quantity +" " + measure + ")";
            finalFormattedString += formattedIngredient+ "\n\n";

        }

        SelectedIngredientPreference.setIngredientPreference(this.getApplicationContext(), finalFormattedString);
    }

    public boolean isSyncFinished(){
        return mRecipeListAdapter != null;
    }
}
