package com.example.apoorvdubey.bakeit;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.apoorvdubey.bakeit.view.activity.RecipeActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RecipeActivityBasicTest {

    @Rule
    public ActivityTestRule<RecipeActivity> mActivityTestRule=
            new ActivityTestRule<>(RecipeActivity.class);

    @Test
    public void onStartLoadsRecipeListinRecyclerViewIfInternetPresent(){

    }
}
