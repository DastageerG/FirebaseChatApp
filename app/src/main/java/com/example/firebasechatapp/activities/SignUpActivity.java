package com.example.firebasechatapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.firebasechatapp.MainActivity;
import com.example.firebasechatapp.R;
import com.example.firebasechatapp.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity
{
    private EditText editTextName , editTextEmail , editTextPassword;
    private Button buttonSignUp;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private Context context = SignUpActivity.this;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firebaseAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolbarSignUpActivity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(this);
        editTextName = findViewById(R.id.editTextSignUpActivityName);
        editTextEmail = findViewById(R.id.editTextSignUpActivityEmail);
        editTextPassword = findViewById(R.id.editTextSignUpActivityPassword);
        buttonSignUp = findViewById(R.id.buttonSignUpActivitySignUp);
        buttonSignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String name = editTextName.getText().toString().trim();
                String email  = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (name.isEmpty())
                {
                    editTextName.setError("Enter Name");
                }
                else if (email.isEmpty())
                {
                    editTextEmail.setError("Enter Email");
                }
                else if (password.isEmpty())
                {
                    editTextPassword.setError("Enter Password");
                }
                else
                {
                    createAnAccount(name,email,password);
                }

            }
        });
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);
    } // onCreate closed

    private void createAnAccount(final String name, String email, String password)
    {
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            currentUser = firebaseAuth.getCurrentUser();
                            String uid = currentUser.getUid();

                            databaseReference = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(uid);
                            HashMap<String,String>map = new HashMap<>();
                            map.put(Utils.displayName,name);
                            map.put(Utils.status,"Hi There Iam Using this App ! ");
                            map.put(Utils.image,"default");
                            map.put(Utils.thumbImage,"default");
                            databaseReference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        progressDialog.dismiss();
                                        Intent intent = new Intent(context, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }); //
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(context, "You Got Some Error", Toast.LENGTH_SHORT).show();
                        }
                    } // onComplete closed
                }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                progressDialog.dismiss();
                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            } // on Failure
        });
    } // create an account Method closed
}