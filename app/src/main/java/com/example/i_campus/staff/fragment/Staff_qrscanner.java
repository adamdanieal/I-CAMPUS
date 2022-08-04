package com.example.i_campus.staff.fragment;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.VIBRATE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.i_campus.R;
import com.example.i_campus.staff.StaffMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.StringValue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import eu.livotov.labs.android.camview.ScannerLiveView;
import eu.livotov.labs.android.camview.scanner.decoder.zxing.ZXDecoder;

public class Staff_qrscanner extends Fragment {

    private ScannerLiveView camera;

    //dialog
    String UID;
    Button btnClose,btnConfirm;
    TextView email,id,fullname,eventname,data_not_found,title;
    String Dataone;

    Calendar date = Calendar.getInstance();


    FirebaseFirestore db= FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String valid_until;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_staff_qrscanner, container, false);

        if (checkPermission()) {
            // if permission is already granted display a toast message
            Toast.makeText(getContext(), "Permission Granted..", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }

        camera = view.findViewById(R.id.camview);

        camera.setScannerViewEventListener(new ScannerLiveView.ScannerViewEventListener() {
            @Override
            public void onScannerStarted(ScannerLiveView scanner) {
                // method is called when scanner is started
                Toast.makeText(getContext(), "Scanner Started", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScannerStopped(ScannerLiveView scanner) {
                // method is called when scanner is stopped.
                Toast.makeText(getContext(), "Scanner Stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScannerError(Throwable err) {
                // method is called when scanner gives some error.
                Toast.makeText(getContext(), "Scanner Error: " + err.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeScanned(String data) {
                // method is called when camera scans the
                // qr code and the data from qr code is
                // stored in data in string format.
                //scannedTV.setText(data);
                //Toast.makeText(StaffScanQrCode.this, data , Toast.LENGTH_SHORT).show();

                Dialog dialog = new Dialog(getContext());
                View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_staff_scan,null);
                dialog.setContentView(view);
                dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.dialog_background));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
                dialog.show();
                dialog.setCancelable(true);

                Dataone = data;

                title = view.findViewById(R.id.title);
                data_not_found = view.findViewById(R.id.data_not_found);
                eventname = view.findViewById(R.id.eventname);
                fullname = view.findViewById(R.id.fullname);
                id = view.findViewById(R.id.id);
                email = view.findViewById(R.id.email);
                btnConfirm = view.findViewById(R.id.btnConfirm);
                btnClose = view.findViewById(R.id.btnClose);

                DocumentReference docRef = db.collection("Event_Booked").document(Dataone);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()){
                                UID = document.getData().get("UID").toString();
                                document.getData().get("EventID").toString();
                                db.collection("Clubs")
                                        .whereEqualTo("UID",mAuth.getCurrentUser().getUid())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()){
                                                    for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                                        documentSnapshot.getReference().collection("Events").document(document.getData().get("EventID").toString())
                                                                .get()
                                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                        DocumentSnapshot docevent= task.getResult();
                                                                        if (task.getResult().getData().get("is_active").equals(true)){
                                                                            docRef.update("check_in", "Completed")
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            if (task.isSuccessful()){
                                                                                                DocumentReference docRef1 = db.collection("Users").document(UID);
                                                                                                docRef1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                                                    @Override
                                                                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                                                        title.setText("Event : " + docevent.getData().get("name").toString() );
                                                                                                        fullname.setText("Student Name "  + documentSnapshot.getData().get("fullname").toString());
                                                                                                        id.setText("Matric Number : " +documentSnapshot.getData().get("id").toString());
                                                                                                        email.setText("Student Email :  "  +documentSnapshot.getData().get("email").toString());
                                                                                                    }
                                                                                                });
                                                                                            }

                                                                                        }
                                                                                    });
                                                                        }
                                                                        else{
                                                                            data_not_found.setVisibility(View.VISIBLE);
                                                                            data_not_found.setText("Event Already Ended");
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                }
                                            }
                                        });

                            }
                            else{
                                facility();
                            }

                        }
                    }
                });




                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext() , StaffMainActivity.class);
                        startActivity(intent);
                    }
                });

                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });


        return view;
    }



    private boolean checkPermission() {
        // here we are checking two permission that is vibrate
        // and camera which is granted by user and not.
        // if permission is granted then we are returning
        // true otherwise false.
        int camera_permission = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), CAMERA);
        int vibrate_permission = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), VIBRATE);
        return camera_permission == PackageManager.PERMISSION_GRANTED && vibrate_permission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // this method is to request
        // the runtime permission.
        int PERMISSION_REQUEST_CODE = 200;
        ActivityCompat.requestPermissions(getActivity(), new String[]{CAMERA, VIBRATE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onPause() {
        // on app pause the
        // camera will stop scanning.
        camera.stopScanner();
        super.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
        ZXDecoder decoder = new ZXDecoder();
        // 0.5 is the area where we have
        // to place red marker for scanning.
        decoder.setScanAreaPercent(0.8);
        // below method will set secoder to camera.
        camera.setDecoder(decoder);
        camera.startScanner();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        // this method is called when user
        // allows the permission to use camera.
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            boolean cameraaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean vibrateaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            if (cameraaccepted && vibrateaccepted) {
                Toast.makeText(getContext(), "Permission granted..", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Permission Denined \n You cannot use app without providing permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

        public void facility(){

        int year = date.get(Calendar.YEAR) ;
        int month = date.get(Calendar.MONTH) +1 ;
        int day = date.get(Calendar.DAY_OF_MONTH);
        String date1= String.format("%s/%s/%s", year, month, day);
        //Log.e("TAG", date1 );

        DocumentReference docRef = db.collection("Facility_Booked").document(Dataone);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        UID = document.getData().get("UID").toString();
                        document.getData().get("facilityid").toString();
                        if (document.getData().get("date").equals(date1)){

                            db.collection("Facility").document(document.getData().get("facilityid").toString())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            DocumentSnapshot docfacility= task.getResult();
                                            if (task.getResult().getData().get("clubid").equals(mAuth.getCurrentUser().getUid())){

                                                docRef.update("status", "Completed")
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){
                                                                    DocumentReference docRef1 = db.collection("Users").document(UID);
                                                                    docRef1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                        @Override
                                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                            title.setText("Facility : " + docfacility.getData().get("name").toString() );
                                                                            fullname.setText("Student Name "  + documentSnapshot.getData().get("fullname").toString());
                                                                            id.setText("Matric Number : " +documentSnapshot.getData().get("id").toString());
                                                                            email.setText("Student Email :  "  +documentSnapshot.getData().get("email").toString());
                                                                        }
                                                                    });
                                                                }

                                                            }
                                                        });

                                            }
                                            else{
                                                title.setText("Data Not Found");
                                                data_not_found.setVisibility(View.VISIBLE);
                                                data_not_found.setText("User Data Not Found");
                                            }


                                        }
                                    });

                        }
                        else{
                            title.setText("Data Not Found");
                            data_not_found.setVisibility(View.VISIBLE);
                            data_not_found.setText("Your booking Date in on "+ document.getData().get("date").toString() );

                        }





                    }
                    else {

                    }
                }
            }
        });


    }

}
