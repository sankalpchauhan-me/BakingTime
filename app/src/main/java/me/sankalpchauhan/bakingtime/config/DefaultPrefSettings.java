package me.sankalpchauhan.bakingtime.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import me.sankalpchauhan.bakingtime.service.model.Recipe;
import me.sankalpchauhan.bakingtime.utils.Utility;

public class DefaultPrefSettings {
    private static final String RECIPE_ID = "RECIPE_ID";
    private static final String RECIPE_NAME = "RECIPE_NAME";
    private static final String RECIPE_INGREDIENTS = "RECIPE_INGREDIENTS";
    private static DefaultPrefSettings mInstance = new DefaultPrefSettings();
    private final Object object = new Object();
    private SharedPreferences defaultPref;

    private DefaultPrefSettings() {
    }

    public static DefaultPrefSettings getInstance() {
        return mInstance;
    }

    public static void init(Context context) {
        mInstance.defaultPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setIngredients(Recipe recipe) {
        synchronized (object) {
            SharedPreferences.Editor editor = defaultPref.edit();
            editor.putInt(RECIPE_ID, recipe.getId());
            editor.putString(RECIPE_NAME, recipe.getName());
            editor.putString(RECIPE_INGREDIENTS, Utility.stringFormatter(recipe.getIngredients()));
            editor.apply();
        }
    }

    public Integer getRecipeId() {
        synchronized (object) {
            return defaultPref.getInt(RECIPE_ID, -1);
        }
    }

    public String getRecipeName() {
        synchronized (object) {
            return defaultPref.getString(RECIPE_NAME, "N/A");
        }
    }

    public String getRecipeIngredients() {
        synchronized (object) {
            return defaultPref.getString(RECIPE_INGREDIENTS, "N/A");
        }
    }

    public void clearSharedPrefs() {
        synchronized (object) {
            SharedPreferences.Editor editor = defaultPref.edit();
            editor.remove(RECIPE_ID);
            editor.remove(RECIPE_NAME);
            editor.remove(RECIPE_INGREDIENTS);
            editor.apply();
        }
    }

    public boolean isPreferenceEmpty() {
        return (defaultPref.getString(RECIPE_NAME, "N/A").equals("N/A"));
    }
}
