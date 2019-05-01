package com.homcooked.homecooked;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Account_Creation extends AppCompatActivity {
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference usersRef = rootRef.child("Users");
    private FirebaseAuth auth;

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
        auth = FirebaseAuth.getInstance();

    }

    protected void onStart() {
        super.onStart();
        findViewById(R.id.create).setOnClickListener(new View.OnClickListener() {
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

                // Verifying valid user input
                if (!password.equals(password2)) {
                    findViewById(R.id.Password_Input).setSelected(true); // check what this does
                    Toast.makeText(getApplicationContext(),"Passwords do not match",
                            Toast.LENGTH_LONG).show();
                } else if (!email.contains("@") || !email.contains(".")) {
                    Toast.makeText(getApplicationContext(), "Please enter a valid email",
                            Toast.LENGTH_LONG).show();
                } else if (password.length() < 8 || firstName.length() < 1 ||
                        lastName.length() < 1 || username.length() < 1) {
                    Toast.makeText(getApplicationContext(), "Invalid length of field",
                            Toast.LENGTH_LONG).show();
                } else {
                    // Creating new user object with all data and Firebase user with just email and password
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        try {
                                            throw task.getException();
                                        } catch (FirebaseAuthWeakPasswordException weakPassword) {
                                            Toast.makeText(getApplicationContext(), weakPassword.getReason(),
                                                    Toast.LENGTH_LONG).show();
                                            // need to figure out what the requirements are and put in layout
                                        } catch (FirebaseAuthUserCollisionException existingEmail) {
                                            Toast.makeText(getApplicationContext(), "User already exists",
                                                    Toast.LENGTH_LONG).show();
                                        } catch (Exception e) {
                                            Toast.makeText(getApplicationContext(), e.getMessage(),
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), R.string.account_created,
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                    /*usersRef.child(auth.getCurrentUser().getUid()).child("name").setValue(username);
                    usersRef.child(auth.getCurrentUser().getUid()).child("email").setValue(email);
                    usersRef.child(auth.getCurrentUser().getUid()).child("password").setValue(password);
                    usersRef.child(auth.getCurrentUser().getUid()).child("numReviews").setValue(0);
                    usersRef.child(auth.getCurrentUser().getUid()).child("totalRating").setValue(0);
                    startNearbyFoods();
                    */
                    login();

                }
            }
        });
    }

    private void startNearbyFoods() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void login(){
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String uid = auth.getCurrentUser().getUid();
                            usersRef.child(uid).child("name").setValue(username);
                            usersRef.child(uid).child("email").setValue(email);
                            usersRef.child(uid).child("password").setValue(password);
                            usersRef.child(uid).child("numReviews").setValue(0);
                            usersRef.child(uid).child("totalRating").setValue(0);
                            startNearbyFoods();
                        } else {

                        }
                    }

                });
    }
}