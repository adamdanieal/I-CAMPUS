package com.example.i_campus.staff.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.i_campus.R;
import com.example.i_campus.object.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class RvStaffEventlistAdapter extends RecyclerView.Adapter<RvStaffEventlistAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Event> event;

    private View view;
    private Dialog dialog;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public RvStaffEventlistAdapter(Context context, ArrayList<Event> event) {
        this.context = context;
        this.event = event;
    }

    @NonNull
    @Override
    public RvStaffEventlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_event,parent,false);
        db= FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        dialog = new Dialog(view.getContext());
        return new RvStaffEventlistAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RvStaffEventlistAdapter.ViewHolder holder, int position) {

        Event currentevent = event.get(position);

        Glide.with(context).load(currentevent.getImageURL()).into(holder.event_image);
        holder.event_date.setText(currentevent.getDate());
        holder.event_month.setText(currentevent.getMonth());
        holder.event_name.setText(currentevent.getName());
        holder.event_venue.setText(currentevent.getVenue());

        holder.event_image.setOnClickListener(v -> {
            //dialogInfo(currentevent);

        });

    }


    private void dialogInfo(Event currentevent) {
        view = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog,null);

        dialog.setContentView(view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        TextView eventname,id,fullname,status;
        ImageView idIVQrcode,data_not_found;
        Button btnConfirm;

        eventname = view.findViewById(R.id.eventname);
        id = view.findViewById(R.id.id);
        fullname = view.findViewById(R.id.fullname);
        status = view.findViewById(R.id.status);
        data_not_found = view.findViewById(R.id.data_not_found);
        idIVQrcode = view.findViewById(R.id.idIVQrcode);
        btnConfirm = view.findViewById(R.id.btnConfirm);

        eventname.setText("Event : " + currentevent.getName());

        DocumentReference docRef = db.collection("Users").document(mAuth.getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    fullname.setText("Name : " + document.getData().get("fullname").toString());
                    id.setText("Matric Number : " + document.getData().get("id").toString());
                }
            }
        });

        btnConfirm.setOnClickListener(v -> dialog.dismiss());

        dialog.setCancelable(false);
        dialog.show();
    }


    @Override
    public int getItemCount() {
        return event == null ? 0:event.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView event_name,event_venue,event_date,event_month;
        ImageView event_image;

        public ViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            event_name = itemView.findViewById(R.id.event_name);
            event_venue = itemView.findViewById(R.id.event_venue);
            event_date = itemView.findViewById(R.id.event_date);
            event_month = itemView.findViewById(R.id.event_month);
            event_image = itemView.findViewById(R.id.event_image);
        }
    }
}
