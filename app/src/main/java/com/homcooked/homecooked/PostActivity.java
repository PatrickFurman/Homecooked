package com.homcooked.homecooked;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PostActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private ImageButton SelectPostImage;
    private Button UploadPostButton;
    private EditText PostDescription;
    private EditText FoodPostName;

    private static final int Gallery_Pick = 1;
    private Uri ImageUri;
    private String Description;
    private String FoodName;

    private StorageReference PostsImagesReference;
    private DatabaseReference UsersRef, PostsRef;
    private FirebaseAuth mAuth;

    private String saveCurrentDate, saveCurrentTime, postRandomName, downloadUrl, current_user_id, userName, photoKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();
        //userName = mAuth.getCurrentUser().getDisplayName();

        PostsImagesReference = FirebaseStorage.getInstance().getReference();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostsRef = FirebaseDatabase.getInstance().getReference("Posts");

        SelectPostImage = (ImageButton) findViewById(R.id.select_post_image);
        UploadPostButton = (Button) findViewById(R.id.upload_post_button);
        PostDescription = (EditText) findViewById(R.id.post_description);
        FoodPostName = (EditText) findViewById(R.id.food_name);

        SelectPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        UploadPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidatePostInfo();
            }
        });

    }


    private void ValidatePostInfo() {
        Description = PostDescription.getText().toString();
        FoodName = FoodPostName.getText().toString();

        if (ImageUri == null){
            Toast.makeText(this, "Please select an image.", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(Description)) {
            Toast.makeText(this, "Please write a description.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(FoodName)) {
            Toast.makeText(this, "Please write a title/food name.", Toast.LENGTH_SHORT).show();
        }
        else {
            StoringImageToFirebaseStorage();
        }
    }

    private void StoringImageToFirebaseStorage() {
        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(calFordTime.getTime());

        postRandomName = saveCurrentDate + saveCurrentTime;


        StorageReference filePath = PostsImagesReference.child("Post Images").child(ImageUri.getLastPathSegment() + postRandomName + ".jpg");

        filePath.putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()) {
                    downloadUrl = task.getResult().getStorage().getDownloadUrl().toString();
                    photoKey = ImageUri.getLastPathSegment() + postRandomName + ".jpg";
                    Toast.makeText(PostActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();

                    SavingPostInformationToDatabase();
                }
                else {
                    String message = task.getException().getMessage();
                    Toast.makeText(PostActivity.this, "Error occurred: " + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void SavingPostInformationToDatabase() {

        /*final DatabaseReference ref = UsersRef.child(current_user_id);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //User user = dataSnapshot.getValue(User.class);
                //userName = user.getName();

                userName = dataSnapshot.child("name").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

*/
        DatabaseReference ref = UsersRef.child(current_user_id).child("name");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PostsRef.child(current_user_id + postRandomName).setValue(new Posts(current_user_id, saveCurrentTime, saveCurrentDate,
                        downloadUrl, Description, userName, FoodName, photoKey));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //PostsRef.child(current_user_id + postRandomName).setValue(new Posts(current_user_id, saveCurrentTime, saveCurrentDate,
                //downloadUrl, Description, userName, FoodName, photoKey));

        SendUserToMainActivity();
        //SendUserToNearbyFoods();

    }


    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_Pick);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Gallery_Pick && resultCode==RESULT_OK && data!=null){
            ImageUri = data.getData();
            SelectPostImage.setImageURI(ImageUri);
        }
    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(PostActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }

    private void SendUserToFoodDetails() {
        Intent foodDetailsIntent = new Intent(PostActivity.this, ViewFoodDetails.class);
        startActivity(foodDetailsIntent);
    }

    private void SendUserToNearbyFoods() {
        Intent nearbyFoodsIntent = new Intent (PostActivity.this, Nearby_Foods.class);
        startActivity(nearbyFoodsIntent);
    }

}