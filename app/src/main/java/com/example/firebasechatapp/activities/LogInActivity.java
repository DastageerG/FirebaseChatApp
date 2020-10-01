package com.example.firebasechatapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firebasechatapp.MainActivity;
import com.example.firebasechatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity
{
    private Button buttonCreateAccount,buttonLogIn;
    private Context context = LogInActivity.this;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private EditText editTextEmail , editTextPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);

        buttonCreateAccount = findViewById(R.id.buttonLogInActivityCreateAccount);
        buttonCreateAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(context,SignUpActivity.class));
            }
        });
        editTextEmail = findViewById(R.id.editTextLogInActivityEmail);
        editTextPassword = findViewById(R.id.editTextLogInActivityPassword);
        buttonLogIn = findViewById(R.id.buttonLogInActivityLogIn);

        buttonLogIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String email  = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                 if (email.isEmpty())
                {
                    editTextEmail.setError("Enter Email");
                }
                else if (password.isEmpty())
                {
                    editTextPassword.setError("Enter Password");
                }
                else
                {
                    LogIntoAccount(email,password);
                }
            } // on click closed
        }); // click listener closed

    } // onCreate closed

    private void LogIntoAccount(String email, String password)
    {
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
    {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task)
        {
            if(task.isSuccessful())
            {
                progressDialog.dismiss();
                firebaseUser = firebaseAuth.getCurrentUser();
                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
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
    }
} // Main Activity closed