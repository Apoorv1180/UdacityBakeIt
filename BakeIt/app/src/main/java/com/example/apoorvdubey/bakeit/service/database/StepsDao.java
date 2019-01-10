package com.example.apoorvdubey.bakeit.service.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.apoorvdubey.bakeit.service.model.Step;

import java.util.List;

@Dao
public interface StepsDao {
    @Query("SELECT * FROM Step where recipeId =(:value)")
    LiveData<List<Step>> loadAllSteps(Integer value);

    @Insert
    void insertSteps(Step result);

    @Query("SELECT COUNT(*) FROM Step where recipeId =(:value) LIMIT 1")
    int getStepsCount(Integer value);

}
