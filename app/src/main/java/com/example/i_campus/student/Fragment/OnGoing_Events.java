package com.example.i_campus.student.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.i_campus.student.Adapter.RvBookedEventAdapter;
import com.example.i_campus.R;
import com.example.i_campus.object.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class OnGoing_Events extends Fragment {

    private RecyclerView rv_ongoingbooking;

    View view;
    private TextView tvEmptyDb;

    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    RvBookedEventAdapter rvBookedEventAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_history_booking, container, false);
        ArrayList<Event> event = new ArrayList<>();
        tvEmptyDb = view.findViewById(R.id.tvEmptyDb);
        rv_ongoingbooking = view.findViewById(R.id.rv_historybooking);

        rvBookedEventAdapter= new RvBookedEventAdapter(view.getContext(),event);
        rv_ongoingbooking.setAdapter(rvBookedEventAdapter );
        rv_ongoingbooking.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL,false));

        db.collection("Event_Booked")
                .whereEqualTo("UID",mAuth.getCurrentUser().getUid())
                .whereEqualTo("check_in","UnCompleted")
                .whereEqualTo("status","Completed")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()){
                                tvEmptyDb.setVisibility(View.VISIBLE);
                            }else{
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (document.exists()){
                                        db.collection("Clubs")
                                                .whereEqualTo("is_active",true)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {

                                                            for (QueryDocumentSnapshot documentEvent : task.getResult()) {
                                                                documentEvent.getReference().collection("Events").document(document.getData().get("EventID").toString())
                                                                        .get()
                                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                if (task.isSuccessful()){

                                                                                    DocumentSnapshot documentSnapshot = task.getResult();
                                                                                    if (documentSnapshot.exists()){
                                                                                        event.add(new Event(
                                                                                                        documentSnapshot.getData().get("name").toString(),
                                                                                                        documentSnapshot.getData().get("venue").toString(),
                                                                                                        documentSnapshot.getData().get("imageURL").toString(),
                                                                                                        documentSnapshot.getId(),
                                                                                                        documentSnapshot.getData().get("month").toString(),
                                                                                                        documentSnapshot.getData().get("date").toString()
                                                                                                )
                                                                                        );


                                                                                    }   rvBookedEventAdapter.notifyDataSetChanged();

                                                                                }

                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    }
                                                });


                                    }
                                }

                            }

                        }
                    }
                });

        return view;
    }
}