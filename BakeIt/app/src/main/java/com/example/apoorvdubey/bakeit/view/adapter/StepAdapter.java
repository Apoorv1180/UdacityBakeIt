package com.example.apoorvdubey.bakeit.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.apoorvdubey.bakeit.R;
import com.example.apoorvdubey.bakeit.service.model.RecipeResponse;
import com.example.apoorvdubey.bakeit.service.model.Step;

import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {

    private List<Step> mData;
    private LayoutInflater mInflater;
    private Context context;
    private ItemClickListener mClickListener;

    public StepAdapter(Context context, List<Step> result) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = result;
        this.context = context;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_step_list_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mData.get(position).getDescription() == null)
            holder.stepDescription.setText(R.string.na);
        else
            holder.stepDescription.setText(mData.get(position).getDescription().toString());

        if (mData.get(position).getShortDescription() == null)
            holder.recipeShortDescription.setText(R.string.na);
        else
            holder.recipeShortDescription.setText("\u2022 Bullet"+" "+String.valueOf(mData.get(position).getShortDescription()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView stepDescription;
        TextView recipeShortDescription;

        ViewHolder(View itemView) {
            super(itemView);
            stepDescription = itemView.findViewById(R.id.desc_step);
            recipeShortDescription = itemView.findViewById(R.id.short_desc_step);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}