package com.example.mycard.adapter;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mycard.DatabaseHelper;
import com.example.mycard.databinding.CardlayoutBinding;
import com.example.mycard.intedrface.MyTextEditMethod;

public class CardBaseAdapter extends RecyclerView.Adapter<CardBaseAdapter.CardBaseViewHolder> implements MyTextEditMethod {
    private Cursor cursor;
    private OnStateClickListener onClickListner;

    public CardBaseAdapter(Cursor cursor,
                           OnStateClickListener onClickListener) {
        this.cursor = cursor;
        this.onClickListner = onClickListener;
    }

    //определяем интерфейс слушателя
    public interface OnStateClickListener{
        void onStateClick(int i);
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

    public static class CardBaseViewHolder extends RecyclerView.ViewHolder {

        CardlayoutBinding bindingCList;

        CardBaseViewHolder(CardlayoutBinding binding) {
            super(binding.getRoot());
            bindingCList = binding;
        }
    }

    @NonNull
    @Override
    public CardBaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new CardBaseViewHolder(CardlayoutBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CardBaseViewHolder cardBaseViewHolder, @SuppressLint("RecyclerView") int i) {
        cursor.moveToPosition(i);
        int cardNumID = cursor.getColumnIndex(DatabaseHelper.COLUMN_CARDNUM);
        int cardMonthID= cursor.getColumnIndex(DatabaseHelper.COLUMN_CARDMONTH);
        int cardYearID= cursor.getColumnIndex(DatabaseHelper.COLUMN_CARDYEAR);
        int cardCVVID= cursor.getColumnIndex(DatabaseHelper.COLUMN_CARDCVV);
        int paysysID = cursor.getColumnIndex(DatabaseHelper.COLUMN_PAYSYS);
        int cardDescr = cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION);
        int cardColor = cursor.getColumnIndex(DatabaseHelper.COLUMN_CARD_COLOR);
        int cardIDID = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
        //в зависимости от количества символов в номере карты изменяется размер шрифта
        //поскольку текст может не поместиться на карточку
        if (cursor.getString(cardNumID).length()<=16){
            cardBaseViewHolder.bindingCList.cardNum.setTextSize(21);
        }else{
            cardBaseViewHolder.bindingCList.cardNum.setTextSize(18);
        }
        cardBaseViewHolder.bindingCList.cardNum.setText(cardNumFormat(cursor.getString(cardNumID)));
        cardBaseViewHolder.bindingCList.cardMonth.setText(cursor.getString(cardMonthID));
        cardBaseViewHolder.bindingCList.cardYear.setText(cursor.getString(cardYearID));
        cardBaseViewHolder.bindingCList.cardCvv.setText(cursor.getString(cardCVVID));
        cardBaseViewHolder.bindingCList.paysysImg.setImageResource(imageRes(cursor.getInt(paysysID)));
        cardBaseViewHolder.bindingCList.cardDescr.setText(cursor.getString(cardDescr));
        cardBaseViewHolder.bindingCList.cardImage.setColorFilter(cursor.getInt(cardColor));
        cardBaseViewHolder.itemView.setId(Integer.parseInt(cursor.getString(cardIDID)));
        cardBaseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onClickListner.onStateClick(i);
                //изначально передавался номер записи в списке (см. строку выше)
                //но при перемещении карточки в списке номер позиции в адаптере не меняется
                //поэтому каджому элементу присваивается ID в виде ID записи в БД, по которому потом
                //можно найти будет запись в БД
                onClickListner.onStateClick(cardBaseViewHolder.itemView.getId());
                cardBaseViewHolder.itemView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            }
        });
    }
    //определение логотипа банка в зависимости от платежной системы
    @Override
    public int imageRes(int paysysID) {
        return MyTextEditMethod.super.imageRes(paysysID);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    //добавление разделителей в номер карты для ее лучшей читаемости
    @Override
    public String cardNumFormat(String cardNum) {
        return MyTextEditMethod.super.cardNumFormat(cardNum);
    }

    //проверка введенных данных
    @Override
    public Boolean cardDataCheck(FragmentManager ft, String cardNum, String cardMonth, String cardYear, String cardCVV) {
        return MyTextEditMethod.super.cardDataCheck(ft, cardNum, cardMonth, cardYear, cardCVV);
    }
}
