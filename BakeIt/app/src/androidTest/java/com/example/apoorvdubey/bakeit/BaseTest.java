
package com.example.apoorvdubey.bakeit;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;

import com.example.apoorvdubey.bakeit.view.activity.RecipeActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;


public abstract class BaseTest {
    protected BaseApplication baseApplication;
    protected IdlingResource mIdlingResource;

    @Rule
    public ActivityTestRule<RecipeActivity> activityTestRule = new ActivityTestRule<>(RecipeActivity.class);

    @Before
    public void registerIdlingResource() {
        baseApplication = (BaseApplication) activityTestRule.getActivity().getApplicationContext();
        mIdlingResource = baseApplication.getIdlingResource();
        // Register Idling Resources
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}
