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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DeletePost extends AppCompatActivity {
    StorageReference storageRef;
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference usersRef = rootRef.child("Profile");
    private DatabaseReference postRef = rootRef.child("Posts");
    private DatabaseReference DeletedPostsRef = rootRef.child("Deleted Posts");
    // getting views from layout
    TextView pet_description;
    TextView email;
    ImageView pet_image;
    //FirebaseAuth mAuth;
    private String currentUserID;
    private String postID, date, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_post);
        //currentUserID = mAuth.getCurrentUser().getUid();
        // Initializing variables
        storageRef = FirebaseStorage.getInstance().getReference();
        pet_description = findViewById(R.id.post_description);
        email = findViewById(R.id.email);
        pet_image = findViewById(R.id.post_image);
        // retrieving info on what to display
        Intent intent = getIntent();
        String description = "Name: " + intent.getStringExtra("pet name") + "\nDescription: " +
                intent.getStringExtra("pet details");
        final String sellerName = intent.getStringExtra("Seller name");
        String sellerEmail = intent.getStringExtra("Seller email");
        storageRef.child("Post Images").child(intent.getStringExtra("PhotoKey")).getBytes(1024*1024*7)
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
        currentUserID = intent.getStringExtra("uid");
        usersRef.child(currentUserID).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        email.setText("Seller email: " + dataSnapshot.child("profile_email").getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Seller email not found",
                                Toast.LENGTH_LONG).show();
                    }
                });
        String photokey = intent.getStringExtra("PhotoKey").substring(2);
        String tempPostID = currentUserID + photokey;
        postID = tempPostID.substring(0, tempPostID.length() - 4);
        date = postID.substring(currentUserID.length(), postID.length() - 5);
        time = postID.substring(postID.length() - 5, postID.length());

        Button deletePostButton = findViewById(R.id.delete_post_button);
        deletePostButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DeleteCurrentPost();
            }
        });

    }

    private void DeleteCurrentPost() {
        Intent intent = getIntent();
        DeletedPostsRef.child(postID).setValue(new Posts(currentUserID, time, date, null,
                intent.getStringExtra("pet details"), intent.getStringExtra("Seller name"),
                intent.getStringExtra("pet name"), intent.getStringExtra("PhotoKey"),
                intent.getDoubleExtra("Latitude", 0), intent.getDoubleExtra("Longitude", 0)));

        postRef.child(postID).removeValue();
        SendUserToMainActivity();
        Toast.makeText(this, "Post deleted.", Toast.LENGTH_SHORT).show();
        //for testing to make sure postID is correct
        //Toast.makeText(this, postID, Toast.LENGTH_SHORT).show();
    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(DeletePost.this, MainActivity.class);
        startActivity(mainIntent);
    }
}