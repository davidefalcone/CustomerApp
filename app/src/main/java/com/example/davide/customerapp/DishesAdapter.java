package com.example.davide.customerapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.davide.customerapp.DataModel.Dish;
import com.example.davide.customerapp.DataModel.Order;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class DishesAdapter extends RecyclerView.Adapter<DishesViewHolder> {
    private ArrayList<Dish> dishesList;
    private Context context;
    private StorageReference path;

    //this variable is used to get from which fragment the adapter is been using
    private int fragment;

    //fragment variable can fit only the following values
    public static final int CHOOSE_DISH = 0;
    public static final int CHECKOUT = 1;

    public DishesAdapter(Context context, int fragment) {
        this.context = context;
        dishesList = new ArrayList<>();
        path = FirebaseStorage.getInstance().getReference()
                .child("restaurants");
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public DishesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.dish_item, parent, false);
        DishesViewHolder dishesViewHolder = new DishesViewHolder(v);
        return dishesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DishesViewHolder holder, int position) {
        Dish dish = dishesList.get(position);
        holder.dishQuantity1.setVisibility(View.GONE);
        holder.dishName.setText(dish.getName());
        holder.dishDescription.setText(dish.getDescription());
        holder.dishCost.setText("€ " + String.format("%d", dish.getPrice()));

        Glide.with(context).load(path.child(dish.getRestaurantID()).child("account_image.jpg"))
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                }).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(holder.dishImage);

        holder.addButton.setOnClickListener(view -> {
            holder.optionView.setVisibility(View.VISIBLE);
            int newQuantity = dish.getQuantity() + 1;
            dish.setQuantity(newQuantity);
            if(dish.getQuantity() == 0) holder.optionView.setVisibility(View.GONE);
            else {
                holder.dishQuantity.setText(Integer.toString(newQuantity));
            }
        });

        holder.deleteButton.setOnClickListener(view -> {
            int newQuantity = dish.getQuantity() - 1;
            dish.setQuantity(newQuantity);
            if (newQuantity == 0) holder.optionView.setVisibility(View.GONE);
            else holder.dishQuantity.setText(Integer.toString(newQuantity));
        });

        if (fragment == CHECKOUT) {
            holder.optionView.setVisibility(View.GONE);
            holder.addButton.setVisibility(View.GONE);
            holder.dishQuantity1.setText("Quantity: " + String.format("%d", dish.getQuantity()));
        }

    }

    @Override
    public int getItemCount() {
        return dishesList.size();
    }

    public void setDishesList(ArrayList<Dish> dishesList) {
        this.dishesList = dishesList;
    }

    public Order fillOrder(Order order) {
        for (Dish dish : dishesList) {
            if(dish.getQuantity() != 0) {
                order.addDish(dish);
                int newTotal = order.getTotal() + dish.getPrice();
                order.setTotal(newTotal);
            }
        }
        return order;
    }
}

class DishesViewHolder extends RecyclerView.ViewHolder{
    public ImageView dishImage;
    public TextView dishName;
    public TextView dishDescription;
    public TextView dishCost;
    public View optionView;
    public MaterialButton addButton;
    public MaterialButton deleteButton;
    public TextView dishQuantity;
    public ProgressBar progressBar;
    public TextView dishQuantity1;

    public DishesViewHolder(@NonNull View itemView) {
        super(itemView);
        dishImage = itemView.findViewById(R.id.dishImage);
        dishName = itemView.findViewById(R.id.dishName);
        dishDescription = itemView.findViewById(R.id.dishDescription);
        dishCost = itemView.findViewById(R.id.dishCost);
        addButton = itemView.findViewById(R.id.addDish);
        deleteButton = itemView.findViewById(R.id.removeDish);
        dishQuantity = itemView.findViewById(R.id.dishQuantity);
        progressBar = itemView.findViewById(R.id.progressBar);
        optionView = itemView.findViewById(R.id.optionView);
        dishQuantity1 = itemView.findViewById(R.id.dishQuantity1);
    }
}


