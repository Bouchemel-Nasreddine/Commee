package com.exemple.commee.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.exemple.commee.product.Item;
import com.exemple.commee.product.ProductsUtils;
import com.exemple.commee.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class ProductPageActivity extends AppCompatActivity {

    private ImageView picture;
    private MaterialButton back;
    private TextView name, price, description;
    private ExtendedFloatingActionButton productUnits, plus, mince;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details_activity);

        int position = getIntent().getIntExtra("position", 0);
        Item item = ProductsUtils.getProducts().get(position);

        picture = findViewById(R.id.product_details_picture);
        back = findViewById(R.id.details_back);
        name = findViewById(R.id.product_deatils_name);
        price = findViewById(R.id.product_details_price);
        description = findViewById(R.id.product_details_description);
        productUnits = findViewById(R.id.product_units);
        plus = findViewById(R.id.add_unit);
        mince = findViewById(R.id.take_unit);

        Glide.with(ProductPageActivity.this).load(item.getImageUrl()).signature(new ObjectKey(2000)).into(picture);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        name.setText(item.getName());
        price.setText(Float.toString(item.getPrice())+"$");
        description.setText(item.getDescription());
        productUnits.setText(String.valueOf(ProductsUtils.getProductUnits(item.getID())));


        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nbr = Integer.parseInt(productUnits.getText().toString()) + 1 ;
                productUnits.setText(String.valueOf(nbr));
                ProductsUtils.addUnitToCart(getApplicationContext(), ProductsUtils.getProducts().get(position).getID(), Integer.parseInt(productUnits.getText().toString()));
            }
        });
        mince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nbr = Integer.parseInt(productUnits.getText().toString());
                if (nbr>0) {
                    nbr--;
                    productUnits.setText(String.valueOf(nbr));
                }
                if (nbr == 0) {
                    ProductsUtils.removeUnitFromCart(getApplicationContext(), item.getID());
                }
            }
        });

    }

}
