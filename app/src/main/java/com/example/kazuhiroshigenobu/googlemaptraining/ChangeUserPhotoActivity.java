package com.example.kazuhiroshigenobu.googlemaptraining;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChangeUserPhotoActivity extends AppCompatActivity {


    ImageView userImageView;
    Button buttonChooseImage;
    Button buttonSaveImage;



    String newPhotoURL = "";

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference().child("images");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_photo);

        layoutReady();
    }


    private void layoutReady(){

        userImageView = (ImageView)findViewById(R.id.userChangeImageView);
        buttonChooseImage = (Button)findViewById(R.id.buttonChangeChoosePhoto);
        buttonSaveImage = (Button)findViewById(R.id.buttonChangeSavePhoto);

        Toolbar toolbar;
        toolbar = (Toolbar)findViewById(R.id.app_bar_change_user_image);
        setSupportActionBar(toolbar);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     goBackToAccountView();
                                                 }
                                             }
        );

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        }



        String userPhoto = UserInfo.userImageURL;


        if (!userPhoto.equals("")){
            Uri uri = Uri.parse(userPhoto);
            Picasso.with(getApplicationContext()).load(uri).into(userImageView);

        } else {
            userImageView.setImageResource(R.drawable.app_default_user_icon);
        }

        buttonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkPermissionAndAddPhoto();
            }
        });


        buttonSaveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveImageAction();

                //Check if user actually change the photo or not

            }
        });

    }

    @SuppressWarnings("VisibleForTests")
    private void uploadImageToDatabase(Uri file) {


        String photoId = UUID.randomUUID().toString();


// Create the file metadata
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build();

// Upload file and metadata to the path 'images/mountains.jpg'
        UploadTask uploadTask = storageRef.child(photoId).putFile(file, metadata);

// Listen for state changes, errors, and completion of the upload.
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                System.out.println("Upload is " + progress + "% done");
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("Upload is paused");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Handle successful uploads on complete

                     Uri downloadUrl = taskSnapshot.getDownloadUrl();

                     if (downloadUrl != null){
                         newPhotoURL = downloadUrl.toString();
                        }


            }
        });
    }


    private void saveImageAction(){

        if (newPhotoURL.equals("")){
            pleaseChoosePhoto();
        } else {
            //firebase user info update

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (user != null) {

                String userId = user.getUid();

                Map<String, Object> childUpdates = new HashMap<>();

                childUpdates.put("userPhoto", newPhotoURL);

                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
                userRef.child(userId).updateChildren(childUpdates);

                goBackToAccountView();



            }

        }
    }

    private void pleaseChoosePhoto(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //AlertDialog.Builder builder = new AlertDialog.Builder();
        builder.setTitle("写真を選択してください");
        //Set title localization
        builder.setItems(new CharSequence[]
                        {"はい"},

                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                break;
                        }
                    }
                });
        builder.create().show();
    }

    private void goBackToAccountView(){

        Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
        startActivity(intent);
        finish();


    }
    public void checkPermissionAndAddPhoto() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //request permission...
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 2);

            } else {
                //Have a permission
                showPhoto();
            }
        } else {
            //Build.VERSION.SDK_INT < Build.VERSION_CODES.M(23)

            showPhoto();

        }

    }


    private void showPhoto() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {

            Uri selectedImage = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                userImageView.setImageBitmap(bitmap);

                uploadImageToDatabase(selectedImage);


            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }



}
