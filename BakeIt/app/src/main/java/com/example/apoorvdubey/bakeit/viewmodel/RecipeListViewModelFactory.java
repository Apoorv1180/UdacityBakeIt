package com.example.apoorvdubey.bakeit.viewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

public class RecipeListViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Application mApplication;


    public RecipeListViewModelFactory(Application application) {
        mApplication = application;

    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new RecipeListViewModel(mApplication);
    }
}
