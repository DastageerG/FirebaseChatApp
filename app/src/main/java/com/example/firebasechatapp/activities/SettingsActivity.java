package com.example.firebasechatapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebasechatapp.R;
import com.example.firebasechatapp.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iceteck.silicompressorr.SiliCompressor;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class SettingsActivity extends AppCompatActivity
{
    private static final int PICK_IMAGE_CODE = 1113;
    private ImageView imageViewImage;
    private TextView textViewDisplayName, textViewStatus;
    private Button buttonChangeImage, buttonChangeStatus;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private String uid = "";
    private Context context = SettingsActivity.this;
    private String currentStatus = "";
    private StorageReference storageReference;
    private Uri imageUri;
    private ProgressDialog progressDialog;
    private String profileURL;
    private Bitmap thumb_bitmap;
    private StorageReference filePath;
    private StorageReference thumbFilePath;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();


        if (firebaseUser != null)
        {
            uid = firebaseUser.getUid();
        }
        databaseReference = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(uid);

        imageViewImage = findViewById(R.id.imageViewSettingActivityImage);
        textViewDisplayName = findViewById(R.id.textViewSettingActivityDisplayName);
        textViewStatus = findViewById(R.id.textViewSettingActivityStatus);
        buttonChangeStatus = findViewById(R.id.buttonSettingActivityChangeStatus);
        buttonChangeImage = findViewById(R.id.buttonSettingActivityChangeImage);


        buttonChangeStatus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, StatusActivity.class);
                if (currentStatus.isEmpty())
                {
                    intent.putExtra("status", "No Status Yet");
                } else
                {
                    intent.putExtra("status", currentStatus);
                }
                startActivity(intent);
            }
        });
        buttonChangeImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*;");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), PICK_IMAGE_CODE);


            }
        });

        getValuesFromFireBase();

        progressDialog.setTitle("Loading ...");
        progressDialog.setMessage("Uploading image");
        progressDialog.setCanceledOnTouchOutside(false);
    } // onCreate closed


    private void getValuesFromFireBase()
    {
        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    textViewDisplayName.setText("" + dataSnapshot.child(Utils.displayName).getValue());
                    textViewStatus.setText(dataSnapshot.child(Utils.status).getValue().toString());
                    currentStatus = dataSnapshot.child(Utils.status).getValue().toString();
                    if (dataSnapshot.child(Utils.image).getValue().equals("default"))
                    {
                        imageViewImage.setImageResource(R.drawable.ic_baseline_person_24);
                    } else
                    {
                        Picasso.get().load(dataSnapshot.child(Utils.image).getValue().toString()).placeholder(R.drawable.ic_baseline_person_24)
                                .into(imageViewImage);
                    }
                } else
                {
                    Toast.makeText(context, "NO Data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    } // getValueFromDatabase closed

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                imageUri = data.getData();
                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .start(this);
            } // if closed


        } // if closed for system image request code

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK)
            {
                Uri resultUri = result.getUri();

                progressDialog.show();

                filePath = storageReference.child("profile_images").child(uid)
                        .child("profile_image.jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                    {
                        if (task.isComplete())
                        {
                            progressDialog.dismiss();
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                            {
                                @Override
                                public void onSuccess(Uri uri)
                                {
                                    if (uri != null)
                                    {
                                        profileURL = uri.toString();
                                        databaseReference.child(Utils.image).setValue(profileURL);

                                    } else
                                    {
                                        Toast.makeText(context, "No Profile Found", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } // if closed
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Cant Upload Image", Toast.LENGTH_SHORT).show();
                        }
                    } // omComplete closed
                });
            } // if closed
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error = result.getError();
            } // else if closed
        } // if closed

    } // onActivityResult closed
} // SettingsActivity closed