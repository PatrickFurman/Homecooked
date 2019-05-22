package com.homcooked.homecooked;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ViewPetDetails extends AppCompatActivity {
    StorageReference storageRef;
    User seller;
    String postName;
    Intent intent;
    // getting views from layout
    TextView pet_description;
    TextView email;
    ImageView pet_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pet_details);
        // Initializing variables
        Intent i = getIntent();
        postName = i.getStringExtra("Post name");
        storageRef = FirebaseStorage.getInstance().getReference();
        pet_description = findViewById(R.id.post_description);
        email = findViewById(R.id.email);
        pet_image = findViewById(R.id.post_image);
        seller = new User(i.getStringExtra("Seller name"), i.getStringExtra("Seller email"));
        seller.setUserId(i.getStringExtra("Seller uid"));
        // retrieving info on what to display
        String description = "Name: " + i.getStringExtra("pet name") + "\nDescription: " +
                i.getStringExtra("pet details");
        storageRef.child("Post Images").child(i.getStringExtra("PhotoKey")).getBytes(1024*1024*7)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        pet_image.setImageBitmap(Bitmap.createScaledBitmap(bmp, pet_image.getWidth(),
                                pet_image.getHeight(), false));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Photo not found", Toast.LENGTH_LONG).show();
            }
        });

        // updating layout
        pet_description.setText(description);
        email.setText("Seller name: " + seller.getName() + "\nSeller email: " + seller.getEmail());
    }
}