package com.example.i_campus.staff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.i_campus.R;
import com.example.i_campus.object.BookedFacility;
import com.example.i_campus.staff.adapter.RvStaffBookedFacilityAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;

public class Staff_facility extends AppCompatActivity {

    RecyclerView rcfacilitybookedlist;
    TextView datetoday;
    ImageView data_not_found;

    String id,facility_name;
    Calendar date = Calendar.getInstance();

    FirebaseFirestore db= FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_facility);

        ArrayList<BookedFacility> bookedFacilities = new ArrayList<>();
        rcfacilitybookedlist = findViewById(R.id.rcfacilitybookedlist);
        datetoday = findViewById(R.id.datetoday);
        data_not_found = findViewById(R.id.data_not_found);

        int year = date.get(Calendar.YEAR) ;
        int month = date.get(Calendar.MONTH) +1 ;
        int day = date.get(Calendar.DAY_OF_MONTH);
        String date1= String.format("%s/%s/%s", year, month, day);
        datetoday.setText("Today : " + date1);

        db.collection("Facility")
                .whereEqualTo("clubid" ,mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            if (task.getResult().isEmpty()){

                            }
                            else{
                                for (QueryDocumentSnapshot document : task.getResult()){
                                    facility_name = document.getData().get("name").toString();

                                    document.getId();
                                    db.collection("Facility_Booked")
                                            .whereEqualTo("facilityid",document.getId())
                                            .whereEqualTo("date",date1)
                                            .whereEqualTo("status","UnCompleted")
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()){
                                                        if (task.getResult().isEmpty()){
                                                            data_not_found.setVisibility(View.VISIBLE);

                                                        }else{
                                                            for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                                                bookedFacilities.add(new BookedFacility(
                                                                        documentSnapshot.getData().get("UID").toString(),
                                                                        documentSnapshot.getData().get("facilityid").toString(),
                                                                        documentSnapshot.getData().get("check_in").toString(),
                                                                        documentSnapshot.getData().get("check_out").toString(),
                                                                        documentSnapshot.getData().get("date").toString(),
                                                                        documentSnapshot.getData().get("status").toString(),
                                                                        documentSnapshot.getId()
                                                                        )
                                                                );
                                                            }
                                                            RvStaffBookedFacilityAdapter rvStaffBookedFacilityAdapter = new RvStaffBookedFacilityAdapter(Staff_facility.this, facility_name, bookedFacilities);
                                                            rcfacilitybookedlist.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false));
                                                            rcfacilitybookedlist.setAdapter(rvStaffBookedFacilityAdapter);
                                                        }

                                                    }

                                                }
                                            });

                                }
                            }
                        }

                    }
                });
    }
}