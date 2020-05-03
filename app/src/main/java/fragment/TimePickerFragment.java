package fragment;


import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.progetto_mobile.R;

import java.util.Calendar;
import java.util.List;
import java.util.zip.Inflater;

//Classe per il time-picker come da documentazione Google
//Eliminata interfaccia "onTimeSetListener" --> gestito in InsertRide.class

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    @Override @NonNull
    public TimePickerDialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }                                                      //non questa classe -> no this (TimePickerDialog.OnTimeSetListener) getActivity()

    public static String convertDate(int input) {
        if (input >= 10) return String.valueOf(input);
        else return ("0" + String.valueOf(input));
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String ora = convertDate(hourOfDay) + " : " + convertDate(minute);
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view2 = inflater.inflate(R.layout.insert2_frag,null);
        EditText editText_ora=view2.findViewById(R.id.textView_ora);
        editText_ora.setText(ora);
        }
}