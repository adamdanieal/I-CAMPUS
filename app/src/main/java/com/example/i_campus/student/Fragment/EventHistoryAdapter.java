package com.example.i_campus.student.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class EventHistoryAdapter extends FragmentStateAdapter {
    private Bundle bundle;

    public EventHistoryAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, Bundle bundle) {
        super(fragmentManager, lifecycle);
        this.bundle = bundle;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            History_Events history_events = new History_Events();
            history_events.setArguments(bundle);
            return  history_events;
        }

        OnGoing_Events onGoing_events = new OnGoing_Events();
        onGoing_events.setArguments(bundle);
        return  onGoing_events;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
