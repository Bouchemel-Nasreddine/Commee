package com.exemple.commee.database;

import android.provider.BaseColumns;

public class CartContract {

    public static abstract class CartEntry implements BaseColumns {

        public static final String TABLE_NAME = "cart";

        public static final String _ID = BaseColumns._ID;
        public static final String UNITS = "units";

    }

}
