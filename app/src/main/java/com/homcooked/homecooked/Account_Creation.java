package com.homcooked.homecooked;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Account_Creation extends AppCompatActivity {
    // Creating references to database
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference usersRef = rootRef.child("Users");
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account__creation);

        // retrieve user input info from account creation screen and generate user with it
        EditText editText = findViewById(R.id.Name_Input);
        String name = editText.getText().toString();
        editText = findViewById(R.id.Email_Input);
        String email = editText.getText().toString();
        editText = findViewById(R.id.Password_Input);
        String password = editText.getText().toString();
        this.user = new User(name, email, password, usersRef); // update with real ZipCode info

    }

    protected void onStart() {
        super.onStart();
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usersRef.child("Users").child(user.getUserID()).setValue(user); // check if this adds a value, not just sets it
            }
        });
    }
}
