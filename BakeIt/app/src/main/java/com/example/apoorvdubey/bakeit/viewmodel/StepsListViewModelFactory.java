package com.example.apoorvdubey.bakeit.viewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

public class StepsListViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Application mApplication;
    private Integer mParam;

    public StepsListViewModelFactory(Application application, Integer param) {
        mApplication = application;
        mParam = param;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new StepsListViewModel(mApplication, mParam);
    }
}
