package com.example.progetto_mobile;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RegisterActivity extends Dialog implements View.OnClickListener {

    private EditText editText_nome;
    private EditText editText_cognome;
    private EditText editText_data;
    private EditText editText_email;
    private EditText editText_password;
    private EditText editText_matricola;
    private EditText editText_patente;
    private EditText editText_numero;

    private Button cancelBtn;
    private Button registerBtn;

    public String email = null;
    public String password = null;
    public String nome = null;
    public String cognome = null;
    public String matricola = null;
    public String patente = null;
    public Date data = null;
    public String numero = null;

    private FirebaseAuth mAuth;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super(context);
        this.context = context;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        setTitle(context.getResources().getString(R.string.registerTitle));   //OPPURE this.getContext().

        editText_email = findViewById(R.id.editText_email);
        editText_password = findViewById(R.id.editText_password);
        editText_nome = findViewById(R.id.editText_nome);
        editText_cognome = findViewById(R.id.editText_cognome);
        editText_patente = findViewById(R.id.editText_patente);
        editText_matricola = findViewById(R.id.editText_matricola);
        editText_data = findViewById(R.id.editText_data);
        editText_numero = findViewById(R.id.editText_num);

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
                matricola = editText_matricola.getText().toString();
                patente = editText_patente.getText().toString();
                data = editText_data.getText().toString();      //VERIFICA con formato DATE
                //numero = editText_numero.getText().toString();

                List<String> data = new ArrayList<String>();
                data.add(email);
                data.add(password);
                data.add(nome);
                data.add(cognome);
                data.add(matricola);
                data.add(data);
                //data.add(numero);

                for(final String current: data)
                    if(current.isEmpty()){
                        toString(current).setError(context.getResources().getString(R.string.emptyField2));)
                        toString(current).requestFocus();
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
                }
                if(matricola.isEmpty()){
                    editText_matricola.setError("dati da inserire");
                    editText_matricola.requestFocus();
                    return;
                }

                if(numero.isEmpty()){
                    editText_numero.setError("dati da inserire");
                    editText_numero.requestFocus();
                    return;
                }
                if((numero.length() != 10)){
                    editText_numero.setError("Deve essere di 10 numero");
                    editText_numero.requestFocus();
                    return;
                }
                */
                registration();
                dismiss();
                break;

            case R.id.cancelBtn:
                dismiss();
                Intent intent=new Intent(this, MainActivity.class);
                break;
        }
    }

    private void registration(String email, String password, final String nome, String matricola, String patente, Date data) {
        if(password.length()<=6) {
            Toast.makeText(RegisterActivity.this, context.getString(R.string.passAlert),Toast.LENGTH_SHORT).show();
        }
        else mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() { //operazione asincrona
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    FirebaseUser user=mAuth.getCurrentUser();
                    //AGGIUNGERE DATI UTENTE    esempio: collection Utente(email, password, nome, cognome...)
                }
                else Toast.makeText(RegisterActivity.this, context.getString(R.string.errorSignup),Toast.LENGTH_SHORT).show();
            }
        }
    }
}