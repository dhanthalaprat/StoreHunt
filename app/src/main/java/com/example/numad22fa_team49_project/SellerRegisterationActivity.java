package com.example.numad22fa_team49_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SellerRegisterationActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText signUpEmail, signUpName, signUpPassword;
    Button sellerRegistration;

    DatabaseReference mReference;
    SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registeration);

        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("storeHunt", MODE_PRIVATE);


        signUpEmail = findViewById(R.id.seller_signup_email);
        signUpName = findViewById(R.id.seller_signup_full_name);
        signUpPassword = findViewById(R.id.seller_signup_password);
        sellerRegistration = findViewById(R.id.seller_register_button);

        mReference = FirebaseDatabase.getInstance().getReference().child("seller");

        sellerRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });


    }

    public void registerUser(){
        ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        String name = signUpName.getText().toString();
        String email = signUpEmail.getText().toString();
        String password = signUpPassword.getText().toString();

        // Form Validation
        if (TextUtils.isEmpty(name)){
            progress.dismiss();
            Toast.makeText(this,"Please enter a valid name",Toast.LENGTH_SHORT).show();
            signUpName.requestFocus();
        } else if (TextUtils.isEmpty(email)){
            progress.dismiss();
            Toast.makeText(this,"Please enter a valid email",Toast.LENGTH_SHORT).show();
            signUpEmail.requestFocus();
        } else if (TextUtils.isEmpty(password)){
            progress.dismiss();
            Toast.makeText(this,"Password is required to create an account",Toast.LENGTH_SHORT).show();
            signUpPassword.requestFocus();
        } else{
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progress.dismiss();
                        Toast.makeText(SellerRegisterationActivity.this,"User signed up successfully", Toast.LENGTH_SHORT).show();
//                        mReference.push().setValue(mAuth.getUid());
                        Intent intent = new Intent(SellerRegisterationActivity.this,SellerProfileActivity.class);
                        intent.setFlags(Intent. FLAG_ACTIVITY_NEW_TASK | Intent. FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("username_registration",name);
                        SharedPreferences.Editor editSharedPreferences = sharedPreferences.edit();
                        editSharedPreferences.putBoolean("asSeller", true);
                        editSharedPreferences.apply();
                        startActivity(intent);
                    }else{
                        progress.dismiss();
                        Toast.makeText(SellerRegisterationActivity.this,"Register error: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}