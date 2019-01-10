package com.example.apoorvdubey.bakeit.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.apoorvdubey.bakeit.service.model.Ingredient;
import com.example.apoorvdubey.bakeit.service.repository.DataRepository;

import java.util.List;

public class IngredientListViewModel extends AndroidViewModel {
    private final LiveData<List<Ingredient>> ingredientListObservable;

    DataRepository dataRepository;

    public IngredientListViewModel(Application application, Integer mParam) {
        super(application);

        dataRepository = DataRepository.getInstance(application);
        ingredientListObservable = DataRepository.getInstance(application).getIngredientsList(mParam);
    }

    public LiveData<List<Ingredient>> getIngredientObservable() {
        return ingredientListObservable;
    }

    public void Insert(Ingredient result) {
        dataRepository.insertIngredients(result);
    }

}