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

public class view_food_details extends AppCompatActivity {
    StorageReference storageRef;
    User seller;
    String postName;
    Intent intent;
    // getting views from layout
    TextView food_description;
    TextView email;
    ImageView food_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_food_details);
        // Initializing variables
        Intent i = getIntent();
        postName = i.getStringExtra("Post name");
        storageRef = FirebaseStorage.getInstance().getReference();
        food_description = findViewById(R.id.post_description);
        email = findViewById(R.id.email);
        food_image = findViewById(R.id.post_image);
        seller = new User(i.getStringExtra("Seller name"), i.getStringExtra("Seller email"));
        seller.setUserId(i.getStringExtra("Seller uid"));
        // retrieving info on what to display
        String description = "Name: " + i.getStringExtra("Food name") + "\nDescription: " +
                i.getStringExtra("Food details");
        storageRef.child("Post Images").child(i.getStringExtra("PhotoKey")).getBytes(1024*1024*7)
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

        findViewById(R.id.feedbackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNextActivity(FeedbackActivity.class);
            }
        });
        findViewById(R.id.viewFeedbackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNextActivity(ViewReviews.class);
            }
        });
        findViewById(R.id.btnProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNextActivity(View_Profile_Activity.class);
            }
        });
        // updating layout
        food_description.setText(description);
        email.setText("Seller name: " + seller.getName() + "\nSeller email: " + seller.getEmail());
    }

    private void startNextActivity(Class activity) {
        intent = new Intent(this, activity);
        intent.putExtra("Seller name", seller.getName());
        intent.putExtra("Seller uid", seller.getUserID());
        intent.putExtra("Post name", postName);
        startActivity(intent);
    }
}