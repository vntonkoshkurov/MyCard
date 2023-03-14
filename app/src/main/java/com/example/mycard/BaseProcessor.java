package com.example.mycard;

import static com.example.mycard.DatabaseHelper.COLUMN_ID;
import static com.example.mycard.DatabaseHelper.TABLE;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class BaseProcessor {

    private View view;
    private Toast toast;
    private Cursor cursor;

    public BaseProcessor(View view) {
        this.view = view;
    }

    //метод обновления данных карты в БД
    public boolean baseCardUpdate (String cardnum, String cardmonth, String cardyear,
                                   String cardcvv, String carddescr, String cardid, String cardpaysys,
                                   int cardColor, int bankID){
        DatabaseHelper databaseHelper = new DatabaseHelper(view.getContext());
        SQLiteDatabase database  = databaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        try{
            cv.put(DatabaseHelper.COLUMN_CARDNUM, cardnum);
            cv.put(DatabaseHelper.COLUMN_CARDMONTH, cardmonth);
            cv.put(DatabaseHelper.COLUMN_CARDYEAR, cardyear);
            cv.put(DatabaseHelper.COLUMN_CARDCVV, cardcvv);
            cv.put(DatabaseHelper.COLUMN_PAYSYS, cardpaysys);
            cv.put(DatabaseHelper.COLUMN_DESCRIPTION, carddescr);
            cv.put(DatabaseHelper.COLUMN_CARD_COLOR, cardColor);
            cv.put(DatabaseHelper.COLUMN_BANK_ID, bankID);
            database.update(TABLE, cv, COLUMN_ID + "=" + cardid, null);
            database.close();
            toast = Toast.makeText(view.getContext(), view.getContext().getString(R.string.edit_result_good), Toast.LENGTH_LONG);
            toast.show();
            return true;
        }
        catch (Exception e) {
            toast = Toast.makeText(view.getContext(), view.getContext().getString(R.string.edit_result_bad), Toast.LENGTH_LONG);
            toast.show();
            return false;
        }
    }

    //Метод добавления новой карты в БД
    public boolean baseCardAdd (String cardnum, String cardmonth, String cardyear,
                                String cardcvv, String carddescr, String cardpaysys,
                                int cardColor, int bankID) {
        DatabaseHelper databaseHelper = new DatabaseHelper(view.getContext());
        SQLiteDatabase database  = databaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        try {
            cursor = database.query(DatabaseHelper.TABLE, null, null, null, null, null, null);
            cursor.moveToLast();
            cv.put(DatabaseHelper.COLUMN_CARDNUM, cardnum);
            cv.put(DatabaseHelper.COLUMN_CARDMONTH, String.valueOf(cardmonth));
            cv.put(DatabaseHelper.COLUMN_CARDYEAR, String.valueOf(cardyear));
            cv.put(DatabaseHelper.COLUMN_CARDCVV, String.valueOf(cardcvv));
            cv.put(DatabaseHelper.COLUMN_PAYSYS, String.valueOf(cardpaysys));
            cv.put(DatabaseHelper.COLUMN_DESCRIPTION, String.valueOf(carddescr));
            cv.put(DatabaseHelper.COLUMN_CARD_COLOR, cardColor);
            cv.put(DatabaseHelper.COLUMN_BANK_ID, bankID);
            cv.put(DatabaseHelper.COLUMN_ITEMINLIST, cursor.getCount() + 1);
            database.insert(DatabaseHelper.TABLE, null, cv);
            database.close();
            toast = Toast.makeText(view.getContext(), view.getContext().getString(R.string.add_result_good), Toast.LENGTH_LONG);
            toast.show();
            return true;
        }
        catch (Exception e) {
            toast = Toast.makeText(view.getContext(), view.getContext().getString(R.string.add_result_bad), Toast.LENGTH_LONG);
            toast.show();
            return false;
        }
    }

    //метод удаления записи из БД
    public boolean baseClear(){
        DatabaseHelper databaseHelper = new DatabaseHelper(view.getContext());
        SQLiteDatabase database  = databaseHelper.getWritableDatabase();
        try {
            database.delete(TABLE, null, null);
            database.close();
            toast = Toast.makeText(view.getContext(), view.getContext().getString(R.string.cardbase_delete_good), Toast.LENGTH_LONG);
            toast.show();
            return true;
        }
        catch (Exception e) {
            toast = Toast.makeText(view.getContext(), view.getContext().getString(R.string.cardbase_delete_bad), Toast.LENGTH_LONG);
            toast.show();
            return false;
        }

    }

    //метод нумерует записи в БД, согласно их положения на экране, потому что выводе карт на экран данные предварительно
    //сортируются
    @SuppressLint("RestrictedApi")
    public void baseItemPos (int position_target, int position_dragged) {
        DatabaseHelper databaseHelper = new DatabaseHelper(view.getContext());
        SQLiteDatabase database  = databaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cursor = database.query(DatabaseHelper.TABLE, null, null, null, null, null, DatabaseHelper.COLUMN_ITEMINLIST);
        int colID = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
        int pos;
        //обаботка, если карточка была перемещена снизу вверх
        if (position_target < position_dragged) {
            //доходим от начала до позиции назначения
            for (pos = 0; pos <= position_target; pos++) {
                if (pos == 0) {
                    cursor.moveToFirst();
                } else {
                    cursor.moveToNext();
                }
            }
            //увеличиваем значения каждой последующей записи до позиции откуда началось перемещение
            for (pos = position_target; pos <= position_dragged; pos++) {
                if (pos != position_target) {
                    cursor.moveToNext();
                }
                cv.clear();
                cv.put(DatabaseHelper.COLUMN_ITEMINLIST, pos + 1);
                database.update(TABLE, cv, COLUMN_ID + "=" + cursor.getInt(colID), null);
                if (pos == position_dragged) {
                    cv.put(DatabaseHelper.COLUMN_ITEMINLIST, position_target);
                    database.update(TABLE, cv, COLUMN_ID + "=" + cursor.getInt(colID), null);
                }
            }
        }
        //обаботка, если карточка была перемещена сверху вниз
        if (position_target > position_dragged) {
            //доходим от начала до позиции назначения
            for (pos = 0; pos <= position_dragged; pos ++) {
                if (pos == 0) {
                    cursor.moveToFirst();
                } else {
                    cursor.moveToNext();
                }
            }
            //уменьшаем значения каждой последующей записи до позиции куда было произведено перемещение
            for (pos = position_dragged; pos <= position_target; pos ++) {
                if (pos == position_dragged) {
                    cv.put(DatabaseHelper.COLUMN_ITEMINLIST, position_target);
                    database.update(TABLE, cv, COLUMN_ID + "=" + cursor.getInt(colID), null);
                } else {
                    cv.clear();
                    cv.put(DatabaseHelper.COLUMN_ITEMINLIST, pos - 1);
                    database.update(TABLE, cv, COLUMN_ID + "=" + cursor.getInt(colID), null);
                }
                cursor.moveToNext();
            }
        }
        //нумерация записей, если запись была удалена
        //после удаления записи появляется пропуск в нумерации,
        // который в дальнейшем может быть вреден для сортировки
        if (position_target == 0 && position_dragged == 0) {
            cursor.moveToFirst();
            for (pos = 1; pos<= cursor.getCount(); pos++) {
                cv.put(DatabaseHelper.COLUMN_ITEMINLIST, pos);
                database.update(TABLE, cv, COLUMN_ID + "=" + cursor.getInt(colID), null);
                cursor.moveToNext();
            }
        }
        database.close();
    }

    public void baseBankIdChange (String position, int bankID){
        DatabaseHelper databaseHelper = new DatabaseHelper(view.getContext());
        SQLiteDatabase database  = databaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cursor = database.query(DatabaseHelper.TABLE, null, null, null, null, null, DatabaseHelper.COLUMN_ITEMINLIST);

        int cardItemPos = cursor.getColumnIndex(DatabaseHelper.COLUMN_ITEMINLIST);
        int colID = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);

        cursor.moveToFirst();
        while (cursor.getInt(cardItemPos)!= Integer.parseInt(position)){
            cursor.moveToNext();
        }
        cv.put(DatabaseHelper.COLUMN_BANK_ID, bankID);
        database.update(TABLE, cv, COLUMN_ID + "=" + String.valueOf(cursor.getInt(colID)), null);
        database.close();
    }
}
