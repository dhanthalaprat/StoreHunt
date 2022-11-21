package com.example.numad22fa_team49_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.Bundle;

import com.example.numad22fa_team49_project.adapters.GeneralProductHomeAdapter;

public class HomeActivity extends AppCompatActivity {

    RecyclerView generalProductsRecyclerView;
    GeneralProductHomeAdapter generalProductHomeAdapter;

    RecyclerView recentlyViewedProductsRecyclerView;
    RecyclerView newProductRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        generalProductsRecyclerView = findViewById(R.id.general_product_home_recycler_view);
        recentlyViewedProductsRecyclerView = findViewById(R.id.recently_viewed_products_recycler_view);
        newProductRecyclerView = findViewById(R.id.new_product_home_recycler_view);



        generalProductHomeAdapter = new GeneralProductHomeAdapter(this);
        generalProductsRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        generalProductsRecyclerView.setAdapter(generalProductHomeAdapter);

        recentlyViewedProductsRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recentlyViewedProductsRecyclerView.setAdapter(generalProductHomeAdapter);

        newProductRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(newProductRecyclerView);
        newProductRecyclerView.setAdapter(generalProductHomeAdapter);


    }
}