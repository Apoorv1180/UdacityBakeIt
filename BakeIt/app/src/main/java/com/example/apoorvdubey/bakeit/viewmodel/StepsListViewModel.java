package com.example.apoorvdubey.bakeit.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.apoorvdubey.bakeit.service.model.Step;
import com.example.apoorvdubey.bakeit.service.repository.DataRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class StepsListViewModel extends AndroidViewModel {
    private LiveData<List<Step>> stepsListObservable;

    DataRepository dataRepository;

    public StepsListViewModel(Application application, Integer mParam) {
        super(application);

        dataRepository = DataRepository.getInstance(application);
        try {
            stepsListObservable = DataRepository.getInstance(application).getStepsList(mParam);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public LiveData<List<Step>> getStepsListObservable() {
        return stepsListObservable;
    }

    public void Insert(Step result) {
        dataRepository.insertStep(result);
    }

}
