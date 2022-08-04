package com.example.i_campus.student.Adapter;

import static android.content.Context.WINDOW_SERVICE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.i_campus.R;
import com.example.i_campus.object.BookedFacility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.WriterException;

import java.util.ArrayList;
import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class RvBookedFacilityAdapter extends RecyclerView.Adapter<RvBookedFacilityAdapter.ViewHolder> {
    private Context context;
    public String userid;
    private final ArrayList<BookedFacility> bookedFacilities;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private View view;
    private Dialog orderDialog;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;


    // creating constructor for our adapter class
    public RvBookedFacilityAdapter(Context context, String userid, ArrayList<BookedFacility> bookedFacilities) {
        this.bookedFacilities = bookedFacilities;
        this.context = context;
        this.userid = userid;
    }

    @NonNull
    @Override
    public RvBookedFacilityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_bookedhistory,parent,false);


        orderDialog = new Dialog(view.getContext());
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RvBookedFacilityAdapter.ViewHolder holder, int position) {
        BookedFacility booked = bookedFacilities.get(position);
        holder.name.setText("Facility name : " + booked.getFacilityid());
        holder.time.setText("Time : " + booked.getCheck_in() + " - " + booked.getCheck_out());
        holder.id_matric.setText("Matric Number : " + userid);
        holder.id.setText("Booked ID : " + booked.getId());
        holder.status.setText("Status : " + booked.getStatus());

        holder.cardOrderInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInfo(booked);
            }
        });

    }

    private void dialogInfo(BookedFacility booked) {
        view = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_booked,null);

        orderDialog.setContentView(view);
        orderDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        orderDialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        ImageView idIVQrcode;
        Button btnConfirm;

        idIVQrcode = view.findViewById(R.id.idIVQrcode);
        btnConfirm = view.findViewById(R.id.btnConfirm);


        WindowManager manager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        // initializing a variable for default display.
        Display display = manager.getDefaultDisplay();

        // creating a variable for point which
        // is to be displayed in QR Code.
        Point point = new Point();
        display.getSize(point);

        // getting width and
        // height of a point
        int width = point.x;
        int height = point.y;

        // generating dimension from width and height.
        int dimen = width < height ? width : height;
        dimen = dimen * 3 / 4;

        // setting this dimensions inside our qr code
        // encoder to generate our qr code.
        qrgEncoder = new QRGEncoder(booked.getId(), null, QRGContents.Type.TEXT, dimen);
        try {
            // getting our qrcode in the form of bitmap.
            bitmap = qrgEncoder.encodeAsBitmap();

        } catch (WriterException e) {
            // this method is called for
            // exception handling.

        }
        idIVQrcode.setImageBitmap(bitmap);

        btnConfirm.setOnClickListener(v -> orderDialog.dismiss());

        orderDialog.setCancelable(false);
        orderDialog.show();
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
        }
    }
}

