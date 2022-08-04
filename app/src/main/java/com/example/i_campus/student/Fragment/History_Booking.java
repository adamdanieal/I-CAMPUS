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
import android.widget.TextView;

import com.example.i_campus.student.Adapter.RvBookedFacilityAdapter;
import com.example.i_campus.R;
import com.example.i_campus.object.BookedFacility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class History_Booking extends Fragment{

    private RecyclerView rv_historybooking;

    View view;

    private TextView tvEmptyDb;

    String userid;

    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    RvBookedFacilityAdapter rvBookedFacilityAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history_booking, container, false);

        Bundle bundle = this.getArguments();
        userid = bundle.getString("userid");

        tvEmptyDb = view.findViewById(R.id.tvEmptyDb);
        rv_historybooking = view.findViewById(R.id.rv_historybooking);

        ArrayList<BookedFacility> bookedFacilities = new ArrayList<>();
        rvBookedFacilityAdapter= new RvBookedFacilityAdapter(view.getContext(),userid,bookedFacilities);
        rv_historybooking.setAdapter(rvBookedFacilityAdapter );
        rv_historybooking.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL,false));

        db.collection("Facility_Booked")
                .whereEqualTo("UID",mAuth.getCurrentUser().getUid())
                .whereEqualTo("status","Completed")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
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