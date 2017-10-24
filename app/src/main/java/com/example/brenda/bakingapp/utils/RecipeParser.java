package com.example.brenda.bakingapp.utils;

import android.util.Log;

import com.example.brenda.bakingapp.models.Ingredients;
import com.example.brenda.bakingapp.models.Recipes;
import com.example.brenda.bakingapp.models.Steps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by brenda on 10/24/17.
 */

public class RecipeParser {
    private static String KEY_ID = "id";
    private static String KEY_NAME = "name";
    private static String KEY_INGREDIENT = "ingredients";
    private static String KEY_STEPS = "steps";
    private static String KEY_SERVINGS = "servings";
    private static String KEY_IMAGE = "image";

    //key for ingredients
    private static String KEY_INGREDIENTS_QUANTITY = "quantity";
    private static String KEY_INGREDIENTS_MEASURE = "measure";
    private static String KEY_INGREDIENTS_INGREDIENT = "ingredient";

    //key for steps
    private static String KEY_STEPS_ID = "id";
    private static String KEY_STEPS_SHORT_DESCRIPTION = "shortDescription";
    private static String KEY_STEPS_DESCRIPTION = "description";
    private static String KEY_STEPS_VIDEO_URL = "videoURL";
    private static String KEY_STEPS_THUMBNAIL_URL = "thumbnailURL";

    private static String LOG_TAG = RecipeParser.class.getSimpleName();

    public static ArrayList<Recipes> parseRecipe(JSONArray jsonArray) {
        JSONObject recipeJSON;
        ArrayList<Recipes> recipeList = new ArrayList<>();


        int id;
        String name;
        ArrayList<Ingredients> ingredients;
        ArrayList<Steps> steps;
        int servings;
        String imageURL;
        Recipes recipe;

        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                recipeJSON = jsonArray.getJSONObject(i);
                id = recipeJSON.getInt(KEY_ID);
                name = recipeJSON.getString(KEY_NAME);
                ingredients = parseIngredient(recipeJSON.getJSONArray(KEY_INGREDIENT));
                steps = parseStep(recipeJSON.getJSONArray(KEY_STEPS));
                servings = recipeJSON.getInt(KEY_SERVINGS);
                imageURL = recipeJSON.getString(KEY_IMAGE);
                recipe = new Recipes(id, name, ingredients, steps, servings, imageURL);
                recipeList.add(recipe);

            } catch (JSONException jsonException) {
                Log.e(LOG_TAG, jsonException.getMessage());
            }
        }

        return  recipeList;

    }

    private static ArrayList<Ingredients> parseIngredient(JSONArray ingredientListJSON) throws JSONException {
        ArrayList<Ingredients> ingredients = new ArrayList<>();
        Ingredients recipeIngredient;
        JSONObject ingredientJSON;

        int quantity;
        String measure;
        String ingredient;

        for (int i = 0; i < ingredientListJSON.length(); i++) {

            ingredientJSON = ingredientListJSON.getJSONObject(i);
            quantity = ingredientJSON.getInt(KEY_INGREDIENTS_QUANTITY);
            measure = ingredientJSON.getString(KEY_INGREDIENTS_MEASURE);
            ingredient = ingredientJSON.getString(KEY_INGREDIENTS_INGREDIENT);
            recipeIngredient = new Ingredients(quantity, measure, ingredient);
            ingredients.add(recipeIngredient);

        }

        return ingredients;

    }

    private static ArrayList<Steps> parseStep(JSONArray stepListJSON) throws JSONException {

        ArrayList<Steps> steps = new ArrayList<>();
        Steps recipeStep;
        JSONObject stepJSON;

        int id;
        String shortDescription;
        String description;
        String videoURL;
        String thumbnailURL;

        for(int i = 0 ; i < stepListJSON.length(); i++){
            stepJSON = stepListJSON.getJSONObject(i);
            id = stepJSON.getInt(KEY_STEPS_ID);
            shortDescription = stepJSON.getString(KEY_STEPS_SHORT_DESCRIPTION);
            description = stepJSON.getString(KEY_STEPS_DESCRIPTION);
            videoURL = stepJSON.getString(KEY_STEPS_VIDEO_URL);
            thumbnailURL = stepJSON.getString(KEY_STEPS_THUMBNAIL_URL);

            recipeStep = new Steps(id, shortDescription, description, videoURL, thumbnailURL);
            steps.add(recipeStep);

        }

        return  steps;
    }
}

