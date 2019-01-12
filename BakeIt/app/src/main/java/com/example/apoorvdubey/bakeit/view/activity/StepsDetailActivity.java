package com.example.apoorvdubey.bakeit.view.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.apoorvdubey.bakeit.R;
import com.example.apoorvdubey.bakeit.service.model.RecipeResponse;
import com.example.apoorvdubey.bakeit.service.model.Step;
import com.example.apoorvdubey.bakeit.view.adapter.DetailsPagerAdapter;
import com.example.apoorvdubey.bakeit.view.callbacks.ListenFromActivity;
import com.example.apoorvdubey.bakeit.view.fragment.StepListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsDetailActivity extends AppCompatActivity {
    int position;
    int recipeId;
    List<Step> stepList = new ArrayList<>();
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.details_sliding_tabs)
    TabLayout tabLayout;
    public ListenFromActivity activityListener;


    public void setActivityListener(ListenFromActivity activityListener) {
        this.activityListener = activityListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_detail);
        setActivityListener(activityListener);
        getBundleValues();
        ButterKnife.bind(this);
        setupTabs();
    }

    private void getBundleValues() {
        if (getIntent().hasExtra("responseId"))
            Toast.makeText(getApplicationContext(), getIntent().getIntExtra("responseId", 0) + "", Toast.LENGTH_SHORT).show();
        if (getIntent().hasExtra("position"))
            position = getIntent().getIntExtra("position", 1);
        if (getIntent().hasExtra("recipeId"))
            recipeId = getIntent().getIntExtra("recipeId", 1);
        if (getIntent().hasExtra("list"))
            stepList.addAll(getIntent().<Step>getParcelableArrayListExtra("list"));
    }


    private void setupTabs() {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager.setAdapter(new DetailsPagerAdapter(getSupportFragmentManager(), this, stepList, position, recipeId));
        setTabOnClickListener();
        // Give the TabLayout the ViewPager
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setTabOnClickListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("tagg", "yoyo");
                if (null != activityListener) {
                    activityListener.doSomethingInFragment(position);
                }
            }

            @Override
            public void onPageSelected(int position) {
                Log.e("tagg", "lili");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e("tagg", "yeye");

            }
        });
        viewPager.setCurrentItem(position);
    }
}
