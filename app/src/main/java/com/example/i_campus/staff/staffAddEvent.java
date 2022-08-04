package com.example.i_campus.staff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.i_campus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class staffAddEvent extends AppCompatActivity {

    Spinner month,year;
    ImageView event_image;
    EditText event_name,event_venue,event_description,event_contact,date,event_time;
    SwitchCompat activebtn;


    private Uri uri;
    String id;

    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    StorageReference storage = FirebaseStorage.getInstance().getReference("Events/");

    ArrayList<String> bulan = new ArrayList<>();
    ArrayList <String>tahun = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_add_event);

        bulan = new ArrayList<String>(Arrays.asList("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"));
        tahun = new ArrayList<String>(Arrays.asList("2022","2023","2024"));

        id= getIntent().getExtras().getString("id");

        month = findViewById(R.id.month);
        year = findViewById(R.id.year);
        event_image = findViewById(R.id.event_image);
        event_name = findViewById(R.id.event_name);
        event_venue = findViewById(R.id.event_venue);
        event_description = findViewById(R.id.event_description);
        event_contact = findViewById(R.id.event_contact);
        date = findViewById(R.id.date);
        event_time = findViewById(R.id.event_time);
        activebtn = findViewById(R.id.activebtn);


        ArrayAdapter<String> adapter1= new ArrayAdapter<String>(staffAddEvent.this, android.R.layout.simple_list_item_1,bulan);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        month.setAdapter(adapter1);

        ArrayAdapter<String>adapter3= new ArrayAdapter<String>(staffAddEvent.this, android.R.layout.simple_list_item_1,tahun);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(adapter3);
    }

    public void changeimage(View view) {
        openFileChooser();
    }

    public void confirm(View view) {
        if (event_name.getText().toString().isEmpty() && event_venue.getText().toString().isEmpty()&& event_description.getText().toString().isEmpty()&& event_contact.getText().toString().isEmpty()){
            event_name.setError("field cannot be empty");
            event_venue.setError("field cannot be empty");
            event_description.setError("field cannot be empty");
            event_contact.setError("field cannot be empty");
        }
        else{
            if(uri !=null){
                addintodb();

            }
            else{
                Toast.makeText(staffAddEvent.this,"Picture is Empty!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openFileChooser(){
        Intent photoPickerIntent = new Intent();
        photoPickerIntent.setType("image/*");
        photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(photoPickerIntent, 1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            uri = data.getData();
            Picasso.get().load(uri).into(event_image);
        }
    }

    public void addintodb(){

        StorageReference fileReference = storage.child(event_name.getText().toString() + ".jpg" );

        fileReference.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {

                                Map<String, Object> event = new HashMap<>();
                                event.put("date",date.getText().toString());
                                event.put("description", event_description.getText().toString());
                                event.put("imageURL",task.getResult().toString());
                                event.put("is_active",activebtn.isChecked());
                                event.put("month",month.getSelectedItem().toString());
                                event.put("name",event_name.getText().toString());
                                event.put("venue",event_venue.getText().toString());

                                db.collection("Clubs").document(id).collection("Events")
                                        .add(event)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(staffAddEvent.this,"Successful Added" , Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(staffAddEvent.this , StaffMainActivity.class);
                                                overridePendingTransition(R.transition.fadein,R.transition.fadeout);
                                                finish();
                                                startActivity(intent);
                                            }
                                        });

                            }
                        });
                    }
                });



    }

    public void cancel(View view) {
        Intent intent = new Intent(staffAddEvent.this , StaffMainActivity.class);
        overridePendingTransition(R.transition.fadein,R.transition.fadeout);
        finish();
        startActivity(intent);
    }
}