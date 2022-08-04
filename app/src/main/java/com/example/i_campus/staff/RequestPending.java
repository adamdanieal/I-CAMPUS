package com.example.i_campus.staff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.i_campus.R;
import com.example.i_campus.staff.adapter.RvRequestPendingAdapter;
import com.example.i_campus.object.BookedEvent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RequestPending extends AppCompatActivity {

    private RecyclerView rv_historybooking;

    private ImageView imageEmpty;

    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    RvRequestPendingAdapter rvRequestPendingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_pending);


        imageEmpty = findViewById(R.id.imageEmpty);
        rv_historybooking = findViewById(R.id.rv_historybooking);

        ArrayList<BookedEvent> bookedEvents = new ArrayList<>();
        rvRequestPendingAdapter= new RvRequestPendingAdapter(this,bookedEvents);
        rv_historybooking.setAdapter(rvRequestPendingAdapter);
        rv_historybooking.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        db.collection("Clubs")
                .whereEqualTo("UID", mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getReference().collection("Events")
                                        .get()
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()){
                                                for (QueryDocumentSnapshot queryDocumentSnapshot : task1.getResult()){
                                                    db.collection("Event_Booked")
                                                            .whereEqualTo("EventID", queryDocumentSnapshot.getId())
                                                            .whereEqualTo("status","UnCompleted")
                                                            .get()
                                                            .addOnCompleteListener(task11 -> {
                                                                if (task11.isSuccessful()) {
                                                                    if (task11.getResult().isEmpty()) {
                                                                        imageEmpty.setVisibility(View.VISIBLE);
                                                                    }
                                                                    else{
                                                                        for (QueryDocumentSnapshot document1 : task11.getResult()) {
                                                                            if (document1.exists()){
                                                                                bookedEvents.add(new BookedEvent(
                                                                                        document1.getData().get("UID").toString(),
                                                                                        document1.getData().get("EventID").toString(),
                                                                                        document1.getId(),
                                                                                        document1.getData().get("status").toString(),
                                                                                        document1.getData().get("check_in").toString()
                                                                                        )
                                                                                );
                                                                                Log.e("TAG", document1.getId() );
                                                                            }rvRequestPendingAdapter.notifyDataSetChanged();
                                                                        }
                                                                    }

                                                                }
                                                            });

                                                }
                                            }

                                        });
                            }
                        }
                    }
                });

    }
}