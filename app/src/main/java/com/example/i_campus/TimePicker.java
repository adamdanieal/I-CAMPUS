package com.example.i_campus;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.util.Log;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

class CustomTimePickerDialog extends TimePickerDialog {

    private final static int TIME_PICKER_INTERVAL = 5;
    private TimePicker mTimePicker;
    private final TimePickerDialog.OnTimeSetListener mTimeSetListener;
    boolean is24HourView;

    public CustomTimePickerDialog(Context context, OnTimeSetListener listener,
                                  int hourOfDay, int minute, boolean is24HourView) {
        super(context, TimePickerDialog.THEME_HOLO_LIGHT, null, hourOfDay,
                minute / TIME_PICKER_INTERVAL, is24HourView);
        mTimeSetListener = listener;
        this.is24HourView = is24HourView;
    }

    @Override
    public void updateTime(int hourOfDay, int minuteOfHour) {
        mTimePicker.setCurrentHour(hourOfDay);
        mTimePicker.setCurrentMinute(minuteOfHour / TIME_PICKER_INTERVAL);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_POSITIVE:
                if (mTimeSetListener != null) {
                    Log.e("vjenvibv",mTimePicker.getCurrentHour().toString());
                    mTimeSetListener.onTimeSet(mTimePicker, mTimePicker.getCurrentHour(),
                            mTimePicker.getCurrentMinute());
                }
                break;
            case BUTTON_NEGATIVE:
                cancel();
                break;
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            Class<?> classForid = Class.forName("com.android.internal.R$id");
            Field timePickerField = classForid.getField("timePicker");
            mTimePicker = (TimePicker) findViewById(timePickerField.getInt(null));
            Field field = classForid.getField("minute");

            NumberPicker minuteSpinner = (NumberPicker) mTimePicker
                    .findViewById(field.getInt(null));
            minuteSpinner.setMinValue(0);
            minuteSpinner.setMaxValue(59);
            List<String> displayedValues = new ArrayList<>();
            for (int i = 0; i < 60; i += 1) {
                displayedValues.add(String.format("%02d", i));
            }

            minuteSpinner.setDisplayedValues(displayedValues
                    .toArray(new String[displayedValues.size()]));

            NumberPicker mHourSpinner =(NumberPicker) mTimePicker.findViewById(Resources.getSystem().getIdentifier(
                    "hour",
                    "id",
                    "android"
            ));


            mHourSpinner.setMinValue(8);
            mHourSpinner.setMaxValue(13);
            List<String> displayedValue = new ArrayList<>();
                for (int i = 8; i <= 13; i += 1) {
                    displayedValue.add(String.format("%02d", i));
                }
                mHourSpinner.setDisplayedValues(displayedValue
                        .toArray(new String[displayedValue.size()]));




            } catch (Exception e) {
            e.printStackTrace();
        }

    }
}