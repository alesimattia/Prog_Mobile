package com.example.progetto_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class Drive_Pass extends AppCompatActivity implements View.OnClickListener {

    private Dialog2 customDialog;
    private Dialog_Drive customDialog2;


    private ImageButton pass;
    private ImageButton drive;
    private int j;
    String email2;
    String password2;









        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.drive_pass);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Intent i = getIntent();
            Bundle bundle = i.getBundleExtra("loginb");



            email2 = bundle.getString("emb");
            password2 = bundle.getString("psb");





            pass = findViewById(R.id.imageButton_pass);
            drive = findViewById(R.id.imageButton_drive);

            pass.setOnClickListener(this);
            drive.setOnClickListener(this);}

      private void Controllo_Patente () {
          HttpURLConnection client = null;

          URL url;
          try{

              url = new URL("http://unicar.altervista.org/Controllo_Patente1.php?email=" + this.email2);
              client = (HttpURLConnection) url.openConnection();

              client.setRequestMethod("GET");
              client.setDoInput(true);
              InputStream in = client.getInputStream();
              String json_string = ReadResponse.readStream(in).trim();
              JSONObject json_data = convert2JSON(json_string);


              if (json_string.equals("1")) {
                  Toast.makeText(this, "Accesso Autorizzato ", Toast.LENGTH_LONG).show();

                  customDialog2 = new Dialog_Drive(this, true);

                  customDialog2.show();


              } else {
                  Toast.makeText(this, "Non hai inserito la PATENTE", Toast.LENGTH_LONG).show();

                  customDialog = new Dialog2(this, true, email2);

                  customDialog.show();



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
            case R.id.imageButton_pass:
                break;

            case R.id.imageButton_drive:
                Controllo_Patente();
                break;
        }

    }}

