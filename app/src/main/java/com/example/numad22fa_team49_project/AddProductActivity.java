package com.example.numad22fa_team49_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.numad22fa_team49_project.models.GeneralProductHome;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddProductActivity extends AppCompatActivity {

    Button selectGallery, uploadProduct;
    StorageReference saveImage;
    String downloadedImage;
    String key;
    DatabaseReference productReference;
    String imageUri;
    EditText productName, productCost, productDescription, productCategory;
    ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        selectGallery = findViewById(R.id.select_image_gallery);
        uploadProduct = findViewById(R.id.upload_product);
        back = findViewById(R.id.back_button_add_product);

        productName = findViewById(R.id.add_product_name);
        productCost = findViewById(R.id.add_product_cost);
        productDescription = findViewById(R.id.add_product_description);
        productCategory  = findViewById(R.id.add_product_category);

        saveImage = FirebaseStorage.getInstance().getReference().child("product");
        productReference = FirebaseDatabase.getInstance().getReference("products");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        selectGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImageFromGallery();
            }
        });

        uploadProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProductToDatabase();
            }
        });
    }

    private void selectImageFromGallery() {
        Log.d("TAG_123", "openGallery: ");
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),201);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==201 && resultCode==RESULT_OK && data!=null){
            Uri ImageURI = data.getData();
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM dd, yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss a");
            key = dateFormat.format(calendar.getTime()).toString()+timeFormat.format(calendar.getTime()).toString();
            StorageReference newImg = saveImage.child(ImageURI.getLastPathSegment()+key+".jpg");
            final UploadTask uploadTask = newImg.putFile(ImageURI);
            Log.d("TAG_123", "onActivityResult: ");

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("TAG_123", "onFailure: dfghj");
                    Toast.makeText(AddProductActivity.this,"sdfghjk",Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AddProductActivity.this,"Successful",Toast.LENGTH_SHORT).show();

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
                                        imageUri = uri.toString();
                                    }
                                });
                                Toast.makeText(AddProductActivity.this,"retrived image added to database",Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            });

        }
    }

    private void saveProductToDatabase() {

        String name, cost, category, description;
        name = productName.getText().toString();
        cost = productCost.getText().toString();
        category = productCategory.getText().toString();
        description = productDescription.getText().toString();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"Please enter a valid name",Toast.LENGTH_SHORT).show();
            productName.requestFocus();
        }else if(TextUtils.isEmpty(cost)){
            Toast.makeText(this,"Please enter a valid price",Toast.LENGTH_SHORT).show();
            productCost.requestFocus();
        }else if(TextUtils.isEmpty(category)){
            Toast.makeText(this,"Please enter a valid category",Toast.LENGTH_SHORT).show();
            productCategory.requestFocus();
        }else if(TextUtils.isEmpty(description)){
            Toast.makeText(this,"Please enter a valid description",Toast.LENGTH_SHORT).show();
            productDescription.requestFocus();
        }else{
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("pid",key);
            productMap.put("price",cost);
            productMap.put("name",name);
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM dd, yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss a");
            productMap.put("date",dateFormat+"");
            productMap.put("time",timeFormat+"");
            productMap.put("category",category);
            productMap.put("image_uri",imageUri);
            productMap.put("rating","0");
            productMap.put("description",description);
//        GeneralProductHome productHome = new GeneralProductHome("name","description","100","4","date","time",url,"category");
            productReference.child(key).updateChildren(productMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(AddProductActivity.this,"Added sucessefully",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(AddProductActivity.this,""+task.getException().toString(),Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

        }


    }
}