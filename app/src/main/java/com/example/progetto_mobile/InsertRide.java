package com.example.progetto_mobile;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import fragment.DatePickerFragment;
import fragment.TimePickerFragment;

//spinner                          //selezione tempo
public class InsertRide extends AppCompatActivity implements AdapterView.OnItemSelectedListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener, RadioGroup.OnCheckedChangeListener {

    public RadioGroup radioGroup;
    public Button btnOrario;
    public Button btnData;
    public ImageButton btnOk;
    public ImageButton btnBack;

    private String tratta=null;
    private String ora = null;
    private String data=null;
    private String verso = null;
    private String posti=null;
    private String phone=null;

    private FirebaseUser user;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;



    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_ride);

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
        btnData = findViewById(R.id.button);
        btnBack=findViewById(R.id.button_back1);

        btnOrario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        btnData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });


        btnOk.setOnClickListener(new View.OnClickListener() {   //oppure con gestione dichiarativa
            @Override
            public void onClick(View v) {
                sumbit(tratta, ora, data, verso, posti);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
           @Override public void onClick(View v) {   finish();   }
        });

    }



    public void onRadioButtonClick (View v){        //gestione dichiarativa (xml) dei radiobutton
        int checked=radioGroup.getCheckedRadioButtonId();
        RadioButton radio = v.findViewById(checked);
        verso = radio.getText().toString();
    }



    @Override   //da interfaccia dello spinner  --> implementato come da documentazione Google
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()){
            case R.id.spinner_posti :
                posti=parent.getSelectedItem().toString();
                break;
            case R.id.spinner_tratta :
                tratta=parent.getSelectedItem().toString();
                break;
        }
    }



    public static String convertDate(int input) {
        if (input >= 10) return String.valueOf(input);
        else return ("0" + String.valueOf(input));
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView textView = findViewById(R.id.textView_ora);
        ora = convertDate(hourOfDay) + " : " + convertDate(minute);
        textView.setText(ora);
    }



    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        data = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(c.getTime());

        TextView textView = findViewById(R.id.textView_data);
        textView.setText(data);
    }



    final AtomicBoolean check = new AtomicBoolean(false);
    public void getNumber() {

        DocumentReference docRef = db.collection("utenti").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        phone=document.getString("telefono");
                        check.set(true);
                    }
                }
                else Log.w("tag","errore accesso db");
            }
        });
    }



    public void sumbit(final String tratta,final String ora, final String data, final String verso, final String posti){
        if (verso==null || ora==null || data==null)
            Toast.makeText(InsertRide.this, R.string.emptyField, Toast.LENGTH_SHORT).show();
        else {
            getNumber();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (check.get()) {  //attende la lettura dal db del telefono
                        Map<String, Object> pack = new HashMap<>();
                        pack.put("tratta", tratta);
                        pack.put("ora", ora);
                        pack.put("data", data);
                        pack.put("verso", verso);
                        pack.put("posti", posti);
                        pack.put("telefono", phone);
                        pack.put("user", user.getUid());

                        /*db.collection("viaggi").document(user.getUid()).collection("travel").document(radndom UID)
                        Salva un documento per ogni utente -> poi subcollection con un documento per viaggio --> di difficile accesso*/
                        db.collection("viaggi").document(/*radndom UID*/)   //un documento per viaggio
                                .set(pack)
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("db_error", "Errore aggiunta viaggio al db " + e);
                                        Toast.makeText(InsertRide.this, getString(R.string.dbWriteErr), Toast.LENGTH_LONG).show();
                                        getParentActivityIntent();
                                    }
                                });

                        Toast.makeText(InsertRide.this, getString(R.string.added), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(InsertRide.this, ChooseActivity.class);
                        startActivity(intent);
                    }
                }
            }, 1000);
        }
    }





    //Metodi dell'interfaccia Timepicker.onSetListener  --> non utilizzati
    @Override public void onNothingSelected(AdapterView<?> parent) { /*null*/ }
    @Override public void onCheckedChanged(RadioGroup group, int checkedId) { /*null*/ }
    @Override public void onPointerCaptureChanged(boolean hasCapture) { /*null*/ }
}

