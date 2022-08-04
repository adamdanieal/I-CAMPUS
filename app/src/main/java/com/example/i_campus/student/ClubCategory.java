package com.example.i_campus.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.i_campus.student.Adapter.RvEventAdapter;
import com.example.i_campus.R;
import com.example.i_campus.object.Event;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ClubCategory extends AppCompatActivity {

    TextView clubname, clubtitle;
    ImageView clubimage,data_not_found;
    RecyclerView rcEvent;
    private String name,clubid,image,link;

    private FirebaseFirestore db= FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_category);


        clubid = getIntent().getStringExtra("id");
        image = getIntent().getStringExtra("image");
        name = getIntent().getStringExtra("name");
        link = getIntent().getStringExtra("link");

        clubname = findViewById(R.id.clubname);
        clubtitle = findViewById(R.id.clubtitle);
        clubimage = findViewById(R.id.clubimage);
        data_not_found = findViewById(R.id.data_not_found);

        rcEvent = findViewById(R.id.rcEvent);

        //Display club Info
        clubname.setText(name);
        clubtitle.setText(name);
        Picasso.get().load(image).into(clubimage);

        recyclerview();


    }

    private void recyclerview() {

        db.collection("Clubs")
                .whereEqualTo("name",name)
                .get()
                .addOnCompleteListener(taskClub -> {
                    if (taskClub.isSuccessful()){
                        for (QueryDocumentSnapshot documentCategory : taskClub.getResult()){
                            documentCategory.getReference().collection("Events")
                                    .whereEqualTo("is_active",true)
                                    .get()
                                    .addOnCompleteListener(taskevent -> {
                                        if (taskevent.isSuccessful()){
                                            if (taskevent.getResult().isEmpty()){
                                                data_not_found.setVisibility(View.VISIBLE);
                                            }
                                            else{
                                                ArrayList<Event> event = new ArrayList<>();
                                                for (QueryDocumentSnapshot documentEvent : taskevent.getResult()){
                                                    event.add(new Event(
                                                                    documentEvent.getData().get("name").toString(),
                                                                    documentEvent.getData().get("venue").toString(),
                                                                    documentEvent.getData().get("imageURL").toString(),
                                                                    documentEvent.getId(),
                                                                    documentEvent.getData().get("month").toString(),
                                                                    documentEvent.getData().get("date").toString()
                                                            )
                                                    );
                                                }

                                                rcEvent.setAdapter(new RvEventAdapter(this,clubid,event));
                                                rcEvent.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
                                            }

                                        }
                                    });
                        }
                    }
                });

    }

    public void gotoinstagram(View view) {
        if (link.equals("")){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setCancelable(false);
            dialog.setTitle("Instagram Account Not Found");
            dialog.setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
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

        }
        else{
            Uri uri = Uri.parse(link);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }
}