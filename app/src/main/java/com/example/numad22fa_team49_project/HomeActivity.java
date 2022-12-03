package com.example.numad22fa_team49_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Gallery;
import android.widget.Toast;

import com.example.numad22fa_team49_project.adapters.GeneralProductHomeAdapter;
import com.example.numad22fa_team49_project.models.GeneralProductHome;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity {

    RecyclerView generalProductsRecyclerView;
    GeneralProductHomeAdapter generalProductHomeAdapter;

    RecyclerView recentlyViewedProductsRecyclerView;
    RecyclerView newProductRecyclerView;

    CardView search;
    DatabaseReference reference;

    ArrayList<GeneralProductHome> generalProductHomes;
    FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        Log.d("TAG_564", "onCreate: "+mAuth.getUid());
        sharedPreferences = getSharedPreferences("storeHunt",MODE_PRIVATE);
        SharedPreferences.Editor editSharedPreferences = sharedPreferences.edit();

        editSharedPreferences.putString("userId", mAuth.getUid());
        editSharedPreferences.apply();

        generalProductsRecyclerView = findViewById(R.id.general_product_home_recycler_view);
        recentlyViewedProductsRecyclerView = findViewById(R.id.recently_viewed_products_recycler_view);
        newProductRecyclerView = findViewById(R.id.new_product_home_recycler_view);


        generalProductHomes = new ArrayList<>();
        generalProductHomeAdapter = new GeneralProductHomeAdapter(this, generalProductHomes);
        generalProductsRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        generalProductsRecyclerView.setAdapter(generalProductHomeAdapter);
        generalProductsRecyclerView.setNestedScrollingEnabled(false);

        reference = FirebaseDatabase.getInstance().getReference("products");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    GeneralProductHome productItem = data.getValue(GeneralProductHome.class);
                    generalProductHomes.add(productItem);
                }
                generalProductHomeAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        recentlyViewedProductsRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recentlyViewedProductsRecyclerView.setAdapter(generalProductHomeAdapter);

        newProductRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(newProductRecyclerView);
        newProductRecyclerView.setAdapter(generalProductHomeAdapter);

        search = findViewById(R.id.searchButton);




    }
}