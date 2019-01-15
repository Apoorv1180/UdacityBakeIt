package com.example.apoorvdubey.bakeit.view.fragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apoorvdubey.bakeit.R;
import com.example.apoorvdubey.bakeit.service.model.Ingredient;
import com.example.apoorvdubey.bakeit.view.adapter.IngredientAdapter;
import com.example.apoorvdubey.bakeit.viewmodel.IngredientListViewModel;
import com.example.apoorvdubey.bakeit.viewmodel.IngredientListViewModelFactory;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientFragment extends Fragment {
    int id = 0;
    @BindView(R.id.ingredients_list)
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    IngredientAdapter adapter;
    ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();

    public IngredientFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        id = args.getInt(getString(R.string.recId));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_ingregient, container, false);
        setView(root);
        return root;

    }

    private void setView(View root) {
        ButterKnife.bind(this, root);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new IngredientAdapter(getActivity(), ingredientArrayList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final IngredientListViewModel viewModel =
                ViewModelProviders.of(this, new IngredientListViewModelFactory(getActivity().getApplication(), id))
                        .get(IngredientListViewModel.class);
        observeViewModel(viewModel);
    }

    private void observeViewModel(IngredientListViewModel viewModel) {
        viewModel.getIngredientObservable().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable List<Ingredient> result) {
                if (result != null) {
                    if (result.size() != 0) {
                        ingredientArrayList.addAll(result);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}
