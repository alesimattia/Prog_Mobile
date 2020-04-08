package com.example.progetto_mobile;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Tratta.db";
    public static final String TABLE_NAME = "tratta_table";
    public static final String COL_1= "tratt";
    public static final String COL_2 = "orario";
    public static final String COL_3 = "posti_disp";
    public static final String COL_4 = "conducente";
    public static final String COL_5 = "num_cond";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }
}
