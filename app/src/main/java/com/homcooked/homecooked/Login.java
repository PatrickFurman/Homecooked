package com.homcooked.homecooked;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.homcooked.homecooked.Nearby_Foods;
import com.homcooked.homecooked.R;

public class Login extends AppCompatActivity {

    private EditText Name;
    private EditText Password;
    private Button btnLogin;
    private FirebaseAuth mAuth;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Firebase
        dialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            Toast.makeText(getApplicationContext(), "User already signed in", Toast.LENGTH_LONG).show();
        }

        Name = findViewById(R.id.etName);
        Password = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        final String userName = Name.getText().toString();
        final String userPassword = Password.getText().toString();

        if(TextUtils.isEmpty(userName)){
            Toast.makeText(getApplicationContext(), "Enter email address", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(userPassword)){
            Toast.makeText(getApplicationContext(), "Enter password",Toast.LENGTH_SHORT).show();
        }

        mAuth.signInWithEmailAndPassword(userName, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Signing in", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Login.this, Nearby_Foods.class); // change to main activity
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }
}
