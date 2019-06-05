package com.homcooked.homecooked;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ViewPetDetails extends AppCompatActivity {
    StorageReference storageRef;
    private DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Profile");
    User seller;
    String postName;
    Intent intent;
    // getting views from layout
    TextView pet_description;
    TextView email;
    ImageView pet_image;
    Button btnView;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pet_details);
        // Initializing variables
        Intent i = getIntent();
        postName = i.getStringExtra("Post name");
        storageRef = FirebaseStorage.getInstance().getReference();
        pet_description = findViewById(R.id.post_description);
        email = findViewById(R.id.email);
        pet_image = findViewById(R.id.post_image);
        btnView = findViewById(R.id.btnView);
        uid = i.getStringExtra("Seller uid");
        // retrieving info on what to display
        String description = "Name: " + i.getStringExtra("pet name") + "\nDescription: " +
                i.getStringExtra("pet details");
        usersRef.child(i.getStringExtra("Seller uid")).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        email.setText("Seller name: "+ dataSnapshot.child("profile_name").getValue().toString() + "\nSeller email: " +
                                dataSnapshot.child("profile_email").getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Seller email not found",
                                Toast.LENGTH_LONG).show();
                    }
                });
        storageRef.child("Post Images").child(i.getStringExtra("PhotoKey")).getBytes(1024*1024*7)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        pet_image.setImageBitmap(Bitmap.createScaledBitmap(bmp, pet_image.getWidth(),
                                pet_image.getHeight(), false));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Photo not found", Toast.LENGTH_LONG).show();
            }
        });

        // updating layout
        pet_description.setText(description);



        findViewById(R.id.btnView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewPetDetails.this, View_Profile_Activity.class);
                i.putExtra("uid", uid);
                startActivity(i);
            }
    });
    }
}