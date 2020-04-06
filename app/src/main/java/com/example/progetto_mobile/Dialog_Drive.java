package com.example.progetto_mobile;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class Dialog_Drive extends Dialog implements View.OnClickListener  {

    private Button inserisci;
    private Button modifica;

    private Context context2;

    private Boolean type;




    public Dialog_Drive(Context context2, Boolean type) {

        super(context2);
        this.context2 = context2;
        this.type = type;


}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_drive);

        inserisci = findViewById(R.id.button_inseriscitratta);
        modifica = findViewById(R.id.button_modificatratta);

        inserisci.setOnClickListener(this);
        modifica.setOnClickListener(this);
}

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_inseriscitratta:
                Intent i = new Intent(context2, Inserisci_Tratta.class);
                context2.startActivity(i);

                break;

            case R.id.button_modificatratta:

                break;
        }
}
}