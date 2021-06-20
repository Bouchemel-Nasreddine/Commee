package com.exemple.commee.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.exemple.commee.activities.MainActivity;
import com.exemple.commee.product.Item;
import com.exemple.commee.adapters.ItemAdapter;
import com.exemple.commee.product.ItemLoader;
import com.exemple.commee.activities.ProductPageActivity;
import com.exemple.commee.product.ProductsUtils;
import com.exemple.commee.R;
import com.exemple.commee.activities.Splash;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements ItemAdapter.OnItemListener, LoaderManager.LoaderCallbacks<ArrayList<Item>> {

    private String mode;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    ItemAdapter adapter;
    private View view = null;

    public HomeFragment() {
        this.mode = "new";
    }

    public HomeFragment(String mode) {
        this.mode = mode;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        if (view == null) {
            view = inflater.inflate(R.layout.home_fragment, container, false);

            /**list.add(new Item("table", 2000, 12, R.drawable.iz));
             list.add(new Item("table", 2000, 12, R.drawable.jimmy));
             list.add(new Item("table", 2000, 12, R.drawable.silviu));
             list.add(new Item("table", 2000, 12, R.drawable.picture));
             list.add(new Item("table", 2000, 12, R.drawable.sam));
             list.add(new Item("table", 2000, 12, R.drawable.kareya));
             list.add(new Item("table", 2000, 12, R.drawable.ian));
             list.add(new Item("table", 2000, 12, R.drawable.jimmy));
             list.add(new Item("table", 2000, 12, R.drawable.ian));
             list.add(new Item("table", 2000, 12, R.drawable.silviu));
             list.add(new Item("table", 2000, 12, R.drawable.sam));
             list.add(new Item("table", 2000, 12, R.drawable.ian));
             list.add(new Item("table", 2000, 12, R.drawable.kareya));
             list.add(new Item("table", 2000, 12, R.drawable.picture));
             list.add(new Item("table", 2000, 12, R.drawable.silviu));**/

            recyclerView = view.findViewById(R.id.recycler_view);
            progressBar = view.findViewById(R.id.progress);


            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

            adapter = new ItemAdapter(getContext(), ProductsUtils.getProducts(), this);
            recyclerView.setAdapter(adapter);

            LoaderManager manager = LoaderManager.getInstance(this);
            manager.initLoader(0, null, this);

        }

        return view;
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), ProductPageActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    @NonNull
    @Override
    public Loader<ArrayList<Item>> onCreateLoader(int id, @Nullable Bundle args) {
        if (mode.equals("replace")) return new Loader<>(getContext());
        if (!ProductsUtils.getProducts().isEmpty()) return new ItemLoader(getContext(), "stop");
        progressBar.setVisibility(View.VISIBLE);
        return new ItemLoader(getContext());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Item>> loader, ArrayList<Item> data) {
        if (mode.equals("new") && data != null) {
            ProductsUtils.setAllProducts(data);
            ProductsUtils.setProducts(data);
        } else if (!mode.equals("replace")) {
            ProductsUtils.setProducts(ProductsUtils.getAllProducts());
        }
        progressBar.setVisibility(View.INVISIBLE);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Item>> loader) {

    }

}
