package com.example.apoorvdubey.bakeit.service.repository;


import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.example.apoorvdubey.bakeit.Utils.Constant;
import com.example.apoorvdubey.bakeit.service.database.IngredientsDao;
import com.example.apoorvdubey.bakeit.service.database.RecipeDatabase;
import com.example.apoorvdubey.bakeit.service.database.RecipeResponseDao;
import com.example.apoorvdubey.bakeit.service.database.StepsDao;
import com.example.apoorvdubey.bakeit.service.model.Ingredient;
import com.example.apoorvdubey.bakeit.service.model.RecipeResponse;
import com.example.apoorvdubey.bakeit.service.model.Step;
import com.example.apoorvdubey.bakeit.service.network.GetRecipeApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataRepository {
    private GetRecipeApiService getRecipeApiService;
    private static DataRepository dataRepository;
    private RecipeResponseDao recipeResponseDao;
    private IngredientsDao ingredientsDao;
    private StepsDao stepsDao;
    private static Context context;
    private RecipeDatabase recipeDatabase;

    private DataRepository(Application application) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        getRecipeApiService = retrofit.create(GetRecipeApiService.class);
        recipeDatabase = RecipeDatabase.getsInstance(application);
        recipeResponseDao = recipeDatabase.recipeResponseDao();
        ingredientsDao = recipeDatabase.ingredientsDao();
        stepsDao = recipeDatabase.stepsDao();

    }

    public synchronized static DataRepository getInstance(Application application) {
        if (dataRepository == null) {
            if (dataRepository == null) {
                dataRepository = new DataRepository(application);
                context = application.getApplicationContext();
            }
        }
        return dataRepository;
    }

    public LiveData<List<RecipeResponse>> getRecipeList() {
        final MutableLiveData<List<RecipeResponse>> data = new MutableLiveData<>();
        if (Constant.isConnected(context)) {
            getRecipeApiService.getRecipes().enqueue(new Callback<List<RecipeResponse>>() {

                @Override
                public void onResponse(Call<List<RecipeResponse>> call, Response<List<RecipeResponse>> response) {
                    if (response.body().size() != 0)
                        data.setValue(response.body());
                    for (int i = 0; i < data.getValue().size(); i++) {
                        insertRecipe(data.getValue().get(i));
                        setMyIngredient(data.getValue().get(i), ingredientsDao,data.getValue().get(i).getId());
                        setMySteps(data.getValue().get(i),stepsDao,data.getValue().get(i).getId());
                    }
                }

                @Override
                public void onFailure(Call<List<RecipeResponse>> call, Throwable t) {
                    data.setValue(null);
                }
            });
        } else {
            return recipeResponseDao.loadAllRecipes();
        }
        return data;
    }

    private void setMySteps(RecipeResponse recipeResponse, StepsDao stepsDao, int recipeId) {
        ArrayList<Step> steps = new ArrayList<>();
        steps.addAll(recipeResponse.getSteps());
        for (int i = 0; i < steps.size(); i++) {
            steps.get(i).setRecipeId(recipeResponse.getId());
        }
        for (int i = 0; i < steps.size(); i++) {
            if (steps.size() != stepsDao.getStepsCount(recipeId)) {
                insertStep(steps.get(i));
            }
        }
    }

    private void setMyIngredient(RecipeResponse recipeResponse, IngredientsDao ingredientsDao,int recipeId) {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ingredients.addAll(recipeResponse.getIngredients());
        for (int i = 0; i < ingredients.size(); i++) {
            ingredients.get(i).setRecipeId(recipeResponse.getId());
        }
        for (int i = 0; i < ingredients.size(); i++) {
            if (ingredients.size() != ingredientsDao.getIngredientCount(recipeId)) {
                insertIngredients(ingredients.get(i));
            }
        }
    }

    public LiveData<List<Ingredient>> getIngredientsList(Integer id) {
        return ingredientsDao.loadAllIngredients(id);
    }

    public LiveData<List<Step>> getStepsList(Integer id) {
        return stepsDao.loadAllSteps(id);
    }

    public void insertRecipe(RecipeResponse result) {
        new InsertRecipeAsynTask(recipeResponseDao).execute(result);
    }

    public void updateRecipe(RecipeResponse result) {
        new UpdateRecipeAsynTask(recipeResponseDao).execute(result);
    }

    public void insertIngredients(Ingredient result) {
        new insertIngredientsAsynTask(ingredientsDao).execute(result);
    }

    public void insertStep(Step result) {
        new InsertStepsAsynTask(stepsDao).execute(result);
    }

    private static class InsertRecipeAsynTask extends AsyncTask<RecipeResponse, Void, Void> {
        private RecipeResponseDao resultDao;

        private InsertRecipeAsynTask(RecipeResponseDao resultDao) {
            this.resultDao = resultDao;
        }

        @Override
        protected Void doInBackground(RecipeResponse... detailResults) {
            resultDao.insertRecipe(detailResults[0]);
            return null;
        }
    }

    private static class UpdateRecipeAsynTask extends AsyncTask<RecipeResponse, Void, Void> {
        private RecipeResponseDao resultDao;

        private UpdateRecipeAsynTask(RecipeResponseDao resultDao) {
            this.resultDao = resultDao;
        }

        @Override
        protected Void doInBackground(RecipeResponse... detailResults) {
            resultDao.insertRecipe(detailResults[0]);
            return null;
        }
    }


    private static class insertIngredientsAsynTask extends AsyncTask<Ingredient, Void, Void> {
        private IngredientsDao ingredientsDao;

        private insertIngredientsAsynTask(IngredientsDao resultDao) {
            this.ingredientsDao = resultDao;
        }

        @Override
        protected Void doInBackground(Ingredient... detailResults) {
            ingredientsDao.insertIngredient(detailResults[0]);
            return null;
        }
    }


    private static class InsertStepsAsynTask extends AsyncTask<Step, Void, Void> {
        private StepsDao stepsDao;

        private InsertStepsAsynTask(StepsDao resultDao) {
            this.stepsDao = resultDao;
        }

        @Override
        protected Void doInBackground(Step... detailResults) {
            stepsDao.insertSteps(detailResults[0]);
            return null;
        }
    }
}
