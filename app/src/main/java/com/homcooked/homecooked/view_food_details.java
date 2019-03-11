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
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    // getting views from layout
    TextView food_description = findViewById(R.id.post_description);
    ImageView food_image = findViewById(R.id.post_image);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_food_details);

        // retrieving info on what to display
        Intent intent = getIntent();
        String description = intent.getStringExtra("Food details");

        // updating layout
        // will need to change later to use correct picture, is currently always the same
        food_description.setText(description);
        storageRef.child("Post Images/1719-December-201809:16.jpg").getBytes(70000)
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
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
