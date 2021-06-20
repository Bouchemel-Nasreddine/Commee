package com.exemple.commee.product;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.exemple.commee.database.DataBaseUtils;

import java.util.ArrayList;
import java.util.List;

public class ProductsUtils {

    private static ArrayList<Item> products = new ArrayList<>();
    private static ArrayList<Item> allProducts = new ArrayList<>();
    private static ArrayList<Pair<Long, Integer>> cartList = new ArrayList<>();

    public static ArrayList<Item> getProducts() {
        return products;
    }

    public static ArrayList<Item> getAllProducts() {
        return allProducts;
    }

    public static void setProducts(ArrayList<Item> products) {
        ProductsUtils.products.clear();
        ProductsUtils.products.addAll(products);
    }

    public static void setAllProducts(ArrayList<Item> allProducts) {
        ProductsUtils.allProducts.clear();
        ProductsUtils.allProducts.addAll(allProducts);
    }

    public static ArrayList<Pair<Long, Integer>> getCartList() {
        return cartList;
    }

    public static void addUnitToCart(Context context, long id, int nbr) {
        int position = getProductPosition(id);
        if (position == -1) {
            cartList.add(new Pair<>(id, nbr));
            DataBaseUtils.addProductToDataBase(context, id, nbr);
        } else {
            cartList.remove(position);
            cartList.add(position, new Pair<>(id, nbr));
            DataBaseUtils.updateProductOnDataBase(context, id, nbr);
        }
        Log.d("cart :", cartList.toString());
    }

    public static void removeUnitFromCart(Context context, long id) {
        int position = getProductPosition(id);
        if (getProductPosition(id) != -1) cartList.remove(position);
        DataBaseUtils.removeFromDataBase(context, id);
        Log.d("cart :", cartList.toString());
    }

    public static int getProductPosition(long id) {
        for (int i = 0; i<cartList.size(); i++) {
            if (cartList.get(i).first.equals(id)) return i;
        }
        return -1;
    }

    public static int getProductUnits(long id) {
        for (int i = 0; i<cartList.size(); i++) {
            if (cartList.get(i).first.equals(id)) return cartList.get(i).second;
        }
        return 0;
    }

    public static void setCartList(List<Pair<Long, Integer>> list) {
        cartList.clear();
        cartList.addAll(list);
    }

    public static Item getItemById(long id) {
        for (Item item: allProducts) {
            if (item.getID() == id) return item;
        }
        return null;
    }

    public static int getItemPositionByName(String name) {
        for (int i = 0; i<products.size(); i++) {
            if (products.get(i).getName() == name) return i;
        }
        return -1;
    }

    public static String getTotal() {
        int total = 0;
        for (Pair<Long, Integer> i: cartList) {
            Item item = getItemById(i.first);
            total += item.getPrice();
        }
        return String.valueOf(total);
    }

    public static void clearCartList(Context context) {
        cartList.clear();
        DataBaseUtils.clearDataBase(context);
    }

}
