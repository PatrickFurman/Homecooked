package com.homcooked.homecooked;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PetMap extends FragmentActivity implements OnMapReadyCallback {
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference usersRef = rootRef.child("Profile");
    private DatabaseReference postsRef = rootRef.child("Posts");
    private GoogleMap mMap;
    Location mCurrentLocation;
    LocationCallback mLocationCallback;
    LocationRequest locationRequest;
    double latitude;
    double longitude;
    String sellerEmail;
    String sellerName;
    private FusedLocationProviderClient mFusedLocationClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setNumUpdates(1);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mCurrentLocation = locationResult.getLastLocation();
                latitude = mCurrentLocation.getLatitude();
                longitude = mCurrentLocation.getLongitude();
            }
        };
        getLocation();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                TextView v = (TextView) marker.getTag();
                try {
                    usersRef.child(v.getTag(R.integer.Seller).toString()).addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    sellerEmail = dataSnapshot.child("profile_email").getValue(String.class);
                                    sellerName = dataSnapshot.child("profile_name").getValue(String.class);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(getApplicationContext(), "Seller email not found",
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                    if (sellerEmail == null)
                        sellerEmail = "Error 404 Email not found";
                    if (sellerName == null)
                        sellerName = "Error 404 Name not found";
                    Intent intent = new Intent(getApplicationContext(), ViewPetDetails.class);
                    intent.putExtra("pet details", v.getTag(R.integer.Description).toString());
                    intent.putExtra("pet name", v.getTag(R.integer.Name).toString());
                    intent.putExtra("Seller name", sellerName);
                    intent.putExtra("Seller uid", v.getTag(R.integer.Seller).toString());
                    intent.putExtra("Seller email", sellerEmail);
                    intent.putExtra("PhotoKey", v.getTag(R.integer.PhotoKey).toString());
                    intent.putExtra("Post name", v.getTag(R.integer.Parent).toString());
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    return false;
                }
               return true;
            }
        });
        postsRef.orderByChild("latitude").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    TextView view = new TextView(getApplicationContext());
                    if (child.child("photoKey").getValue(String.class) == null)
                        view.setTag(R.integer.PhotoKey, "No photo found");
                    else
                        view.setTag(R.integer.PhotoKey, child.child("photoKey").getValue(String.class));
                    if (child.child("petName").getValue(String.class) == null)
                        view.setTag(R.integer.Name, "No name given");
                    else
                        view.setTag(R.integer.Name, child.child("petName").getValue(String.class));
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
                    Marker marker;
                    marker = mMap.addMarker(new MarkerOptions().position(postLoc).title(view.getTag(R.integer.Name).toString()));
                    marker.setTag(view);
                }
            }

            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), R.string.error +
                        databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        LatLng currentLoc = new LatLng(47.568, -122.321);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLoc));
        CameraUpdateFactory.zoomTo(15);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    Toast.makeText(getApplicationContext(), "Not all features of this app will work without" +
                            " location services turned on", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void getLocation() {
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            try {
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                mFusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, null);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), R.string.error + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
