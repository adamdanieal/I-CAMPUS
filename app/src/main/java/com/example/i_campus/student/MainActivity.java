package com.example.i_campus.student;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.i_campus.student.Fragment.Facility;
import com.example.i_campus.student.Fragment.Menu;
import com.example.i_campus.student.Fragment.Profile;
import com.example.i_campus.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {

    ChipNavigationBar chipNavigationBar;
    FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chipNavigationBar = findViewById(R.id.nav_bar);

        if (savedInstanceState == null)
        {
            chipNavigationBar.setItemSelected(R.id.nav_home, true);
            fragmentManager = getSupportFragmentManager();
            Menu menu = new Menu();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, menu)
                    .commit();
        }

// item selection part
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i)
                {
                    case R.id.nav_home:
                        fragment = new Menu();
                        break;
                    case R.id.nav_facility:
                        fragment = new Facility();
                        break;
                    case R.id.nav_profile:
                        fragment = new Profile();
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
