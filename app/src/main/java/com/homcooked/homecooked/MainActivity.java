package com.homcooked.homecooked;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button viewpetButton = (Button)findViewById(R.id.view_pet_button);
        viewpetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NearbyPets.class);
                startActivity(i);
            }
        });

        Button postpetButton = (Button)findViewById(R.id.post_pet_button);
        postpetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PostActivity.class);
                startActivity(i);
            }
        });

        Button usersPostsButton = findViewById(R.id.users_posts_button);
        usersPostsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, UsersPosts.class);
                startActivity(i);
            }
        });

        Button profileButton = findViewById(R.id.btnProfile);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });

    }



}
