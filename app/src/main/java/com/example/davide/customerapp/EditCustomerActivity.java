package com.example.davide.customerapp;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import com.example.davide.customerapp.DataModel.*;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.content.Intent;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class EditCustomerActivity extends AppCompatActivity {

    //references for views
    private TextInputLayout editName;
    private TextInputLayout editMail;
    private TextInputLayout editDescription;
    private TextInputLayout editDeliveryAddress;
    private ImageView editImage;
    private ProgressBar progressBar;
    private FloatingActionButton fab;

    //some tags
    private String tagDialog;
    private String tagCustomer;
    private final int GALLERY = 0;
    private final int CAMERA = 1;

    //keeping track of the fact that imageview is empty or not
    private boolean imageViewEmpty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tagDialog = "dialog";
        tagCustomer = CustomerDetailFragment.tagCustomer;

        //linking view with relative references
        editName = findViewById(R.id.editName);
        editMail = findViewById(R.id.editEmail);
        editDescription = findViewById(R.id.editDescription);
        editDeliveryAddress = findViewById(R.id.editDeliveryAddress);
        editImage = findViewById(R.id.editImage);
        progressBar = findViewById(R.id.progressBar);
        fab = findViewById(R.id.fab);

        setImageview();

        setFAB();

        if(retrieveCustomerData() != null) {
            setCustomerData(retrieveCustomerData());
            imageViewEmpty = false;
        }

    }

    //this method set the image editimage.png located in the drawable folder
    //and set a click listener.
    private void setImageview() {

        editImage.setImageResource(R.drawable.editimage);
        imageViewEmpty = true;
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //the user wants to change image
                startDialog();
            }
        });
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
                    imageViewEmpty = false;

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            editImage.setImageBitmap((Bitmap) data.getExtras().get("data"));
            imageViewEmpty = false;
        }
    }

    //this method receives a customer object as parameter and fill all the gaps
    //of the activity with its data
    private void setCustomerData(Customer customer) {
        editName.getEditText().setText(customer.getName());
        editMail.getEditText().setText(customer.getMail());
        editDescription.getEditText().setText(customer.getDescription());
        editDeliveryAddress.getEditText().setText(customer.getDeliveryAddress());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        StorageReference path = FirebaseStorage.getInstance().getReference()
                .child("customers").child(user.getUid()).child("account_image.jpg");
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(this).load(path).addListener(new RequestListener<Drawable>() {
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
                .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(editImage);
    }

    //this method gets customer details from DetailActivity and returns a Customer object
    private Customer retrieveCustomerData() {
        Intent intent = getIntent();
        return ((Customer) intent.getSerializableExtra(tagCustomer));
    }

    //this method returns treu if all gaps are filled, false instead.
    private boolean gapsFilled() {

        boolean result = true;

        if (TextUtils.isEmpty(editName.getEditText().getText().toString()))
            result = false;



        if (TextUtils.isEmpty(editMail.getEditText().getText().toString()))
            result = false;


        if (TextUtils.isEmpty(editDescription.getEditText().getText().toString()))
            result = false;


        if (TextUtils.isEmpty(editDeliveryAddress.getEditText().getText().toString()))
            result = false;


        if (imageViewEmpty)
            result = false;

        return result;

    }

    //this method simply shows a snackbar
    private void showSnackbar() {

        Snackbar.make(findViewById(R.id.coordinatorLayout), R.string.messageMissingMultipleData, Snackbar.LENGTH_SHORT).show();

    }

    private void setFAB() {
        fab.setOnClickListener(view -> {
            if (gapsFilled())
                onFinish();
            else showSnackbar();
        });
    }

    private void onFinish() {
        Customer customer = readCustomer();
        uploadImage();
        Intent i = getIntent();
        i.putExtra(tagCustomer, customer);
        setResult(RESULT_OK, i);
    }

    private void uploadImage(){
        View v = findViewById(R.id.loadingView);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        StorageReference imagePath = FirebaseStorage.getInstance().getReference().
                child("customers").child(user.getUid()).child("account_image.jpg");
        imagePath.delete();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = ((BitmapDrawable)editImage.getDrawable()).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imagePath.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                v.setVisibility(View.GONE);
                finish();
            }
        });
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                v.setVisibility(View.VISIBLE);
            }
        });
        return;
    }

    private Customer readCustomer() {
        Customer customer = new Customer();
        customer.setName(editName.getEditText().getText().toString());
        customer.setMail(editMail.getEditText().getText().toString());
        customer.setDescription(editDescription.getEditText().getText().toString());
        customer.setDeliveryAddress(editDeliveryAddress.getEditText().getText().toString());
        return customer;
    }
}


