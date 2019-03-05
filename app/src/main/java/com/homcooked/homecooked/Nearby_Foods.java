package com.homcooked.homecooked;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Nearby_Foods extends AppCompatActivity {
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference foodsRef = rootRef.child("Foods");
    // private DatabaseReference postsRef = rootRef.child("Posts");
    private DatabaseReference usersRef = rootRef.child("Users");
    String foodName;
    String sellerEmail;
    String sellerName;
    String unprocessed;
    double latitude;
    double longitude;
    private FusedLocationProviderClient mFusedLocationClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby__foods);
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.getApplicationContext(),
                        FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            try {
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                } else {
                                    Toast.makeText(getApplicationContext(), R.string.location_error,
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), R.string.error + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    protected void onStart() {
        super.onStart();
        Query query = foodsRef.orderByChild("Latitude").limitToFirst(3); // change limit later and maybe start/endAt value
        query.addValueEventListener(new ValueEventListener() {
            @Override
            // Scans the string version of the data and fills in TextViews with results
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                unprocessed = "" + dataSnapshot.getValue();
                TextView food1View = findViewById(R.id.food1Text);
                food1View.setOnClickListener(listener);
                process(unprocessed, food1View);
                TextView food2View = findViewById(R.id.food2Text);
                food2View.setOnClickListener(listener);
                process(unprocessed, food2View);
                TextView food3View = findViewById(R.id.food3Text);
                food3View.setOnClickListener(listener);
                process(unprocessed, food3View);
            }

            @Override
            // Displaying error message if necessary
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), R.string.error +
                        databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        /*
        Query query1 = postsRef.orderByChild("time").limitToFirst(3);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // will need to update process method to work with info in posts
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        */
        Query sellerQuery = usersRef.child(sellerName);
        sellerQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sellerEmail = "" + dataSnapshot.getValue();
                sellerEmail = sellerEmail.substring(sellerEmail.indexOf("email"), sellerEmail.indexOf("password") - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), R.string.error +
                        databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), view_food_details.class);
            TextView view = (TextView) v;
            intent.putExtra("Food details", view.getText());
            intent.putExtra("Food name", foodName);
            intent.putExtra("Seller email", sellerEmail);
            startActivity(intent);
        }
    };

    private void process (String unprocessed, TextView view) {
        foodName = unprocessed.substring(1, unprocessed.indexOf('='));
        String description = foodName + "\n" + unprocessed.substring(unprocessed.indexOf("Seller"), unprocessed.indexOf("Photo") - 2)
                + "\n" + unprocessed.substring(unprocessed.indexOf("Description"), unprocessed.indexOf("}"));

        sellerName = unprocessed.substring(unprocessed.indexOf("Seller"), unprocessed.indexOf("Photo") - 2);
        try {
            unprocessed = unprocessed.substring(unprocessed.indexOf("Photo") - 2);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.error +
                    e.getMessage(), Toast.LENGTH_LONG).show();
        }
        view.setText(description);
    }
}