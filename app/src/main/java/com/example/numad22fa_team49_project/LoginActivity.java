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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    EditText loginEmail, loginPassword;
    TextView signUp, loginAsSeller;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_Button);
        signUp = findViewById(R.id.from_login_signup);

        mAuth = FirebaseAuth.getInstance();

//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                loginUser();
//            }
//        });
//
//        signUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
//            }
//        });

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.login_Button) {
            loginUser();
        } else if(view.getId() == R.id.from_login_signup) {
            startActivity(new Intent(LoginActivity.this,SignupActivity.class));
        } else if(view.getId() == R.id.login_as_seller) {
            startActivity(new Intent(LoginActivity.this, SellerLoginActivity.class));
        }
    }

    private void loginUser() {

        ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        String email = loginEmail.getText().toString();
        String password = loginPassword.getText().toString();

       if (TextUtils.isEmpty(email)) {
            Toast.makeText(this,"Please enter a valid email",Toast.LENGTH_SHORT).show();
           progress.dismiss();
            loginEmail.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this,"Incorrect password",Toast.LENGTH_SHORT).show();
           progress.dismiss();
            loginPassword.requestFocus();
        } else {
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        progress.dismiss();
                        Toast.makeText(LoginActivity.this,"User logged in successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                        finish();
                    } else{
                        progress.dismiss();
                        Toast.makeText(LoginActivity.this,"Login error: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}