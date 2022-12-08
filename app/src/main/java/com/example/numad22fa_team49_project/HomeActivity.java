package com.example.numad22fa_team49_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
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


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView generalProductsRecyclerView;
    GeneralProductHomeAdapter generalProductHomeAdapter, recentProductsAdapter;

    static final float END_SCALE=0.7f;
    LinearLayout contentView;

    RecyclerView recentlyViewedProductsRecyclerView;
    RecyclerView newProductRecyclerView;

    CardView search;
    DatabaseReference reference, recentRef;

    ArrayList<GeneralProductHome> generalProductHomes, recentProducts;
    FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;

    ImageView cartView, menuButton;

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        contentView= findViewById(R.id.content);


        mAuth = FirebaseAuth.getInstance();

        Log.d("TAG_564", "onCreate: " + mAuth.getUid());
        sharedPreferences = getSharedPreferences("storeHunt", MODE_PRIVATE);
        SharedPreferences.Editor editSharedPreferences = sharedPreferences.edit();

        editSharedPreferences.putString("userId", mAuth.getUid());
        editSharedPreferences.apply();

        generalProductsRecyclerView = findViewById(R.id.general_product_home_recycler_view);
        recentlyViewedProductsRecyclerView = findViewById(R.id.recently_viewed_products_recycler_view);
        newProductRecyclerView = findViewById(R.id.new_product_home_recycler_view);
        menuButton = findViewById(R.id.menu_button);


        generalProductHomes = new ArrayList<>();
        generalProductHomeAdapter = new GeneralProductHomeAdapter(this, generalProductHomes);
        generalProductsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        generalProductsRecyclerView.setAdapter(generalProductHomeAdapter);
        generalProductsRecyclerView.setNestedScrollingEnabled(false);

        reference = FirebaseDatabase.getInstance().getReference("products");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    GeneralProductHome productItem = data.getValue(GeneralProductHome.class);
                    generalProductHomes.add(productItem);
                }
                generalProductHomeAdapter.notifyDataSetChanged();
                Log.d("TAG_90", "onDataChange: " + generalProductHomes.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        recentProducts = new ArrayList<>();
        recentProductsAdapter = new GeneralProductHomeAdapter(this, recentProducts);
        recentlyViewedProductsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recentlyViewedProductsRecyclerView.setAdapter(recentProductsAdapter);

//        newProductRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        SnapHelper snapHelper = new PagerSnapHelper();
//        snapHelper.attachToRecyclerView(newProductRecyclerView);
//        newProductRecyclerView.setAdapter(generalProductHomeAdapter);

        search = findViewById(R.id.searchButton);

        cartView = findViewById(R.id.view_cart_button);

        cartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, CartActivity.class));
            }
        });
        navigationDrawer();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, SearchActivity.class));
            }
        });

        recentRef = FirebaseDatabase.getInstance().getReference("user").child(mAuth.getUid());

        recentRef.child("recent").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recentProducts.clear();
                for(DataSnapshot data: snapshot.getChildren()){
                    GeneralProductHome productHome = data.getValue(GeneralProductHome.class);
                    recentProducts.add(productHome);
                }
                recentProductsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        menuButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mAuth.signOut();
//                Intent i = new Intent(HomeActivity.this, LoginActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(i);
//            }
//        });



    }

    //Navigation Drawer functions

    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.END))
                    drawerLayout.closeDrawer(GravityCompat.END);
                else drawerLayout.openDrawer(GravityCompat.END);

            }
        });

        animateNavigationDrawer();
        
        

    }

    private void animateNavigationDrawer() {
        drawerLayout.setScrimColor(getResources().getColor(androidx.cardview.R.color.cardview_shadow_start_color));
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);
                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = -xOffset +xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else super.onBackPressed();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }
}