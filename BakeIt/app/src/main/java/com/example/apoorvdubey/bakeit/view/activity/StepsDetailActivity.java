package com.example.apoorvdubey.bakeit.view.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.apoorvdubey.bakeit.R;
import com.example.apoorvdubey.bakeit.service.model.Step;
import com.example.apoorvdubey.bakeit.view.adapter.DetailsPagerAdapter;
import com.example.apoorvdubey.bakeit.view.callbacks.ListenFromActivity;

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
        if (getIntent().hasExtra(getString(R.string.res_id)))
            Toast.makeText(getApplicationContext(), getIntent().getIntExtra(getString(R.string.res_id), 0) + "", Toast.LENGTH_SHORT).show();
        if (getIntent().hasExtra(getString(R.string.position)))
            position = getIntent().getIntExtra(getString(R.string.position), 1);
        if (getIntent().hasExtra(getString(R.string.recipId)))
            recipeId = getIntent().getIntExtra(getString(R.string.recipId), 1);
        if (getIntent().hasExtra(getString(R.string.lsit)))
            stepList.addAll(getIntent().<Step>getParcelableArrayListExtra(getString(R.string.lsit)));
    }

    private void setupTabs() {
        viewPager.setAdapter(new DetailsPagerAdapter(getSupportFragmentManager(), this, stepList, position, recipeId));
        setTabOnClickListener();
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setTabOnClickListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (null != activityListener) {
                    activityListener.doSomethingInFragment(position);
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        viewPager.setCurrentItem(position);
    }
}
