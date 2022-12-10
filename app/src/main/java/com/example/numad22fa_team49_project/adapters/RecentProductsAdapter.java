package com.example.numad22fa_team49_project.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.numad22fa_team49_project.ProductViewActivity;
import com.example.numad22fa_team49_project.R;
import com.example.numad22fa_team49_project.models.GeneralProductHome;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecentProductsAdapter extends RecyclerView.Adapter<RecentProductsAdapter.RecentProductsViewHolder>{

    Context context;
    ArrayList<GeneralProductHome> products;

    public RecentProductsAdapter(Context context, ArrayList<GeneralProductHome> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public RecentProductsAdapter.RecentProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecentProductsViewHolder(LayoutInflater.from(context).inflate(R.layout.recent_product_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecentProductsAdapter.RecentProductsViewHolder holder, int position) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        ViewGroup.LayoutParams params = holder.productClick.getLayoutParams();
        params.width = (int) (width*0.85);
        holder.productClick.setLayoutParams(params);
        GeneralProductHome productHome = products.get(position);
        holder.name.setText(productHome.getName());
        holder.price.setText(productHome.getPrice());
        Log.d("TAG_342", "onBindViewHolder: "+productHome.getImage_uri());
        Picasso.get().load(productHome.getImage_uri()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        if(products.size()>10){
            return 10;
        }else{
            return products.size();
        }

    }

    public class RecentProductsViewHolder extends RecyclerView.ViewHolder {
        CardView productClick;
        TextView name, price;
        ImageView image;
        public RecentProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.recentProductName);
            price = itemView.findViewById(R.id.recentProductCost);
            image = itemView.findViewById(R.id.recentProductImage);

            productClick = itemView.findViewById(R.id.recentItem);

            productClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ProductViewActivity.class);
                    intent.putExtra("product_info",  products.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
