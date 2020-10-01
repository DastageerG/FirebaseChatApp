package com.example.firebasechatapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.firebasechatapp.R;
import com.example.firebasechatapp.model.UserData;
import com.example.firebasechatapp.utils.Utils;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AllUsersActivity extends AppCompatActivity
{
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private Context context = AllUsersActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.show();
        firebaseUser = firebaseAuth.getCurrentUser();
        recyclerView = findViewById(R.id.recyclerViewAllUsersActivity);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        databaseReference = FirebaseDatabase.getInstance().getReference().child(Utils.USERS);
    } // onCreate closed

    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseRecyclerAdapter<UserData,UsersViewHolder>firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UserData, UsersViewHolder>
                (
                        UserData.class,
                        R.layout.all_users_recycler_view,
                        UsersViewHolder.class,
                        databaseReference
                )
        {
            @Override
            protected void populateViewHolder(UsersViewHolder usersViewHolder, UserData userData, int i)
            {
                usersViewHolder.setDisplayName(userData.getDisplayName());
                usersViewHolder.setStatus(userData.getStatus());
                usersViewHolder.setImage(userData.getImage());

                final String userId = getRef(i).getKey();
                usersViewHolder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(context, UsersProfileActivity.class);
                        intent.putExtra("userId",userId);
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();
        progressDialog.dismiss();

    } // onStart closed
    public static class UsersViewHolder extends RecyclerView.ViewHolder
    {
        View view;

        public UsersViewHolder(@NonNull View itemView)
        {
            super(itemView);
            view = itemView;
        } // constructor closed
        public void setDisplayName(String name)
        {
            TextView textViewName;
            textViewName = view.findViewById(R.id.textViewAllUsersRecyclerViewName);
            textViewName.setText(name);
        }
        public void setStatus(String status)
        {
            TextView textViewStatus;
            textViewStatus = view.findViewById(R.id.textViewAllUsersRecyclerViewStatus);
            textViewStatus.setText(status);
        }
        public void setImage(String imageUri)
        {
            ImageView imageView;
            imageView = view.findViewById(R.id.imageViewAllUsersRecyclerView);
            if(imageUri.isEmpty() || imageUri.equals("default"))
            {

                imageView.setImageResource(R.drawable.ic_baseline_person_24);
            }
            else
            {
                Picasso.get().load(imageUri).placeholder(R.drawable.ic_baseline_person_24).into(imageView);
            }
        }
} // UsersViewHolderClosed

} // AllUsersActivityClosed