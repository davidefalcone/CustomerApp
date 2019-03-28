package com.example.davide.customerapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;


public class ChoosePictureDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialogTitle);
        builder.setView(R.layout.picture_dialog);
        builder.setAdapter(new GalleriaCameraAdapter(this.getContext()), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (i == 0) ((EditCustomerActivity) getActivity()).gallerySelected();
                else ((EditCustomerActivity) getActivity()).cameraSelected();
            }
        });

        return builder.create();

    }


}
