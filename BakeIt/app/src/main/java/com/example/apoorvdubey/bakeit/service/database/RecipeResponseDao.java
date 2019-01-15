package com.example.apoorvdubey.bakeit.service.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.apoorvdubey.bakeit.service.model.RecipeResponse;


import java.util.List;

@Dao
public interface RecipeResponseDao {
    @Query("SELECT * FROM RecipeResponse")
    LiveData<List<RecipeResponse>> loadAllRecipes();

    @Query("SELECT * FROM RecipeResponse where name =(:name)")
    LiveData<RecipeResponse> getRecipeByName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipe(RecipeResponse result);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRecipe(RecipeResponse result);

}
