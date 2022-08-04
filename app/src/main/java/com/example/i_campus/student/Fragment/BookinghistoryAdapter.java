package com.example.i_campus.student.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class BookinghistoryAdapter extends FragmentStateAdapter {
    private Bundle bundle;


    public BookinghistoryAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, Bundle bundle) {
        super(fragmentManager, lifecycle);
        this.bundle = bundle;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if (position == 1) {
            History_Booking historyBooking = new History_Booking();
            historyBooking.setArguments(bundle);
            return  historyBooking;
        }

        OnGoing_Booking onGoingBooking = new OnGoing_Booking();
        onGoingBooking.setArguments(bundle);
        return  onGoingBooking;

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
