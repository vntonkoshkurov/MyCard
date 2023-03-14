package com.example.mycard;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "cards.db";  // название бд
    public static final int DATABASE_VERSION = 1;           // версия базы данных
    public static final String TABLE = "cards";             // название таблицы в бд карт
    public static final String TABLE_BANKS = "banks";       // название таблицы в бд банков

    // названия столбцов для БД "Карты"
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CARDNUM = "cardnum";
    public static final String COLUMN_CARDMONTH = "cardmonth";
    public static final String COLUMN_CARDYEAR = "cardyear";
    public static final String COLUMN_CARDCVV = "cardcvv";
    public static final String COLUMN_PAYSYS = "paysys";
    public static final String COLUMN_DESCRIPTION = "descr";
    public static final String COLUMN_ITEMINLIST = "itenum";
    public static final String COLUMN_BANK_ID = "bank_id";
    //названия столбцов для БД "Банки"
    public static final String COLUMN_BANK_NAME = "bank_name";
    public static final String COLUMN_CARD_COLOR = "card_color";
    public static final String COLUMN_BANK_COLOR = "bank_color";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_BANKS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_ITEMINLIST + " INTEGER, "
                + COLUMN_BANK_COLOR + " INTEGER, "
                + COLUMN_BANK_NAME + " VARCHAR (40));");
        db.execSQL("CREATE TABLE " + TABLE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CARDNUM + " TEXT, "
                + COLUMN_CARDMONTH + " TEXT, "
                + COLUMN_CARDYEAR + " TEXT, "
                + COLUMN_CARDCVV + " TEXT, "
                + COLUMN_PAYSYS + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT, "
                + COLUMN_ITEMINLIST + " INTEGER, "
                + COLUMN_BANK_ID + " INTEGER, "
                + COLUMN_CARD_COLOR + " INTEGER, "
                + "FOREIGN KEY (" + COLUMN_BANK_ID + ") REFERENCES "
                + TABLE_BANKS + " (" + COLUMN_BANK_ID + "));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE);
        onCreate(db);
    }

}
