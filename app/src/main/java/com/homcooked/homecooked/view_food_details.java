package com.homcooked.homecooked;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class view_food_details extends AppCompatActivity {
    StorageReference storageRef;
    // getting views from layout
    TextView food_description;
    TextView email;
    ImageView food_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_food_details);
        // Initializing variables
        storageRef = FirebaseStorage.getInstance().getReference();
        food_description = findViewById(R.id.post_description);
        email = findViewById(R.id.email);
        food_image = findViewById(R.id.post_image);
        // retrieving info on what to display
        Intent intent = getIntent();
        String description = "Name: " + intent.getStringExtra("Food name") + "\nDescription: " +
                intent.getStringExtra("Food details");
        String sellerEmail = intent.getStringExtra("Seller email");
        storageRef.child("Post Images").child(intent.getStringExtra("PhotoKey")).getBytes(1024*1024*7)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        food_image.setImageBitmap(Bitmap.createScaledBitmap(bmp, food_image.getWidth(),
                                food_image.getHeight(), false));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Photo not found", Toast.LENGTH_LONG).show();
            }
        });
        // updating layout
        food_description.setText(description);
        email.setText(sellerEmail);
    }
}