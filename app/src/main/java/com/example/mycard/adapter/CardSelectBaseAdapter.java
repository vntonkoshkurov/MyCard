package com.example.mycard.adapter;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mycard.DatabaseHelper;
import com.example.mycard.R;
import com.example.mycard.databinding.FragmentCardListSelectBinding;
import com.example.mycard.intedrface.MyTextEditMethod;
import java.util.ArrayList;

public class CardSelectBaseAdapter extends RecyclerView.Adapter<CardSelectBaseAdapter.CardSelectViewHolder> implements MyTextEditMethod {
    private final Cursor cursor;
    private ArrayList <String> controlSelect = new ArrayList<>();
    private CallBack callBack;

    public interface CallBack {
        public void selectedList (ArrayList <String> selectList);
    }

    public CardSelectBaseAdapter (Cursor contentValues, CallBack callBack) {
        this.cursor = contentValues;
        this.callBack = callBack;
    }

    public static class CardSelectViewHolder extends RecyclerView.ViewHolder {
        FragmentCardListSelectBinding binding;

        public CardSelectViewHolder (FragmentCardListSelectBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public CardSelectViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CardSelectViewHolder(FragmentCardListSelectBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CardSelectViewHolder holder, @SuppressLint("RecyclerView") int i) {
        cursor.moveToPosition(i);
        int cardNum = cursor.getColumnIndex(DatabaseHelper.COLUMN_CARDNUM);
        int cardDescr = cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION);
        int cardItemPos = cursor.getColumnIndex(DatabaseHelper.COLUMN_ITEMINLIST);
        holder.binding.cardNumTXT.setText(holder.itemView.getContext().getString(R.string.card_short) + " *" + cardNumShort(cursor.getString(cardNum)));
        holder.binding.descriprionTXT.setText(cursor.getString(cardDescr));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor.moveToFirst();
                int pos = 0;
                while (pos != i) {
                    cursor.moveToNext();
                    pos++;
                }
                if (controlSelect.contains(Integer.toString(cursor.getInt(cardItemPos)))){
                    holder.binding.border.setColorFilter(null);
                    controlSelect.remove(Integer.toString(cursor.getInt(cardItemPos)));
                    callBack.selectedList(controlSelect);
                } else {
                    holder.binding.border.setColorFilter(holder.itemView.getContext().getColor(R.color.light_yellow));
                    controlSelect.add(Integer.toString(cursor.getInt(cardItemPos)));
                    callBack.selectedList(controlSelect);
                }
                holder.itemView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
}
