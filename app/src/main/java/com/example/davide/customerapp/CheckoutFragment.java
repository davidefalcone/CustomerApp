package com.example.davide.customerapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CheckoutFragment extends Fragment {
    private RecyclerView recyclerView;
    private TextView restaurant;
    private DishesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.checkout_fragment, container, false);

        recyclerView = v.findViewById(R.id.recyclerView);
        restaurant = v.findViewById(R.id.textRestaurant);

        restaurant.setText(((EditOrderActivity) getActivity()).getRestaurant().getName());

        setRecyclerView();

        return v;
    }

    private void setRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        //changing in content do not change the layout
        // size of the RecyclerView (from developer.android)
        recyclerView.setHasFixedSize(true);
        int fragment = DishesAdapter.CHECKOUT;
        adapter = new DishesAdapter(getContext(), fragment);
        adapter.setDishesList(((EditOrderActivity) getActivity()).getOrder().getOrderedDishes());
        recyclerView.setAdapter(adapter);
    }

}
