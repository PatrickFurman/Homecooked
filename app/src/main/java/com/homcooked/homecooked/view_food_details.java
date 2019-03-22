package com.homcooked.homecooked;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
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

import static java.lang.Integer.parseInt;

public class view_food_details extends AppCompatActivity {
    StorageReference storageRef;
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    User seller;
    String sellerUsername;
    // getting views from layout
    TextView food_description;
    TextView email;
    TextView ratingIndicator;
    ImageView food_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_food_details);
        Intent intent = getIntent();
        // Initializing variables
        storageRef = FirebaseStorage.getInstance().getReference();
        food_description = findViewById(R.id.post_description);
        email = findViewById(R.id.email);
        ratingIndicator = findViewById(R.id.ratingIndicator);
        food_image = findViewById(R.id.post_image);
        seller = new User(intent.getStringExtra("Seller name"), intent.getStringExtra("Seller email"));
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        // retrieving info on what to display
        String description = "Name: " + intent.getStringExtra("Food name") + "\nDescription: " +
                intent.getStringExtra("Food details");
        final String sellerName = intent.getStringExtra("Seller name");
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
        // Getting initial review info for seller
        final DatabaseReference sellerRef = rootRef.child("Users").child(sellerName);
        sellerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                seller.setNumReviews(dataSnapshot.child("numReviews").getValue(Integer.class));
                seller.setTotalRating(dataSnapshot.child("totalRating").getValue(Integer.class));
                sellerUsername = dataSnapshot.child("name").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        // updating layout
        food_description.setText(description);
        email.setText("Seller name: " + sellerUsername + "\nSeller email: " + sellerEmail);
        // Updating seller review info when rating changed
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                int intRating = (int) rating;
                ratingBar.setRating(intRating);
                seller.setTotalRating(seller.getTotalRating() + intRating);
                seller.setNumReviews(seller.getNumReviews() + 1);
                sellerRef.child("rating").setValue(Math.round(seller.getTotalRating() / seller.getNumReviews()));
                sellerRef.child("numReviews").setValue(seller.getNumReviews());
                sellerRef.child("totalRating").setValue(seller.getTotalRating());
                ratingIndicator.setText("Seller's Rating: " + seller.getTotalRating() / seller.getNumReviews());
                ratingBar.setIsIndicator(true);
            }
        });
    }
}