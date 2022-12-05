package com.example.numad22fa_team49_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
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
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.numad22fa_team49_project.adapters.GeneralProductHomeAdapter;
import com.example.numad22fa_team49_project.models.GeneralProductHome;
import com.google.android.material.navigation.NavigationView;
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

    ImageView cartView, menuButton;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
   // Toolbar toolbar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
       // toolbar=findViewById(R.id.toolbar);

       // setSupportActionBar(toolbar);
//        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,);






        mAuth = FirebaseAuth.getInstance();

        Log.d("TAG_564", "onCreate: "+mAuth.getUid());
        sharedPreferences = getSharedPreferences("storeHunt",MODE_PRIVATE);
        SharedPreferences.Editor editSharedPreferences = sharedPreferences.edit();

        editSharedPreferences.putString("userId", mAuth.getUid());
        editSharedPreferences.apply();

        generalProductsRecyclerView = findViewById(R.id.general_product_home_recycler_view);
        recentlyViewedProductsRecyclerView = findViewById(R.id.recently_viewed_products_recycler_view);
        newProductRecyclerView = findViewById(R.id.new_product_home_recycler_view);
        menuButton = findViewById(R.id.menu_button);


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

        cartView = findViewById(R.id.view_cart_button);

        cartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, CartActivity.class));
            }
        });

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent i = new Intent(HomeActivity.this,LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });



    }
}