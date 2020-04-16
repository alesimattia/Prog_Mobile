package com.example.progetto_mobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editText_nome;
    private EditText editText_cognome;
    private EditText editText_data;
    private EditText editText_email;
    private EditText editText_password;
    private EditText editText_patente;
    private EditText editText_tel;

    private Button cancelBtn;
    private Button registerBtn;

    public String email = null;
    public String password = null;
    public String nome = null;
    public String cognome = null;
    public String patente = null;
    public String data = null;
    public String tel = null;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.getApplicationContext();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.register_layout);
        setTitle(getResources().getString(R.string.registerTitle));

        db = FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();

        editText_email = findViewById(R.id.editText_email);
        editText_password = findViewById(R.id.editText_password);
        editText_nome = findViewById(R.id.editText_nome);
        editText_cognome = findViewById(R.id.editText_cognome);
        editText_patente = findViewById(R.id.editText_patente);
        editText_data = findViewById(R.id.editText_data);
        editText_tel = findViewById(R.id.editText_num);

        cancelBtn = findViewById(R.id.cancelBtn);
        registerBtn = findViewById(R.id.registerBtn);
        cancelBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.registerBtn:

                email = editText_email.getText().toString();
                password = editText_password.getText().toString();
                nome = editText_nome.getText().toString();
                cognome = editText_cognome.getText().toString();
                patente = editText_patente.getText().toString();
                data = editText_data.getText().toString();      //VERIFICA con formato DATE
                tel = editText_tel.getText().toString();


               if(nome.isEmpty()){
                    editText_nome.setError(getResources().getString(R.string.err_richiesto));   //VERIFICA SE BASTA getString(...)
                    editText_nome.requestFocus();
                    return;
                }
                if(cognome.isEmpty()){
                    editText_cognome.setError(getResources().getString(R.string.err_richiesto));
                    editText_cognome.requestFocus();
                    return;
                }
                if(data.isEmpty()){
                    editText_data.setError(getResources().getString(R.string.err_richiesto));
                    editText_data.requestFocus();
                    return; }
                if(email.isEmpty()){
                    editText_email.setError(getResources().getString(R.string.err_richiesto));
                    editText_email.requestFocus();
                    return;
                }
                if(password.isEmpty() || password.length()<6){
                    editText_password.setError(getResources().getString(R.string.passAlert));
                    editText_password.requestFocus();
                    return;
                }
                if(tel.isEmpty()){
                    editText_tel.setError(getResources().getString(R.string.err_richiesto));
                    editText_tel.requestFocus();
                    return;
                }

                registration(email, password, nome, patente, data, tel);
                break;

            case R.id.cancelBtn:
                Intent main=new Intent(this, MainActivity.class);
                startActivity(main);
                break;
        }
    }

    private void registration(final String email, final String password, final String nome, final String patente, final String dataN, final String tel) {

         mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() { //operazione asincrona
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {

                    //CollectionReference user = db.collection("utenti");
                    //raccolgo tutti i dati dell'utente
                    Map<String, Object> data = new HashMap<>();
                    data.put("email", email);   //oppure mAuth.getCurrentUser().getEmail();
                    data.put("password", password);
                    data.put("nome", nome);
                    data.put("patente",patente);
                    data.put("data", dataN);
                    data.put("telefono",tel);

                    db.collection("utenti").document(mAuth.getCurrentUser().getUid())
                            .set(data, SetOptions.merge()) //senza SetOptions se il file esiste viene sovrascritto
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("db_error", "Errore aggiunta info. utente al db"+e);
                                    Toast.makeText(RegisterActivity.this, getString(R.string.dbWriteErr),Toast.LENGTH_LONG).show();
                                    finish();   //riavvia l'activity
                                    startActivity(getIntent());
                                }
                            })
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent intent = new Intent(RegisterActivity.this, ChooseActivity.class);
                                    startActivity(intent);
                                }
                            });
                }
                else Toast.makeText(RegisterActivity.this, getString(R.string.errorSignup),Toast.LENGTH_SHORT).show();
            }
        });
    }

}