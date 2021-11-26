package com.mart.mymartbee.commons;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.mart.mymartbee.storage.StorageDatas;

public class CustomTimePickerDialog extends TimePickerDialog {

    private final static int TIME_PICKER_INTERVAL = 30;
    private TimePicker mTimePicker;
    private final OnTimeSetListener mTimeSetListener;

    private int lastHour = -1;
    private int lastMinute = -1;
    private final boolean mIs24HourView;

    public CustomTimePickerDialog(Context context, OnTimeSetListener listener,
                                  int hourOfDay, int minute, boolean is24HourView) {
        super(context, TimePickerDialog.THEME_HOLO_LIGHT, null, hourOfDay,
                minute / TIME_PICKER_INTERVAL, is24HourView);
        lastHour = hourOfDay;
        lastMinute = minute;
        mTimeSetListener = listener;
        mIs24HourView = is24HourView;
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
                    mTimeSetListener.onTimeSet(mTimePicker, mTimePicker.getHour(),
                            mTimePicker.getMinute() * TIME_PICKER_INTERVAL);

                    Log.e("appSample", "CustomHour: " + mTimePicker.getHour());
                    Log.e("appSample", "CustomMinute: " + mTimePicker.getMinute());
                    Log.e("appSample", "CustomMinute2: " + mTimePicker.getMinute() * TIME_PICKER_INTERVAL);

                    Log.e("appSample", "Hour: " + mTimePicker.getHour());
                    Log.e("appSample", "Minute: " + mTimePicker.getMinute());

                    StorageDatas.getInstance().setCurrHour("" + mTimePicker.getHour());
                    StorageDatas.getInstance().setCurrMint("" + mTimePicker.getMinute() * TIME_PICKER_INTERVAL);
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
            minuteSpinner.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1);
            List<String> displayedValues = new ArrayList<>();
            for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
                displayedValues.add(String.format("%02d", i));
            }
            minuteSpinner.setDisplayedValues(displayedValues
                    .toArray(new String[displayedValues.size()]));

            minuteSpinner.setOnScrollListener(new NumberPicker.OnScrollListener() {
                @Override
                public void onScrollStateChange(NumberPicker view, int scrollState) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        super.onTimeChanged(view, hourOfDay, minute);
        if (lastHour != hourOfDay && lastMinute != minute) {
            view.setCurrentHour(lastHour);
            lastMinute = minute;
        } else {
            lastHour = hourOfDay;
            lastMinute = minute;
        }
    }
}
