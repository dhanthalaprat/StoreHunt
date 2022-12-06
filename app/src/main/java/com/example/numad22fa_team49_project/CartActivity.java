package com.example.numad22fa_team49_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.numad22fa_team49_project.adapters.CartItemViewAdapter;
import com.example.numad22fa_team49_project.models.GeneralProductHome;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    DatabaseReference mReference;
    ArrayList<GeneralProductHome> products;
    RecyclerView cartItemRecyclerView;
    CartItemViewAdapter cartItemViewAdapter;
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        products = new ArrayList<>();

        sharedPreferences = getSharedPreferences("storeHunt",MODE_PRIVATE);
        mReference = FirebaseDatabase.getInstance().getReference().child("user").child(sharedPreferences.getString("userId","")).child("cart");



        cartItemRecyclerView = findViewById(R.id.cart_recycler_view);
        cartItemRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartItemViewAdapter = new CartItemViewAdapter(this,products);
        cartItemRecyclerView.setAdapter(cartItemViewAdapter);
        backButton = findViewById(R.id.back_button);


        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for( DataSnapshot data: snapshot.getChildren()){
                    GeneralProductHome cartItem = data.getValue(GeneralProductHome.class);
                    products.add(cartItem);
                    Log.d("TAG_134", "onCreate: "+sharedPreferences.getString("userId","")+"-"+products.get(0));
                }
                double totalPrice = 0.0;
                for(int i = 0; i<products.size(); i++) {
                    totalPrice += Float.parseFloat(products.get(i).getPrice().substring(1));
                }
                totalPrice = Math.round(totalPrice * 100.0) / 100.0;
                String finalPrice = "$" + totalPrice;
                Log.d("TAG_135",finalPrice);
                cartItemViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}