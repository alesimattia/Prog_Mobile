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

    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context=super.getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        setTitle(context.getResources().getString(R.string.registerTitle));

        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();

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

                List<String> dati = new ArrayList<String>();
                dati.add(email);
                dati.add(password);
                dati.add(nome);
                dati.add(cognome);
                dati.add(data);
                dati.add(tel);

                for(String current : dati)
                    if(current.isEmpty()){
                        context.getResources().getString(R.string.emptyField2);
                        //context.requestFocus(current);
                        return;
                    }

                /*
               if(nome.isEmpty()){
                    editText_nome.setError(context.getResources().getString(R.string.emptyField2));   //VERIFICA SE BASTA getString(...)
                    editText_nome.requestFocus();
                    return;
                }
                if(cognome.isEmpty()){
                    editText_cognome.setError("dati da inserire");
                    editText_cognome.requestFocus();
                    return;
                }
                if(data.isEmpty()){
                    editText_data.setError("dati da inserire");
                    editText_data.requestFocus();
                    return; }
                if(email.isEmpty()){
                    editText_email.setError("dati da inserire");
                    editText_email.requestFocus();
                    return;
                }
                if(password.isEmpty()){
                    editText_password.setError("dati da inserire");
                    editText_password.requestFocus();
                    return;
                }
                if((password.length())<6) {
                    editText_password.setError("deve contenere almeno 6 caratteri");
                    editText_password.requestFocus();
                    return;
                */

                registration(email,password,nome,patente,data,tel);
                Intent intent=new Intent(this, ChooseActivity.class);
                startActivity(intent);
                break;

            case R.id.cancelBtn:
                Intent main=new Intent(this, MainActivity.class);
                startActivity(main);
                break;
        }
    }

    private void registration(final String email, final String password, final String nome, final String patente, final String dataN, final String tel) {
        if(password.length()<=6) {
            Toast.makeText(RegisterActivity.this, context.getString(R.string.passAlert),Toast.LENGTH_SHORT).show();
        }
        else mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() { //operazione asincrona
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {

                    CollectionReference user = db.collection("utenti");
                    //raccolgo tutti i dati dell'utente
                    Map<String, Object> data = new HashMap<>();
                    data.put("email", email);   //oppure mAuth.getCurrentUser().getEmail();
                    data.put("password", password);
                    data.put("nome", nome);
                    data.put("patente",patente);
                    data.put("data", dataN);
                    data.put("telefono",tel);
                    user.document(mAuth.getCurrentUser().getUid()).set(data);

                    db.collection("utenti").document(mAuth.getCurrentUser().getUid()).set(user, SetOptions.merge()) //senza SetOptions se il file esiste viene sovrascritto --> funge da merge
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("db_error", "Errore aggiunta info. utente al db"+e);
                                    Toast.makeText(RegisterActivity.this, context.getString(R.string.dbWriteErr),Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else Toast.makeText(RegisterActivity.this, context.getString(R.string.errorSignup),Toast.LENGTH_SHORT).show();
            }
        });
    }
}