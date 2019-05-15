package com.homcooked.homecooked;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    TextView profile_name;
    TextView profile_email;
    TextView profile_location;
    TextView profile_description;
    TextView profile_phone;
//add phone number, location, and description
    Button btnEdit;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef, userRef;
    private FirebaseAuth auth;
    //private FirebaseAuth.AuthStateListener authListener;
    private String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profile_name = findViewById(R.id.name);
        profile_email = findViewById(R.id.email);
        profile_location = findViewById(R.id.profile_location);
        profile_description = findViewById(R.id.profile_description);
        profile_phone = findViewById(R.id.phone);

        btnEdit = findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = auth.getCurrentUser();
        currentUser = user.getUid();
        userRef = mDatabaseRef.child("Profile").child(currentUser);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("profile_name").getValue().toString();
                String email = dataSnapshot.child("profile_email").getValue().toString();
                String location = dataSnapshot.child("profile_location").getValue().toString();
                String description = dataSnapshot.child("profile_description").getValue().toString();
                String phone = dataSnapshot.child("profile_phone").getValue().toString();
                profile_phone.setText(phone);
                profile_description.setText(description);
                profile_location.setText(location);
                profile_name.setText(name);
                profile_email.setText(email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnEdit){
            Intent intent = new Intent(ProfileActivity.this, Profile_Edit_Activity.class);
            startActivity(intent);
        }
    }
}

