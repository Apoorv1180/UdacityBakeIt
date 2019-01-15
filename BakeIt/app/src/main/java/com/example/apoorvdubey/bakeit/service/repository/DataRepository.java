package com.example.apoorvdubey.bakeit.service.repository;


import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.AsyncTask;

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
import java.util.concurrent.ExecutionException;

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
                        setMyIngredient(data.getValue().get(i), ingredientsDao, data.getValue().get(i).getId());
                        setMySteps(data.getValue().get(i), stepsDao, data.getValue().get(i).getId());
                    }
                }

                @Override
                public void onFailure(Call<List<RecipeResponse>> call, Throwable t) {
                    data.setValue(null);
                }
            });
        } else {
            try {
                return loadAllRecipes();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
            try {
                if (steps.size() != getStepsCount(stepsDao, recipeId)) {
                    insertStep(steps.get(i));
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void setMyIngredient(RecipeResponse recipeResponse, IngredientsDao ingredientsDao, int recipeId) {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ingredients.addAll(recipeResponse.getIngredients());
        for (int i = 0; i < ingredients.size(); i++) {
            ingredients.get(i).setRecipeId(recipeResponse.getId());
        }
        for (int i = 0; i < ingredients.size(); i++) {
            try {
                if (ingredients.size() != getIngredientsCount(ingredientsDao, recipeId)) {
                    insertIngredients(ingredients.get(i));
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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

    public LiveData<List<Ingredient>> getIngredientsList(Integer id) throws ExecutionException, InterruptedException {
        return new getIngredientsAsyncTask(ingredientsDao, id).execute().get();

    }

    public int getIngredientsCount(IngredientsDao ingredientDao, Integer id) throws ExecutionException, InterruptedException {
        return new getIngredientsCountAsyncTask(ingredientsDao, id).execute().get();

    }

    public int getStepsCount(StepsDao stepsDao, Integer id) throws ExecutionException, InterruptedException {
        return new getStepsCountAsyncTask(stepsDao, id).execute().get();

    }

    public LiveData<List<Step>> getStepsList(Integer id) throws ExecutionException, InterruptedException {
        return new getStepsListAsyncTask(stepsDao, id).execute().get();
    }

    public void insertStep(Step result) {
        new InsertStepsAsynTask(stepsDao).execute(result);
    }

    public LiveData<List<RecipeResponse>> loadAllRecipes() throws ExecutionException, InterruptedException {
        return new LoadAllRecipes(recipeResponseDao).execute().get();
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

    private static class LoadAllRecipes extends AsyncTask<Void, Void, LiveData<List<RecipeResponse>>> {
        private RecipeResponseDao mAsyncTaskDao;

        LoadAllRecipes(RecipeResponseDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected LiveData<List<RecipeResponse>> doInBackground(Void... voids) {
            return mAsyncTaskDao.loadAllRecipes();
        }
    }

    private static class getStepsListAsyncTask extends AsyncTask<Void, Void, LiveData<List<Step>>> {
        int id;
        StepsDao stepsDao;

        public getStepsListAsyncTask(StepsDao stepsDao, Integer id) {
            this.stepsDao = stepsDao;
            this.id = id;
        }

        @Override
        protected LiveData<List<Step>> doInBackground(Void... voids) {
            return stepsDao.loadAllSteps(id);
        }
    }

    private class getIngredientsAsyncTask extends AsyncTask<Void, Void, LiveData<List<Ingredient>>> {
        int id;
        IngredientsDao ingredientsDao;

        public getIngredientsAsyncTask(IngredientsDao ingredientsDao, Integer id) {
            this.ingredientsDao = ingredientsDao;
            this.id = id;
        }

        @Override
        protected LiveData<List<Ingredient>> doInBackground(Void... voids) {
            return ingredientsDao.loadAllIngredients(id);
        }
    }

    private class getIngredientsCountAsyncTask extends AsyncTask<Void, Void, Integer> {
        int id;
        IngredientsDao ingredientsDao;

        public getIngredientsCountAsyncTask(IngredientsDao ingredientsDao, Integer id) {
            this.ingredientsDao = ingredientsDao;
            this.id = id;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return ingredientsDao.getIngredientCount(id);
        }
    }

    private class getStepsCountAsyncTask extends AsyncTask<Void, Void, Integer> {
        int id;
        StepsDao stepsDao;

        public getStepsCountAsyncTask(StepsDao stepsDao, Integer id) {
            this.stepsDao = stepsDao;
            this.id = id;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return stepsDao.getStepsCount(id);
        }
    }
}
