package com.example.apoorvdubey.bakeit.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apoorvdubey.bakeit.R;
import com.example.apoorvdubey.bakeit.service.model.RecipeResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private List<RecipeResponse> mData;
    private LayoutInflater mInflater;
    private Context context;
    private ItemClickListener mClickListener;

    public RecipeAdapter(Context context, List<RecipeResponse> result) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = result;
        this.context = context;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_recipe_list_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mData.get(position).getName() == null)
            holder.recipeName.setText(R.string.na);
        else
            holder.recipeName.setText(mData.get(position).getName());

        if (mData.get(position).getServings() == null)
            holder.recipeServings.setText(R.string.na);
        else
            holder.recipeServings.setText(context.getString(R.string.servings) + String.valueOf(mData.get(position).getServings()));
        if (mData.get(position).getImage() == null || mData.get(position).getImage().equals("") || TextUtils.isEmpty(mData.get(position).getImage())) {
            switch (mData.get(position).getId()) {
                case 1:
                    Picasso.with(context)
                            .load(R.drawable.no_image_1)
                            .centerCrop()
                            .fit()
                            .into(holder.recipeImage);
                    break;
                case 2:
                    Picasso.with(context)
                            .load(R.drawable.no_image_2)
                            .fit()
                            .centerCrop()
                            .into(holder.recipeImage);
                    break;
                case 3:
                    Picasso.with(context)
                            .load(R.drawable.no_image_3)
                            .fit()
                            .centerCrop()
                            .into(holder.recipeImage);
                    break;
                case 4:
                    Picasso.with(context)
                            .load(R.drawable.no_image_4)
                            .fit()
                            .centerCrop()
                            .into(holder.recipeImage);
                    break;
            }
        } else {
            Picasso.with(context)
                    .load(mData.get(position).getImage())
                    .into(holder.recipeImage);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.recipe_name)
        TextView recipeName;
        @BindView(R.id.recipe_servings)
        TextView recipeServings;
        @BindView(R.id.recipe_image)
        ImageView recipeImage;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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