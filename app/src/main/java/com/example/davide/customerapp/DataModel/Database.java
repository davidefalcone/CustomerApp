package com.example.davide.customerapp.DataModel;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.davide.customerapp.DishesAdapter;
import com.example.davide.customerapp.OrdersAdapter;
import com.example.davide.customerapp.RestaurantsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Database {
    private static Database instance;
    private DatabaseReference path;
    private FirebaseUser user;
    private ArrayList<Order> ordersList;
    private ArrayList<Restaurant> restaurantsList;
    private ArrayList<Dish> dishesList;
    private Restaurant restaurant;


    private Database() {
        ordersList = new ArrayList<>();
        restaurantsList = new ArrayList<>();
        dishesList = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        path = FirebaseDatabase.getInstance().getReference();
    }

    public static Database getInstance() {
        if(instance == null) instance = new Database();
        return instance;
    }

    public void fillOrdersDatabase(OrdersAdapter adapter) {
        path.child("orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ordersList.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    Order order = data.getValue(Order.class);
                    if (TextUtils.equals(order.getCustomerID(), user.getUid()))
                        ordersList.add(order);
                }
                adapter.setOrdersList(ordersList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addOrder(Order order) {
        order.setCustomerID(user.getUid());
        order.setID(path.child("orders").push().getKey());
        path.child("orders").child(order.getID()).setValue(order);
    }

    public void fillRestaurantDatabase(RestaurantsAdapter adapter){
        path.child("restaurants").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                restaurantsList.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    restaurantsList.add(data.child("account").getValue(Restaurant.class));
                }
                adapter.setRestaurantsList(restaurantsList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void fillDishesDatabase(DishesAdapter adapter, Restaurant restaurant) {
        path.child("restaurants").child(restaurant.getID()).child("dishes").
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dishesList.clear();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            dishesList.add(data.getValue(Dish.class));
                        }
                        adapter.setDishesList(dishesList);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

}
