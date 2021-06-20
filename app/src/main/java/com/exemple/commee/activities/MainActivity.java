package com.exemple.commee.activities;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.exemple.commee.product.ProductsUtils;
import com.exemple.commee.R;
import com.exemple.commee.database.DataBaseUtils;
import com.exemple.commee.fragments.CartFragment;
import com.exemple.commee.fragments.HomeFragment;
import com.exemple.commee.fragments.ProfileFragment;
import com.exemple.commee.fragments.SearchFragment;
import com.google.android.material.button.MaterialButton;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity implements ChipNavigationBar.OnItemSelectedListener {

    public static ChipNavigationBar navBar;
    public static MaterialButton back;
    private boolean doubleBackToExitPressedOnce = false;
    private int currentFragment = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Splash.changeStatusBarColor(MainActivity.this, R.color.light_grey);

//        CartDataBaseHelper dbHelper = new CartDataBaseHelper(getApplicationContext());
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        String SQL_SHOW_ELEMENT = "SELECT * FROM " + CartContract.CartEntry.TABLE_NAME + " WHERE " +
//                CartContract.CartEntry._ID + " = " + 1;
//        db.execSQL(SQL_SHOW_ELEMENT);

        ProductsUtils.setCartList(DataBaseUtils.getFullListFromDataBase(MainActivity.this));

        back = findViewById(R.id.back);
        navBar = findViewById(R.id.nav_view);
        navBar.setOnItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().add(R.id.frame_container, new HomeFragment()).commit();
        navBar.setItemSelected(R.id.home, true);

    }

    @Override
    public void onItemSelected(int i) {
        back.setVisibility(View.GONE);
        Fragment selectedFragment = null;
        int nextTransition = 0;
        int currentTransition = 0;
        switch (i) {
            case R.id.home:
                selectedFragment = new HomeFragment();
                nextTransition = R.anim.slide_in_left;
                currentTransition = R.anim.slide_out_right;
                currentFragment = 0;
                Splash.changeStatusBarColor(MainActivity.this, R.color.light_grey);
                break;
            case R.id.search:
                selectedFragment = new SearchFragment();
                if (currentFragment < 1) {
                    nextTransition = R.anim.slide_in_right;
                    currentTransition = R.anim.slide_out_left;
                } else {
                    nextTransition = R.anim.slide_in_left;
                    currentTransition = R.anim.slide_out_right;
                }
                currentFragment = 1;
                Splash.changeStatusBarColor(MainActivity.this, R.color.light_grey);
                break;
            case R.id.cart:
                selectedFragment = new CartFragment();
                if (currentFragment < 2) {
                    nextTransition = R.anim.slide_in_right;
                    currentTransition = R.anim.slide_out_left;
                } else {
                    nextTransition = R.anim.slide_in_left;
                    currentTransition = R.anim.slide_out_right;
                }
                currentFragment = 2;
                Splash.changeStatusBarColor(MainActivity.this, R.color.light_grey);
                break;
            case R.id.profile:
                selectedFragment = new ProfileFragment();
                nextTransition = R.anim.slide_in_right;
                currentTransition = R.anim.slide_out_left;
                currentFragment = 3;
                Splash.changeStatusBarColor(MainActivity.this, R.color.white);
                break;
        }

        getSupportFragmentManager().beginTransaction().setCustomAnimations(nextTransition, currentTransition).replace(R.id.frame_container, selectedFragment).commit();

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

}