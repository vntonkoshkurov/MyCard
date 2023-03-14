package com.example.mycard;

import static com.example.mycard.DatabaseHelper.COLUMN_ID;
import static com.example.mycard.DatabaseHelper.TABLE;
import static com.example.mycard.DatabaseHelper.TABLE_BANKS;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Toast;

public class BankBaseProcessor {
    private View view;
    private Toast toast;
    private Cursor cursor;

    public BankBaseProcessor (View view) {this.view = view;}

    //метод обновыления данных банка в БД
    public boolean baseBankUpdate (String bankID, String bankName, int bankColor){
        DatabaseHelper databaseHelper = new DatabaseHelper(view.getContext());
        SQLiteDatabase database  = databaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        try {
            cv.put(DatabaseHelper.COLUMN_BANK_NAME, bankName);
            cv.put(DatabaseHelper.COLUMN_BANK_COLOR, bankColor);
            database.update(TABLE_BANKS, cv, COLUMN_ID + "=" + bankID, null);
            database.close();
            toast = Toast.makeText(view.getContext(), view.getContext().getString(R.string.edit_result_good), Toast.LENGTH_LONG);
            toast.show();
            return  true;
        }
        catch (Exception e) {
            toast = Toast.makeText(view.getContext(), view.getContext().getString(R.string.edit_result_bad), Toast.LENGTH_LONG);
            toast.show();
            return false;
        }
    }

    //метод добавления данных банка в БД
    public boolean baseBankAdd (String bankName, int bankColor){
        DatabaseHelper databaseHelper = new DatabaseHelper(view.getContext());
        SQLiteDatabase database  = databaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        try {
            cursor = database.query(DatabaseHelper.TABLE, null, null, null, null, null, null);
            //cursor.moveToLast();
            cv.put(DatabaseHelper.COLUMN_BANK_NAME, bankName);
            cv.put(DatabaseHelper.COLUMN_BANK_COLOR, bankColor);
            cv.put(DatabaseHelper.COLUMN_ITEMINLIST, cursor.getCount() + 1);
            database.insert(DatabaseHelper.TABLE_BANKS, null, cv);
            database.close();
            toast = Toast.makeText(view.getContext(), view.getContext().getString(R.string.add_result_good), Toast.LENGTH_LONG);
            toast.show();
            return true;
        }
        catch (Exception e) {
            toast = Toast.makeText(view.getContext(), view.getContext().getString(R.string.add_result_bad), Toast.LENGTH_LONG);
            toast.show();
            return true;
        }
    }

    //метод нумерует записи в БД, согласно их положения на экране, потому что выводе карт на экран данные предварительно
    //сортируются
    @SuppressLint("RestrictedApi")
    public void baseItemPos (int position_target, int position_dragged) {
        DatabaseHelper databaseHelper = new DatabaseHelper(view.getContext());
        SQLiteDatabase database  = databaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cursor = database.query(DatabaseHelper.TABLE_BANKS, null, null, null, null, null, DatabaseHelper.COLUMN_ITEMINLIST);
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
                database.update(DatabaseHelper.TABLE_BANKS, cv, COLUMN_ID + "=" + cursor.getInt(colID), null);
                if (pos == position_dragged) {
                    cv.put(DatabaseHelper.COLUMN_ITEMINLIST, position_target);
                    database.update(DatabaseHelper.TABLE_BANKS, cv, COLUMN_ID + "=" + cursor.getInt(colID), null);
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
                    database.update(DatabaseHelper.TABLE_BANKS, cv, COLUMN_ID + "=" + cursor.getInt(colID), null);
                } else {
                    cv.clear();
                    cv.put(DatabaseHelper.COLUMN_ITEMINLIST, pos - 1);
                    database.update(DatabaseHelper.TABLE_BANKS, cv, COLUMN_ID + "=" + cursor.getInt(colID), null);
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
                database.update(DatabaseHelper.TABLE_BANKS, cv, COLUMN_ID + "=" + cursor.getInt(colID), null);
                cursor.moveToNext();
            }
        }
        database.close();
    }
}
