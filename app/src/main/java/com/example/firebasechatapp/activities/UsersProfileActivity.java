package com.example.firebasechatapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebasechatapp.R;
import com.example.firebasechatapp.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UsersProfileActivity extends AppCompatActivity
{

    private DatabaseReference databaseReference;
    private TextView textViewName,textViewStatus,textViewTotalFriends;
    private Button buttonSendRequest;
    private ImageView imageViewProfile;
    String userId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_profile);
        databaseReference = FirebaseDatabase.getInstance().getReference().child(Utils.USERS);
        textViewName = findViewById(R.id.textViewProfileActivityName);
        imageViewProfile = findViewById(R.id.imageViewProfileActivity);
        textViewStatus  = findViewById(R.id.textViewProfileActivityStatus);
        textViewTotalFriends = findViewById(R.id.textViewProfileActivityTotalFriends);
        buttonSendRequest = findViewById(R.id.buttonProfileActivitySendRequest);

        userId = getIntent().getStringExtra("userId");
        if(userId!=null)
        {
            getUserFromFireBase();
        }
    } // onCreate closed

    private void getUserFromFireBase()
    {
        databaseReference.child(userId).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
             if(dataSnapshot.exists())
             {
                 String imageURl = dataSnapshot.child(Utils.image).getValue().toString();
                 String name = dataSnapshot.child(Utils.displayName).getValue().toString();
                 String status = dataSnapshot.child(Utils.status).getValue().toString();
                 Picasso.get().load(imageURl).placeholder(R.drawable.ic_baseline_person_24).into(imageViewProfile);
                 textViewName.setText(name);
                 textViewStatus.setText(status);
             }
             else
             {
                 Toast.makeText(UsersProfileActivity.this, "No Data Exists", Toast.LENGTH_SHORT).show();
             }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }
}