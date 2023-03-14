package com.example.mycard.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.example.mycard.R;

public class CheckCardDataDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        return builder
                .setTitle(getString(R.string.dialog2_title_view))
                .setMessage(getString(getArguments().getInt("name")))
                .setPositiveButton("OK", null)
                .create();
    }
}
