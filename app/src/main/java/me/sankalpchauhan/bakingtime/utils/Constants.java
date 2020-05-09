package me.sankalpchauhan.bakingtime.utils;

public class Constants {
    //max number of retries before fail
    public static final int maxLimit = 3;
    // wait for 5 second before retrying network request
    public static final int waitThreshold = 5000;

    public static final String RECIPE_DATA = "recipeData";
    public static final String INGREDIENT_LIST_DATA = "IngredientListData";
    public static final String INGREDIENT_BOTTOM_SHEET_TAG = "ingredientsFragment";
    public static final String STEP_DATA = "stepData";
    public static final String STEP_LIST_DATA = "stepListData";
    public static final String STEP_POSITION = "stepPosition";
    public static final String CURRENT_STEP_POSITION = "currentPosition";
    public static final String CURRENT_CURSOR_CONSTANT = "CurrentCursorPosition";
    public static final int POSITION_UPDATE_CODE = 101;
    public static final String ACTION_BAR_RESTORE = "restoreActionBar";
    public static final String STEP_FRAGMENT_NAME = "stepFragmentName";
    public static final String PLAY_BACK_POSITION = "playBackPosition";
    public static final String PLAY_WHEN_READY = "playWhenReady";
}
