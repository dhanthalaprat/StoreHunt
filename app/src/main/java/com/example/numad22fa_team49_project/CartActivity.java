package com.example.numad22fa_team49_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

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


        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for( DataSnapshot data: snapshot.getChildren()){
                    GeneralProductHome cartItem = data.getValue(GeneralProductHome.class);
                    products.add(cartItem);
                    Log.d("TAG_134", "onCreate: "+sharedPreferences.getString("userId","")+"-"+products.get(0));
                }
                cartItemViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}