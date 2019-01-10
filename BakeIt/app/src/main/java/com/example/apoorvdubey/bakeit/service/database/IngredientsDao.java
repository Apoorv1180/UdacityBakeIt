package com.example.apoorvdubey.bakeit.service.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.apoorvdubey.bakeit.service.model.Ingredient;


import java.util.List;

@Dao
public interface IngredientsDao {
    @Query("SELECT * FROM Ingredient where recipeId =(:value)")
    LiveData<List<Ingredient>> loadAllIngredients(Integer value);

    @Query("SELECT COUNT(*) FROM Ingredient where recipeId =(:value) LIMIT 1")
    int getIngredientCount(Integer value);

    @Insert
    void insertIngredient(Ingredient result);

    @Update(onConflict = OnConflictStrategy.ABORT)
    void updateIngredient(Ingredient result);

}