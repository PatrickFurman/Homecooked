package com.homcooked.homecooked;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Profile_Edit_Activity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth auth;
    private DatabaseReference profileRef = mDatabase.child("Profile");

    EditText user_name;
    EditText user_email;
    EditText user_location;
    EditText user_phone;
    EditText user_description;
    Button btnSave;

    String profile_name;
    String profile_email;
    String profile_location;
    String profile_phone;
    String profile_description;
    String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__edit_);

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        currentUser = user.getUid();

        }

        @Override
        public void onClick(View view){
            if(view.getId() == R.id.btnSave) {
                user_name = findViewById(R.id.user_name);
                profile_name = user_name.getText().toString();
                user_email = findViewById(R.id.user_email);
                profile_email = user_email.getText().toString();
                user_location = findViewById(R.id.profile_location);
                profile_location = user_location.getText().toString();
                user_phone = findViewById(R.id.phone);
                profile_phone = user_phone.getText().toString();
                user_description = findViewById(R.id.user_description);
                profile_description = user_description.getText().toString();

                profileRef.child(currentUser).setValue(new Profile(profile_name, profile_email, profile_location, profile_phone, profile_description ));

                Intent intent = new Intent(Profile_Edit_Activity.this, MainActivity.class);
                startActivity(intent);
            }


        }
    }
