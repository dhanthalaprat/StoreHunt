package com.example.numad22fa_team49_project;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.numad22fa_team49_project.models.GeneralProductHome;
import com.example.numad22fa_team49_project.models.NewOrderModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CheckoutDialog extends AppCompatDialogFragment {

    private EditText fullName, street, city, state, country, zipCode;
    private Button checkout;
    ArrayList<GeneralProductHome> products;

    public CheckoutDialog(ArrayList<GeneralProductHome> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.checkout_dialog,null);

        fullName = view.findViewById(R.id.details_full_name);
        street = view.findViewById(R.id.details_street);
        city = view.findViewById(R.id.details_city);
        state = view.findViewById(R.id.details_state);
        country = view.findViewById(R.id.details_country);
        zipCode = view.findViewById(R.id.details_zip_code);

        checkout = view.findViewById(R.id.checkout_button);



//        builder.setView(view).setTitle("Enter details").setPositiveButton("Checkout", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                Log.d("TAG_432", "onClick: something");
//                if(TextUtils.isEmpty(fullName.getText())){
//                    Toast.makeText(getContext(),"fghjk",Toast.LENGTH_SHORT).show();
//                    fullName.requestFocus();
//                }
//                else if(TextUtils.isEmpty(street.getText())){
//                    Toast.makeText(getContext(),"fghjk",Toast.LENGTH_SHORT).show();
//                }
//                else if(TextUtils.isEmpty(city.getText())){
//                    Toast.makeText(getContext(),"fghjk",Toast.LENGTH_SHORT).show();
//                }
//                else if(TextUtils.isEmpty(state.getText())){
//                    Toast.makeText(getContext(),"fghjk",Toast.LENGTH_SHORT).show();
//                }
//                else if(TextUtils.isEmpty(country.getText())){
//                    Toast.makeText(getContext(),"fghjk",Toast.LENGTH_SHORT).show();
//                }
//                else if(TextUtils.isEmpty(zipCode.getText())){
//                    Toast.makeText(getContext(),"fghjk",Toast.LENGTH_SHORT).show();
//                }
//            }
//        }).setCancelable(false);

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(fullName.getText())){
                    Toast.makeText(getContext(),"1",Toast.LENGTH_SHORT).show();
                    fullName.requestFocus();
                }
                else if(TextUtils.isEmpty(street.getText())){
                    Toast.makeText(getContext(),"2",Toast.LENGTH_SHORT).show();
                    street.requestFocus();
                }
                else if(TextUtils.isEmpty(city.getText())){
                    Toast.makeText(getContext(),"3",Toast.LENGTH_SHORT).show();
                    city.requestFocus();
                }
                else if(TextUtils.isEmpty(state.getText())){
                    Toast.makeText(getContext(),"4",Toast.LENGTH_SHORT).show();
                    state.requestFocus();
                }
                else if(TextUtils.isEmpty(country.getText())){
                    Toast.makeText(getContext(),"5",Toast.LENGTH_SHORT).show();
                    country.requestFocus();
                }
                else if(TextUtils.isEmpty(zipCode.getText())){
                    Toast.makeText(getContext(),"6",Toast.LENGTH_SHORT).show();
                    zipCode.requestFocus();
                }else{
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("user").child(mAuth.getUid());
                    for(GeneralProductHome productHome : products){
                        Log.d("TAG_285", "onClick: "+productHome.getSeller_id());
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("seller").child(productHome.getSeller_id());
//                        NewOrderModel(String productName, String productPrice, String userName, String userId, String street, String city, String state, String country, String zipcode, String image_uri)
                        NewOrderModel order = new NewOrderModel(productHome.getName(),productHome.getPrice(),"name",
                                mAuth.getUid(), street.getText().toString(), city.getText().toString(), state.getText().toString(),
                                country.getText().toString(),zipCode.getText().toString(),productHome.getImage_uri());
                        ref.child("orders").push().setValue(order);
                        mReference.child("orders").push().setValue(order);
                    }

                    CheckoutDialog.this.dismiss();
                    mReference.child("cart").removeValue();
                    Intent intent = new Intent(getContext(), ViewOrdersActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("fromCheckout",true);
                    getContext().startActivity(intent);


                }
            }
        });

        builder.setView(view).setTitle("Enter Details");

        return builder.create();
    }
}
