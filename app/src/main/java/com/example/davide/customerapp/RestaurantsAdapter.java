package com.example.davide.customerapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.davide.customerapp.DataModel.Database;
import com.example.davide.customerapp.DataModel.Restaurant;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class RestaurantsAdapter extends BaseAdapter {
    private ArrayList<Restaurant> restaurantsList;
    private Context context;
    private StorageReference path;

    public RestaurantsAdapter(Context context){
        this.context = context;
        restaurantsList = new ArrayList<>();
        path = FirebaseStorage.getInstance().getReference()
            .child("restaurants");
    }

    @Override
    public int getCount() {
        return restaurantsList.size();
    }

    @Override
    public Restaurant getItem(int position) {
        return restaurantsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.restaurant_item, null);

        ImageView restaurantImage = convertView.findViewById(R.id.restaurantImage);
        TextView restaurantName = convertView.findViewById(R.id.restaurantName);
        ProgressBar progressBar = convertView.findViewById(R.id.progressBar);

        Restaurant restaurant = restaurantsList.get(position);

        restaurantName.setText(restaurant.getName());
        Glide.with(context).load(path.child(restaurant.getID()).child("account_image.jpg")).
                addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }
        }).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(restaurantImage);

        return convertView;
    }

    public void setRestaurantsList(ArrayList<Restaurant> restaurantsList) {
        this.restaurantsList = restaurantsList;
    }
}
