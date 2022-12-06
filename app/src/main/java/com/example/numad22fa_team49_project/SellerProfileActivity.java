package com.example.numad22fa_team49_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.numad22fa_team49_project.models.SellerModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SellerProfileActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton addProduct;
    TextView viewNewOrders, sellerName;
    String name;
    SharedPreferences sharedPreferences;
    FirebaseAuth mAuth;
    DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_profile);
        name = getIntent().getStringExtra("username_registration");

        addProduct = findViewById(R.id.add_product);
        viewNewOrders = findViewById(R.id.newOrdersViewAll);
        sellerName = findViewById(R.id.seller_name);

        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("storeHunt", MODE_PRIVATE);
        SharedPreferences.Editor editSharedPreferences = sharedPreferences.edit();

        editSharedPreferences.putString("sellerId", mAuth.getUid());
        editSharedPreferences.apply();

        mReference = FirebaseDatabase.getInstance().getReference().child("seller").child(mAuth.getUid());

        if(!TextUtils.isEmpty(name)){
            sellerName.setText("Hi "+name);
            mReference.child("name").setValue(name);
        } else{
            mReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //SellerModel model = new SellerModel();
                    String name = "";

                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        //model = dataSnapshot.getValue(SellerModel.class);
                        name = dataSnapshot.getValue().toString();
                    }

                    sellerName.setText(name);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        int theId = v.getId();

        if(theId == R.id.add_product) {
            startActivity(new Intent(SellerProfileActivity.this, AddProductActivity.class));
        }

        if(theId == R.id.newOrdersViewAll) {
            startActivity(new Intent());
        }
    }
}