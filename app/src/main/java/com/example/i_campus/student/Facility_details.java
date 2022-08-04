package com.example.i_campus.student;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.i_campus.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.WriterException;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class Facility_details extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap map;

    TextView facility_name,facility_session,facility_time;
    Spinner hour,minute,booked_time;
    CalendarView calendar;
    LinearLayout layout_facility,layout_check,layout_cancel,layout_book;

    String facility,facilityid;
    Double latitude,longitude;
    String booked_date,booked_checkout,booked_id,booked_URL;
    String tempRemovedItem = null;

    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private StorageReference storage;

    String user = mAuth.getCurrentUser().getUid();

    ArrayList <Integer>jam = new ArrayList<>();
    ArrayList <String>minit = new ArrayList<>();
    ArrayList <String>jam1 = new ArrayList<>();
    Calendar date = Calendar.getInstance();

    //dialog box declare
    ImageView idIVQrcode;
    Button btnConfirm;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_details);

        jam = new ArrayList<Integer>(Arrays.asList(10,11,12,13,14,15,16,17,18));
        minit = new ArrayList<String>(Arrays.asList("00","30"));
        jam1 = new ArrayList<String>(Arrays.asList("1 Hours","2 Hours","3 Hours"));

        facility = getIntent().getStringExtra("facility");

        //facilty data
        facility_name = findViewById(R.id.facility_name);
        facility_session = findViewById(R.id.facility_session);
        facility_time = findViewById(R.id.facility_time);

        //linear layout
        layout_facility = findViewById(R.id.layout_facility);
        layout_check = findViewById(R.id.layout_check);
        layout_cancel = findViewById(R.id.layout_cancel);
        layout_book = findViewById(R.id.layout_book);


        hour = findViewById(R.id.hour);
        minute = findViewById(R.id.minute);
        booked_time = findViewById(R.id.booked_time);
        calendar = findViewById(R.id.calendar);

        datepicker();

        if (facility.equals("Gymnasium")){
//            CustomTimePickerDialog mTimePicker;
//            mTimePicker = new CustomTimePickerDialog(Facility_details.this, new TimePickerDialog.OnTimeSetListener() {
//                @Override
//                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//
//                    Toast.makeText(Facility_details.this,String.valueOf(selectedHour) + String.valueOf(selectedMinute)  ,Toast.LENGTH_SHORT).show();
//                }
//            }, 15, 0, true);//Yes 24 hour time
//            mTimePicker.setTitle("Select Time");
//            mTimePicker.show();

            ArrayAdapter<Integer>adapter= new ArrayAdapter<Integer>(Facility_details.this, android.R.layout.simple_list_item_1,jam);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapter.remove(13);
            adapter.remove(14);
            hour.setAdapter(adapter);

            ArrayAdapter<String>adapter1= new ArrayAdapter<String>(Facility_details.this, android.R.layout.simple_list_item_1,minit);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            minute.setAdapter(adapter1);

            ArrayAdapter<String>adapter3= new ArrayAdapter<String>(Facility_details.this, android.R.layout.simple_list_item_1,jam1);
            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            booked_time.setAdapter(adapter3);

            hour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (position == 2 || position == 5){
                        if(tempRemovedItem == null){
                            tempRemovedItem = jam1.remove(2);
                        }

                    }
                    else{
                        if (tempRemovedItem != null  ){
                            jam1.add(tempRemovedItem);
                            tempRemovedItem = null;
                        }
                    }
                    adapter3.notifyDataSetChanged();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        }
        else if(facility.equals("Kayak") || facility.equals("Wall Climbing") || facility.equals("Archery")){

            ArrayAdapter<Integer>adapter= new ArrayAdapter<Integer>(Facility_details.this, android.R.layout.simple_list_item_1,jam);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            hour.setAdapter(adapter);
            adapter.remove(10);
            adapter.remove(11);
            adapter.remove(12);
            adapter.remove(13);
            adapter.remove(14);

            ArrayAdapter<String>adapter1= new ArrayAdapter<String>(Facility_details.this, android.R.layout.simple_list_item_1,minit);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            minute.setAdapter(adapter1);

            ArrayAdapter<String>adapter3= new ArrayAdapter<String>(Facility_details.this, android.R.layout.simple_list_item_1,jam1);
            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            booked_time.setAdapter(adapter3);

        }
        else {
            ArrayAdapter<Integer>adapter= new ArrayAdapter<Integer>(Facility_details.this, android.R.layout.simple_list_item_1,jam);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            hour.setAdapter(adapter);

            ArrayAdapter<String>adapter1= new ArrayAdapter<String>(Facility_details.this, android.R.layout.simple_list_item_1,minit);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            minute.setAdapter(adapter1);

            ArrayAdapter<String>adapter3= new ArrayAdapter<String>(Facility_details.this, android.R.layout.simple_list_item_1,jam1);
            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            booked_time.setAdapter(adapter3);

        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        displayfacility();

    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
    }

    public void displayfacility(){

        db.collection("Facility")
                .whereEqualTo("name", facility)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document_facility : task.getResult()) {
                            facilityid = document_facility.getId();
                            facility_time.setText(document_facility.getData().get("time").toString());
                            facility_session.setText("Maximum Capacity :"+ document_facility.getData().get("maximum").toString()+ " per Session ");
                            facility_name.setText(facility);
                            latitude = Double.parseDouble(document_facility.getData().get("latitude").toString());
                            longitude = Double.parseDouble(document_facility.getData().get("longitude").toString());

                            LatLng location =new LatLng(latitude,longitude);
                            float zoomLevel = 16.0f;
                            map.addMarker(new MarkerOptions().position(location).title("location"));
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel));
                            }
                        }
                    }
                });

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void datepicker(){


        date.set(Calendar.DATE,Calendar.getInstance().getActualMinimum(Calendar.DATE));
        long masa = date.getTime().getTime();
        calendar.setMinDate(masa);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                booked_date = year +"/"+ (month +1)  +"/"+ dayOfMonth;

//                Toast.makeText(Facility_details.this,booked_date ,Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void Calulate_check_out(){
        if (booked_time.getSelectedItem() == "1 Hours" ){

            booked_checkout = (Integer.parseInt(hour.getSelectedItem().toString()) + 1) + ":" + minute.getSelectedItem().toString();
        }
        else if (booked_time.getSelectedItem() == "2 Hours" ){
            booked_checkout = (Integer.parseInt(hour.getSelectedItem().toString()) + 2) + ":" + minute.getSelectedItem().toString();

        }
        else {
            booked_checkout = (Integer.parseInt(hour.getSelectedItem().toString()) + 3) + ":" + minute.getSelectedItem().toString();
        }




    }

    public void check(View view) {

        layout_facility.setAlpha(0.3F);
        layout_check.setVisibility(View.GONE);
        layout_cancel.setVisibility(View.VISIBLE);
        layout_book.setVisibility(View.VISIBLE);

    }

    public void book(View view) {

        if (booked_date == null){
            Toast.makeText(Facility_details.this, "empty",Toast.LENGTH_SHORT).show();
        }
        else{
            Calulate_check_out();

            db.collection("Facility")
                    .whereEqualTo("name",facility)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                                    db.collection("Facility_Booked")
                                            .whereEqualTo("facilityid",queryDocumentSnapshot.getId())
                                            .whereEqualTo("date",booked_date)
                                            .whereEqualTo("check_in", hour.getSelectedItem().toString() +":" + minute.getSelectedItem().toString())
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> taskbooked) {
                                                    if (taskbooked.isSuccessful()){
                                                        int i = 0;
                                                        for (QueryDocumentSnapshot documentMenu : taskbooked.getResult()){
                                                            i += 1;
                                                        }
                                                        if (i>=Integer.parseInt(queryDocumentSnapshot.getData().get("maximum").toString())){
                                                            Toast.makeText(Facility_details.this, "fully",Toast.LENGTH_SHORT).show();
                                                        }
                                                        else{
                                                            Map<String, Object> booked = new HashMap<>();
                                                            booked.put("facilityid",queryDocumentSnapshot.getId());
                                                            booked.put("UID", user);
                                                            booked.put("status", "UnCompleted");
                                                            booked.put("date", booked_date);
                                                            booked.put("check_in", hour.getSelectedItem().toString() +":" + minute.getSelectedItem().toString());
                                                            booked.put("check_out", booked_checkout);

                                                            db.collection("Facility_Booked")
                                                                    .add(booked)
                                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                        @Override
                                                                        public void onSuccess(DocumentReference documentReference) {
                                                                            booked_id= documentReference.getId();
                                                                            Dialog();

                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Toast.makeText(Facility_details.this,"tk jadi",Toast.LENGTH_SHORT).show();

                                                                        }
                                                                    });

                                                        }
                                                    }

                                                }
                                            });


                                }


                            }


                        }
                    });
        }


    }

    private void Dialog() {
        Dialog dialog = new Dialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_booked,null);
        dialog.setContentView(view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        dialog.show();
        dialog.setCancelable(true);

        idIVQrcode = view.findViewById(R.id.idIVQrcode);
        btnConfirm = view.findViewById(R.id.btnConfirm);


        QrGenerator();
        idIVQrcode.setImageBitmap(bitmap);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(Facility_details.this,"Booked Facility Successful",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Facility_details.this , MainActivity.class);
                startActivity(intent);

            }
        });

    }

    public void QrGenerator(){
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
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
        qrgEncoder = new QRGEncoder(booked_id, null, QRGContents.Type.TEXT, dimen);
        try {
            // getting our qrcode in the form of bitmap.
            bitmap = qrgEncoder.encodeAsBitmap();

        } catch (WriterException e) {
            // this method is called for
            // exception handling.

        }
    }

    public void cancel(View view) {

        layout_facility.setAlpha(1);
        layout_check.setVisibility(View.VISIBLE);
        layout_cancel.setVisibility(View.INVISIBLE);
        layout_book.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(Facility_details.this , MainActivity.class);
        startActivity(intent);
    }
}
