package com.example.apoorvdubey.bakeit;

import android.content.Context;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.apoorvdubey.bakeit.Utils.Constant;
import com.example.apoorvdubey.bakeit.Utils.Prefs;
import com.example.apoorvdubey.bakeit.service.model.RecipeResponse;
import com.example.apoorvdubey.bakeit.view.activity.RecipeActivity;
import com.example.apoorvdubey.bakeit.view.fragment.StepFragment;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.example.apoorvdubey.bakeit.RecyclerViewItemCountAssertion.withItemCount;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.Matchers.greaterThan;

@RunWith(AndroidJUnit4.class)
public class RecipeActivityBasicTest extends BaseTest {

    RecipeResponse recipeResponse;
    protected BaseApplication baseApplication;
    protected IdlingResource mIdlingResource;

    @Rule
    public ActivityTestRule<RecipeActivity> activityTestRule=
            new ActivityTestRule<>(RecipeActivity.class);


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

    //View Testing
    @Test
    public void onStartLoadsRecipeListInRecyclerViewIfInternetPresent(){
        if(Constant.isConnected(getInstrumentation().getTargetContext().getApplicationContext())){
            onView(withId(R.id.recipe_recycler_view)).check(withItemCount(greaterThan(0)));
        }
    }
    @Test
    public void onStartLoadsRecipeListInRecyclerViewIfNoInternetAndDatabaseHasNoValue(){
        if(!Constant.isConnected((getInstrumentation().getTargetContext().getApplicationContext())) && recipeResponse==null){
            onView(withId(R.id.empty_text_view_recipe)).check(matches(isDisplayed()));
        }
    }

    //Widget Testing and Idling Resources
    @Test
    public void checkAddWidgetButtonFunctionality() {
        // Clear the preferences values
        registerIdlingResource();
        baseApplication.getSharedPreferences(Prefs.PREFS_NAME, Context.MODE_PRIVATE).edit()
                .clear()
                .commit();

        Navigation.getMeToRecipeInfo(0);

        onView(withId(R.id.action_add_to_widget))
                .check(matches(isDisplayed()))
                .perform(click());

        RecipeResponse recipe = Prefs.loadRecipe(baseApplication);
        assertNotNull(recipe);
    }

    //Intent

    @Test
    public void clickOnRecyclerViewStepItem_opensRecipeDetailActivity_orFragment() {
        Navigation.getMeToRecipeInfo(0);

        boolean is_tablet = baseApplication.getResources().getBoolean(R.bool.is_tablet);
        if (!is_tablet) {
            Intents.init();
            Navigation.selectRecipeStep(0);
            intended(hasComponent(StepFragment.class.getName()));
            intended(hasExtraWithKey(activityTestRule.getActivity().getApplicationContext().getResources().getString(R.string.position)));
            intended(hasExtraWithKey(activityTestRule.getActivity().getApplicationContext().getResources().getString(R.string.response)));
            intended(hasExtraWithKey(activityTestRule.getActivity().getApplicationContext().getResources().getString(R.string.recipe_id)));
            intended(hasExtraWithKey(activityTestRule.getActivity().getApplicationContext().getResources().getString(R.string.list)));
            Intents.release();

            // Check TabLayout
            onView(withId(R.id.tablayout))
                    .check(matches(isCompletelyDisplayed()));
        }
    }

}
