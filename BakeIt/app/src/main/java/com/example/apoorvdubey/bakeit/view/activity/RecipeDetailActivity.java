package com.example.apoorvdubey.bakeit.view.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.apoorvdubey.bakeit.R;
import com.example.apoorvdubey.bakeit.service.model.Ingredient;
import com.example.apoorvdubey.bakeit.service.model.RecipeResponse;
import com.example.apoorvdubey.bakeit.service.model.Step;
import com.example.apoorvdubey.bakeit.view.adapter.PageAdapter;
import com.example.apoorvdubey.bakeit.view.callbacks.OnHeadlineSelectedListener;
import com.example.apoorvdubey.bakeit.view.fragment.StepListFragment;
import com.example.apoorvdubey.bakeit.viewmodel.IngredientListViewModel;
import com.example.apoorvdubey.bakeit.viewmodel.IngredientListViewModelFactory;
import com.example.apoorvdubey.bakeit.viewmodel.RecipeListViewModel;
import com.example.apoorvdubey.bakeit.viewmodel.StepsListViewModel;
import com.example.apoorvdubey.bakeit.viewmodel.StepsListViewModelFactory;
import com.example.apoorvdubey.bakeit.widget.AppWidgetService;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity implements OnHeadlineSelectedListener {

    TabLayout tabLayout;
    Toolbar toolbar;
    TabItem tabIngredient,tabSteps;
    ViewPager viewPager;
    PageAdapter pageAdapter;
    RecipeResponse response;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        initView();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    toolbar.setBackgroundColor(ContextCompat.getColor(RecipeDetailActivity.this,
                            R.color.colorPrimary));}
                else {
                    toolbar.setBackgroundColor(ContextCompat.getColor(RecipeDetailActivity.this,
                            R.color.colorPrimary));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);
        tabLayout = findViewById(R.id.tablayout);
        tabIngredient = findViewById(R.id.ingredient_tab);
        tabSteps = findViewById(R.id.steps_tab);
        viewPager = findViewById(R.id.viewPager);
        pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount(),setIntentValues());
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.setAdapter(pageAdapter);
    }

    private Bundle setIntentValues() {
        Bundle bundle = new Bundle();
        if(getIntent().hasExtra("recipeObject"))
        response=(RecipeResponse)getIntent().getParcelableExtra("recipeObject");
        else if(getIntent().hasExtra("recipeAW")) {
            bundle = getIntent().getBundleExtra("recipeAW");
            response = getIntent().getParcelableExtra("recipeObjectAW");
        }else {
            //do nothing
        }
        bundle.putInt("recipeId",response.getId());
        toolbar.setTitle(response.getName());
        return  bundle;
    }

    @Override
    public void onArticleSelected(int position, List<Step> response) {
        Intent intent = new Intent(this,StepsDetailActivity.class);
        intent.putExtra("position",position);
        intent.putExtra("response",response.get(position));
        intent.putExtra("recipeId",response.get(position).getRecipeId());
        intent.putExtra("list", (ArrayList<? extends Parcelable>) response);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipe_info, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_to_widget) {
            AppWidgetService.updateWidget(this, response);
            Toast.makeText(getApplicationContext(),"Updated Widget",Toast.LENGTH_SHORT).show();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }
}
