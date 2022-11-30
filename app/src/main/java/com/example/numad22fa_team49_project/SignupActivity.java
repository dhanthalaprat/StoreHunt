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

public class SignupActivity extends AppCompatActivity {

    EditText signUpName, signUpEmail, signUpPassword;
    Button signUpButton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signUpButton = findViewById(R.id.signupButton);
        signUpName = findViewById(R.id.signup_fullname);
        signUpEmail = findViewById(R.id.signup_email);
        signUpPassword = findViewById(R.id.signup_password);

        mAuth = FirebaseAuth.getInstance();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccountInFirebase();
            }
        });
    }

    private void createAccountInFirebase() {
        String name = signUpName.getText().toString();
        String email = signUpEmail.getText().toString();
        String password = signUpPassword.getText().toString();

        if (TextUtils.isEmpty(name)){
            Toast.makeText(this,"Please enter a valid name",Toast.LENGTH_SHORT);
            signUpName.requestFocus();
        }
        else if (TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter a valid email",Toast.LENGTH_SHORT);
            signUpEmail.requestFocus();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter a password to create account",Toast.LENGTH_SHORT);
            signUpPassword.requestFocus();
        }
        else{
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(SignupActivity.this,"User signed up successfully", Toast.LENGTH_SHORT);
                        startActivity(new Intent(SignupActivity.this,HomeActivity.class));
                    }else{
                        Toast.makeText(SignupActivity.this,"Register error: "+task.getException().getMessage(),Toast.LENGTH_SHORT);
                    }
                }
            });
        }

    }
}