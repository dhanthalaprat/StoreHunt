package com.example.numad22fa_team49_project;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.numad22fa_team49_project.models.NewOrderModel;
import com.squareup.picasso.Picasso;

public class OrderDetailsActivity extends AppCompatActivity {

    ImageView back, productImage;
    NewOrderModel order;
    TextView name, price, username, street, city, state, country, zipcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        back = findViewById(R.id.back_button_orders);

        name = findViewById(R.id.orderProductName);
        price = findViewById(R.id.orderProductPrice);
        username = findViewById(R.id.orderUserName);
        street = findViewById(R.id.orderStreetField);
        city = findViewById(R.id.orderCityField);
        state = findViewById(R.id.orderStateField);
        country = findViewById(R.id.orderCountryField);
        zipcode = findViewById(R.id.orderZipcodeField);
        productImage = findViewById(R.id.order_product_image);


        order = new NewOrderModel();
        order = (NewOrderModel) getIntent().getSerializableExtra("ordered");

        name.setText(order.getProductName());
        price.setText(order.getProductPrice());
        username.setText(order.getUserName());
        street.setText(order.getStreet());
        city.setText(order.getCity());
        state.setText(order.getState());
        country.setText(order.getCountry());
        zipcode.setText(order.getZipcode());

        Picasso.get().load(order.getImage_uri()).into(productImage);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}