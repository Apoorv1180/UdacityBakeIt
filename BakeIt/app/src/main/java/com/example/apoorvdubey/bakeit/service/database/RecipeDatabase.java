package com.example.apoorvdubey.bakeit.service.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.apoorvdubey.bakeit.Utils.Constant;
import com.example.apoorvdubey.bakeit.service.model.Ingredient;
import com.example.apoorvdubey.bakeit.service.model.RecipeResponse;
import com.example.apoorvdubey.bakeit.service.model.Step;

@Database(entities = {Ingredient.class, RecipeResponse.class, Step.class}, version = 1, exportSchema = false)
public abstract class RecipeDatabase extends RoomDatabase {
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = Constant.getDatabaseName();
    private static RecipeDatabase sInstance;

    public static RecipeDatabase getsInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext()
                        , RecipeDatabase.class
                        , RecipeDatabase.DATABASE_NAME).allowMainThreadQueries()
                        .build();
            }
        }
        return sInstance;
    }

    public abstract RecipeResponseDao recipeResponseDao();

    public abstract IngredientsDao ingredientsDao();

    public abstract StepsDao stepsDao();
}
