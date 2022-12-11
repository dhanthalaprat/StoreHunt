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
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.numad22fa_team49_project.adapters.GeneralProductHomeAdapter;
import com.example.numad22fa_team49_project.adapters.RecentProductsAdapter;
import com.example.numad22fa_team49_project.models.GeneralProductHome;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView generalProductsRecyclerView;
    GeneralProductHomeAdapter generalProductHomeAdapter;
    RecentProductsAdapter recentProductsAdapter;

    static final float END_SCALE=0.7f;
    LinearLayout contentView;

    RecyclerView recentlyViewedProductsRecyclerView;
    RecyclerView newProductRecyclerView;

    CardView search;
    DatabaseReference reference, recentRef, cartCountRef;

    ArrayList<GeneralProductHome> generalProductHomes, recentProducts;
    FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;

    ImageView cartView, menuButton, profilePicture, orders;
//    NaopenProfile, logout;

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    LinearLayout toys, crafts, arts, homeDecor, gardening, collectibles;

    TextView recentlyViewedText, cartSize;

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

        cartCountRef = FirebaseDatabase.getInstance().getReference().child("user").child(sharedPreferences.getString("userId","")).child("cart");


        toys = findViewById(R.id.toys);
        crafts = findViewById(R.id.crafts);
        arts = findViewById(R.id.arts);
        homeDecor = findViewById(R.id.home_decor);
        gardening = findViewById(R.id.gardening);
        collectibles = findViewById(R.id.collectibles);
        profilePicture = findViewById(R.id.profile_picture);
        orders = findViewById(R.id.previous_orders);
//        openProfile = findViewById(R.id.nav_profile);
//        logout = findViewById(R.id.nav_logout);
        recentlyViewedText = findViewById(R.id.recently_viewed_text);
        cartSize = findViewById(R.id.cart_size);

        String userName = getIntent().getStringExtra("userName");
        Boolean fromSignUp = getIntent().getBooleanExtra("fromSignUp",false);

        toys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
                intent.putExtra("category","toys");
                startActivity(intent);
            }
        });

        crafts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
                intent.putExtra("category","crafts");
                startActivity(intent);
            }
        });

        arts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
                intent.putExtra("category","arts");
                startActivity(intent);
            }
        });

        homeDecor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
                intent.putExtra("category","homedecor");
                startActivity(intent);
            }
        });

        gardening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
                intent.putExtra("category","gardening");
                startActivity(intent);
            }
        });

        collectibles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
                intent.putExtra("category","collectibles");
                startActivity(intent);
            }
        });

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ViewOrdersActivity.class));
            }
        });

        generalProductsRecyclerView = findViewById(R.id.general_product_home_recycler_view);
        recentlyViewedProductsRecyclerView = findViewById(R.id.recently_viewed_products_recycler_view);
        newProductRecyclerView = findViewById(R.id.new_product_home_recycler_view);
//        menuButton = findViewById(R.id.menu_button);


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
        recentProductsAdapter = new RecentProductsAdapter(this, recentProducts);
        recentlyViewedProductsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recentlyViewedProductsRecyclerView);
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
                if(recentProducts.size()>0){
                    recentlyViewedText.setVisibility(View.VISIBLE);
                }else{
                    recentlyViewedText.setVisibility(View.GONE);

                }
                recentProductsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(fromSignUp){
            recentRef.child("name").setValue(userName);
        }
        recentRef.child("profilepicture").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Picasso.get().load(snapshot.getValue(String.class)).into(profilePicture);
                }else{
                    profilePicture.setImageDrawable(getDrawable(R.drawable.ic_baseline_person_24));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        cartCountRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<GeneralProductHome> generalProductHomeArrayList = new ArrayList<>();
                if(snapshot.exists()){
                    for(DataSnapshot data: snapshot.getChildren()){
                        generalProductHomeArrayList.add(data.getValue(GeneralProductHome.class));
                    }
                    if(generalProductHomeArrayList.size()>0){
                        cartSize.setVisibility(View.VISIBLE);
                        cartSize.setText(""+generalProductHomeArrayList.size());
                    }else{
                        cartSize.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        openProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
//            }
//        });

//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mAuth.signOut();
//                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//            }
//        });


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
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else drawerLayout.openDrawer(GravityCompat.START);

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
                final float xTranslation = xOffset-xOffsetDiff;
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
        switch (item.getItemId()){
            case R.id.nav_profile:
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                break;
            case R.id.nav_logout:
                mAuth.signOut();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.nav_orders:
                startActivity(new Intent(HomeActivity.this, ViewOrdersActivity.class));
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}