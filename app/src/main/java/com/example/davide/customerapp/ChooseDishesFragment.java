package com.example.davide.customerapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.davide.customerapp.DataModel.Database;
import com.example.davide.customerapp.DataModel.Order;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ChooseDishesFragment extends Fragment {
    private RecyclerView recyclerView;
    private DishesAdapter adapter;
    private FloatingActionButton fab;
    private Order order;

    private final String EXTRA_ORDER = OrderListFragment.EXTRA_ORDER;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.choose_dishes_fragment, container, false);

        recyclerView = v.findViewById(R.id.recyclerView);
        fab = v.findViewById(R.id.fab);

        order = new Order(((EditOrderActivity) getActivity()).getRestaurant());

        setRecyclerView();

        setFAB();

        return v;
    }

    private void setRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        int fragment = DishesAdapter.CHOOSE_DISH;
        adapter = new DishesAdapter(getContext(), fragment);
        Database.getInstance().fillDishesDatabase(adapter,
                ((EditOrderActivity) getActivity()).getRestaurant());
        recyclerView.setAdapter(adapter);
    }

    private void setFAB() {
        fab.setOnClickListener(view -> {
            order = adapter.fillOrder(order);
            Intent i = getActivity().getIntent();
            getActivity().setResult(Activity.RESULT_OK, i);
            ((EditOrderActivity)getActivity()).setOrder(order);
            Database.getInstance().addOrder(order);
            ((EditOrderActivity)getActivity()).loadFragment(new CheckoutFragment());
        });
    }
}
