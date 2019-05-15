package com.homcooked.homecooked;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class foodMap extends FragmentActivity implements OnMapReadyCallback {
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference postsRef = rootRef.child("Posts");
    private GoogleMap mMap;
    ArrayList<TextView> viewList;
    double latitude;
    double longitude;
    private FusedLocationProviderClient mFusedLocationClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
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
        postsRef.orderByChild("latitude").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                viewList = new ArrayList<>();

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    TextView view = new TextView(getApplicationContext());
                    if (child.child("photoKey").getValue(String.class) == null)
                        view.setTag(R.integer.PhotoKey, "No photo found");
                    else
                        view.setTag(R.integer.PhotoKey, child.child("photoKey").getValue(String.class));
                    if (child.child("foodName").getValue(String.class) == null)
                        view.setTag(R.integer.Name, "No name given");
                    else
                        view.setTag(R.integer.Name, child.child("foodName").getValue(String.class));
                    if (child.child("description").getValue(String.class) == null)
                        view.setTag(R.integer.Description, "No description given");
                    else
                        view.setTag(R.integer.Description, child.child("description").getValue(String.class));
                    if (child.child("uid").getValue(String.class) == null)
                        view.setTag(R.integer.Seller, "Seller unknown");
                    else
                        view.setTag(R.integer.Seller, child.child("uid").getValue(String.class));
                    if (child.child("date").getValue(String.class) == null)
                        view.setTag(R.integer.Date, null);
                    else
                        view.setTag(R.integer.Date, child.child("date").getValue(String.class));
                    if (child.child("latitude").getValue(Double.class) == null)
                        view.setTag(R.integer.Latitude, null);
                    else
                        view.setTag(R.integer.Latitude, child.child("latitude").getValue(Double.class));
                    if (child.child("longitude").getValue(Double.class) == null)
                        view.setTag(R.integer.Longitude, null);
                    else
                        view.setTag(R.integer.Longitude, child.child("longitude").getValue(Double.class));
                    view.setTag(R.integer.Parent, child.getKey());
                    String viewText = view.getTag(R.integer.Name) + "\n" + view.getTag(R.integer.Description);
                    view.setText(viewText);
                    LatLng postLoc = new LatLng(Double.parseDouble(view.getTag(R.integer.Latitude).toString()),
                            Double.parseDouble(view.getTag(R.integer.Longitude).toString()));
                    mMap.addMarker(new MarkerOptions().position(postLoc).title(view.getTag(R.integer.Name).toString()));
                    viewList.add(view);
                }

            }

            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), R.string.error +
                        databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        LatLng currentLoc = new LatLng(47.568, -122.321);
        mMap.addMarker(new MarkerOptions().position(currentLoc).title("You are here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLoc));
    }
}
