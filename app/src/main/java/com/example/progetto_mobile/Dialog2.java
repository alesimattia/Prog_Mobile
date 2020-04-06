package com.example.progetto_mobile;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Dialog2  extends Dialog implements View.OnClickListener {

    private Context context;
    private Boolean typeRequest;
    private EditText editText_cod;

    private String email3;
    private Button ok;
    private Button canc;
    public String cod = null;






    public Dialog2(Context context, Boolean type, String email2) {
        super(context);
        this.context = context;
        this.typeRequest = type;
        this.email3 = email2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog2_layout);



        setTitle("Aggiungi codice patente & numero di telegono");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);



         editText_cod = findViewById(R.id.editText_cod);

        canc = findViewById(R.id.button_canc);
        ok = findViewById(R.id.button_ok);
        canc.setOnClickListener(this);
        ok.setOnClickListener(this);
    }

    private JSONObject convert2JSON(String json_data){
        JSONObject obj = null;
        try {
            obj = new JSONObject(json_data);
            Log.d("My App", obj.toString());
        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + json_data + "\"");
        }
        return obj;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.button_ok:

                cod = editText_cod.getText().toString();

                make_request1();
                dismiss();
                break;

            case R.id.button_canc:
                dismiss();
                break;
        }

    }

    private void make_request1(){
        HttpURLConnection client = null;
        URL url;
        try {
            // se la richiesta è GET
            if (this.typeRequest) {
                url = new URL("http://unicar.altervista.org/Replace_Into.php?codicepatente=" + this.cod + "&email=" +email3);


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
