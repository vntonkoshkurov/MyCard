package com.example.mycard.adapter;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mycard.DatabaseHelper;
import com.example.mycard.databinding.BanklayoutBinding;

public class BankBaseAdapter extends RecyclerView.Adapter<BankBaseAdapter.BankBaseViewHolder> {
    private Cursor cursor;
    private static final int SETTINGS_CLICKED = 1;
    private static final int BANK_CARD_CLICKED = 2;
    private BankBaseAdapter.OnStateClickListener onClickListner;

    public BankBaseAdapter(Cursor contentValues,
                           BankBaseAdapter.OnStateClickListener onClickListener) {
        this.cursor = contentValues;
        this.onClickListner = onClickListener;
    }

    //определяем интерфейс слушателя
    public interface OnStateClickListener{
        void onStateClick(int i, int type);
    }
    //данный метод вызывается при изменении в составе БД
    //он позволяет обновить список элементов без перезагрузки фрагмента или активности
    public void updateData(Cursor contentValues, Boolean doUpdate) {
        this.cursor = contentValues;
        //проверка - желает ли разработчик обновить recyclerview
        //положительный ответ предоставляется, если в БД имеется изменение в
        //количестве данных
        //в противном случае это необходимо при использовании перетаскивания карточек
        //потому что после перетаскивания необходимо синхронизировать порядок данных
        //из ДБ с порядком данных в reciclerview
        if (doUpdate == true) {notifyDataSetChanged();}
    }

    public static class BankBaseViewHolder extends RecyclerView.ViewHolder {

        BanklayoutBinding bindingBList;

        public BankBaseViewHolder(BanklayoutBinding binding) {
            super(binding.getRoot());
            bindingBList = binding;
        }
    }

    @NonNull
    @Override
    public BankBaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new BankBaseAdapter.BankBaseViewHolder(BanklayoutBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BankBaseViewHolder holder, @SuppressLint("RecyclerView") int i) {
        cursor.moveToPosition(i);
        int bankNameCol = cursor.getColumnIndex(DatabaseHelper.COLUMN_BANK_NAME);
        int bankColorCol = cursor.getColumnIndex(DatabaseHelper.COLUMN_BANK_COLOR);
        int bankID = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
        //обработка нажатия на значек настройки в карточке банка
        holder.bindingBList.bankCardSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked (holder, SETTINGS_CLICKED,
                        holder.itemView.getId());
            }
        });
        holder.bindingBList.bankName.setText(cursor.getString(bankNameCol));
        holder.bindingBList.bankImage.setColorFilter(cursor.getInt(bankColorCol));
        holder.itemView.setId(Integer.parseInt(cursor.getString(bankID)));
        //обработка нажатия на карточку банка
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked (holder, BANK_CARD_CLICKED, holder.itemView.getId());
            }
        });

    }
    //обработка нажатия на элементе. Передается позиция в списке, тип нажатия
    private void clicked (BankBaseViewHolder holder, int type, int i) {
        onClickListner.onStateClick(i, type);
        holder.itemView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

}
