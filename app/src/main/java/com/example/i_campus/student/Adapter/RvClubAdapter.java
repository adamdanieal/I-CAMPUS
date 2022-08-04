package com.example.i_campus.student.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.i_campus.student.ClubCategory;
import com.example.i_campus.R;
import com.example.i_campus.object.Club;

import java.util.ArrayList;

public class RvClubAdapter extends RecyclerView.Adapter<RvClubAdapter.RvClubHolder>
{
    private final Context context;
    private final ArrayList<Club> club;
    int row_index = -1;

    public RvClubAdapter(Context context, ArrayList<Club> club) {
        this.context = context;
        this.club = club;
    }

    @NonNull
    @Override
    public RvClubAdapter.RvClubHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_club,parent,false);
        return new RvClubHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RvClubAdapter.RvClubHolder holder, @SuppressLint("RecyclerView") int position) {
        Club currentClub = club.get(position);

        Glide.with(context).load(currentClub.getImageURL()).into(holder.clubimage);
        holder.clubname.setText(currentClub.getName());
        holder.linearLayout.setOnClickListener(v -> {
            row_index = position;

            Intent intent = new Intent(v.getContext() , ClubCategory.class);
            intent.putExtra("id",currentClub.getId());
            intent.putExtra("name",currentClub.getName());
            intent.putExtra("image",currentClub.getImageURL());
            intent.putExtra("link",currentClub.getlinkURL());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return club.size();
    }

    public static class RvClubHolder extends RecyclerView.ViewHolder {

        TextView clubname;
        ImageView clubimage;
        LinearLayout linearLayout;
        public RvClubHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            clubimage = itemView.findViewById(R.id.clubimage);
            clubname = itemView.findViewById(R.id.clubname);

        }
    }
}
