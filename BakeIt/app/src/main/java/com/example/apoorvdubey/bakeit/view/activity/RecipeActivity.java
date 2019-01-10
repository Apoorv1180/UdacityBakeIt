package com.example.apoorvdubey.bakeit.view.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.apoorvdubey.bakeit.R;
import com.example.apoorvdubey.bakeit.service.model.RecipeResponse;
import com.example.apoorvdubey.bakeit.view.adapter.RecipeAdapter;
import com.example.apoorvdubey.bakeit.viewmodel.RecipeListViewModel;
import com.example.apoorvdubey.bakeit.viewmodel.RecipeListViewModelFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeActivity extends AppCompatActivity implements RecipeAdapter.ItemClickListener {
    @BindView(R.id.recipe_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    RecipeAdapter recipeAdapter;
    RecyclerView.LayoutManager layoutManager;
    List<RecipeResponse> recipeResponseList = new ArrayList<>();
    public static boolean mTabletLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);
        setToolBar();
        setRecyclerView();
        setViewModel();
    }

    private void setToolBar() {
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);
    }

    private void setViewModel() {
        final RecipeListViewModel viewModel =
                ViewModelProviders.of(this, new RecipeListViewModelFactory(this.getApplication()))
                        .get(RecipeListViewModel.class);

        observeViewModel(viewModel);
    }

    private void setRecyclerView() {
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recipeAdapter= new RecipeAdapter(this,recipeResponseList);
        recipeAdapter.setClickListener(this);
        recyclerView.setAdapter(recipeAdapter);
    }

    private void observeViewModel(RecipeListViewModel viewModel) {
        viewModel.getRecipeListObservable().observe(this, new Observer<List<RecipeResponse>>() {
            @Override
            public void onChanged(@Nullable List<RecipeResponse> result) {
                if (result != null) {
                    if (result.size() != 0){
                        recipeResponseList.addAll(result);
                        recipeAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this,RecipeDetailActivity.class);
        intent.putExtra(getString(R.string.recipeObject), (Serializable) recipeResponseList.get(position));
        startActivity(intent);
    }
}