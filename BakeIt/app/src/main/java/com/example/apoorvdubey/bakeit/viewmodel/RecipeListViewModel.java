package com.example.apoorvdubey.bakeit.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.apoorvdubey.bakeit.service.model.RecipeResponse;
import com.example.apoorvdubey.bakeit.service.repository.DataRepository;
import java.util.List;

public class RecipeListViewModel extends AndroidViewModel {
    private final LiveData<List<RecipeResponse>> recipeListObservable;
    DataRepository dataRepository;

    public RecipeListViewModel(Application application) {
        super(application);

        dataRepository = DataRepository.getInstance(application);
        recipeListObservable = DataRepository.getInstance(application).getRecipeList();
    }

    public LiveData<List<RecipeResponse>> getRecipeListObservable() {
        return recipeListObservable;
    }

    public void Insert(RecipeResponse result) {
        dataRepository.insertRecipe(result);
    }

    public void Update(RecipeResponse result) {
        dataRepository.updateRecipe(result);
    }
}
