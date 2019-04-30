package com.example.davide.customerapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.davide.customerapp.DataModel.Customer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class CustomerDetailFragment extends Fragment {
    //view references
    private ImageView customerImage;
    private TextView customerName;
    private TextView customerMail;
    private TextView customerDescription;
    private TextView customerDeliveryAddress;
    private ProgressBar progressBar;
    private FloatingActionButton fab;

    //model
    private Customer customer;

    //some tags
    public static final int ADD_CUSTOMER = 0;
    public static final int EDIT_CUSTOMER = 1;
    public static final String tagCustomer = "tagcustomer";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.customer_detail_fragment, container, false);

        //getting references
        customerImage = v.findViewById(R.id.customerImage);
        customerName = v.findViewById(R.id.textName);
        customerMail = v.findViewById(R.id.textEmail);
        customerDescription = v.findViewById(R.id.textDescription);
        customerDeliveryAddress = v.findViewById(R.id.textDeliveryAddress);
        progressBar = v.findViewById(R.id.progressBar);
        fab = v.findViewById(R.id.fab);

        setFAB();

        getCustomerFromDB();

        return v;
    }

    private void setCustomer(Customer customer){
        customerName.setText(customer.getName());
        customerMail.setText(customer.getMail());
        customerDescription.setText(customer.getDescription());
        customerDeliveryAddress.setText(customer.getDeliveryAddress());

        //Firebase storage reference is needed for showing customer's image
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        StorageReference imagePath = FirebaseStorage.getInstance().getReference()
                .child("customers").child(user.getUid()).child("account_image.jpg");
        Glide.with(getContext()).load(imagePath).addListener(new RequestListener<Drawable>() {
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
        })
                .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(customerImage);
    }

    private void goToEditCustomerActivity(int requestCode) {
        Intent i = new Intent(getActivity(), EditCustomerActivity.class);
        startActivityForResult(i, requestCode);
    }

    private void getCustomerFromDB() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("customers").child(user.getUid()).child("account");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null)
                    //the user hasn't created a customer account yet.
                    goToEditCustomerActivity(ADD_CUSTOMER);
                else {
                    customer = dataSnapshot.getValue(Customer.class);
                    setCustomer(customer);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Customer customer;
        switch (requestCode){
            case ADD_CUSTOMER:
                switch (resultCode){
                    case RESULT_OK:
                        customer = (Customer) data.getSerializableExtra(tagCustomer);
                        writeCustomer(customer);
                        setCustomer(customer);
                        return;
                    case RESULT_CANCELED:
                        goToEditCustomerActivity(ADD_CUSTOMER);
                        return;
                }
            case EDIT_CUSTOMER:
                switch (resultCode){
                    case RESULT_OK:
                        customer = (Customer) data.getSerializableExtra(tagCustomer);
                        writeCustomer(customer);
                        setCustomer(customer);
                        return;
                    case RESULT_CANCELED:
                        return;
                }
        }
    }

    private void writeCustomer(Customer customer) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("customers").child(user.getUid()).child("account");
        customer.setID(user.getUid());
        reference.setValue(customer);
    }

    private void setFAB() {
        fab.setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), EditCustomerActivity.class);
            i.putExtra(tagCustomer, customer);
            startActivityForResult(i, EDIT_CUSTOMER);
        });
    }
}
