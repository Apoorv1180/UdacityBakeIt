package com.example.apoorvdubey.bakeit.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.apoorvdubey.bakeit.R;
import com.example.apoorvdubey.bakeit.service.model.RecipeResponse;

public class Prefs {
    public static final String PREFS_NAME = "prefs";

    public static void saveRecipe(Context context, RecipeResponse recipe) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        prefs.putString(context.getString(R.string.widget_recipe_key), RecipeResponse.toBase64String(recipe));
        prefs.apply();
    }

    public static RecipeResponse loadRecipe(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String recipeBase64 = prefs.getString(context.getString(R.string.widget_recipe_key), "");
        return "".equals(recipeBase64) ? null : RecipeResponse.fromBase64(prefs.getString(context.getString(R.string.widget_recipe_key), ""));
    }
}
