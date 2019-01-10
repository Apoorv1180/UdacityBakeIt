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
import android.widget.Toast;

import com.example.apoorvdubey.bakeit.R;
import com.example.apoorvdubey.bakeit.service.model.Ingredient;
import com.example.apoorvdubey.bakeit.view.adapter.IngredientAdapter;
import com.example.apoorvdubey.bakeit.viewmodel.IngredientListViewModel;
import com.example.apoorvdubey.bakeit.viewmodel.IngredientListViewModelFactory;


import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientFragment extends Fragment implements IngredientAdapter.ItemClickListener {
    int id=0;
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
    id = args.getInt("recipeId");


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
        recyclerView = root.findViewById(R.id.ingredients_list);
        layoutManager= new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter= new IngredientAdapter(getActivity(),ingredientArrayList);
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("ID",id+"");
        final IngredientListViewModel viewModel =
                ViewModelProviders.of(this, new IngredientListViewModelFactory(getActivity().getApplication(),id))
                        .get(IngredientListViewModel.class);

        observeViewModel(viewModel);
    }


    private void observeViewModel(IngredientListViewModel viewModel) {
        viewModel.getIngredientObservable().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable List<Ingredient> result) {
                if (result != null) {
                    if (result.size() != 0){
                        ingredientArrayList.addAll(result);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
