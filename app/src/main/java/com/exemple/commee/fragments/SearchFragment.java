package com.exemple.commee.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.exemple.commee.product.Item;
import com.exemple.commee.activities.MainActivity;
import com.exemple.commee.product.ProductsUtils;
import com.exemple.commee.R;
import com.exemple.commee.activities.Splash;

import java.util.ArrayList;


public class SearchFragment extends Fragment {

    private ConstraintLayout[] categories = new ConstraintLayout[4];
    private ArrayList<Item> subProducts = new ArrayList<>();
    private FragmentManager manager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.search_fragment, container, false);

        manager = getFragmentManager();

        categories[0] = view.findViewById(R.id.cat_men);
        categories[1] = view.findViewById(R.id.cat_women);
        categories[2] = view.findViewById(R.id.cat_electronics);
        categories[3] = view.findViewById(R.id.cat_jewelry);

        for (int i = 0; i<categories.length; i++) {
            int finalI = i;
            categories[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String cat = "";
                    switch (finalI) {
                        case 0:
                            cat = "men's clothing";
                            break;
                        case 1:
                            cat = "women's clothing";
                            break;
                        case 2:
                            cat = "electronics";
                            break;
                        case 3:
                            cat = "jewelery";
                            break;
                    }
                    for (Item item: ProductsUtils.getAllProducts()
                         ) {
                        if (item.getCategory().equals(cat)) subProducts.add(item);
                    }

                    ProductsUtils.setProducts(subProducts);
                    getFragmentManager().beginTransaction().replace(R.id.frame_container, new HomeFragment("replace")).commit();
                    MainActivity.back.setVisibility(View.VISIBLE);

                }
            });

        }

        MainActivity.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.beginTransaction().replace(R.id.frame_container, new SearchFragment()).commit();
                MainActivity.back.setVisibility(View.GONE);
            }
        });

        return view;
    }
}
