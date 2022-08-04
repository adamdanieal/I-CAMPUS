package com.example.i_campus.student.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import com.example.i_campus.R;
import com.example.i_campus.student.Adapter.RvClubAdapter;
import com.example.i_campus.object.Club;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Menu extends Fragment {


    HorizontalScrollView layoutHorizontal;
    ImageView iv1,iv2,iv3,iv4,iv5;
    RecyclerView rcview;

    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    String uid = mAuth.getCurrentUser().getUid();
    ImageView[] imageViewArray = new ImageView[]{iv1, iv2, iv3,iv4,iv5};
    int i = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_menu, container, false);

        imageViewArray[0] = view.findViewById(R.id.iv1);
        imageViewArray[1] = view.findViewById(R.id.iv2);
        imageViewArray[2] = view.findViewById(R.id.iv3);
        imageViewArray[3] = view.findViewById(R.id.iv4);
        imageViewArray[4] = view.findViewById(R.id.iv5);

        rcview = view.findViewById(R.id.rcview);

        DisplayEvents();
        ClubCategory();



        return view;
    }
    public void DisplayEvents() {
        db.collectionGroup("Events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot documentEvents : task.getResult()) {
                                String link = documentEvents.getData().get("imageURL").toString();
                                Picasso.get().load(link).into(imageViewArray[i]);
                                i++;

                            }
                        } else {
                        }
                    }
                });


    }
    public void ClubCategory() {

        db.collection("Clubs")
                .whereEqualTo("is_active",true)
                .get()
                .addOnCompleteListener(taskUser -> {
                    if (taskUser.isSuccessful()) {
                        ArrayList<Club> club = new ArrayList<>();
                        for (QueryDocumentSnapshot documentClub : taskUser.getResult()) {
                            club.add(new Club(
                                    documentClub.getData().get("name").toString(),
                                    documentClub.getData().get("linkURL").toString(),
                                    documentClub.getData().get("imageURL").toString(),
                                    documentClub.getId()
                                    )
                            );
                        }
                        rcview.setAdapter(new RvClubAdapter(getContext(),club));
                        rcview.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    }
                });

    }


}