package com.homcooked.homecooked;

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

public class ProfileActivity extends AppCompatActivity {

    TextView username;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference();
        FirebaseUser user = auth.getCurrentUser();
        //userID = user.getUid();

            FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {

                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    if(firebaseUser != null){
                        //String userID = firebaseUser.getUid();
                        String userEmail = firebaseUser.getEmail();
                        username.setText(userEmail);

                    }
                }
            };
                }
        }//;

    //}

//}
