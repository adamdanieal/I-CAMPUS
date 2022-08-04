package com.example.i_campus.student.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.i_campus.student.Events;
import com.example.i_campus.R;
import com.example.i_campus.object.Event;

import java.util.ArrayList;

public class RvEventAdapter extends RecyclerView.Adapter<RvEventAdapter.RvEventHolder> {
    private final Context context;
    private final ArrayList<Event> event;
    private final String clubid;

    int row_index = -1;

    public RvEventAdapter(Context context,String clubid, ArrayList<Event> event) {
        this.context = context;
        this.event = event;
        this.clubid = clubid;
    }

    @NonNull
    @Override
    public RvEventAdapter.RvEventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_event,parent,false);
        return new RvEventHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RvEventAdapter.RvEventHolder holder, @SuppressLint("RecyclerView") int position) {

        Event currentevent = event.get(position);

        Glide.with(context).load(currentevent.getImageURL()).into(holder.event_image);
        holder.event_date.setText(currentevent.getDate());
        holder.event_month.setText(currentevent.getMonth());
        holder.event_name.setText(currentevent.getName());
        holder.event_venue.setText(currentevent.getVenue());

        holder.event_layout.setOnClickListener(v -> {
            row_index = position;

            Intent intent = new Intent(v.getContext() , Events.class);

            intent.putExtra("clubid",clubid);
            intent.putExtra("id",currentevent.getId());
            intent.putExtra("name",currentevent.getName());
            intent.putExtra("image",currentevent.getImageURL());
            v.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return event.size();
    }

    public static class RvEventHolder extends RecyclerView.ViewHolder{
        ConstraintLayout event_layout;
        TextView event_name,event_venue,event_date,event_month;
        ImageView event_image;
        public RvEventHolder(@NonNull View itemView) {
            super(itemView);
            event_layout = itemView.findViewById(R.id.event_layout);
            event_name = itemView.findViewById(R.id.event_name);
            event_venue = itemView.findViewById(R.id.event_venue);
            event_date = itemView.findViewById(R.id.event_date);
            event_month = itemView.findViewById(R.id.event_month);
            event_image = itemView.findViewById(R.id.event_image);
        }
    }
}
