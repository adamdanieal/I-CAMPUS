package com.example.i_campus.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;

import com.example.i_campus.student.Fragment.BookinghistoryAdapter;
import com.example.i_campus.R;
import com.google.android.material.tabs.TabLayout;

public class BookingHistory extends AppCompatActivity {

    String userid;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    BookinghistoryAdapter bookinghistoryadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);

        Intent intent = getIntent();
        userid = intent.getStringExtra("userid");

        Bundle bundle = new Bundle();
        bundle.putString("userid", userid);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager2);

        tabLayout.addTab(tabLayout.newTab().setText("OnGoing"));
        tabLayout.addTab(tabLayout.newTab().setText("Completed"));

        FragmentManager fragmentManager = getSupportFragmentManager();
        bookinghistoryadapter = new BookinghistoryAdapter(fragmentManager,getLifecycle(),bundle);
        viewPager2.setAdapter(bookinghistoryadapter);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

    }
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(BookingHistory.this , MainActivity.class);
        startActivity(intent);
    }
}
