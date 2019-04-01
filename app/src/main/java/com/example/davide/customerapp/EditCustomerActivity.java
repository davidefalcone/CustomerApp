package com.example.davide.customerapp;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import com.example.davide.customerapp.DataModel.*;
import com.google.gson.Gson;
import android.content.Intent;
import android.widget.Toast;
import java.io.IOException;


public class EditCustomerActivity extends AppCompatActivity {

    //references for views
    private EditText editName;
    private EditText editMail;
    private EditText editDescription;
    private EditText editDeliveryAddress;
    private ImageView editImage;

    //reference for customer
    private Customer customer;

    //some tags
    private String tagDialog;
    private final int GALLERY = 0;
    private final int CAMERA = 1;
    private String notEmpty;
    private String empty;

    //data needed for persistence
    private SharedPreferences preferences;
    private String tagPreferences;
    private String tagCustomer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tagDialog = "dialog";
        tagCustomer = "Customer";
        tagPreferences = "preferences";
        notEmpty = "notempty";
        empty = "empty";

        //linking view with relative references
        editName = findViewById(R.id.editName);
        editMail = findViewById(R.id.editEmail);
        editDescription = findViewById(R.id.editDescription);
        editDeliveryAddress = findViewById(R.id.editDeliveryAddress);
        editImage = findViewById(R.id.editImage);

        setImageview();

        customer = retrieveCustomerData();
        if(customer != null) {

            setCustomerData(customer);
            editImage.setTag(notEmpty);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.save_button, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(gapsFilled()){
            saveCustomer();

            //this code checks if this is the first time the activity is showed:
            if(getIntent().getExtras() == null)
                //it's not the first time so this activity has een called by startactivityforresult method
                goToDetailActivity();
                //it's the first time so it is needed only to pass to te detail activity
            else backToDetail();
        }

        return true;

    }

    private void saveCustomer() {

        String name = editName.getText().toString();
        String mail = editMail.getText().toString();
        String description = editDescription.getText().toString();
        String deliveryAddress = editDeliveryAddress.getText().toString();

        /*
        There are two main ways to store the customer's image:
        1)Translate it in Base64 String and then store the resulting
        string in SharedPreferences file.
        2)Store the image on the device memory and store
        its path in the SharedPreference file.

        In this case, the first method has been choosen.

         */
        String encodedImage = Customer.encodeImage(editImage.getDrawable());

        Customer customer = Customer.getIstance();

        customer.setName(name);
        customer.setMail(mail);
        customer.setDescription(description);
        customer.setDeliveryAddress(deliveryAddress);
        customer.setEncodedImage(encodedImage);

        preferences = getSharedPreferences(tagPreferences, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(customer);
        editor.putString(tagCustomer, json);
        editor.commit();

    }

    //this method set the image editimage.png located in the drawable folder
    //and set a click listener.
    private void setImageview() {

        editImage.setImageResource(R.drawable.editimage);
        editImage.setTag(empty);
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //the user wants to change image
                startDialog();
            }
        });
    }

    private void backToDetail() {

        Intent intent = new Intent(this, CustomerDetailActivity.class);
        startActivity(intent);

    }

    //the aim of this method is to show the dialog in which
    // the user can choose between taking a selfie
    // or picking an image from the gallery

    private void startDialog() {

        new ChoosePictureDialogFragment().show(getSupportFragmentManager(), tagDialog);

    }

    //this method starts a special activity in which
    //the user can choose a photo from his/her gallery
    public void gallerySelected() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);

    }

    //this method starts a special activity in which
    //the user can take a photo by the camera
    public void cameraSelected() {

        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent, CAMERA);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    editImage.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI));
                    editImage.setTag(notEmpty);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            editImage.setImageBitmap((Bitmap) data.getExtras().get("data"));
            editImage.setTag(notEmpty);
        }
    }


    private void goToDetailActivity() {

        Intent intent = new Intent(this, CustomerDetailActivity.class);
        startActivity(intent);

    }

    //this method receives a customer object as parameter and fill all the gaps
    //of the activity with its data
    private void setCustomerData(Customer customer) {

        editName.setText(customer.getName());
        editMail.setText(customer.getMail());
        editDescription.setText(customer.getDescription());
        editDeliveryAddress.setText(customer.getDeliveryAddress());
        editImage.setImageBitmap(Customer.decodeImage(customer.getEncodedImage()));

    }

    //this method gather customer details from DetailActivity and returns a Customer object
    private Customer retrieveCustomerData() {

        Intent intent = getIntent();
        return ((Customer) intent.getSerializableExtra(tagCustomer));

    }

    private boolean gapsFilled() {

        boolean result = true;
        if (TextUtils.isEmpty(editName.getText().toString())) {

            result = false;
            Snackbar.make(findViewById(R.id.coordinatorLayout), R.string.messageMissingName, Snackbar.LENGTH_SHORT).show();

        }

        if (TextUtils.isEmpty(editMail.getText().toString())) {

            result = false;
            Snackbar.make(findViewById(R.id.coordinatorLayout), R.string.messageMissingMail, Snackbar.LENGTH_SHORT).show();

        }

        if (TextUtils.isEmpty(editDescription.getText().toString())) {

            result = false;
            Snackbar.make(findViewById(R.id.coordinatorLayout), R.string.messageMissingDescription, Snackbar.LENGTH_SHORT).show();

        }

        if (TextUtils.isEmpty(editDeliveryAddress.getText().toString())) {

            result = false;
            Snackbar.make(findViewById(R.id.coordinatorLayout), R.string.messageMissingAddress, Snackbar.LENGTH_SHORT).show();

        }

        if (editImage.getTag().toString() == empty) {

            result = false;
            Snackbar.make(findViewById(R.id.coordinatorLayout), R.string.messageMissingPhoto, Snackbar.LENGTH_SHORT).show();

        }


        return result;

    }
}


