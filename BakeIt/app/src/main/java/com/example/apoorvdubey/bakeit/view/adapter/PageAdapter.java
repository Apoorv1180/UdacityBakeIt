package com.example.apoorvdubey.bakeit.view.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.apoorvdubey.bakeit.service.model.Step;
import com.example.apoorvdubey.bakeit.view.fragment.IngredientFragment;
import com.example.apoorvdubey.bakeit.view.fragment.StepFragment;

public class PageAdapter extends FragmentPagerAdapter {
    private int numOfTabs;
    Bundle bundle;

    public PageAdapter(FragmentManager fm, int numOfTabs, Bundle bundle) {
        super(fm);
        this.numOfTabs = numOfTabs;
        this.bundle= bundle;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                IngredientFragment ingredientFragment = new IngredientFragment();
                ingredientFragment.setArguments(this.bundle);
                return  ingredientFragment;
            case 1:
                StepFragment stepFragment = new StepFragment();
                stepFragment.setArguments(this.bundle);
                return stepFragment ;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}