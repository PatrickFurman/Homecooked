package com.homcooked.homecooked;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
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
    // private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    // private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference postsRef = rootRef.child("Posts");
    private DatabaseReference usersRef = rootRef.child("Users");
    TextView loadMoreButton;
    ListView lv;
    Spinner spinner;
    SearchView searchBar;
    ArrayList<TextView> textViewList;
    int startValue = 8;
    String sellerEmail;
    String sellerName;
    String sortType;
    // Use if we go back to location based searching
    /*
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
    */

    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby__foods);
        textViewList = new ArrayList<>();
        spinner = findViewById(R.id.filters);
        searchBar = findViewById(R.id.searchBar);
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty())
                    loadMore(startValue, null);
                loadMore(startValue, newText);
                return false;
            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.search_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // set a variable to use when ordering postsRef query based on selected item
                String temp = parent.getItemAtPosition(position).toString();
                if (temp.equals("Time"))
                    sortType = "date";
                else if (temp.equals("A to Z (Description)"))
                    sortType = "description";
                else if (temp.equals("A to Z (Name)"))
                    sortType = "foodName";
                loadMore(startValue, null);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        loadMore(startValue, null);
        loadMoreButton = new TextView(this);
        int id = generateViewId();
        loadMoreButton.setId(id);
        loadMoreButton.setText(R.string.load_more);
        loadMoreButton.setTextSize(20);
        loadMoreButton.setTextColor(Color.BLACK);
        loadMoreButton.setGravity(Gravity.CENTER_HORIZONTAL);
        loadMoreButton.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        ((ListView)findViewById(R.id.list)).addFooterView(loadMoreButton);
        findViewById(id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startValue += 5;
                loadMore(startValue, null);
            }
        });
    }

    private void loadMore (int i, String s) {
        if (sortType == null)
            sortType = "date";
        Query query;
        if (s == null)
            query = postsRef.orderByChild(sortType).limitToFirst(i);
        else
            query = postsRef.orderByChild("foodName").startAt(s).limitToFirst(i);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lv = findViewById(R.id.list);
                List<String> viewList = new ArrayList<>();
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                        getApplicationContext(), R.layout.list_row, viewList);
                // Clearing lists to avoid duplicates
                lv.setAdapter(arrayAdapter);
                textViewList.clear();
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
                    view.setTag(R.integer.Parent, child.getKey());
                    String viewText = view.getTag(R.integer.Name) + "\n" + view.getTag(R.integer.Description);
                    view.setText(viewText);
                    textViewList.add(view);
                    viewList.add(view.getText().toString());
                }

                // Updating listView
                lv.setAdapter(arrayAdapter);
                // Finding the seller for the view clicked and starting view foods for that food
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view, int position,
                                            long id) {
                        TextView v = textViewList.get(position);
                        Intent intent = new Intent(getApplicationContext(), view_food_details.class);
                        usersRef.child(v.getTag(R.integer.Seller).toString()).addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        sellerEmail = dataSnapshot.child("email").getValue(String.class);
                                        sellerName = dataSnapshot.child("name").getValue(String.class);
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
                        intent.putExtra("Food details", v.getTag(R.integer.Description).toString());
                        intent.putExtra("Food name", v.getTag(R.integer.Name).toString());
                        intent.putExtra("Seller name", sellerName);
                        intent.putExtra("Seller uid", v.getTag(R.integer.Seller).toString());
                        intent.putExtra("Seller email", sellerEmail);
                        intent.putExtra("PhotoKey", v.getTag(R.integer.PhotoKey).toString());
                        intent.putExtra("Post name", v.getTag(R.integer.Parent).toString());
                        startActivity(intent);
                    }
                });
            }

        @Override
            // Displaying error message if necessary
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), R.string.error +
                        databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}