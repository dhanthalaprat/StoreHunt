package com.example.numad22fa_team49_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.numad22fa_team49_project.adapters.NewOrderRecyclerViewAdapter;
import com.example.numad22fa_team49_project.models.NewOrderModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewNewOrdersSellerViewActivity extends AppCompatActivity {

    RecyclerView allNewOrders;
    NewOrderRecyclerViewAdapter adapter;
    ArrayList<NewOrderModel> orders;

    DatabaseReference reference;
    FirebaseAuth mAuth;
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_new_orders_seller_view);

        orders = new ArrayList<>();
        allNewOrders = findViewById(R.id.full_orders_recycler_view);
        backButton = findViewById(R.id.back_button_orders);
        adapter = new NewOrderRecyclerViewAdapter(this, orders, false);
        allNewOrders.setLayoutManager(new LinearLayoutManager(this));
        allNewOrders.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("seller").child(mAuth.getUid()).child("orders");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    NewOrderModel order = data.getValue(NewOrderModel.class);
                    orders.add(order);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}