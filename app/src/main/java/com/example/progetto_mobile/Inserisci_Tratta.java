package com.example.progetto_mobile;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

                                                                                    //spinner                               //selezione tempo
public abstract class Inserisci_Tratta extends AppCompatActivity implements AdapterView.OnItemSelectedListener, TimePickerDialog.OnTimeSetListener, 	RadioGroup.OnCheckedChangeListener {

    public RadioGroup radioGroup;
    public Button btnOrario;
    public Button btnOk;

    private String tratta=null;
    private String ora = null;
    private String verso = null;

    private FirebaseUser user;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.inserisci_tratta);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db=FirebaseFirestore.getInstance();

        Spinner spin_tratta = findViewById(R.id.spinner_tratta);
        Spinner spin_posti = findViewById(R.id.spinner_posti);
                                                                                                                                //layout di default
        ArrayAdapter<CharSequence> adapter_tratta = ArrayAdapter.createFromResource(this, R.array.spinner_tratte, android.R.layout.simple_spinner_item);
        adapter_tratta.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  //layout lista di default
        spin_tratta.setAdapter(adapter_tratta); //applica
        spin_tratta.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter_posti = ArrayAdapter.createFromResource(this, R.array.spinner_posti, android.R.layout.simple_spinner_item);
        adapter_posti.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_posti.setAdapter(adapter_posti);
        spin_posti.setOnItemSelectedListener(this);

        btnOk = findViewById(R.id.button_ok);
        radioGroup = findViewById(R.id.radioGroup);
        btnOrario = findViewById(R.id.button_orario);

        btnOrario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {   //anche con gestione dichiarativa
            @Override
            public void onClick(View v) {
                sumbit(tratta, ora, verso);
            }
        });

    }


    public void onRadioButtonClick (View v){        //gestione dichiarativa (xml) dei radiobutton

        int checked=radioGroup.getCheckedRadioButtonId();
        RadioButton button = v.findViewById(checked);
        verso = button.getText().toString();
    }


    @Override   //da interfaccia dello spinner  --> implementato come da documentazione Google
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        tratta = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView textView = findViewById(R.id.textView_ora);
        textView.setText(hourOfDay + ":" + minute);

        ora = hourOfDay + " : " + minute;
    }


    public void sumbit(String tratta, String ora, String verso){

        if (tratta.isEmpty() || ora.isEmpty() || verso.isEmpty())
            Toast.makeText(this, R.string.emptyField,Toast.LENGTH_SHORT).show();

        else{


            CollectionReference viaggio = db.collection("viaggi");
            //raccolgo tutti i dati del viaggio
            Map<String, Object> data = new HashMap<>();
            data.put("tratta", tratta);
            data.put("ora", ora);
            data.put("verso", verso);
            viaggio.document(mAuth.getCurrentUser().getUid()).set(data);

            db.collection("viaggi").document(mAuth.getCurrentUser().getUid()).set(viaggio, SetOptions.merge()) //senza SetOptions se il file esiste viene sovrascritto
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("db_error", "Errore aggiunta viaggio al db "+e);
                            Toast.makeText(Inserisci_Tratta.this, getString(R.string.dbWriteErr),Toast.LENGTH_LONG).show();
                            getParentActivityIntent();
                        }
                    });

            Toast.makeText(this, getString(R.string.added),Toast.LENGTH_LONG).show();
            Intent intent=new Intent(this, ChooseActivity.class);
            startActivity(intent);
        }
    }


}

