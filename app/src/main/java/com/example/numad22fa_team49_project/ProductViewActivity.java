package com.example.numad22fa_team49_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.numad22fa_team49_project.models.GeneralProductHome;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ProductViewActivity extends AppCompatActivity {

    ImageView productImage;
    TextView productName, productCost, productDescription, productRatingText;
    RatingBar productRating;
    GeneralProductHome product;
    Button addToCart;
    DatabaseReference mReference;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);

        product = (GeneralProductHome) getIntent().getSerializableExtra("product_info");
        Log.d("TAG_89", "onCreate: "+product.getName());

        sharedPreferences =getSharedPreferences("storeHunt",MODE_PRIVATE);
        mReference = FirebaseDatabase.getInstance().getReference().child("user");

        productImage = findViewById(R.id.product_view_image);
        productName = findViewById(R.id.product_view_name);
        productDescription = findViewById(R.id.product_view_description);
        productCost = findViewById(R.id.product_view_cost);
        productRatingText = findViewById(R.id.product_view_rating_text);
        productRating = findViewById(R.id.product_view_rating);
        addToCart = findViewById(R.id.add_to_cart_button);

        productName.setText(product.getName());
        productDescription.setText(product.getDescription());
        productCost.setText(product.getPrice());
        productRatingText.setText(product.getRating());
        productRating.setRating(Float.parseFloat(product.getRating()));
        Picasso.get().load(product.getImage_uri()).into(productImage);

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCartAndDB();
            }
        });
    }

    private void addToCartAndDB() {
        Log.d("TAG_56", "addToCartAndDB: "+sharedPreferences.getString("userId",""));
        mReference.child(sharedPreferences.getString("userId","")).child("cart").child(product.getName()).setValue(product);
    }
}