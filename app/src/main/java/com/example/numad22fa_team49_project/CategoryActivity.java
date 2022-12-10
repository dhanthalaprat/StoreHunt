package com.example.numad22fa_team49_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
    ImageView back;
    TextView categoryTextView;

    @SuppressLint("MissingInflatedId")
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
        back = findViewById(R.id.back_button_categories);
        Query query = mRef.orderByChild("category").equalTo(category);
        categoryTextView = findViewById(R.id.category_name_text);
        switch (category){
            case "toys":
                categoryTextView.setText("Toys");
                break;
            case "crafts":
                categoryTextView.setText("Crafts");
                break;
            case "homedecor":
                categoryTextView.setText("Home Decor");
                break;
            case "arts":
                categoryTextView.setText("Arts");
                break;
            case "collectibles":
                categoryTextView.setText("Collectibles");
                break;
            case "gardening":
                categoryTextView.setText("Gardening");
                break;
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

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