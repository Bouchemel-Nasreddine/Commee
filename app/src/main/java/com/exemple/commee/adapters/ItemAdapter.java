package com.exemple.commee.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.exemple.commee.R;
import com.exemple.commee.product.Item;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Item> list;
    private OnItemListener onItemListener;

    public ItemAdapter(Context context, ArrayList<Item> list, OnItemListener listener) {
        this.context = context;
        this.list = list;
        this.onItemListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_layout, parent, false), onItemListener);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(list.get(position).getName());
        holder.price.setText(list.get(position).getPrice() + "$");
        CircularProgressDrawable progressDrawable = new CircularProgressDrawable(this.context);
        Glide.with(this.context)
                .load(list.get(position).getImageUrl())
                .placeholder(progressDrawable)
                .signature(new ObjectKey(2000))
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RoundedImageView image;
        TextView title, price;
        private OnItemListener listener;

        public ViewHolder(@NonNull View itemView, OnItemListener listener) {
            super(itemView);
            image = itemView.findViewById(R.id.item_image);
            title = itemView.findViewById(R.id.item_title);
            price = itemView.findViewById(R.id.item_price);
            itemView.setOnClickListener(this);
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnItemListener {
        void onItemClick(int position);
    }


}
