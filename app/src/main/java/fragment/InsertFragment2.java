package fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.progetto_mobile.InsertRide;
import com.example.progetto_mobile.R;

import java.text.DateFormat;
import java.util.Calendar;


public class InsertFragment2 extends Fragment {

    private ImageButton btnOrario;
    private ImageButton btnData;
    private ImageButton btnOk;
    private ImageButton btnBack;
    private TextView textView_ora;
    private TextView textView_data;

    private String ora = null;
    private String data = null;

    private TimePickerDialog timePicker;
    private DatePickerDialog datePicker;

    private Bundle bundle;

    public InsertFragment2() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        bundle = getArguments();
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.insert2_frag, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        btnOk = view.findViewById(R.id.button_confirm);
        btnBack = view.findViewById(R.id.button_back2);
        btnOrario = view.findViewById(R.id.button_orario);
        btnData = view.findViewById(R.id.button);
        textView_ora = view.findViewById(R.id.textView_ora);
        textView_data = view.findViewById(R.id.textView_data);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.placeholder, new InsertFragment1()).commit();
            }
        });

        btnOrario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        ora = convertDate(hourOfDay) + " : " + convertDate(minute);
                        textView_ora.setText(ora);
                    }
                }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true);
                timePicker.show();
            }
        });

        btnData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, month);
                        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        data = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(c.getTime());

                        textView_data.setText(data);
                    }
                },  Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                datePicker.show();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {   //oppure con gestione dichiarativa
            @Override
            public void onClick(View v) {
                if (ora==null || data==null)  Toast.makeText(getActivity(), R.string.emptyField, Toast.LENGTH_SHORT).show();
                else  ((InsertRide) getActivity()).sumbit(bundle.getString("tratta"),
                                                          bundle.getString("verso"),
                                                          bundle.getString("posti"), ora, data);
            }
        });
    }


    public static String convertDate(int input) {
        if (input >= 10) return String.valueOf(input);
        else return ("0" + String.valueOf(input));
    }
}
