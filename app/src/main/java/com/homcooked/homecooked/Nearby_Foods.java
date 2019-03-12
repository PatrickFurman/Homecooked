package com.homcooked.homecooked;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.WrapperListAdapter;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.TEXT_ALIGNMENT_CENTER;
import static android.view.View.generateViewId;

public class Nearby_Foods extends AppCompatActivity {
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference postsRef = rootRef.child("Posts");
    private DatabaseReference foodsRef = rootRef.child("Foods");
    private DatabaseReference usersRef = rootRef.child("Users");
    TextView loadMoreButton;
    ListView lv;
    int startValue = 8;
    String sellerEmail;
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
        loadMore(startValue);
        loadMoreButton = new TextView(this);
        int id = generateViewId();
        loadMoreButton.setId(id);
        loadMoreButton.setText(R.string.load_more);
        loadMoreButton.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        ((ListView)findViewById(R.id.list)).addFooterView(loadMoreButton);
        findViewById(id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startValue += 3;
                loadMore(startValue);
            }
        });
    }
    // Use when working with posts
    /*
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby__foods);
        loadMore(startValue);
        loadMoreButton = new TextView(this);
        int id = generateViewId();
        loadMoreButton.setId(id);
        loadMoreButton.setText(R.string.load_more);
        loadMoreButton.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        ((ListView)findViewById(R.id.list)).addFooterView(loadMoreButton);
        findViewById(id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startValue += 3;
                loadMore(startValue);
            }
        });
    }
    */

    protected void onStart() {
        super.onStart();
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), view_food_details.class);
            usersRef.child(v.getTag(R.integer.Seller).toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   sellerEmail = dataSnapshot.child("email").getValue().toString();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), R.string.error +
                            databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            if (sellerEmail == null)
                sellerEmail = "Error 404 Email not found";
            intent.putExtra("Food details", v.getTag(R.integer.Description).toString());
            intent.putExtra("Food name", v.getTag(R.integer.Name).toString());
            intent.putExtra("Seller email", sellerEmail);
            intent.putExtra("PhotoKey", v.getTag(R.integer.PhotoKey).toString());
            startActivity(intent);
        }
    };

    private void loadMore (int i) {
        Query query = postsRef.orderByChild("time").limitToFirst(i);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            // Scans the string version of the data and fills in TextViews with results

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lv = findViewById(R.id.list);
                lv.setAdapter(lv.getAdapter());
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    TextView view = new TextView(getApplicationContext());
                    view.setTag(R.integer.PhotoKey, child.child("postimage").getValue(String.class));
                    view.setTag(R.integer.Name, child.child("foodName").getValue(String.class));
                    view.setTag(R.integer.Description, child.child("description").getValue(String.class));
                    view.setTag(R.integer.Seller, child.child("uid").getValue(String.class));
                    view.setText(view.getTag(R.integer.Name) + "\n" + view.getTag(R.integer.Description));
                    view.setOnClickListener(listener);
                    lv.addHeaderView(view);
                    lv.setAdapter(lv.getAdapter());
                }
            }
            /*
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lv = findViewById(R.id.list);
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    TextView view = new TextView(getApplicationContext());
                    view.setTag(R.integer.PhotoKey, child.child("PhotoKey").getValue(String.class));
                    view.setTag(R.integer.Name, child.getKey());
                    view.setTag(R.integer.Description, child.child("Description").getValue(String.class));
                    view.setTag(R.integer.Seller, child.child("Seller").getValue(String.class));
                    view.setText(view.getTag(R.integer.Name) + "\n" + view.getTag(R.integer.Description));
                    view.setOnClickListener(listener);
                    lv.addHeaderView(view);
                    lv.setAdapter(lv.getAdapter());
                }
            }
            */

            @Override
            // Displaying error message if necessary
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), R.string.error +
                        databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}