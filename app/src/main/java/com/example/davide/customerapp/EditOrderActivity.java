package com.example.davide.customerapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.davide.customerapp.DataModel.Order;
import com.example.davide.customerapp.DataModel.Restaurant;

public class EditOrderActivity extends AppCompatActivity {
    private Restaurant restaurant;
    private Order order;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loadFragment(new ChooseRestaurantFragment());
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public boolean onSupportNavigateUp() {
        Fragment currentFragment = getSupportFragmentManager()
                .findFragmentById(R.id.frameLayout);
        if (currentFragment instanceof ChooseRestaurantFragment) {
            Intent i = getIntent();
            setResult(RESULT_CANCELED, i);
            finish();
        }
        else if (currentFragment instanceof ChooseDishesFragment) {
            loadFragment(new ChooseRestaurantFragment());
        }
        else if (currentFragment instanceof CheckoutFragment) {
            finish();
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
