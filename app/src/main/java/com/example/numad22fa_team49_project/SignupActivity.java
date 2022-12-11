package com.example.numad22fa_team49_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    Button signUpButton;
    EditText signUpName, signUpEmail, signUpPassword;
    TextView loginAsSeller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signUpButton = findViewById(R.id.signup_button);
        signUpName = findViewById(R.id.signup_fullname);
        signUpEmail = findViewById(R.id.signup_email);
        signUpPassword = findViewById(R.id.signup_password);
        loginAsSeller = findViewById(R.id.loginAsSeller);

        mAuth = FirebaseAuth.getInstance();

        loginAsSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.signup_button) {
            createAccountInFirebase();
        }
    }

    private void createAccountInFirebase() {

        ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        // Get the values from the Input fields and convert them to String type
        String name = signUpName.getText().toString();
        String email = signUpEmail.getText().toString();
        String password = signUpPassword.getText().toString();

        // Form Validation
        if (TextUtils.isEmpty(name)){
            Toast.makeText(this,"Please enter a valid name",Toast.LENGTH_SHORT).show();
            progress.dismiss();
            signUpName.requestFocus();
        } else if (TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter a valid email",Toast.LENGTH_SHORT).show();
            progress.dismiss();
            signUpEmail.requestFocus();
        } else if (TextUtils.isEmpty(password)){
            Toast.makeText(this,"Password is required to create an account",Toast.LENGTH_SHORT).show();
            progress.dismiss();
            signUpPassword.requestFocus();
        } else{
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progress.dismiss();
                        Toast.makeText(SignupActivity.this,"User signed up successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignupActivity.this,HomeActivity.class);
                        intent.putExtra("userName",name);
                        intent.putExtra("fromSignUp",true);
                        startActivity(intent);
                    }else{
                        progress.dismiss();
                        Toast.makeText(SignupActivity.this,"Register error: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}