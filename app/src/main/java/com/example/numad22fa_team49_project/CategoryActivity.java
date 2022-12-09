package com.example.numad22fa_team49_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.numad22fa_team49_project.adapters.GeneralProductHomeAdapter;
import com.example.numad22fa_team49_project.models.GeneralProductHome;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    String category;
    DatabaseReference mRef;
    ArrayList<GeneralProductHome> products;
    GeneralProductHomeAdapter generalProductHomeAdapter;
    RecyclerView categoryRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        category = getIntent().getStringExtra("category");
        products = new ArrayList<>();
        categoryRecyclerView = findViewById(R.id.category_recycler_view);
        generalProductHomeAdapter = new GeneralProductHomeAdapter(this, products);
        categoryRecyclerView.setAdapter(generalProductHomeAdapter);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRef = FirebaseDatabase.getInstance().getReference("products");
        Query query = mRef.orderByChild("category").equalTo(category);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot data : snapshot.getChildren()){
                        GeneralProductHome product = data.getValue(GeneralProductHome.class);
                        products.add(product);
                    }
                    generalProductHomeAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}