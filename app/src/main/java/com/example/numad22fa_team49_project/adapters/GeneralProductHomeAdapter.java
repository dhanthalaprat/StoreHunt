package com.example.numad22fa_team49_project.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.numad22fa_team49_project.R;
import com.example.numad22fa_team49_project.models.GeneralProductHome;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GeneralProductHomeAdapter extends RecyclerView.Adapter<GeneralProductHomeAdapter.GeneralProductHomeViewHolder> {

    Context context;
    ArrayList<GeneralProductHome> products;

    public GeneralProductHomeAdapter(Context context) {
        this.context = context;
    }

    public GeneralProductHomeAdapter(Context context, ArrayList<GeneralProductHome> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public GeneralProductHomeAdapter.GeneralProductHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GeneralProductHomeAdapter.GeneralProductHomeViewHolder(LayoutInflater.from(context).inflate(R.layout.general_product_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull GeneralProductHomeAdapter.GeneralProductHomeViewHolder holder, int position) {
        GeneralProductHome productHome = products.get(position);
        holder.name.setText(productHome.getName());
        holder.price.setText(productHome.getPrice());
        Log.d("TAG_342", "onBindViewHolder: "+productHome.getImage_uri());
        Picasso.get().load(productHome.getImage_uri()).into(holder.image);
//        Picasso.with(iv.getContext())
//                .load(imageUrl)
//                .fit()
//                .centerCrop()
//                .into(holder.photo_thumbnail);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class GeneralProductHomeViewHolder extends RecyclerView.ViewHolder {

        TextView name, price;
        ImageView image;
        public GeneralProductHomeViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.prod_name);
            price = itemView.findViewById(R.id.prod_price);
            image = itemView.findViewById(R.id.prod_image);
        }
    }
}
