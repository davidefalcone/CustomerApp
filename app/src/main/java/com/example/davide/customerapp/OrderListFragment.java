package com.example.davide.customerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.davide.customerapp.DataModel.Database;
import com.example.davide.customerapp.DataModel.Order;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class OrderListFragment extends Fragment {
    //view references
    private ListView listView;
    private FloatingActionButton fab;
    private OrdersAdapter adapter;

    public static final String EXTRA_ORDER = "order";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.order_list_fragment, container, false);

        listView = v.findViewById(R.id.listView);
        fab = v.findViewById(R.id.fab);

        setFAB();

        setListView();

        return v;
    }

    private void setFAB() {
        fab.setOnClickListener(view -> {
            Intent i = new Intent(getContext(), EditOrderActivity.class);
            startActivityForResult(i, 0);
        });
    }

    private void setListView() {
        adapter = new OrdersAdapter(getContext());
        Database.getInstance().fillOrdersDatabase(adapter);
        listView.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                adapter.notifyDataSetChanged();
                return;
            case RESULT_CANCELED:
                //ignore
                return;
        }
    }
}
