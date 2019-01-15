
package com.example.apoorvdubey.bakeit.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.example.apoorvdubey.bakeit.R;
import com.example.apoorvdubey.bakeit.Utils.Prefs;
import com.example.apoorvdubey.bakeit.service.model.RecipeResponse;
import com.example.apoorvdubey.bakeit.view.activity.RecipeDetailActivity;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId) {
        RecipeResponse recipe = Prefs.loadRecipe(context);
        if (recipe != null) {
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, RecipeDetailActivity.class), 0);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.bake_it_widget);
            views.setTextViewText(R.id.recipe_widget_name_text, recipe.getName());
            views.setOnClickPendingIntent(R.id.recipe_widget_name_text, pendingIntent);
            Intent intent = new Intent(context, AppWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.putExtra(context.getString(R.string.recipe_widget), setNewRecipeObject(context, recipe));
            views.setRemoteAdapter(R.id.recipe_widget_listview, intent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.recipe_widget_listview);
        }
    }

    private static Bundle setNewRecipeObject(Context context, RecipeResponse recipe) {
        RecipeResponse recipeResponse = new RecipeResponse(recipe.getId(), recipe.getName(), recipe.getServings(), recipe.getImage());
        Bundle bundle = new Bundle();
        bundle.putParcelable(context.getString(R.string.recipe_object_widget), recipeResponse);
        return bundle;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}

