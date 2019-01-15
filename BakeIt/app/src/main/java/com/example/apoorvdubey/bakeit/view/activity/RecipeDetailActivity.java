package com.example.apoorvdubey.bakeit.view.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apoorvdubey.bakeit.R;
import com.example.apoorvdubey.bakeit.Utils.Prefs;
import com.example.apoorvdubey.bakeit.service.model.RecipeResponse;
import com.example.apoorvdubey.bakeit.service.model.Step;
import com.example.apoorvdubey.bakeit.view.adapter.PageAdapter;
import com.example.apoorvdubey.bakeit.view.callbacks.ListenFromActivity;
import com.example.apoorvdubey.bakeit.view.callbacks.OnHeadlineSelectedListener;
import com.example.apoorvdubey.bakeit.view.fragment.StepListFragment;
import com.example.apoorvdubey.bakeit.widget.AppWidgetService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity implements OnHeadlineSelectedListener {
    @BindView(R.id.tablayout)
    TabLayout tabLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_text)
    TextView textViewToolbar;
    @Nullable
    @BindView(R.id.ingredient_tab_item)
    TabItem tabIngredient;
    @BindView(R.id.steps_tab)
    @Nullable
    TabItem tabSteps;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tablet_step_list_fragment_container)
    FrameLayout frameLayout;
    PageAdapter pageAdapter;
    RecipeResponse response;
    boolean tabletSize;
    public ListenFromActivity activityListener;

    public void setActivityListener(ListenFromActivity activityListener) {
        this.activityListener = activityListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        initView();
        setActivityListener(activityListener);
        tabletSize = getResources().getBoolean(R.bool.is_tablet);
    }

    private void initView() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), setIntentValues());
        setToolBarImage();
        setViewPagerListeners();
    }

    private void setViewPagerListeners() {
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                setToolBarImage();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                setToolBarImage();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        viewPager.setAdapter(pageAdapter);
    }

    private void setToolBarImage() {
        switch (response.getId()) {
            case 1:
                toolbar.setBackground(getResources().getDrawable(R.drawable.no_image_1));
                textViewToolbar.setText(response.getName());
                break;
            case 2:
                toolbar.setBackground(getResources().getDrawable(R.drawable.no_image_2));
                textViewToolbar.setText(response.getName());
                break;
            case 3:
                toolbar.setBackground(getResources().getDrawable(R.drawable.no_image_3));
                textViewToolbar.setText(response.getName());
                break;
            case 4:
                toolbar.setBackground(getResources().getDrawable(R.drawable.no_image_4));
                textViewToolbar.setText(response.getName());
                break;
        }
    }

    private Bundle setIntentValues() {
        Bundle bundle = new Bundle();
        if (getIntent().hasExtra(getString(R.string.recipe_object)))
            response = (RecipeResponse) getIntent().getParcelableExtra(getString(R.string.recipe_object));
        else if (getIntent().hasExtra(getString(R.string.recipe_android_widget))) {
            bundle = getIntent().getBundleExtra(getString(R.string.recipe_android_widget));
            response = getIntent().getParcelableExtra(getString(R.string.recipe_object_android_widget));
        } else {
            response = Prefs.loadRecipe(this);
        }
        bundle.putInt(getString(R.string.recipe_id), response.getId());
        toolbar.setTitle(response.getName());
        return bundle;
    }

    @Override
    public void onArticleSelected(int position, List<Step> response) {
        if (!tabletSize) {
            Intent intent = new Intent(this, StepsDetailActivity.class);
            intent.putExtra(getString(R.string.position), position);
            intent.putExtra(getString(R.string.response), response.get(position));
            intent.putExtra(getString(R.string.recipe_id), response.get(position).getRecipeId());
            intent.putExtra(getString(R.string.list), (ArrayList<? extends Parcelable>) response);
            startActivity(intent);
        } else {
            frameLayout.setVisibility(View.VISIBLE);
            Bundle arguments = new Bundle();
            arguments.putParcelable(StepListFragment.STEP_KEY, response.get(position));
            StepListFragment fragment = new StepListFragment();
            fragment.setArguments(arguments);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.tablet_step_list_fragment_container, fragment, getString(R.string.step_list_container));
            ft.commit();
        }
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
            Toast.makeText(getApplicationContext(), getString(R.string.Updated_Widget), Toast.LENGTH_SHORT).show();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
