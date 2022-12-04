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

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    Button signUpButton;
    EditText signUpName, signUpEmail, signUpPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signUpButton = findViewById(R.id.signup_button);
        signUpName = findViewById(R.id.signup_fullname);
        signUpEmail = findViewById(R.id.signup_email);
        signUpPassword = findViewById(R.id.signup_password);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.signup_button) {
            createAccountInFirebase();
        }
    }

    private void createAccountInFirebase() {
        // Get the values from the Input fields and convert them to String type
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
                        Toast.makeText(SignupActivity.this,"User signed up successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignupActivity.this,HomeActivity.class));
                    }else{
                        Toast.makeText(SignupActivity.this,"Register error: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}