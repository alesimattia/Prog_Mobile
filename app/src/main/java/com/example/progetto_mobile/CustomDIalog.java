package com.example.progetto_mobile;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CustomDIalog extends Dialog implements View.OnClickListener {
    private EditText editText_email;
    private EditText editText_password;
    private EditText editText_nome;
    private EditText editText_cognome;
    private EditText editText_matricola;
    private EditText editText_patente;
    private EditText editText_data;
    private EditText editText_numero;

    private Button cancelButton;
    private Button inserisciButton;

    public String email = null;
    public String password = null;
    public String nome = null;
    public String cognome = null;
    public String matricola = null;
    public String patente = null;
    public String data = null;
    public String numero = null;
    private Context context;
    private Boolean typeRequest;

    public CustomDIalog(Context context, Boolean type) {
        super(context);
        this.context = context;
        this.typeRequest = type;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout);
        setTitle("Aggiungi utente");

        editText_email = findViewById(R.id.editText_email);
        editText_password = findViewById(R.id.editText_password);
        editText_nome = findViewById(R.id.editText_nome);
        editText_cognome = findViewById(R.id.editText_cognome);
        editText_patente = findViewById(R.id.editText_patente);
        editText_matricola = findViewById(R.id.editText_matricola);
        editText_data = findViewById(R.id.editText_data);
        editText_numero = findViewById(R.id.editText_num);

        cancelButton = findViewById(R.id.dialog_cancella);
        inserisciButton = findViewById(R.id.dialog_inserisci);
        cancelButton.setOnClickListener(this);
        inserisciButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.dialog_inserisci:
                email = editText_email.getText().toString();
                password = editText_password.getText().toString();
                nome = editText_nome.getText().toString();
                cognome = editText_cognome.getText().toString();
                matricola = editText_matricola.getText().toString();
                patente = editText_patente.getText().toString();
                data = editText_data.getText().toString();
                numero = editText_numero.getText().toString();


               if(nome.isEmpty()){
                    editText_nome.setError("dati da inserire");
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

                make_request();
                dismiss();
                break;

            case R.id.dialog_cancella:
                dismiss();
                break;
        }
    }

    private void make_request(){
        HttpURLConnection client = null;
        URL url;
        try {
            // se la richiesta è GET
            if (this.typeRequest) {
                url = new URL("http://unicar.altervista.org/aggiungi_utente.php?nome=" + this.nome + "&cognome=" + this.cognome + "&datadinascita=" + this.data + "&email=" + this.email
                        + "&password=" + this.password +  "&matricola=" +this.matricola
                + "&codicepatente=" + this.patente + "&numerotelefono=" + this.numero);
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("GET");
                client.setDoInput(true);}

            InputStream in = client.getInputStream();
            String json_string = ReadResponse.readStream(in).trim();


            if (json_string.equals("1")) {
                Toast.makeText(this.context, "Inserimento effettuato", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this.context, "Errore nell'inserimento", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.disconnect();
            }
        }
    }
}