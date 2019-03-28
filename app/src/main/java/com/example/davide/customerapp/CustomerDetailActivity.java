package com.example.davide.customerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.davide.customerapp.DataModel.*;
import com.google.gson.Gson;


public class CustomerDetailActivity extends AppCompatActivity {

    //references
    private ImageView customerImage;
    private TextView customerName;
    private TextView customerMail;
    private TextView customerDescription;
    private TextView customerDeliveryAddress;

    //this string will be used in managing sharedPreferences
    private String tagCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);

        tagCustomer = "Customer";

        //linking view with relative references
        customerImage = findViewById(R.id.customerImage);
        customerName = findViewById(R.id.textName);
        customerMail = findViewById(R.id.textEmail);
        customerDescription = findViewById(R.id.textDescription);
        customerDeliveryAddress = findViewById(R.id.textDeliveryAddress);

        setData();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, EditCustomerActivity.class);
        startActivityForResult(intent, 1);

        return true;
    }

    //this method fulls both imageview and textviews with customer's data
    private void setData(){

        Customer customer = retrieveData();

        customerName.setText(customer.getName());
        customerMail.setText(customer.getMail());
        customerDescription.setText(customer.getDescription());
        customerDeliveryAddress.setText(customer.getDeliveryAddress());

    }

    //this method uses sharedPreference to retrieve the customer object
    private Customer retrieveData() {

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString(tagCustomer, "");
        return gson.fromJson(json, Customer.class);

    }
}