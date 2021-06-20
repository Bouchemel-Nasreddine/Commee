package com.exemple.commee.product;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.exemple.commee.APIUtils;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class ItemLoader extends AsyncTaskLoader<ArrayList<Item>> {

    private String mode = "";

    public ItemLoader(@NonNull Context context) {
        super(context);
    }

    public ItemLoader(Context context, String mode) {
        super(context);
        this.mode = mode;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public ArrayList<Item> loadInBackground() {
        try {
            if (mode.equals("stop")) return null;
            Log.d("API response status", "loadInBackground: " + APIUtils.getSampleResponse());
            String response = APIUtils.makeHttpRequest();
            ArrayList<Item> data = APIUtils.extractDataFromJSONResponse(response);
            return data;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Log.d("err", "loadInBackground: " + e.getMessage());
        }
        return null;
    }
}
