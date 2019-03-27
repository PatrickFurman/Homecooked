package com.homcooked.homecooked;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
                startFeedbackActivity();
            }
        });
        findViewById(R.id.viewFeedbackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startViewReviews();
            }
        });
        // updating layout
        food_description.setText(description);
        email.setText("Seller name: " + seller.getName() + "\nSeller email: " + seller.getEmail());

    }

    private void startFeedbackActivity() {
        intent = new Intent(this, FeedbackActivity.class);
        intent.putExtra("Seller name", seller.getName());
        intent.putExtra("Seller uid", seller.getUserID());
        intent.putExtra("Post name", postName);
        startActivity(intent);
    }

    private void startViewReviews() {
        intent = new Intent(this, ViewReviews.class);
        intent.putExtra("Seller name", seller.getName());
        rootRef.child("Users").child(seller.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                intent.putExtra("Seller rating", dataSnapshot.child("rating").getValue(Integer.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        intent.putExtra("Post name", postName);
        startActivity(intent);
    }
}