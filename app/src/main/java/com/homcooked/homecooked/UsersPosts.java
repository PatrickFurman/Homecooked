package com.homcooked.homecooked;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.TEXT_ALIGNMENT_CENTER;
import static android.view.View.generateViewId;

public class UsersPosts extends AppCompatActivity {
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference postsRef = rootRef.child("Posts");
    private DatabaseReference usersRef = rootRef.child("Users");
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    TextView loadMoreButton;
    ListView lv;
    ArrayList<TextView> textViewList;
    int startValue = 8;
    String sellerEmail;
    String currentUserID;

    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUserID = mAuth.getCurrentUser().getUid();

        setContentView(R.layout.users_posts);
        textViewList = new ArrayList<>();
        // TODO change the color of listView and maybe the text so it's more in line with login page
        loadMore(startValue);
        loadMoreButton = new TextView(this);
        int id = generateViewId();
        loadMoreButton.setId(id);
        loadMoreButton.setText(R.string.load_more);
        loadMoreButton.setTextSize(20);
        loadMoreButton.setTextColor(Color.BLACK);
        loadMoreButton.setGravity(Gravity.CENTER_HORIZONTAL);
        loadMoreButton.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        ((ListView)findViewById(R.id.list)).addFooterView(loadMoreButton);
        findViewById(id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startValue += 5;
                loadMore(startValue);
            }
        });
    }

    private void loadMore (int i) {
        Query query = postsRef.startAt(null, currentUserID).orderByChild("date").limitToFirst(i);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lv = findViewById(R.id.list);
                List<String> viewList = new ArrayList<>();
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                        getApplicationContext(), R.layout.list_row, viewList);
                // Clearing lists to avoid duplicates
                lv.setAdapter(arrayAdapter);
                textViewList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    TextView view = new TextView(getApplicationContext());
                    if (child.child("photoKey").getValue(String.class) == null)
                        view.setTag(R.integer.PhotoKey, "No photo found");
                    else
                        view.setTag(R.integer.PhotoKey, child.child("photoKey").getValue(String.class));
                    if (child.child("foodName").getValue(String.class) == null)
                        view.setTag(R.integer.Name, "No name given");
                    else
                        view.setTag(R.integer.Name, child.child("foodName").getValue(String.class));
                    if (child.child("description").getValue(String.class) == null)
                        view.setTag(R.integer.Description, "No description given");
                    else
                        view.setTag(R.integer.Description, child.child("description").getValue(String.class));
                    if (child.child("uid").getValue(String.class) == null)
                        view.setTag(R.integer.Seller, "Seller unknown");
                    else
                        view.setTag(R.integer.Seller, child.child("uid").getValue(String.class));
                    String viewText = view.getTag(R.integer.Name) + "\n" + view.getTag(R.integer.Description);
                    view.setText(viewText);
                    textViewList.add(view);
                    viewList.add(view.getText().toString());
                }
                // Updating listView
                lv.setAdapter(arrayAdapter);
                // Finding the seller for the view clicked and starting view foods for that food
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view, int position,
                                            long id) {
                        TextView v = textViewList.get(position);
                        Intent intent = new Intent(getApplicationContext(), DeletePost.class);
                        usersRef.child(v.getTag(R.integer.Seller).toString()).addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        sellerEmail = dataSnapshot.child("email").getValue(String.class);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(getApplicationContext(), "Seller email not found",
                                                Toast.LENGTH_LONG).show();
                                    }
                                });
                        if (sellerEmail == null)
                            sellerEmail = "Error 404 Email not found";
                        intent.putExtra("Food details", v.getTag(R.integer.Description).toString());
                        intent.putExtra("Food name", v.getTag(R.integer.Name).toString());
                        intent.putExtra("Seller name", v.getTag(R.integer.Seller).toString());
                        intent.putExtra("Seller email", sellerEmail);
                        intent.putExtra("PhotoKey", v.getTag(R.integer.PhotoKey).toString());
                        intent.putExtra("uid", currentUserID);
                        startActivity(intent);
                    }
                });
            }

            @Override
            // Displaying error message if necessary
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), R.string.error +
                        databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}