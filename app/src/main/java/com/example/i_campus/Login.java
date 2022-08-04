package com.example.i_campus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.i_campus.object.User;
import com.example.i_campus.staff.StaffMainActivity;
import com.example.i_campus.student.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Login extends AppCompatActivity {

    TextInputLayout email_log, password_log;

    //Dialog box declare
    TextInputLayout email_dialog;
    Button btnConfirm, btnCancel;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    User user= new User();

    String status;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        email_log = findViewById(R.id.email_log);
        password_log = findViewById(R.id.password_log);


    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        SharedPreferences prefs = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        String usertype = prefs.getString("usertype", "");//"No name defined" is the default value.
        // Check if user is signed in (non-null) and update UI accordingly.
        if(currentUser != null){
            if (usertype.equals("student"))
            {
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
            }
            else if(usertype.equals("admin")){
                Intent intent = new Intent(Login.this, AdminMenu.class);
                startActivity(intent);
            }
            else if (usertype.equals("staff")){
                Intent intent = new Intent(Login.this, StaffMainActivity.class);
                startActivity(intent);
            }
        }
    }


    public void login(View view) {
        if (validateEmail() && validatePassword() == true)
        {
            LoginUser();
        }
        else{return;
        }
    }

    public void register(View view) {
        Intent intent = new Intent(Login.this ,Register.class);
        startActivity(intent);
        overridePendingTransition(R.transition.fadein,R.transition.fadeout);
        finish();
    }

    public void forgotpassword(View view) {
        ForgotPassword();
    }

    public boolean validateEmail(){
        String val = email_log.getEditText().getText().toString();

        if (val.isEmpty()){
            email_log.setError("field cannot be empty");
            return false;
        }
        else if (!val.matches(emailPattern)){
            email_log.setError("Invalid email address");
            return false;
        }
        else {
            email_log.setError(null);
            email_log.setErrorEnabled(false);
            return true;
        }
    }
    public boolean validatePassword(){
        String val = password_log.getEditText().getText().toString();
        if (val.isEmpty()){
            password_log.setError("field cannot be empty");
            return false;
        }
        else if (!(val.length() ==8)){
            password_log.setError("password length is 8 character");
            return false;
        }
        else {
            password_log.setError(null);
            password_log.setErrorEnabled(false);
            return true;
        }
    }

    private void LoginUser() {

        user.setEmail(email_log.getEditText().getText().toString());
        user.setPassword(password_log.getEditText().getText().toString());

        mAuth.signInWithEmailAndPassword(user.getEmail(),user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String UID = task.getResult().getUser().getUid();
                        if(task.isSuccessful()){
                            DocumentReference updatepassword = db.collection("Users").document(UID);
                            updatepassword
                                    .update("password", user.getPassword())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            db.collection("Users")
                                                    .whereEqualTo("email", user.getEmail())
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                    status = document.getData().get("usertype").toString();

                                                                }
                                                                if (status.equals("student")){
                                                                    SharedPreferences.Editor editor = getSharedPreferences("UserPreferences", MODE_PRIVATE).edit();
                                                                    editor.putString("usertype", status);
                                                                    editor.apply();
                                                                    Toast.makeText(Login.this, "Login Successful",Toast.LENGTH_SHORT).show();
                                                                    Intent intent = new Intent(Login.this , MainActivity.class);
                                                                    startActivity(intent);
                                                                }
                                                                else if (status.equals("staff")){
                                                                    SharedPreferences.Editor editor = getSharedPreferences("UserPreferences", MODE_PRIVATE).edit();
                                                                    editor.putString("usertype", status);
                                                                    editor.apply();
                                                                    Toast.makeText(Login.this, "Login Successful",Toast.LENGTH_SHORT).show();
                                                                    Intent intent = new Intent(Login.this , StaffMainActivity.class);
                                                                    startActivity(intent);

                                                                }
                                                                else if (status.equals("admin")){
                                                                    SharedPreferences.Editor editor = getSharedPreferences("UserPreferences", MODE_PRIVATE).edit();
                                                                    editor.putString("usertype", status);
                                                                    editor.apply();
                                                                    Toast.makeText(Login.this, "Login Successful",Toast.LENGTH_SHORT).show();
                                                                    Intent intent = new Intent(Login.this , AdminMenu.class);
                                                                    startActivity(intent);

                                                                }
                                                            }
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                        }
                        else{
                            Toast.makeText(Login.this, "Register Unsuccessful",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    private void ForgotPassword() {
        Dialog dialog = new Dialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_forgotpassword,null);
        dialog.setContentView(view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        dialog.show();
        dialog.setCancelable(true);


        email_dialog = view.findViewById(R.id.email_dialog);

        btnConfirm = view.findViewById(R.id.btnConfirm);
        btnCancel = view.findViewById(R.id.btnCancel);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = email_dialog.getEditText().getText().toString();

                if (val.isEmpty())
                {
                    email_dialog.setError("field cannot be empty");
                }
                else if(!val.matches(emailPattern)){
                    email_dialog.setError("Invalid email address");
                }
                else{

                    mAuth.sendPasswordResetEmail(val)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Login.this, "Email has been Sent",Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }else{
                                        Toast.makeText(Login.this, "UnSuccessful",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

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



}