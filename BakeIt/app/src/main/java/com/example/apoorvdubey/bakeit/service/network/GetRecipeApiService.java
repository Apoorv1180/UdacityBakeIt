package com.example.apoorvdubey.bakeit.service.network;

import com.example.apoorvdubey.bakeit.service.model.RecipeResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetRecipeApiService {
    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<RecipeResponse>> getRecipes();
}