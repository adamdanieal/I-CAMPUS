package com.example.i_campus.student.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.i_campus.student.Facility_details;
import com.example.i_campus.R;

public class Facility extends Fragment {

    LinearLayout layout,layout1,gym,badminton,takraw,ping_pong,bicycle,squash,kayak
            ,tennis,futsal,rugby,football
            ,show_more, volleyball,softball,hockey,wall_climbing,petanque,basketball,archery;

    String facility;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_facility, container, false);

        //facility declare
        gym = view.findViewById(R.id.gym);
        badminton = view.findViewById(R.id.badminton);
        takraw = view.findViewById(R.id.takraw);
        ping_pong = view.findViewById(R.id.ping_pong);
        bicycle = view.findViewById(R.id.bicycle);
        squash = view.findViewById(R.id.squash);
        kayak = view.findViewById(R.id.kayak);
        tennis = view.findViewById(R.id.tennis);
        futsal = view.findViewById(R.id.futsal);
        rugby = view.findViewById(R.id.rugby);
        football = view.findViewById(R.id.football);
        show_more = view.findViewById(R.id.show_more);
        volleyball = view.findViewById(R.id.volleyball);
        softball = view.findViewById(R.id.softball);
        hockey = view.findViewById(R.id.hockey);
        wall_climbing = view.findViewById(R.id.wall_climbing);
        petanque = view.findViewById(R.id.petanque);
        basketball = view.findViewById(R.id.basketball);
        archery = view.findViewById(R.id.archery);

        //layout declare
        layout = view.findViewById(R.id.layout);
        layout1 = view.findViewById(R.id.layout1);

        show_more.setOnClickListener(v -> {
            show_more.setVisibility(View.GONE);
            volleyball.setVisibility(View.VISIBLE);
            layout.setVisibility(View.VISIBLE);
            layout1.setVisibility(View.VISIBLE);
        });

        selector();

        return view;
    }
    public void selector(){
        gym.setOnClickListener(v -> {
            facility = "Gymnasium";
            gotofacility();
        });

        badminton.setOnClickListener(v -> {
            facility = "Badminton";
            gotofacility();
        });

        takraw.setOnClickListener(v -> {
            facility = "Sepak Takraw";
            gotofacility();
        });

        ping_pong.setOnClickListener(v -> {
            facility = "Ping Pong";
            gotofacility();
        });

        bicycle.setOnClickListener(v -> {
            facility = "Bicycle";
            gotofacility();
        });

        squash.setOnClickListener(v -> {
            facility = "Squash";
            gotofacility();
        });

        kayak.setOnClickListener(v -> {
            facility = "Kayak";
            gotofacility();
        });

        tennis.setOnClickListener(v -> {
            facility = "Tennis";
            gotofacility();
        });

        futsal.setOnClickListener(v -> {
            facility = "Futsal";
            gotofacility();
        });

        rugby.setOnClickListener(v -> {
            facility = "Rugby";
            gotofacility();
        });

        football.setOnClickListener(v -> {
            facility = "Football";
            gotofacility();
        });

        volleyball.setOnClickListener(v -> {
            facility = "Volleyball";
            gotofacility();
        });

        softball.setOnClickListener(v -> {
            facility = "Softball";
            gotofacility();
        });

        hockey.setOnClickListener(v -> {
            facility = "Hockey";
            gotofacility();
        });

        wall_climbing.setOnClickListener(v -> {
            facility = "Wall Climbing";
            gotofacility();
        });

        petanque.setOnClickListener(v -> {
            facility = "Petanque";
            gotofacility();
        });

        basketball.setOnClickListener(v -> {
            facility = "Basketball";
            gotofacility();
        });

        archery.setOnClickListener(v -> {
            facility = "Archery";
            gotofacility();
        });

    }

    public void gotofacility(){
        Intent intent = new Intent(getActivity(), Facility_details.class);
        intent.putExtra("facility",facility);
        startActivity(intent);
    }


}