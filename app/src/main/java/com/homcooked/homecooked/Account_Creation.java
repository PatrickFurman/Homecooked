package com.homcooked.homecooked;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Account_Creation extends AppCompatActivity {
    // Creating references to database
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference usersRef = rootRef.child("Users");

    private EditText editText;
    private String password;
    private String password2;
    private String name;
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account__creation);
    }

    protected void onStart() {
        super.onStart();
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // retrieve user input info from account creation screen
                editText = findViewById(R.id.Password_Input);
                password = editText.getText().toString();
                editText = findViewById(R.id.Password_Input_2);
                password2 = editText.getText().toString();
                editText = findViewById(R.id.FirstNameInput);
                name = editText.getText().toString();
                editText = findViewById(R.id.Email_Input);
                email = editText.getText().toString();
                if (!password.equals(password2)) {
                   findViewById(R.id.Password_Input).setSelected(true); // check what this does

                } else {
                    createNewUser();
                }
            }
        });
    }

    private void createNewUser() {
        User user = new User(name, email, password, usersRef); // update with real ZipCode info

        // probably a better way to set new user than getting ID after making user then remaking
        usersRef.child("Users").child(user.getName()).setValue(user); // won't work if duplicate names
        user.setUserID(usersRef.child("Users").child(user.getName()).getKey());
        usersRef.child("Users").child(user.getName()).setValue(user);
    }
}
