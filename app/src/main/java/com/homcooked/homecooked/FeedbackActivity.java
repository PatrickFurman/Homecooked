package com.homcooked.homecooked;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FeedbackActivity extends AppCompatActivity {
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference sellerRef;
    DatabaseReference postsRef;
    FirebaseAuth auth;
    User seller;
    String sellerUsername;
    EditText writtenFeedback;
    RatingBar ratingBar;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        auth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        final String postName = intent.getStringExtra("Post name");
        sellerUsername = intent.getStringExtra("Seller name");
        seller = new User(sellerUsername);
        writtenFeedback = findViewById(R.id.writtenFeedback);
        ratingBar = findViewById(R.id.ratingBar);
        submitButton = findViewById(R.id.submit_Review);
        // Getting initial review info for seller
        sellerRef = rootRef.child("Users").child(intent.getStringExtra("Seller uid"));
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

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postsRef = rootRef.child("Posts").child(postName);
                postsRef.child("Reviews").child(postName + auth.getCurrentUser().getUid()).
                        setValue(writtenFeedback.getText().toString());
                Toast.makeText(getApplicationContext(), "Feedback received", Toast.LENGTH_SHORT).show();
                startMainActivity();
            }
        });

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
                ratingBar.setIsIndicator(true);
            }
        });
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
