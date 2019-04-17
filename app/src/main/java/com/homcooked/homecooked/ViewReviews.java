package com.homcooked.homecooked;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewReviews extends AppCompatActivity {
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    ListView lv;
    TextView nameView;
    TextView ratingView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reviews);
        nameView = findViewById(R.id.sellerName);
        ratingView = findViewById(R.id.avgRating);
        Intent intent = getIntent();
        String post = intent.getStringExtra("Post name");
        nameView.setText("Seller: " + intent.getStringExtra("Seller name"));
        rootRef.child("Users").child(intent.getStringExtra("Seller uid")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("rating").getValue() != null) {
                    ratingView.setText("Average Rating: " + dataSnapshot.child("rating").getValue(Integer.class).toString());
                }
                else
                    ratingView.setText("This user has no ratings yet");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        rootRef.child("Posts").child(post).child("Reviews").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lv = findViewById(R.id.reviewList);
                List<String> viewList = new ArrayList<>();
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                        getApplicationContext(), R.layout.list_row, viewList);
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    viewList.add(child.getValue(String.class));
                }
                if (viewList.isEmpty())
                    viewList.add("No reviews yet for this food");
                lv.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
