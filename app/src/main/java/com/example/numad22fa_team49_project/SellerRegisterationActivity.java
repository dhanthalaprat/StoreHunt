package com.example.numad22fa_team49_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SellerRegisterationActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText signUpEmail, signUpName, signUpPassword;
    Button sellerRegistration;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registeration);

        mAuth = FirebaseAuth.getInstance();

        signUpEmail = findViewById(R.id.seller_signup_email);
        signUpName = findViewById(R.id.seller_signup_full_name);
        signUpPassword = findViewById(R.id.seller_signup_password);
        sellerRegistration = findViewById(R.id.seller_register_button);

        sellerRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });


    }

    public void registerUser(){
        String name = signUpName.getText().toString();
        String email = signUpEmail.getText().toString();
        String password = signUpPassword.getText().toString();

        // Form Validation
        if (TextUtils.isEmpty(name)){
            Toast.makeText(this,"Please enter a valid name",Toast.LENGTH_SHORT).show();
            signUpName.requestFocus();
        } else if (TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter a valid email",Toast.LENGTH_SHORT).show();
            signUpEmail.requestFocus();
        } else if (TextUtils.isEmpty(password)){
            Toast.makeText(this,"Password is required to create an account",Toast.LENGTH_SHORT).show();
            signUpPassword.requestFocus();
        } else{
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(SellerRegisterationActivity.this,"User signed up successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SellerRegisterationActivity.this,SellerProfileActivity.class));
                    }else{
                        Toast.makeText(SellerRegisterationActivity.this,"Register error: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}