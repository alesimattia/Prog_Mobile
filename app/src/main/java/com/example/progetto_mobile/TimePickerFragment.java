package com.example.progetto_mobile;


import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import java.util.Calendar;

//Classe per il time-picker come da documentazione Google
//Eliminata interfaccia "onTimeSetListener" --> gestito in InsertRide.class

public class TimePickerFragment extends DialogFragment {

    @Override @NonNull
    public TimePickerDialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener) getActivity(), hour, minute, DateFormat.is24HourFormat(getActivity()));
    }                                                      //non questa classe -> no this

}