package com.homcooked.homecooked;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.Scanner;

public class Nearby_Foods extends AppCompatActivity {
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference foodsRef = rootRef.child("Foods");
    double latitude;
    double longitude;
    private FusedLocationProviderClient mFusedLocationClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby__foods);
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                Toast.makeText(getApplicationContext(), "Couldn't find location, " +
                                        "make sure location services are turned on", Toast.LENGTH_LONG).show();
                            } else {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    });
        }
    }

    protected void onStart() {
        super.onStart();
        Query firstQuery = foodsRef.startAt(latitude - 10).endAt(latitude + 10);
        Query query = firstQuery.startAt(longitude - 10).endAt(longitude + 10);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            // Scans the string version of the data and fills in textviews with results
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Scanner s = new Scanner(dataSnapshot.toString());
                TextView food1View = findViewById(R.id.food1Text);
                food1View.setText(s.next());
                TextView food2View = findViewById(R.id.food2Text);
                food2View.setText(s.next());
                TextView food3View = findViewById(R.id.food3Text);
                food3View.setText(s.next());
            }

            @Override
            // Displaying error message if necessary
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error: " +
                        databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}

