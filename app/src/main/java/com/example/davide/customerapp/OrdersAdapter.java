package com.example.davide.customerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.davide.customerapp.DataModel.Order;
import java.util.ArrayList;

public class OrdersAdapter extends BaseAdapter {
    private ArrayList<Order> ordersList;
    private Context context;

    private static final int RECEIVED = Order.RECEIVED;
    private static final int IN_DELIVERY = Order.IN_DELIVERY;
    private static final int DELIVERED = Order.DELIVERED;

    public OrdersAdapter(Context context){
        this.context = context;
        ordersList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return ordersList.size();
    }

    @Override
    public Order getItem(int position) {
        return ordersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.order_item, null);

        TextView orderedDishes = convertView.findViewById(R.id.orderedDishes);
        TextView orderRestaurant = convertView.findViewById(R.id.orderRestaurant);
        TextView orderStatus = convertView.findViewById(R.id.orderStatus);

        Order order = ordersList.get(position);

        orderedDishes.setText(order.getOrderedDishes().toString());
        orderRestaurant.setText(order.getRestaurant().getName());

        switch (order.getStatus()) {
            case RECEIVED:
                orderStatus.setText("Order received");
                break;
            case IN_DELIVERY:
                orderStatus.setText("In delivery");
                break;
            case DELIVERED:
                orderStatus.setText("Delivered");
                break;
        }

        return convertView;
    }

    public void setOrdersList(ArrayList<Order> ordersList){
        this.ordersList = ordersList;
    }
}
