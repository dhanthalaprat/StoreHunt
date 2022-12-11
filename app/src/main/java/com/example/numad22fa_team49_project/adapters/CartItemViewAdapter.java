package com.example.numad22fa_team49_project.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.numad22fa_team49_project.CartActivity;
import com.example.numad22fa_team49_project.R;
import com.example.numad22fa_team49_project.models.GeneralProductHome;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartItemViewAdapter extends RecyclerView.Adapter<CartItemViewAdapter.CartItemViewHolder> {
    Context context;
    ArrayList<GeneralProductHome> products;

    public CartItemViewAdapter(Context context, ArrayList<GeneralProductHome> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public CartItemViewAdapter.CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartItemViewHolder(LayoutInflater.from(context).inflate(R.layout.cart_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewAdapter.CartItemViewHolder holder, int position) {
        GeneralProductHome product = products.get(position);
        holder.productName.setText(product.getName());
        Picasso.get().load(product.getImage_uri()).into(holder.productImage);
        holder.productCost.setText(product.getPrice());
        holder.productRatingText.setText(product.getRating());
        holder.ratingBar.setRating(Float.parseFloat(product.getRating()));
        holder.deleteFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG_324", "onClick: "+products.get(holder.getAdapterPosition()).getName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class CartItemViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage, deleteFromCart;
        TextView productName, productCost, productRatingText;
        RatingBar ratingBar;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.cart_product_image);
            productName = itemView.findViewById(R.id.cart_item_name);
            ratingBar = itemView.findViewById(R.id.cart_item_rating);
            productCost = itemView.findViewById(R.id.cart_item_price);
            productRatingText = itemView.findViewById(R.id.rating_cart_item_text);
            deleteFromCart = itemView.findViewById(R.id.delete_from_cart);




        }
    }
}
