package com.example.numad22fa_team49_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    Button editProfile;
    ImageView profilePic, back;
    DatabaseReference reference;
    SharedPreferences sharedPreferences;
    StorageReference saveImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profilePic = findViewById(R.id.profile_pic);
        editProfile = findViewById(R.id.edit_profile_pic);
        back = findViewById(R.id.back_button_profile);
        sharedPreferences = getSharedPreferences("storeHunt",MODE_PRIVATE);
        reference = FirebaseDatabase.getInstance().getReference("user").child(sharedPreferences.getString("userId",""));
        saveImage = FirebaseStorage.getInstance().getReference().child("product");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        reference.child("profilepicture").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Picasso.get().load(snapshot.getValue(String.class)).into(profilePic);
                }else{
                    profilePic.setImageDrawable(getDrawable(R.drawable.ic_baseline_person_24));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ProfileActivity.this, new String[] {Manifest.permission.CAMERA}, 100);

        }

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });
    }

    private void openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ProfileActivity.this, new String[] {Manifest.permission.CAMERA}, 100);

        }else{
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(intent, 200);
        }


    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200&& resultCode==RESULT_OK && data!=null){
            Bitmap image = (Bitmap) data.getExtras().get("data");
//            ImageView imageview = (ImageView) findViewById(R.id.ImageView01); //sets imageview as the bitmap
            profilePic.setImageBitmap(image);
            Uri ImageURI = getImageUri(ProfileActivity.this,image);
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM dd, yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss a");
            String key = dateFormat.format(calendar.getTime()).toString()+timeFormat.format(calendar.getTime()).toString();
            StorageReference newImg = saveImage.child(ImageURI.getLastPathSegment()+key+".jpg");
            final UploadTask uploadTask = newImg.putFile(ImageURI);
            Log.d("TAG_123", "onActivityResult: ");

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("TAG_123", "onFailure: dfghj");
//                    Toast.makeText(AddProductActivity.this,"sdfghjk",Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(AddProductActivity.this,"Successful",Toast.LENGTH_SHORT).show();

                    Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if(!task.isSuccessful()){
                                throw task.getException();
                            }
//                            downloadedImage =

//                            Log.d("TAG_456", "then: "+downloadedImage);
                            return newImg.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
//                                downloadedImage = newImg.getDownloadUrl().toString();
//                                Log.d("TAG_456", "then: "+downloadedImage);
                                newImg.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
//                                        saveProductToDatabase(uri.toString());
//                                        imageUri = uri.toString();
                                        reference.child("profilepicture").setValue(uri.toString());

                                    }
                                });
//                                Toast.makeText(AddProductActivity.this,"retrived image added to database",Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            });

        }

    }
}