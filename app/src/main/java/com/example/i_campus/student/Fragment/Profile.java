package com.example.i_campus.student.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.i_campus.student.BookingHistory;
import com.example.i_campus.student.EventHistory;
import com.example.i_campus.Login;
import com.example.i_campus.R;
import com.example.i_campus.student.editAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class Profile extends Fragment {

    TextView fullname,email;
    ImageView profile_pic;
    LinearLayout account,booking,event,logout;

    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    String uid = mAuth.getCurrentUser().getUid();
    String userid;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);


        fullname = view.findViewById(R.id.fullname);
        email = view.findViewById(R.id.email);
        profile_pic = view.findViewById(R.id.profile_pic);
        account = view.findViewById(R.id.account);
        booking = view.findViewById(R.id.booking);
        event = view.findViewById(R.id.event);
        logout = view.findViewById(R.id.logout);

        profilepage();


        account.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), editAccount.class);
            startActivity(intent);
        });
        booking.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), BookingHistory.class);
            intent.putExtra("userid",userid);
            startActivity(intent);
        });
        event.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EventHistory.class);
            intent.putExtra("userid",userid);
            startActivity(intent);
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
                            dialog.dismiss();

                        }
                    });

            final AlertDialog alert = dialog.create();
            alert.show();

            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(android.R.color.holo_red_light));
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));

        });



        return view;
    }

    public void profilepage() {

        DocumentReference account = db.collection("Users").document(uid);
        account.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        fullname.setText(document.getData().get("fullname").toString());
                        email.setText("@" + document.getData().get("email").toString());
                        userid = document.getData().get("id").toString();
                        String link = document.getData().get("imageURL").toString();
                        Picasso.get().load(link).into(profile_pic);
                    }
                }
            }
        });
    }

    public void logoutUser(){

        SharedPreferences settings = getContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        settings.edit().clear().commit();
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getContext(), Login.class);
        startActivity(intent);

    }



}