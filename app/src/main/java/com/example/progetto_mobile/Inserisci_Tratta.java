package com.example.progetto_mobile;

import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

public class Inserisci_Tratta extends AppCompatActivity implements AdapterView.OnItemSelectedListener, TimePickerDialog.OnTimeSetListener, 	RadioGroup.OnCheckedChangeListener {

    private RadioGroup radioGroup;

    private String verso = null;
    private String ora = null;
    private Button btnOrario;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.inserisci_tratta);

        Spinner posti = findViewById(R.id.spinner_posti);
        Spinner tratta = findViewById(R.id.spinner_tratta);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        btnOrario = (Button) findViewById(R.id.button_orario);

        ArrayAdapter<CharSequence> adapter_tratte = ArrayAdapter.createFromResource(this, R.array.spinner_tratte, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter_posti = ArrayAdapter.createFromResource(this, R.array.spinner_posti, android.R.layout.simple_spinner_item);

        adapter_tratte.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_posti.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        posti.setAdapter(adapter_tratte);
        posti.setOnItemSelectedListener(this);

        tratta.setAdapter(adapter_posti);
        tratta.setOnItemSelectedListener(this);


        btnOrario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });
    }

    public void onRadioButtonClick (View v){

        int checked=radioGroup.getCheckedRadioButtonId();
        RadioButton button = (RadioButton) v.findViewById(checked);
        verso =(String) button.getText().toString();

    }

/////////////////////////////CONTINUA DA QUI -> STUDIA LO SPINNER ////////////////////////////////

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text1 = parent.getItemAtPosition(position).toString();
        String text2 = parent.getItemAtPosition(position).toString();

    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView textView = (TextView) findViewById(R.id.textView_Ora);
        textView.setText(hourOfDay + ":" + minute);

        ora = String.valueOf(hourOfDay +":" +minute);
    }




    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }
}

