package com.aitekteam.developer.playnote.helpers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

import androidx.fragment.app.DialogFragment;

public class DatePickerHelper extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    private DatePickerHandler handler;

    public void setHandler(DatePickerHandler handler) {
        this.handler = handler;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (handler != null) {
            handler.onDateSet(year, month, dayOfMonth);
        }
    }

    public interface DatePickerHandler {
        void onDateSet(int yer, int month, int dayOfMonth);
    }
}
