package com.example.i_campus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.i_campus.student.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class AdminMenu extends AppCompatActivity {

    TextView tvtotalclub,tvstudent;

    FirebaseFirestore db= FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    StorageReference storage = FirebaseStorage.getInstance().getReference("Clubs/");

    EditText clubname,clubemail,clubpassword;
    ImageView clubimage;
    SwitchCompat activebtn;
    Button btnConfirm, btnCancel;

    private Uri uri;
    private String UID;
    int club,student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);


        tvtotalclub = findViewById(R.id.tvtotalclub);
        tvstudent = findViewById(R.id.tvstudent);

        getclub();
        getstudent();
    }
    public void getclub(){

        db.collection("Clubs")
                .whereEqualTo("is_active", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                club = task.getResult().size();
                            }
                            tvtotalclub.setText(String.valueOf(club));
                        }
                    }
                });

    }

    public void getstudent(){
        db.collection("Users")
                .whereEqualTo("usertype", "student")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                student = task.getResult().size();
                            }
                            tvstudent.setText(String.valueOf(student));
                        }
                    }
                });

    }

    public void gotostudentlist(View view) {
    }

    public void gotoclublist(View view) {
    }

    public void gotoaddclub(View view) {

        Dialog dialog = new Dialog(this);
        view = LayoutInflater.from(this).inflate(R.layout.dialog_admin_add_club,null);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.dialog_background));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        dialog.show();
        dialog.setCancelable(true);

        clubname = view.findViewById(R.id.clubname);
        clubemail = view.findViewById(R.id.clubemail);
        clubpassword = view.findViewById(R.id.clubpassword);
        clubimage = view.findViewById(R.id.clubimage);

        activebtn = view.findViewById(R.id.activebtn);
        btnConfirm = view.findViewById(R.id.btnConfirm);
        btnCancel = view.findViewById(R.id.btnCancel);

        clubimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clubname.getText().toString().isEmpty() && clubemail.getText().toString().isEmpty() && clubpassword.getText().toString().isEmpty()){
                    clubname.setError("field cannot be empty");
                    clubemail.setError("field cannot be empty");
                    clubpassword.setError("field cannot be empty");
                }
                else{
                    if(uri !=null){
                        addintodb();
                        dialog.dismiss();

                    }
                    else{
                        Toast.makeText(AdminMenu.this,"Picture is Empty!!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    private void openFileChooser(){
        Intent photoPickerIntent = new Intent();
        photoPickerIntent.setType("image/*");
        photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(photoPickerIntent, 1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            uri = data.getData();
            Picasso.get().load(uri).into(clubimage);
        }
    }


    public void addintodb(){

        // search if item already have
        db.collection("Clubs")
                .whereEqualTo("name",clubname.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        if (task.getResult().isEmpty()){
                            Log.e("tag","1");
                            mAuth.createUserWithEmailAndPassword(clubemail.getText().toString(), clubpassword.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            UID = task.getResult().getUser().getUid();
                                            if(task.isSuccessful()){
                                                Log.e("tag","2");
                                                StorageReference fileReference = storage.child(UID + ".jpg");
                                                StorageTask mUploadTask = fileReference.putFile(uri)
                                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                            @Override
                                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                                Log.e("tag","3");
                                                                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Uri> task) {

                                                                        //proceed to add data to database
                                                                        Map<String, Object> user = new HashMap<>();
                                                                        user.put("email",clubemail.getText().toString());
                                                                        user.put("password", clubpassword.getText().toString());
                                                                        user.put("usertype","staff");

                                                                        db.collection("Users").document(UID)
                                                                                .set(user)
                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void avoid) {
                                                                                        Map<String, Object> item = new HashMap<>();
                                                                                        item.put("UID",UID);
                                                                                        item.put("imageURL", task.getResult().toString());
                                                                                        item.put("is_active",activebtn.isChecked());
                                                                                        item.put("linkURL","");
                                                                                        item.put("name",clubname.getText().toString());
                                                                                        db.collection("Clubs")
                                                                                                .add(item)
                                                                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                                                    @Override
                                                                                                    public void onSuccess(DocumentReference documentReference) {
                                                                                                        Log.e("tag","4");
                                                                                                        Toast.makeText(AdminMenu.this,"Successful Added" , Toast.LENGTH_SHORT).show();

                                                                                                    }
                                                                                                });
                                                                                    }
                                                                                })
                                                                                .addOnFailureListener(new OnFailureListener() {
                                                                                    @Override
                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                        Toast.makeText(AdminMenu.this,"UnSuccessful" , Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                });
                                                                    }
                                                                });
                                                            }
                                                        });


                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            Log.e("tag","6");
                                            Toast.makeText(AdminMenu.this, "tak jadi",Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }else{

                            Log.e("tag","7");
                            Toast.makeText(AdminMenu.this,"Club Already Exist", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    public void gotologout(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);
        dialog.setTitle("Logout Confirmation");
        dialog.setMessage("Are you sure you want to logout?" );
        dialog.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //Action for "Logout".
                logoutUser();
            }
        })
                .setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Action for "Cancel".

                    }
                });

        final AlertDialog alert = dialog.create();
        alert.show();

        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(android.R.color.holo_red_light));
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));
    }

    public void logoutUser(){

        SharedPreferences settings = this.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        settings.edit().clear().commit();
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);

    }
}