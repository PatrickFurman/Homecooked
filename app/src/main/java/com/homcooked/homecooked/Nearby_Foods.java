package com.homcooked.homecooked;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.Scanner;

public class Nearby_Foods extends AppCompatActivity {
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference foodsRef = rootRef.child("Foods");
    int zipCode = 98112; // Set to current user's zipcode
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby__foods);
        /*
        try {
            // pretty sure this is reading the email as being the zipcode - want to read associated user's zipCode
            String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            Query query = rootRef.child("Users").child("Email").equalTo(userEmail);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    zipCode = (int) dataSnapshot.getValue();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } catch(NullPointerException e) {
            Toast.makeText(getApplicationContext(), "Couldn't find user", Toast.LENGTH_LONG).show();
        }
        */
    }

    protected void onStart() {
        super.onStart();
        // Queries the first 3 foods in database with the same zipcode to user
        Query query = foodsRef.orderByChild("ZipCode").equalTo(zipCode).limitToFirst(3);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            // Scans the string version of the data and fills in textviews with results
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Scanner s = new Scanner(dataSnapshot.toString());
                TextView food1View = findViewById(R.id.food1Text);
                food1View.setText(dataSnapshot.toString());
                /*
                TextView food2View = findViewById(R.id.food2Text);
                food2View.setText(s.nextLine());
                TextView food3View = findViewById(R.id.food3Text);
                food3View.setText(s.nextLine());
                */
            }

            @Override
            // Displaying error message if necessary
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}

