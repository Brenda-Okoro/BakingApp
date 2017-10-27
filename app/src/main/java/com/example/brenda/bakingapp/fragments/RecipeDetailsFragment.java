package com.example.brenda.bakingapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.brenda.bakingapp.R;
import com.example.brenda.bakingapp.activities.RecipeDetailsActivity;
import com.example.brenda.bakingapp.activities.RecipeStepsActivity;
import com.example.brenda.bakingapp.adapters.RecipeListDetailsAdapter;
import com.example.brenda.bakingapp.models.Ingredients;
import com.example.brenda.bakingapp.models.Recipes;
import com.example.brenda.bakingapp.models.Steps;

import java.util.ArrayList;

/**
 * Created by brenda on 10/23/17.
 */

public class RecipeDetailsFragment extends Fragment implements RecipeListDetailsAdapter.RecipeStepItemTouchListener {
    Recipes mRecipe;
    private RecyclerView mRecipeListSteps;
    private FragmentDetailsStepSelectedListener listener;
    private static final String KEY_POSITION = "position";
    private static final String KEY_RECIPE = "recipe";
    private LinearLayoutManager linearLayoutManager;

    public interface FragmentDetailsStepSelectedListener {
        void recipeStepSelected(int position);

        boolean isReadToListen();
    }

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.listener = (FragmentDetailsStepSelectedListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments.containsKey(RecipeDetailsActivity.KEY_RECIPE)) {
            mRecipe = arguments.getParcelable(RecipeDetailsActivity.KEY_RECIPE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (linearLayoutManager != null && mRecipe != null) {
            outState.putParcelable(KEY_POSITION, linearLayoutManager.onSaveInstanceState());
            outState.putParcelable(KEY_RECIPE, mRecipe);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        mRecipeListSteps = (RecyclerView) rootView.findViewById(R.id.rv_recipe_list_steps);
        linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());

        if (savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(KEY_RECIPE);
            linearLayoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(KEY_POSITION));
            mRecipeListSteps.setLayoutManager(linearLayoutManager);
            showRecipeDetails();
            return rootView;
        }
        mRecipeListSteps.setLayoutManager(linearLayoutManager);
        showRecipeDetails();
        return rootView;
    }


    public static RecipeDetailsFragment getNewInstance(Recipes recipe) {
        Bundle args = new Bundle();
        args.putParcelable(RecipeDetailsActivity.KEY_RECIPE, recipe);
        RecipeDetailsFragment fragment = new RecipeDetailsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private void showRecipeDetails() {
        ArrayList<Ingredients> ingredients = mRecipe.getIngredients();
        ArrayList<Steps> steps = mRecipe.getSteps();
        Context appContext = getActivity().getApplicationContext();
        RecipeListDetailsAdapter recipeStepsAdapter = new RecipeListDetailsAdapter(appContext, ingredients, steps);
        recipeStepsAdapter.setRecipeStepOnTouchListener(this);
        mRecipeListSteps.setAdapter(recipeStepsAdapter);
    }


    @Override
    public void onRecipeStepItemTouched(int position) {
        boolean isTablet = listener.isReadToListen();
        if (isTablet) {
            listener.recipeStepSelected(position);
        } else {
            Intent intent = new Intent(getActivity(), RecipeStepsActivity.class);
            intent.putExtra(RecipeStepsActivity.KEY_STEPS, mRecipe.getSteps());
            intent.putExtra(RecipeStepsActivity.KEY_POSITION, position);
            startActivity(intent);
        }
    }
}

