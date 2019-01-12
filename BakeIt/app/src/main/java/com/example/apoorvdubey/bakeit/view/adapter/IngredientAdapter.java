package com.example.apoorvdubey.bakeit.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.apoorvdubey.bakeit.R;
import com.example.apoorvdubey.bakeit.service.model.Ingredient;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private List<Ingredient> mData;
    private LayoutInflater mInflater;
    private Context context;
    private ItemClickListener mClickListener;

    public IngredientAdapter(Context context, List<Ingredient> result) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = result;
        this.context = context;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_ingredient_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mData.get(position).getIngredient() == null)
            holder.ingredientName.setText(R.string.na);
        else
            holder.ingredientName.setText("\u2022 Bullet"+" "+mData.get(position).getIngredient().toString());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView ingredientName;

        ViewHolder(View itemView) {
            super(itemView);
            ingredientName = itemView.findViewById(R.id.ingredient_name);
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