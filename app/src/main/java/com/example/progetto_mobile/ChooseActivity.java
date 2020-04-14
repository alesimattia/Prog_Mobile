package com.example.progetto_mobile;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class ChooseActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton hitch;
    private ImageButton driver;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_layout);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db=FirebaseFirestore.getInstance();
        setTitle("Ciao "+user.getEmail());

        hitch = findViewById(R.id.imageButton_hitch);
        driver = findViewById(R.id.imageButton_driver);

        hitch.setOnClickListener(this);
        driver.setOnClickListener(this);
    }

    public boolean check_patente() {

        DocumentReference docRef = db.collection("utenti").document(user.getUid());
        /*docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (!document.exists())   Log.w("utente non trovato");
                }
                else Log.w("errore accesso db");
            }
        });*/

        if(! docRef.get().getResult().get("patente").toString().isEmpty())    //docRef.get().getResult().getString("patente")
            return true;
        return false;
    }


    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.imageButton_hitch:
                break;

            case R.id.imageButton_driver:
                if(check_patente()){
                    Toast.makeText(this, "Inizia a guidare!", Toast.LENGTH_LONG).show();
                    Intent driveIntent=new Intent(this, Dialog.class);
                    startActivity(driveIntent);
                }
                else {
                    Intent intent = new Intent(this, LicenceActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

}

