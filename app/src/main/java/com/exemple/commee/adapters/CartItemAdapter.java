package com.exemple.commee.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.exemple.commee.R;
import com.exemple.commee.product.Item;
import com.exemple.commee.product.ProductsUtils;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder> {

    private Context context;
    private OnItemListener onItemListener;

    public CartItemAdapter(Context context, OnItemListener listener) {
        this.context = context;
        this.onItemListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.cart_item_layout, parent, false), onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item currentItem = ProductsUtils.getItemById(ProductsUtils.getCartList().get(position).first);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        holder.name.setText(currentItem.getName());
        String text = "unit price: " + currentItem.getPrice() + "$";
        Typeface font = ResourcesCompat.getFont(context, R.font.metropolis_thin);
        SpannableString span = new SpannableString(text);
        ForegroundColorSpan pink = new ForegroundColorSpan(Color.rgb(255, 67, 110));
        ForegroundColorSpan grey = new ForegroundColorSpan(Color.rgb(118, 118, 118));
        span.setSpan(grey,  0, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(pink, 12, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.unitPrice.setText(span);

        text = "units: " + ProductsUtils.getCartList().get(position).second;
        span = new SpannableString(text);
        pink = new ForegroundColorSpan(Color.rgb(255, 67, 110));
        grey = new ForegroundColorSpan(Color.rgb(118, 118, 118));
        span.setSpan(grey, 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(pink, 7, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.units.setText(span);

        float totalPrice = ProductsUtils.getCartList().get(position).second * currentItem.getPrice();
        text = "total price: " + totalPrice + "$";
        span = new SpannableString(text);
        span.setSpan(grey, 0, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(pink, 13, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.totalPrice.setText(span);

        Glide.with(context)
                .load(currentItem.getImageUrl())
                .signature(new ObjectKey(2000))
                .into(holder.picture);
    }

    @Override
    public int getItemCount() {
        return ProductsUtils.getCartList().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ConstraintLayout layout;
        TextView name, unitPrice, totalPrice, units;
        ImageView picture;
        private OnItemListener listener;

        public ViewHolder(@NonNull View itemView, OnItemListener listener) {
            super(itemView);
            this.name = itemView.findViewById(R.id.cart_item_name);
            this.unitPrice = itemView.findViewById(R.id.cart_item_unit_price);
            this.totalPrice = itemView.findViewById(R.id.cart_item_total_price);
            this.units = itemView.findViewById(R.id.cart_item_units);
            this.picture = itemView.findViewById(R.id.cart_item_picture);
            itemView.setOnClickListener(this);
            this.listener = listener;

        }

        @Override
        public void onClick(View v) {
            String name = ((TextView) v.findViewById(R.id.cart_item_name)).getText().toString();
            listener.onItemClick(ProductsUtils.getItemPositionByName(name));
        }
    }

    public interface OnItemListener {
        void onItemClick(int position);
    }

}
