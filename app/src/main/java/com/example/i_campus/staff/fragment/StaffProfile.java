package com.example.i_campus.staff.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.i_campus.Login;
import com.example.i_campus.R;
import com.example.i_campus.object.Club;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class StaffProfile extends Fragment {


    LinearLayout layout_editProfile,logout;
    ImageButton button1,button2;
    EditText clubname,linkURL,password;
    TextView tvclubname,tvclubemail;
    ImageView image;
    Button save;

    StorageReference storage = FirebaseStorage.getInstance().getReference("Clubs/");
    FirebaseFirestore db= FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private Uri mImageUri;
    private StorageTask mUploadTask;

    Club club = new Club();
    int request,event;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_staff_profile, container, false);


        layout_editProfile = view.findViewById(R.id.layout_editProfile);
        button1 = view.findViewById(R.id.button1);
        button2 = view.findViewById(R.id.button2);
        clubname = view.findViewById(R.id.clubname);
        linkURL = view.findViewById(R.id.linkURL);
        password = view.findViewById(R.id.password);
        tvclubname = view.findViewById(R.id.tvclubname);
        tvclubemail = view.findViewById(R.id.tvclubemail);
        image = view.findViewById(R.id.image);
        save = view.findViewById(R.id.save);
        logout = view.findViewById(R.id.logout);

        club();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button1.setVisibility(View.GONE);
                button2.setVisibility(View.VISIBLE);
                layout_editProfile.setVisibility(View.VISIBLE);
                getprofile();
                clubname.setText(club.getName());
                linkURL.setText(club.getlinkURL());
                image.setEnabled(true);
                image.setOnClickListener(view1 -> openFileChooser());

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button1.setVisibility(View.VISIBLE);
                button2.setVisibility(View.GONE);
                layout_editProfile.setVisibility(View.GONE);
                image.setEnabled(false);

//                image.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        club();
//                    }
//                });

                //hide keyboard if want to cancel
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

            }

        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateUserProfile();

            }
        });

        logout.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
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

        });

        return view;
    }
    public void getprofile(){

        DocumentReference account = db.collection("Users").document(mAuth.getCurrentUser().getUid());
        account.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        password.setText(document.getData().get("password").toString());
                    }
                }
            }
        });
    }

    public void club(){
        db.collection("Clubs")
                .whereEqualTo("UID", mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                club.setId(document.getId());
                                club.setName(document.getData().get("name").toString());
                                tvclubname.setText(document.getData().get("name").toString());
                                club.setlinkURL(document.getData().get("linkURL").toString());
                                String link = document.getData().get("imageURL").toString();
                                Picasso.get().load(link).into(image);
                                document.getReference().collection("Events")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()){
                                                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                                                        event = task.getResult().size();
                                                    }
                                                }

                                            }
                                        });
                            }
                        } else {

                        }
                    }
                });
        tvclubemail.setText(mAuth.getCurrentUser().getEmail());
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

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(image);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    public void updateUserProfile(){

        button1.setVisibility(View.VISIBLE);
        button2.setVisibility(View.GONE);
        layout_editProfile.setVisibility(View.GONE);
        image.setEnabled(false);

        if (mImageUri != null) {
            StorageReference fileReference = storage.child(mAuth.getCurrentUser().getUid() + "." + getFileExtension(mImageUri));

            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String new_password = password.getText().toString();
                                    user.updatePassword(new_password)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> taskimage) {
                                                    if (taskimage.isSuccessful()) {
                                                        DocumentReference documentReference = db.collection("Users").document(mAuth.getCurrentUser().getUid());
                                                        documentReference
                                                                .update(
                                                                        "password",password.getText().toString()
                                                                );
                                                        DocumentReference documentReference1 = db.collection("Clubs").document(club.getId());
                                                        documentReference1
                                                                .update(
                                                                        "name",clubname.getText().toString(),
                                                                        "linkURL",linkURL.getText().toString(),
                                                                        "imageURL",task.getResult().toString()
                                                                );
                                                        club.setImageURL(task.getResult().toString());

                                                    }
                                                }
                                            });
                                }
                            });
                        }
                    });


        } else {
            String new_password = password.getText().toString();
            user.updatePassword(new_password)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                DocumentReference documentReference = db.collection("Users").document(mAuth.getCurrentUser().getUid());
                                documentReference
                                        .update(
                                                "password",password.getText().toString()
                                        );
                                DocumentReference documentReference1 = db.collection("Clubs").document(club.getId());
                                documentReference1
                                        .update(
                                                "name",clubname.getText().toString(),
                                                "LinkURL",linkURL.getText().toString()
                                        );
                                Toast.makeText(getContext(), "Profile successfully updated", Toast.LENGTH_SHORT).show();
                            }
                            else{

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "tk jadi", Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

    public void logoutUser(){

        SharedPreferences settings = getContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        settings.edit().clear().commit();
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getContext(), Login.class);
        startActivity(intent);

    }
}