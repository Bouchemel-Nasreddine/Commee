package com.exemple.commee.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import com.exemple.commee.fragments.CartFragment;

import java.util.ArrayList;
import java.util.List;

public class DataBaseUtils {

    public static void addProductToDataBase(Context context, Long id, int units) {
        CartDataBaseHelper dbHelper = new CartDataBaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues values =new ContentValues();
        values.put(CartContract.CartEntry._ID, id);
        values.put(CartContract.CartEntry.UNITS, units);
        db.insert(CartContract.CartEntry.TABLE_NAME, null, values);
    }

    public static void updateProductOnDataBase(Context context, Long id, int units) {
        CartDataBaseHelper dbHelper = new CartDataBaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String SQL_UPDATE_UNITS = "UPDATE " + CartContract.CartEntry.TABLE_NAME +
                " SET " + CartContract.CartEntry.UNITS + " = " + units + " WHERE " +
                CartContract.CartEntry._ID + " = " + id + ";";

        db.execSQL(SQL_UPDATE_UNITS);
        CartFragment.notifyAdapter();
    }

    public static void removeFromDataBase(Context context, Long id) {
        CartDataBaseHelper dbHelper = new CartDataBaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String SQL_REMOVE_PRODUCT = "DELETE FROM " + CartContract.CartEntry.TABLE_NAME +
                " WHERE " + CartContract.CartEntry._ID + " = " + id + ";";

        db.execSQL(SQL_REMOVE_PRODUCT);
        CartFragment.notifyAdapter();
    }


    public static void clearDataBase(Context context) {
        CartDataBaseHelper dbHelper = new CartDataBaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String SQL_CLEAR_PRODUCTS = "DELETE FROM " + CartContract.CartEntry.TABLE_NAME  + ";";
        db.execSQL(SQL_CLEAR_PRODUCTS);
        CartFragment.notifyAdapter();
    }

    public static List<Pair<Long, Integer>> getFullListFromDataBase(Context context) {
        List<Pair<Long, Integer>> cart = new ArrayList<>();

        String [] projection = {
                CartContract.CartEntry._ID,
                CartContract.CartEntry.UNITS
        };

        CartDataBaseHelper dbHelper = new CartDataBaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(CartContract.CartEntry.TABLE_NAME, projection, null,
                null, null, null, null);
        int idIndex = cursor.getColumnIndex(CartContract.CartEntry._ID);
        int unitsIndex = cursor.getColumnIndex(CartContract.CartEntry.UNITS);
        int count = cursor.getCount();
        if (count == 0) return cart;

        cursor.moveToFirst();

        cart.add(new Pair<>(cursor.getLong(idIndex), cursor.getInt(unitsIndex)));
        try {
            while (cursor.moveToNext()) {
                cart.add(new Pair<>(cursor.getLong(idIndex), cursor.getInt(unitsIndex)));
            }
        } finally {
            cursor.close();
        }
        return cart;
    }

}
