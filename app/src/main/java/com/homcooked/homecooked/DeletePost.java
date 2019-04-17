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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DeletePost extends AppCompatActivity {
    StorageReference storageRef;
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference postRef = rootRef.child("Posts");
    private DatabaseReference DeletedPostsRef = rootRef.child("Deleted Posts");
    // getting views from layout
    TextView food_description;
    TextView email;
    ImageView food_image;
    //FirebaseAuth mAuth;
    private String currentUserID;
    private String postID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_post);
        //currentUserID = mAuth.getCurrentUser().getUid();
        // Initializing variables
        storageRef = FirebaseStorage.getInstance().getReference();
        food_description = findViewById(R.id.post_description);
        email = findViewById(R.id.email);
        food_image = findViewById(R.id.post_image);
        // retrieving info on what to display
        Intent intent = getIntent();
        String description = "Name: " + intent.getStringExtra("Food name") + "\nDescription: " +
                intent.getStringExtra("Food details");
        final String sellerName = intent.getStringExtra("Seller name");
        String sellerEmail = intent.getStringExtra("Seller email");
        storageRef.child("Post Images").child(intent.getStringExtra("PhotoKey")).getBytes(1024*1024*7)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        food_image.setImageBitmap(Bitmap.createScaledBitmap(bmp, food_image.getWidth(),
                                food_image.getHeight(), false));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Photo not found", Toast.LENGTH_LONG).show();
            }
        });
        // updating layout
        food_description.setText(description);
        email.setText(sellerEmail);

        currentUserID = intent.getStringExtra("uid");
        String photokey = intent.getStringExtra("PhotoKey").substring(2);
        String tempPostID = currentUserID + photokey;
        postID = tempPostID.substring(0, tempPostID.length() - 4);

        Button deletePostButton = findViewById(R.id.delete_post_button);
        deletePostButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DeleteCurrentPost();
            }
        });

    }

    private void DeleteCurrentPost() {
        Intent intent = getIntent();
        DeletedPostsRef.child(postID).setValue(new Posts(currentUserID, null, null, null,
                intent.getStringExtra("Food details"), intent.getStringExtra("Seller name"),
                intent.getStringExtra("Food name"), intent.getStringExtra("PhotoKey")));

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