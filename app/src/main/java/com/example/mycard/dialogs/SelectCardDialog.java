package com.example.mycard.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telecom.Call;
import android.view.HapticFeedbackConstants;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mycard.BaseProcessor;
import com.example.mycard.DatabaseHelper;
import com.example.mycard.R;
import com.example.mycard.intedrface.MyTextEditMethod;
import com.example.mycard.ui.cardlist.CardList;
import com.example.mycard.ui.cardview.CardView;

@SuppressLint("ValidFragment")
public class SelectCardDialog extends DialogFragment implements MyTextEditMethod {
    private SQLiteDatabase db;
    private Bundle bundle;
    public Callback callback;
    private BaseProcessor baseProcessor;

    public interface Callback {
        void dialogCallViewCard(Bundle bundle);
        void dialogCloseAfterDelCV();
    }

    @SuppressLint("ValidFragment")
    public SelectCardDialog(SQLiteDatabase db, Bundle bundle, Callback callback, View view) {
        this.db = db;
        this.bundle = bundle;
        this.callback = callback;
        this.baseProcessor = new BaseProcessor(view);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        return builder
                .setTitle(getString(R.string.dialog1_title_view) + cardNumShort(getArguments().getString("cardnum")))
                .setMessage(getString(R.string.dialog1_message_view))
                .setPositiveButton(getString(R.string.dialog1_pos_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        callback.dialogCallViewCard(bundle);
                    }
                })
                .setNegativeButton(getString(R.string.dialog1_neg_button), new DialogInterface.OnClickListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String itemselected = getArguments().getString("cardid");
                        db.delete(DatabaseHelper.TABLE, "_id = ?", new String[]{itemselected});
                        baseProcessor.baseItemPos(0,0);
                        dialog.cancel();
                        callback.dialogCloseAfterDelCV();
                    }
                })
                .create();
    }
}