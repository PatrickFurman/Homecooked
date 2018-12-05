package com.homcooked.homecooked;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Nearby_Foods extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby__foods);
    }

    protected void onStart() {
        super.onStart();
        findViewById(R.id.jellyfish_picture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                Intent intent = new Intent(view.getContext(), view_food_details.class);
                intent.putExtra("Jellyfish", "Food description here");
                startActivity(intent);
            }
        });
        findViewById(R.id.vegetarian_picture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                Intent intent = new Intent(view.getContext(), view_food_details.class);
                intent.putExtra("Food title here", "Food description here");
                startActivity(intent);
            }
        });
    }
}
