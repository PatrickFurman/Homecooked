package com.homcooked.homecooked;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class View_Profile_Activity extends AppCompatActivity {

    String id;
    TextView profile_name, profile_email, profile_location, profile_description, profile_phone, profile_web;

    private DatabaseReference mDatabaseRef, userRef;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__profile_);

        Intent intent = getIntent();
        id = intent.getStringExtra("uid");


        profile_name = findViewById(R.id.name);
        profile_email = findViewById(R.id.email);
        profile_location = findViewById(R.id.profile_location);
        profile_description = findViewById(R.id.profile_description);
        profile_phone = findViewById(R.id.phone);
        profile_web = findViewById(R.id.website);

        auth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        userRef = mDatabaseRef.child("Profile").child(id);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("profile_name").getValue().toString();
                String email = dataSnapshot.child("profile_email").getValue().toString();
                String location = dataSnapshot.child("profile_location").getValue().toString();
                String description = dataSnapshot.child("profile_description").getValue().toString();
                String phone = dataSnapshot.child("profile_phone").getValue().toString();
                String web = dataSnapshot.child("profile_web").getValue().toString();
                profile_phone.setText(phone);
                profile_description.setText(description);
                profile_location.setText(location);
                profile_name.setText(name);
                profile_email.setText(email);
                profile_web.setText(web);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
