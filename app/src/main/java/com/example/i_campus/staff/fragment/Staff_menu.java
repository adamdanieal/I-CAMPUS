package com.example.i_campus.staff.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.i_campus.R;
import com.example.i_campus.object.BookedFacility;
import com.example.i_campus.staff.Staff_eventList;
import com.example.i_campus.staff.Staff_facility;
import com.example.i_campus.object.Club;
import com.example.i_campus.staff.RequestPending;
import com.example.i_campus.staff.adapter.RvStaffBookedFacilityAdapter;
import com.example.i_campus.staff.staffAddEvent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;

public class Staff_menu extends Fragment {

    LinearLayout addevents,requestpending,event_list,facility;
    TextView tvrequest,tvtotalevent,tvfacilitybooked;

    FirebaseFirestore db= FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    Calendar date = Calendar.getInstance();
    Club club = new Club();
    int request,event,facilitybooked;
    String date1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_staff_menu, container, false);

        addevents = view.findViewById(R.id.addevents);
        requestpending = view.findViewById(R.id.requestpending);
        event_list = view.findViewById(R.id.event_list);
        tvrequest = view.findViewById(R.id.tvrequest);
        tvtotalevent = view.findViewById(R.id.tvtotalevent);
        facility = view.findViewById(R.id.facility);
        tvfacilitybooked = view.findViewById(R.id.tvfacilitybooked);

        int year = date.get(Calendar.YEAR) ;
        int month = date.get(Calendar.MONTH) +1 ;
        int day = date.get(Calendar.DAY_OF_MONTH);
        date1= String.format("%s/%s/%s", year, month, day);

        totalrequest();
        getevents();
        facilitybooked();

        addevents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext() , staffAddEvent.class);
                intent.putExtra("id",club.getId());
                startActivity(intent);
                getActivity().overridePendingTransition(R.transition.fadein,R.transition.fadeout);
                getActivity().finish();


            }
        });
        requestpending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext() , RequestPending.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.transition.fadein,R.transition.fadeout);
                getActivity().finish();

            }
        });
        event_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext() , Staff_eventList.class);
                intent.putExtra("id",club.getId());
                startActivity(intent);
                getActivity().overridePendingTransition(R.transition.fadein,R.transition.fadeout);
                getActivity().finish();

            }
        });
        facility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext() , Staff_facility.class);
                intent.putExtra("id",club.getId());
                startActivity(intent);
                getActivity().overridePendingTransition(R.transition.fadein,R.transition.fadeout);
                getActivity().finish();

            }
        });
        return view;
    }
    public void getevents(){
        db.collection("Clubs")
                .whereEqualTo("UID", mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                club.setId(document.getId());
                                document.getReference().collection("Events")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()){
                                                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                                                        event = task.getResult().size();
                                                    }
                                                    tvtotalevent.setText(String.valueOf(event));

                                                }

                                            }
                                        });
                            }
                        } else {

                        }
                    }
                });
    }

    public void totalrequest(){

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
                                                                    for (QueryDocumentSnapshot document1 : task11.getResult()) {
                                                                        //Log.d("TAG", document.getId() + " => " + document.getData());
                                                                        request = task11.getResult().size();

                                                                    }
                                                                    tvrequest.setText(String.valueOf(request));
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

    public void facilitybooked(){

        db.collection("Facility")
                .whereEqualTo("clubid" ,mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){

                                db.collection("Facility_Booked")
                                        .whereEqualTo("facilityid",document.getId())
                                        .whereEqualTo("date",date1)
                                        .whereEqualTo("status","UnCompleted")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()){
                                                    for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                                        facilitybooked = task.getResult().size();

                                                    }
                                                    tvfacilitybooked.setText(String.valueOf(facilitybooked));
                                                }

                                            }
                                        });

                            }
                        }

                    }
                });

    }




}