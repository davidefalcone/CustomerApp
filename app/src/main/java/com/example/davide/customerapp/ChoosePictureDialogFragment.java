package com.example.davide.customerapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;


public class ChoosePictureDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.myDialog);
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
