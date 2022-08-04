package com.example.i_campus.staff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.i_campus.R;
import com.example.i_campus.object.Event;
import com.example.i_campus.staff.StaffMainActivity;
import com.example.i_campus.staff.adapter.RvStaffEventlistAdapter;
import com.example.i_campus.staff.staffAddEvent;
import com.example.i_campus.student.Adapter.RvBookedEventAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Staff_eventList extends AppCompatActivity {

    RecyclerView rceventlist;
    RvStaffEventlistAdapter rvStaffEventlistAdapter;

    String id;

    FirebaseFirestore db= FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_event_list);

        id= getIntent().getExtras().getString("id");
        ArrayList<Event> event = new ArrayList<>();
        rceventlist = findViewById(R.id.rceventlist);
        rvStaffEventlistAdapter= new RvStaffEventlistAdapter(this,event);
        rceventlist.setAdapter(rvStaffEventlistAdapter );
        rceventlist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        db.collection("Clubs").document(id).collection("Events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            if (task.getResult().isEmpty()){

                            }
                            else{
                                for (QueryDocumentSnapshot document : task.getResult()){
                                    event.add(new Event(
                                            document.getData().get("name").toString(),
                                            document.getData().get("venue").toString(),
                                            document.getData().get("imageURL").toString(),
                                            document.getId(),
                                            document.getData().get("month").toString(),
                                            document.getData().get("date").toString()
                                            )
                                    );
                                } rvStaffEventlistAdapter.notifyDataSetChanged();
                            }
                        }

                    }
                });
    }
}