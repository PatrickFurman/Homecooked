package com.homcooked.homecooked;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class view_food_details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_food_details);
        Intent intent = getIntent();
        String descriptionInfo = intent.getExtras().toString();
        TextView description = findViewById(R.id.post_description);
        description.setText(descriptionInfo);
    }
}
