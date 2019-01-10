    package com.example.apoorvdubey.bakeit.view.fragment;


    import android.arch.lifecycle.Observer;
    import android.arch.lifecycle.ViewModelProviders;
    import android.content.Context;
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
    import com.example.apoorvdubey.bakeit.service.model.Step;
    import com.example.apoorvdubey.bakeit.view.adapter.IngredientAdapter;
    import com.example.apoorvdubey.bakeit.view.adapter.StepAdapter;
    import com.example.apoorvdubey.bakeit.view.callbacks.OnHeadlineSelectedListener;
    import com.example.apoorvdubey.bakeit.viewmodel.StepsListViewModel;
    import com.example.apoorvdubey.bakeit.viewmodel.StepsListViewModelFactory;

    import java.util.ArrayList;
    import java.util.List;

    public class StepFragment extends Fragment implements StepAdapter.ItemClickListener {
        int id=0;
        RecyclerView recyclerView;
        RecyclerView.LayoutManager layoutManager;
        StepAdapter adapter;
        ArrayList<Step> stepArrayList = new ArrayList<>();
        private OnHeadlineSelectedListener mCallback;
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            final Bundle args = getArguments();
            id = args.getInt("recipeId");

        }
        public StepFragment() {
            // Required empty public constructor
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View root = inflater.inflate(R.layout.fragment_steps, container, false);
            setView(root);
            return root;    }

        private void setView(View root) {
            recyclerView = root.findViewById(R.id.step_recycler_view);
            layoutManager= new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            adapter= new StepAdapter(getActivity(),stepArrayList);
            recyclerView.setAdapter(adapter);
            adapter.setClickListener(this);
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            Log.e("ID",id+"");
            final StepsListViewModel viewModelStep =
                    ViewModelProviders.of(this, new StepsListViewModelFactory(getActivity().getApplication(),id))
                            .get(StepsListViewModel.class);
            observeViewModelStep(viewModelStep);
        }
        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            try {
                mCallback = (OnHeadlineSelectedListener) context;
            } catch (ClassCastException e) {
                throw new ClassCastException(context.toString()
                        + getString(R.string.error));
            }
        }

        private void observeViewModelStep(StepsListViewModel viewModel) {
            viewModel.getStepsListObservable().observe(this, new Observer<List<Step>>() {
                @Override
                public void onChanged(@Nullable List<Step> result) {
                    if (result != null) {
                        if (result.size() != 0){
                          stepArrayList.addAll(result);
                          adapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }


        @Override
        public void onItemClick(View view, int position) {
            mCallback.onArticleSelected(position, stepArrayList);
        }

    }