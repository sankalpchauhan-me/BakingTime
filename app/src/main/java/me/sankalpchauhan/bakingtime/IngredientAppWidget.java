package me.sankalpchauhan.bakingtime;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import me.sankalpchauhan.bakingtime.config.DefaultPrefSettings;
import me.sankalpchauhan.bakingtime.view.activity.MainActivity;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget);
        if (DefaultPrefSettings.getInstance().isPreferenceEmpty()) {
            views.setTextViewText(R.id.recipe_name, "It's Baking Time");
            views.setTextViewText(R.id.recipe_ingredients, "Set your ingredient list here by going to \nRecipe->INGREDIENTS->Add To Widget");
        } else {
            views.setTextViewText(R.id.recipe_name, DefaultPrefSettings.getInstance().getRecipeName());
            views.setTextViewText(R.id.recipe_ingredients, DefaultPrefSettings.getInstance().getRecipeIngredients());
        }

        //Open App via Pending Intent
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.recipe_name, pendingIntent);
        views.setOnClickPendingIntent(R.id.recipe_ingredients, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

