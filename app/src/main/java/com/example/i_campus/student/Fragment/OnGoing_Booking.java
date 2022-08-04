package com.example.i_campus.student.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.i_campus.student.Adapter.RvBookedFacilityAdapter;
import com.example.i_campus.R;
import com.example.i_campus.object.BookedFacility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OnGoing_Booking extends Fragment {

    RecyclerView rv_ongoingbooking;

    View view;
    ProgressBar loadingPB;

    private TextView tvEmptyDb;

    String userid;


    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    RvBookedFacilityAdapter rvBookedFacilityAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_on_going__booking, container, false);
        Bundle bundle = this.getArguments();
        userid = bundle.getString("userid");

        loadingPB = view.findViewById(R.id.idProgressBar);
        tvEmptyDb = view.findViewById(R.id.tvEmptyDb);
        rv_ongoingbooking = view.findViewById(R.id.rv_ongoingbooking);

        ArrayList<BookedFacility> bookedFacilities = new ArrayList<>();
        rvBookedFacilityAdapter= new RvBookedFacilityAdapter(view.getContext(),userid,bookedFacilities);
        rv_ongoingbooking.setAdapter(rvBookedFacilityAdapter );
        rv_ongoingbooking.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL,false));

        db.collection("Facility_Booked")
                .whereEqualTo("UID",mAuth.getCurrentUser().getUid())
                .whereEqualTo("status","UnCompleted")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            loadingPB.setVisibility(View.GONE);
                            if (task.getResult().isEmpty()){
                                tvEmptyDb.setVisibility(View.VISIBLE);
                            }
                            else{
                                for (QueryDocumentSnapshot document :task.getResult()){
                                    bookedFacilities.add(new BookedFacility(
                                                    document.getData().get("UID").toString(),
                                                    document.getData().get("facilityid").toString(),
                                                    document.getData().get("check_in").toString(),
                                                    document.getData().get("check_out").toString(),
                                                    document.getData().get("date").toString(),
                                                    document.getData().get("status").toString(),
                                                    document.getId()
                                            )
                                    );

                                }rvBookedFacilityAdapter.notifyDataSetChanged();
                            }
                        }

                    }
                });

        return view;
    }
}