package com.example.progetto_mobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import androidx.appcompat.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button registrazione;
    private Button logIn;
    private CustomDIalog customDialog;
    private Context context1;
    private EditText editText_email1;
    private EditText editText_password1;
    public String email = null;
    public String password = null;
    private int j;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        registrazione = findViewById(R.id.Registrati);
        logIn = findViewById(R.id.login);
        editText_email1 = findViewById(R.id.editText_email1);
        editText_password1 = findViewById(R.id.editText_password1);


        registrazione.setOnClickListener(this);
        logIn.setOnClickListener(this);
    }





private void Log_In(){
    HttpURLConnection client = null;

    URL url;
    try{

        url = new URL("http://unicar.altervista.org/login_andrea.php?email=" + this.email + "&password=" +this.password);
                client = (HttpURLConnection) url.openConnection();

        client.setRequestMethod("GET");
        client.setDoInput(true);
        InputStream in = client.getInputStream();
        String json_string = ReadResponse.readStream(in).trim();
        JSONObject json_data = convert2JSON(json_string);


        if (json_string.equals("1")) {
            Toast.makeText(this, "LogIn effettuato", Toast.LENGTH_LONG).show();
            j=1;
            if(j==1){
                Intent i = new Intent(this, Drive_Pass.class );

                Bundle bundle = new Bundle();
                bundle.putString("emb", email);
                bundle.putString("psb", password);
                i.putExtra("loginb", bundle);

                startActivity(i);}
        } else {
            Toast.makeText(this, "Errore nell'inserimento", Toast.LENGTH_LONG).show();
            j=0;
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    finally{
        if (client!= null){
            client.disconnect();
        }

    }
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
            case R.id.Registrati:
                customDialog = new CustomDIalog(this, true);
                customDialog.show();
                break;

                case R.id.login:


                    email = editText_email1.getText().toString();
                    password = editText_password1.getText().toString();
                    Log_In();
                    break;
        }


    }

}



