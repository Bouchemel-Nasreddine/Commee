package com.exemple.commee.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.exemple.commee.database.CartContract;

public class CartDataBaseHelper extends SQLiteOpenHelper {

    private static final String NAME = "cart.db";
    private static final int VERSION = 1;

    public CartDataBaseHelper(@Nullable Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_CART_TABLE = "CREATE TABLE " + CartContract.CartEntry.TABLE_NAME + "("
                + CartContract.CartEntry._ID + " LONG PRIMARY KEY NOT NULL, "
                + CartContract.CartEntry.UNITS + " INTEGER NOT NULL" + ");";

        db.execSQL(SQL_CREATE_CART_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}