package com.homcooked.homecooked;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Account_Creation extends AppCompatActivity {
    // Creating references to database
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference usersRef = rootRef.child("Users");

    private EditText editText;
    private String password;
    private String password2;
    private String username;
    private String firstName;
    private String lastName;
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
                firstName = editText.getText().toString();
                editText = findViewById(R.id.LastNameInput);
                lastName = editText.getText().toString();
                editText = findViewById(R.id.Username_Input);
                username = editText.getText().toString();
                editText = findViewById(R.id.Email_Input);
                email = editText.getText().toString();
                if (!password.equals(password2)) {
                   findViewById(R.id.Password_Input).setSelected(true); // check what this does
                    Toast.makeText(getApplicationContext(),"Passwords do not match",Toast.LENGTH_LONG).show();
                }
                else if(password.equals(null) || password2.equals(null) || username.equals(null) || email.equals(null)
                        || firstName.equals(null) || lastName.equals(null)) {
                    Toast.makeText(getApplicationContext(),"Please complete all fields",Toast.LENGTH_LONG).show();
                } else {
                    createNewUser();
                }
            }
        });
    }
    private void createNewUser() {
        User user = new User(username, firstName, lastName, email, password);

        // probably a better way to set new user than getting ID after making user then remaking
        usersRef.child("Users").child(user.getName()).setValue(user); // won't work if duplicate names
        user.setUserID(usersRef.child("Users").child(user.getName()).getKey());
        usersRef.child("Users").child(user.getName()).setValue(user);
    }
}
