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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class view_food_details extends AppCompatActivity {
    StorageReference storageRef;
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference foodsRef = rootRef.child("Foods");
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
        String description = intent.getStringExtra("Food details");
        String foodName = intent.getStringExtra("Food name");
        String sellerEmail = intent.getStringExtra("Seller email");
        final String photoKey = intent.getStringExtra("PhotoKey");
        Query query = foodsRef.child(foodName).child("PhotoKey");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                storageRef.child(photoKey).getBytes(1024*1024*5)
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), R.string.error +
                        databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        // updating layout
        food_description.setText(description);
        email.setText(sellerEmail);

    }
}