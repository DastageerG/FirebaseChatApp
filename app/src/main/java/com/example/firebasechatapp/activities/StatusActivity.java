package com.example.firebasechatapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import com.example.firebasechatapp.R;
import com.example.firebasechatapp.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class StatusActivity extends AppCompatActivity
{
    private Toolbar toolbar;
    private Context context = StatusActivity.this;
    private EditText editTextUpdateStatus;
    private Button buttonSaveChanges;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private String currentStatus = "";
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        toolbar = findViewById(R.id.toolbarStatusActivity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Account Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Loading ...");
        progressDialog.setMessage("Updating Status");

        editTextUpdateStatus = findViewById(R.id.editTextStatusActivityUpdateStatus);
        buttonSaveChanges = findViewById(R.id.buttonStatusActivitySaveChanges);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        String uid = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(uid);

        currentStatus = getIntent().getStringExtra("status");
        editTextUpdateStatus.setHint(currentStatus);

        buttonSaveChanges.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String status = editTextUpdateStatus.getText().toString().trim();
                if(status.isEmpty())
                {
                    editTextUpdateStatus.setError("Enter new Status to Proceed");
                }
                else
                {
                    updateStatus(status);
                }
            }
        });




    }

    private void updateStatus(String status)
    {
        progressDialog.show();
        databaseReference.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    progressDialog.dismiss();
                    startActivity(new Intent(context,SettingsActivity.class));
                    finish();
                }
                else
                {

                    progressDialog.dismiss();
                    Toast.makeText(context, "SomeThing is Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {

                progressDialog.dismiss();
                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("LogCheck", "onFailure: "+e.getMessage());
            }
        });
    }
}