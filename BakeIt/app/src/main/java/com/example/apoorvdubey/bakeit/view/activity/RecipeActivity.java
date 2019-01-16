package com.example.apoorvdubey.bakeit.view.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Nullable @BindView(R.id.empty_text_view_recipe)
    TextView emptyTextView;
    RecipeAdapter recipeAdapter;
    RecyclerView.LayoutManager layoutManager;
    GridLayoutManager gridLayoutManager;
    List<RecipeResponse> recipeResponseList = new ArrayList<>();
    boolean tabletSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        tabletSize = getResources().getBoolean(R.bool.is_tablet);
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
        if(!tabletSize) {
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
        }
        else{
            gridLayoutManager = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(gridLayoutManager);
        }
        recyclerView.setVisibility(View.GONE);
        recipeAdapter = new RecipeAdapter(this, recipeResponseList);
        recipeAdapter.setClickListener(this);
        recyclerView.setAdapter(recipeAdapter);
    }

    private void observeViewModel(RecipeListViewModel viewModel) {
        progressBar.setVisibility(View.VISIBLE);
        viewModel.getRecipeListObservable().observe(this, new Observer<List<RecipeResponse>>() {
            @Override
            public void onChanged(@Nullable List<RecipeResponse> result) {
                if (result != null) {
                    if (result.size() != 0){
                        recipeResponseList.addAll(result);
                        recipeAdapter.notifyDataSetChanged();
                        recyclerView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        emptyTextView.setVisibility(View.GONE);
                    }
                    else{
                        emptyTextView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
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