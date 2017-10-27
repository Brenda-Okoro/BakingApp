package com.example.brenda.bakingapp.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by brenda on 10/24/17.
 */

public class SelectedIngredientPreference {
    public static final String INGREDIENT_PREFERENCE = "ingredient_preference";
    private static final String KEY_INGREDIENT = "ingredient";
    private static final String DEFAULT_MESSAGE = "No Ingredient at the moment";
    private SharedPreferences sharedPreferences;
    static SelectedIngredientPreference INSTANCE;

    private SelectedIngredientPreference(Context context) {
        sharedPreferences = context.getSharedPreferences(INGREDIENT_PREFERENCE, Context.MODE_PRIVATE);

    }

    String getIngredientPreference() {
        return sharedPreferences.getString(KEY_INGREDIENT, DEFAULT_MESSAGE);
    }

    public static void setIngredientPreference(Context context, String ingredient) {
        getInstance(context).setIngredientPreference(ingredient);
    }

    public void setIngredientPreference(String ingredient) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_INGREDIENT, ingredient);
        editor.commit();
    }

    public static String getLastSelectedIngredient(Context context) {
        return getInstance(context).getIngredientPreference();
    }

    private static SelectedIngredientPreference getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SelectedIngredientPreference(context);
        }
        return INSTANCE;
    }
}

