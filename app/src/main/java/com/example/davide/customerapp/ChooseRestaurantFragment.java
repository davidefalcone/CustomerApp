package com.example.davide.customerapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.davide.customerapp.DataModel.Database;

public class ChooseRestaurantFragment extends Fragment {
    private ListView listView;
    private RestaurantsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.choose_restaurant_fragment, container, false);

        listView = v.findViewById(R.id.listView);

        setListView();

        return v;
    }

    private void setListView() {
        adapter = new RestaurantsAdapter(getContext());
        Database.getInstance().fillRestaurantDatabase(adapter);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((EditOrderActivity) getActivity()).setRestaurant(adapter.getItem(position));
                ((EditOrderActivity) getActivity()).loadFragment(new ChooseDishesFragment());
            }
        });
    }
}
