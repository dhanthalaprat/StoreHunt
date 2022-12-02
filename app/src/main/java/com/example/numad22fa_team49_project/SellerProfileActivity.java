package com.example.numad22fa_team49_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SellerProfileActivity extends AppCompatActivity {

    FloatingActionButton addProduct;
    TextView viewNewOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_profile);

        addProduct = findViewById(R.id.add_product);
        viewNewOrders = findViewById(R.id.newOrdersViewAll);

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SellerProfileActivity.this, AddProductActivity.class));
            }
        });

        viewNewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent());
            }
        });
    }
}