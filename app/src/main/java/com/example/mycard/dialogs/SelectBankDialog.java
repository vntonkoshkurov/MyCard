package com.example.mycard.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mycard.BankBaseProcessor;
import com.example.mycard.DatabaseHelper;
import com.example.mycard.R;
import com.example.mycard.adapter.BankBaseAdapter;

public class SelectBankDialog extends DialogFragment {
    private SQLiteDatabase db;
    private Bundle bundle;
    private String bankName;
    public Callback callback;
    public BankBaseProcessor baseProcessor;

    public interface Callback {
        void dialogCallViewBank (Bundle bundle);
        void dialogCloseAfterDelBV ();
        void dialogCallBankCardSelect (Bundle bundle);
     }

    @SuppressLint("ValidFragment")
     public SelectBankDialog (SQLiteDatabase db, Bundle bundle, Callback callback, View view) {
         this.db = db;
         this.bundle = bundle;
         this.callback = callback;
         this.baseProcessor = new BankBaseProcessor(view);
     }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        bankName = getArguments().getString("bankname");
        return builder
                .setTitle(getString(R.string.dialog3_title_view) + bankName)
                .setMessage(getString(R.string.dialog3_message_view))
                .setPositiveButton(getString(R.string.dialog1_pos_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        callback.dialogCallViewBank(bundle);
                    }
                })
                .setNegativeButton(getString(R.string.dialog1_neutr_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        callback.dialogCallBankCardSelect(bundle);
                    }
                })
                .setNeutralButton(getString(R.string.dialog1_neg_button), new DialogInterface.OnClickListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String itemselected = getArguments().getString("bankid");
                        db.delete(DatabaseHelper.TABLE_BANKS, "_id = ?", new String[]{itemselected});
                        baseProcessor.baseItemPos(0,0);
                        dialog.cancel();
                        callback.dialogCloseAfterDelBV();
                    }
                })
                .create();
    }
}
