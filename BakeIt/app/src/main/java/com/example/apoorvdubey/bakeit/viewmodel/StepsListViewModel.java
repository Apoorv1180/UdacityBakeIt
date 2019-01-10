package com.example.apoorvdubey.bakeit.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.apoorvdubey.bakeit.service.model.Step;
import com.example.apoorvdubey.bakeit.service.repository.DataRepository;

import java.util.List;

public class StepsListViewModel extends AndroidViewModel {
    private final LiveData<List<Step>> stepsListObservable;

    DataRepository dataRepository;

    public StepsListViewModel(Application application, Integer mParam) {
        super(application);

        dataRepository = DataRepository.getInstance(application);
        stepsListObservable = DataRepository.getInstance(application).getStepsList(mParam);
    }

    public LiveData<List<Step>> getStepsListObservable() {
        return stepsListObservable;
    }

    public void Insert(Step result) {
        dataRepository.insertStep(result);
    }

}
