package com.example.apoorvdubey.bakeit.view.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.example.apoorvdubey.bakeit.service.model.Step;
import com.example.apoorvdubey.bakeit.view.activity.StepsDetailActivity;
import com.example.apoorvdubey.bakeit.view.fragment.StepListFragment;

import java.util.ArrayList;
import java.util.List;

public class DetailsPagerAdapter extends FragmentPagerAdapter {

    Context mContext;
    FragmentManager fm;
    List<Step> stepArrayList = new ArrayList<>();
    int mRecipeId;
    int mPositionSelected ;
    private ArrayList<String> tabTitles = new ArrayList<String>();
    private static final String INTRODUCTION_TAB_TITLE = "Introduction";

    public DetailsPagerAdapter(FragmentManager supportFragmentManager, Context stepsDetailActivity, List<Step> stepList, int position, int recipeId) {
        super(supportFragmentManager);
        mContext = stepsDetailActivity;
        stepArrayList=stepList;
        mRecipeId=recipeId;
        mPositionSelected=position;
        addTitlesDynamically();

    }

    private void addTitlesDynamically() {
        tabTitles.add(INTRODUCTION_TAB_TITLE);
        for(int i=1;i<stepArrayList.size();i++){
            tabTitles.add("Step"+ i);
        }


    }
    @Override
    public Fragment getItem(int position) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(StepListFragment.STEP_KEY, stepArrayList.get(position));
        StepListFragment fragment = new StepListFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public int getCount() {
        return tabTitles.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }
}
