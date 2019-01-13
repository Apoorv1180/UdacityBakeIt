package com.example.apoorvdubey.bakeit.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.apoorvdubey.bakeit.service.model.Ingredient;
import com.example.apoorvdubey.bakeit.service.repository.DataRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class IngredientListViewModel extends AndroidViewModel {
    private LiveData<List<Ingredient>> ingredientListObservable;

    DataRepository dataRepository;

    public IngredientListViewModel(Application application, Integer mParam) {
        super(application);

        dataRepository = DataRepository.getInstance(application);
        try {
            ingredientListObservable = DataRepository.getInstance(application).getIngredientsList(mParam);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public LiveData<List<Ingredient>> getIngredientObservable() {
        return ingredientListObservable;
    }

    public void Insert(Ingredient result) {
        dataRepository.insertIngredients(result);
    }

}