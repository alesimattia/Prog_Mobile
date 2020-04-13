package com.example.progetto_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

//Creare un dialog con solo 2 bottoni
public class Dialog extends AppCompatActivity implements View.OnClickListener  {

    private Button inserisci;
    private Button elimina;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);

        inserisci = findViewById(R.id.button_inseriscitratta);
        elimina= findViewById(R.id.button_eliminatratta);

        inserisci.setOnClickListener(this);
        elimina.setOnClickListener(this);
}

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_inseriscitratta:
                Intent i = new Intent(this, Inserisci_Tratta.class);
                startActivity(i);

                break;

            case R.id.button_eliminatratta:
                //Intent m = new Intent(this, Elimina_Tratta.class);
                //startActivity(m);
                break;
        }
}
}