package com.example.progetto_mobile;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

public class Inserisci_Tratta extends AppCompatActivity implements AdapterView.OnItemSelectedListener, TimePickerDialog.OnTimeSetListener {




    RadioGroup rg;
    RadioButton rb;
    String and_rit = null;
    String ora = null;
    Button button;





    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.inserisci_tratta);

        Spinner spinner = findViewById(R.id.spinner1);
        Spinner spinner2 = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Tratte, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.Posti, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);
        rg = (RadioGroup) findViewById(R.id.rgroup);
        button = (Button) findViewById(R.id.button_Time);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView textView = (TextView) findViewById(R.id.textView_Ora);
        textView.setText(hourOfDay + ":" + minute);

        ora = String.valueOf(hourOfDay +":" +minute);
    }


    public void rbclick (View v){

        int radiobuttonid = rg.getCheckedRadioButtonId();
        RadioButton rb = (RadioButton) findViewById(radiobuttonid);
        and_rit = rb.getText().toString(); //inserisce il testo del radio button alla stringa and_rit

    }






    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text1 = parent.getItemAtPosition(position).toString();
        String text2 = parent.getItemAtPosition(position).toString();

    }



    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    }

