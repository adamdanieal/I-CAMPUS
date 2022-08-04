package com.example.i_campus.staff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.i_campus.R;
import com.example.i_campus.staff.fragment.StaffProfile;
import com.example.i_campus.staff.fragment.Staff_menu;
import com.example.i_campus.staff.fragment.Staff_qrscanner;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class StaffMainActivity extends AppCompatActivity {

    ChipNavigationBar chipNavigationBar;
    FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_main);

        chipNavigationBar = findViewById(R.id.nav_bar);

        if (savedInstanceState == null)
        {
            chipNavigationBar.setItemSelected(R.id.nav_home, true);
            fragmentManager = getSupportFragmentManager();
            Staff_menu staff_menu = new Staff_menu();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, staff_menu)
                    .commit();
        }

        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i)
                {
                    case R.id.nav_home:
                        fragment = new Staff_menu();
                        break;
                    case R.id.nav_qrscanner:
                        fragment = new Staff_qrscanner();
                        break;
                    case R.id.nav_profile:
                        fragment = new StaffProfile();
                        break;
                }
                if (fragment != null)
                {
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
    }


}