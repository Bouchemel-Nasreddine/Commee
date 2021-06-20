package com.exemple.commee;

import com.exemple.commee.product.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public final class APIUtils {

    private final static String basicUrl = "https://fakestoreapi.com/products";
    private final static String basicCategoriesUrl = "https://fakestoreapi.com/products/categories";

    public static String getSampleResponse() throws IOException {
        URL url = new URL(basicUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        return connection.getResponseMessage();
    }

    public static String makeHttpRequest() throws IOException {
        URL url = new URL(basicUrl);
        HttpsURLConnection urlCon = (HttpsURLConnection) url.openConnection();
        urlCon.setRequestMethod("GET");
        urlCon.setReadTimeout(20000);
        urlCon.setConnectTimeout(20000);
        urlCon.connect();
        InputStream inputStream = urlCon.getInputStream();
        String str = readFromInputStream(inputStream);
        if (urlCon != null) {
            urlCon.disconnect();
        }
        return str;
    }

    public static String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder response = new StringBuilder();
        response.append("{ \"response\":");
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader buffer = new BufferedReader(inputStreamReader);
            String line = buffer.readLine();
            while (line != null) {
                response.append(line);
                line = buffer.readLine();
            }
        }
        response.append("}");
        return response.toString();
    }

    public static ArrayList<Item> extractDataFromJSONResponse(String response) throws JSONException {
        ArrayList<Item> data = new ArrayList<>();

        JSONObject jObj = new JSONObject(response);
        JSONArray jArray = jObj.getJSONArray("response");

        for (int i = 0; i < jArray.length(); i++) {
            JSONObject element = jArray.getJSONObject(i);
            long id = Long.parseLong(element.getString("id"));
            String title = element.getString("title");
            String category = element.getString("category");
            long price = element.getLong("price");
            String description = element.getString("description");
            String image = element.getString("image");

            data.add(new Item(title, category, id, price, image, description));
        }

        return data;
    }

}
