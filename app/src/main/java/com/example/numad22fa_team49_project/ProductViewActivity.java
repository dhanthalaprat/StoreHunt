package com.example.numad22fa_team49_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.numad22fa_team49_project.models.GeneralProductHome;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductViewActivity extends AppCompatActivity {

    ImageView productImage, back, cart;
    TextView productName, productCost, productDescription, productRatingText;
    RatingBar productRating;
    GeneralProductHome product;
    Button addToCart;
    DatabaseReference mReference, cartCountRef;
    SharedPreferences sharedPreferences;
    TextView cartSize;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);

        product = (GeneralProductHome) getIntent().getSerializableExtra("product_info");
        Log.d("TAG_89", "onCreate: "+product.getName());

        sharedPreferences =getSharedPreferences("storeHunt",MODE_PRIVATE);
        mReference = FirebaseDatabase.getInstance().getReference().child("user");



        back = findViewById(R.id.back_button_product);
        productImage = findViewById(R.id.product_view_image);
        productName = findViewById(R.id.product_view_name);
        productDescription = findViewById(R.id.product_view_description);
        productCost = findViewById(R.id.product_view_cost);
        productRatingText = findViewById(R.id.product_view_rating_text);
        productRating = findViewById(R.id.product_view_rating);
        addToCart = findViewById(R.id.add_to_cart_button);
        cart = findViewById(R.id.view_cart_button_product);
        cartSize = findViewById(R.id.cart_size_product);

        productName.setText(product.getName());
        productDescription.setText(product.getDescription());
        productCost.setText(product.getPrice());
        productRatingText.setText(product.getRating());
        productRating.setRating(Float.parseFloat(product.getRating()));
        Picasso.get().load(product.getImage_uri()).into(productImage);
        mReference.child(sharedPreferences.getString("userId","")).child("recent").child(product.getName()).setValue(product);

        cartCountRef = FirebaseDatabase.getInstance().getReference().child("user").child(sharedPreferences.getString("userId","")).child("cart");

        if(sharedPreferences.getBoolean("asSeller",false)){
            addToCart.setVisibility(View.GONE);
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCartAndDB();
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductViewActivity.this, CartActivity.class));
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
                }else{
                    cartSize.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void addToCartAndDB() {

        Query query = mReference.child(sharedPreferences.getString("userId","")).child("cart").orderByChild("name").equalTo(product.getName());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
//                    progress.dismiss();
                    new AlertDialog.Builder(ProductViewActivity.this).setTitle("Already in cart")
                            .setMessage("Product is already in your cart.")
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .show();
                }else{
                    pushToDB();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void pushToDB(){
        Log.d("TAG_56", "addToCartAndDB: "+sharedPreferences.getString("userId",""));
        ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Adding product to cart");
        progress.setMessage("Please wait while we add the product to your cart");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        mReference.child(sharedPreferences.getString("userId","")).child("cart").child(product.getName()).setValue(product);
        Query query = mReference.child(sharedPreferences.getString("userId","")).child("cart").orderByChild("name").equalTo(product.getName());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    progress.dismiss();

                    new AlertDialog.Builder(ProductViewActivity.this).setTitle("Product added")
                            .setMessage("Product is successfully added to your cart.")
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}