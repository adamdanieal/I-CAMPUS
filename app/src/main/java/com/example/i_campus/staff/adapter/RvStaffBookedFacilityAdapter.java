package com.example.i_campus.staff.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.i_campus.R;
import com.example.i_campus.object.BookedFacility;

import java.util.ArrayList;

import eu.livotov.labs.android.camview.ScannerLiveView;

public class RvStaffBookedFacilityAdapter extends RecyclerView.Adapter<RvStaffBookedFacilityAdapter.ViewHolder> {
    private final Context context;
    private final String facility_name;
    private final ArrayList<BookedFacility> bookedFacilities;

    private View view;
    private Dialog dialog;
    private ScannerLiveView camera;


    public RvStaffBookedFacilityAdapter(Context context, String facility_name, ArrayList<BookedFacility> bookedFacilities) {
        this.bookedFacilities = bookedFacilities;
        this.context = context;
        this.facility_name = facility_name;
    }

    @NonNull
    @Override
    public RvStaffBookedFacilityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_bookedhistory,parent,false);
        return new RvStaffBookedFacilityAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RvStaffBookedFacilityAdapter.ViewHolder holder, int position) {
        BookedFacility booked = bookedFacilities.get(position);
        holder.name.setText("Facility name : " + facility_name);
        holder.time.setText("Time : " + booked.getCheck_in() + " - " + booked.getCheck_out());
        holder.id.setText("Booked ID : " + booked.getId());
        holder.status.setText("Status : " + booked.getStatus());

        holder.cardOrderInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }



    @Override
    public int getItemCount() {
        return bookedFacilities.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardOrderInfo;
        TextView name,time,id,id_matric,status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardOrderInfo = itemView.findViewById(R.id.cardOrderInfo);
            name = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.time);
            id = itemView.findViewById(R.id.id);
            id_matric = itemView.findViewById(R.id.id_matric);
            status = itemView.findViewById(R.id.status);

            id_matric.setVisibility(View.GONE);
        }
    }
}
