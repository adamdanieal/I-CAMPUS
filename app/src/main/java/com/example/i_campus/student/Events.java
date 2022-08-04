package com.example.i_campus.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.i_campus.R;
import com.example.i_campus.object.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Events extends AppCompatActivity {

    private String name,id,image,contact,clubid;
    ImageView event_image;
    EditText event_contact;
    TextView event_name,event_month,event_date,event_time,event_description,event_venue;

    String event_id,message;

    User user = new User();
    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        id = getIntent().getStringExtra("id");
        clubid = getIntent().getStringExtra("clubid");
        image = getIntent().getStringExtra("image");
        name = getIntent().getStringExtra("name");

        event_image = findViewById(R.id.event_image);
        event_name = findViewById(R.id.event_name);
        event_month = findViewById(R.id.event_month);
        event_date = findViewById(R.id.event_date);
        event_time = findViewById(R.id.event_time);
        event_description = findViewById(R.id.event_description);
        event_venue = findViewById(R.id.event_venue);
        event_contact = findViewById(R.id.event_contact);

        displayEvents();
    }

    public void displayEvents() {

        Picasso.get().load(image).into(event_image);
        event_name.setText(name);

        DocumentReference documentReference = db.collection("Clubs").document(clubid).collection("Events").document(id);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentClub = task.getResult();
                    if (documentClub.exists()) {

                        event_id = documentClub.getId();
                        event_month.setText(documentClub.getData().get("month").toString());
                        event_date.setText(documentClub.getData().get("date").toString());
                        event_time.setText(documentClub.getData().get("time").toString());
                        event_description.setText(documentClub.getData().get("description").toString());
                        event_venue.setText(documentClub.getData().get("venue").toString());
                        contact = documentClub.getData().get("contact").toString();
                        event_contact.setText(contact);



                    }
                }
            }
        });


    }


    public void Join(View view) {

        db.collection("Event_Booked")
                .whereEqualTo("UID", mAuth.getCurrentUser().getUid())
                .whereEqualTo("EventID", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot queryDocumentSnapshot = task.getResult();
                        if (task.getResult().isEmpty()){
                            AddBooked();
                        }
                        else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(Events.this);

                            builder.setMessage("Please wait for the organizer to approve your request");
                            builder.setTitle("You already join the events");
                            builder.setCancelable(false);

                            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.cancel();
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            alertDialog.setCancelable(true);
                        }

                    }
                });




    }

    public void AddBooked(){

        Map<String, Object> booked_data = new HashMap<>();
        booked_data.put("UID",mAuth.getCurrentUser().getUid());
        booked_data.put("EventID", id);
        booked_data.put("check_in", "UnCompleted");
        booked_data.put("status", "UnCompleted");
        db.collection("Event_Booked")
                .add(booked_data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Toast.makeText(Events.this,"Success", Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder builder = new AlertDialog.Builder(Events.this);

                        builder.setMessage("Please contact the organizer to approve your booked");
                        builder.setTitle("Booked Successful");
                        builder.setCancelable(false);

                        builder.setNegativeButton("Contact", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                message ="I have booked the event " + name + System.getProperty("line.separator") + "My Booked ID is " + mAuth.getCurrentUser().getUid();
                                openwhatsapp();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        alertDialog.setCancelable(true);

                    }
                });
    }






    public void gotowhatsapp(View view) {
         message ="Can i get the detailed about the events";
         openwhatsapp();

    }

    public void openwhatsapp(){
        try{
            PackageManager packageManager = this.getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);
            String url = "https://api.whatsapp.com/send?phone=+6"+ contact +"&text=" + message;
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                startActivity(i);
            }
        } catch(Exception e) {
        }

    }
}