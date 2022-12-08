package com.example.numad22fa_team49_project;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.numad22fa_team49_project.adapters.GeneralProductHomeAdapter;
import com.example.numad22fa_team49_project.models.GeneralProductHome;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewOrdersActivity extends AppCompatActivity {

    Boolean isFromCheckout;
    DatabaseReference mReference;
    FirebaseAuth mAuth;
    ArrayList<GeneralProductHome> productsInOrders;
    RecyclerView viewOrdersRecyclerView;
    GeneralProductHomeAdapter generalProductHomeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);
        mAuth = FirebaseAuth.getInstance();

        mReference = FirebaseDatabase.getInstance().getReference("user").child(mAuth.getUid());
        isFromCheckout = getIntent().getBooleanExtra("fromCheckout",false);
        if(isFromCheckout){
//            LayoutInflater inflater = getLayoutInflater();
//            View dialoglayout = inflater.inflate(R.layout.placing_order, null);
//            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
//            builder.setView(dialoglayout);
//            builder.show();
//            final AlertDialog alertDialog = builder.show();
            ProgressDialog progress = new ProgressDialog(this);
            progress.setTitle("Loading");
            progress.setMessage("Wait while loading...");
            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            progress.show();
            LayoutInflater inflater = getLayoutInflater();
            View dialoglayout = inflater.inflate(R.layout.successful_checkout, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialoglayout);
// To dismiss the dialog

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                    dialog.cancel();
                    progress.dismiss();
                    builder.show();

                }
            },2000);
        }

        viewOrdersRecyclerView = findViewById(R.id.view_orders_recycler_view);
        productsInOrders = new ArrayList<>();
        generalProductHomeAdapter = new GeneralProductHomeAdapter(this,productsInOrders);
        viewOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewOrdersRecyclerView.setAdapter(generalProductHomeAdapter);

        mReference.child("orders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    GeneralProductHome product = data.getValue(GeneralProductHome.class);
                    productsInOrders.add(product);
                }
                generalProductHomeAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent startMain = new Intent(Intent.ACTION_MAIN);
//        startMain.addCategory(Intent.CATEGORY_HOME);
//        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(startMain);
        Intent intent = new Intent(this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}