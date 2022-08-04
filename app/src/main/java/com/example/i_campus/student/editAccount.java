package com.example.i_campus.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.i_campus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

public class editAccount extends AppCompatActivity {

    ImageView profile_pic;
    EditText fullname,id,email,phonenum,password;

     StorageReference storage = FirebaseStorage.getInstance().getReference("Users/");
     FirebaseFirestore db= FirebaseFirestore.getInstance();
     FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private static final int  CAMERA_REQUEST_CODE = 111;
    String currentPhotoPath;
    private Uri mImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        profile_pic = findViewById(R.id.profile_pic);
        fullname = findViewById(R.id.fullname);
        id = findViewById(R.id.id);
        email = findViewById(R.id.email);
        phonenum = findViewById(R.id.phonenum);
        password = findViewById(R.id.password);

        displayaccount();


    }
    public void displayaccount() {

        DocumentReference account = db.collection("Users").document(mAuth.getCurrentUser().getUid());
        account.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        fullname.setText(document.getData().get("fullname").toString());
                        id.setText(document.getData().get("id").toString());
                        email.setText(document.getData().get("email").toString());
                        phonenum.setText(document.getData().get("phonenum").toString());
                        password.setText(document.getData().get("password").toString());
                        String link = document.getData().get("imageURL").toString();
                        Picasso.get().load(link).into(profile_pic);
                    }
                }
            }
        });
    }

    public void changeimage(View view) {
        openFileChooser();

    }

    public void save(View view) {
        updateUserProfile();
    }

    private void openFileChooser(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(editAccount.this);
        dialog.setCancelable(false);
        dialog.setPositiveButton("Take Photo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (ContextCompat.checkSelfPermission(editAccount.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                    //Take Permission
                    ActivityCompat.requestPermissions(editAccount.this, new String[]{Manifest.permission.CAMERA},  CAMERA_REQUEST_CODE);

                } else {
                    Toast.makeText(editAccount.this, "Permission already Granted", Toast.LENGTH_SHORT).show();
                    dispatchTakePictureIntent();
                }
            }
        })
                .setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent photoPickerIntent = new Intent();
                        photoPickerIntent.setType("image/*");
                        photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(photoPickerIntent, 1);
                    }
                });
        final AlertDialog alert = dialog.create();
        alert.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case  CAMERA_REQUEST_CODE:
                if(resultCode == RESULT_OK){
                    File f = new File(currentPhotoPath);
                    profile_pic.setImageURI(Uri.fromFile(f));
                    mImageUri = Uri.fromFile(f);
                }
                break;
            case 1:
                if(resultCode == RESULT_OK){
                    mImageUri = data.getData();
                    Picasso.get().load(mImageUri).into(profile_pic);
                }
                break;
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = mAuth.getCurrentUser().getUid() + ".";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    public void updateUserProfile(){

        if (mImageUri != null) {
            StorageReference fileReference = storage.child(mAuth.getCurrentUser().toString() + "." + getFileExtension(mImageUri));

            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    String new_password = password.getText().toString();
                                    user.updatePassword(new_password)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> taskimage) {
                                                    if (taskimage.isSuccessful()) {
                                                        DocumentReference documentReference = db.collection("Users").document(mAuth.getCurrentUser().getUid());
                                                        documentReference
                                                                .update(
                                                                        "fullname",fullname.getText().toString(),
                                                                        "id",id.getText().toString(),
                                                                        "email",email.getText().toString(),
                                                                        "phonenum",phonenum.getText().toString(),
                                                                        "password",password.getText().toString(),
                                                                        "imageURL",task.getResult().toString()
                                                                );
                                                        Toast.makeText(editAccount.this, "Profile successfully updated", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                }
                            });
                        }
                    });

            Intent intent = new Intent(editAccount.this , MainActivity.class);
            startActivity(intent);

        } else {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String new_password = password.getText().toString();
            user.updatePassword(new_password)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                DocumentReference documentReference = db.collection("Users").document(mAuth.getCurrentUser().getUid());
                                documentReference
                                        .update(
                                                "fullname",fullname.getText().toString(),
                                                "id",id.getText().toString(),
                                                "email",email.getText().toString(),
                                                "phonenum",phonenum.getText().toString(),
                                                "password",password.getText().toString()
                                        );
                                Toast.makeText(editAccount.this, "done", Toast.LENGTH_SHORT).show();
                            }
                            else{

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(editAccount.this, "tk jadi", Toast.LENGTH_SHORT).show();
                        }
                    });
            Intent intent = new Intent(editAccount.this , MainActivity.class);
            startActivity(intent);



        }
    }

}