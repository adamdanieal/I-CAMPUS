package com.example.i_campus.staff.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.i_campus.R;
import com.example.i_campus.object.BookedEvent;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class RvRequestPendingAdapter extends RecyclerView.Adapter<RvRequestPendingAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<BookedEvent> bookedEvents;

    private Dialog dialog;
    private View view;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public RvRequestPendingAdapter(Context context, ArrayList<BookedEvent> bookedEvents) {
        this.context = context;
        this.bookedEvents = bookedEvents;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_staffrequest,parent,false);
        db= FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        BookedEvent currentbooked = bookedEvents.get(position);

        holder.booked_id.setText("Booked ID : " + currentbooked.getId());
        holder.uid.setText("User ID " + currentbooked.getUID());
        holder.event_id.setText("Event ID : " + currentbooked.getEvent_id());
        holder.status.setText("Status: " + currentbooked.getStatus());

        holder.cardOrderInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                dialog.setCancelable(false);
                dialog.setTitle("Request Pending");
                dialog.setMessage("Are you Sure want to Appprove?");
                dialog.setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Action for "Cancel".
                        dialog.dismiss();

                    }
                })
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DocumentReference washingtonRef = db.collection("Event_Booked").document(currentbooked.getId());

                                washingtonRef
                                        .update("status", "Completed")
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(view.getContext(), "Successful Approved",
                                                        Toast.LENGTH_LONG).show();
                                                dialog.dismiss();
                                            }
                                        });

                            }
                        });

                final AlertDialog alert = dialog.create();
                alert.show();

                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(view.getResources().getColor(android.R.color.holo_red_light));
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(view.getResources().getColor(R.color.black));



            }
        });
    }


    @Override
    public int getItemCount() {

        return bookedEvents == null ? 0:bookedEvents.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardOrderInfo;
        TextView booked_id,uid,event_id,status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardOrderInfo = itemView.findViewById(R.id.cardOrderInfo);
            booked_id = itemView.findViewById(R.id.booked_id);
            uid = itemView.findViewById(R.id.uid);
            event_id = itemView.findViewById(R.id.event_id);
            status = itemView.findViewById(R.id.status);
        }
    }
}
