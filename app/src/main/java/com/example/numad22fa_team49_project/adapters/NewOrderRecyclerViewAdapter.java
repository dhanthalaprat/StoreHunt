package com.example.numad22fa_team49_project.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.numad22fa_team49_project.OrderDetailsActivity;
import com.example.numad22fa_team49_project.R;
import com.example.numad22fa_team49_project.models.NewOrderModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewOrderRecyclerViewAdapter extends RecyclerView.Adapter<NewOrderRecyclerViewAdapter.NewOrderViewHolder>{

    Context context;
    ArrayList<NewOrderModel> orders;

    public NewOrderRecyclerViewAdapter(Context context, ArrayList<NewOrderModel> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public NewOrderRecyclerViewAdapter.NewOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewOrderViewHolder(LayoutInflater.from(context).inflate(R.layout.seller_new_order_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewOrderRecyclerViewAdapter.NewOrderViewHolder holder, int position) {
        NewOrderModel model = orders.get(position);
        Picasso.get().load(model.getImage_uri()).into(holder.imageView);
        holder.orderProductName.setText(model.getProductName());
        holder.orderCost.setText(model.getProductPrice());

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class NewOrderViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView orderProductName, ratingText, orderCost;
        RatingBar rating;
        CardView orderItem;

        public NewOrderViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.order_product_image);
            orderProductName = itemView.findViewById(R.id.order_item_name);
//            ratingText = itemView.findViewById(R.id.order_rating_text);
            orderCost = itemView.findViewById(R.id.order_item_price);
            orderItem = itemView.findViewById(R.id.order_item);

//            rating = itemView.findViewById(R.id.order_item_rating);
            orderItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, OrderDetailsActivity.class));
                }
            });
        }
    }
}
