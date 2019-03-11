package com.homcooked.homecooked;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener {

    EditText Name;
    EditText Password;
    TextView btnSign_up;
    CardView btnSign_in;
    private FirebaseAuth mAuth;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Name = findViewById(R.id.etName);
        Password = findViewById(R.id.etPassword);
        btnSign_in = findViewById(R.id.Sign_in);
        btnSign_up = findViewById(R.id.Sign_up);

        btnSign_in.setOnClickListener(this);
        btnSign_up.setOnClickListener(this);
        dialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.Sign_in) {
            loginUser(Name.getText().toString(), Password.getText().toString());
        }
        if(view.getId() == R.id.Sign_up){
            Intent intent = new Intent(Login.this, Account_Creation.class);
            startActivity(intent);
        }
    }

    private void loginUser(String userName, String userPassword){
//    if(TextUtils.isEmpty(userName)){
//        Toast.makeText(getApplicationContext(), "Enter email address", Toast.LENGTH_SHORT).show();
//    }
//        if(TextUtils.isEmpty(userPassword)){
//        Toast.makeText(getApplicationContext(), "Enter password",Toast.LENGTH_SHORT).show();
//    }

        mAuth.signInWithEmailAndPassword(userName, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Signing in", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Login.this, Nearby_Foods.class);
                            startActivity(intent);
                            //finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Wrong email or password.", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }
}
