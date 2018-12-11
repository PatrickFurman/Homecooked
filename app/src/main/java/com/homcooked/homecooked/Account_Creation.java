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
                } else if (email.contains("@") && email.contains(".")) {
                    Toast.makeText(getApplicationContext(), "Please enter a valid email", Toast.LENGTH_LONG).show();
                } else if (password.length() < 8 || firstName.length() < 1 || lastName.length() < 1 || username.length() < 1) {
                    Toast.makeText(getApplicationContext(), "Invalid length of field", Toast.LENGTH_LONG).show();
                } else {
                    User user = new User(username, firstName, lastName, email, password);
                    usersRef.child(user.getName()).setValue(user);
                }
            }
        });
    }
}
