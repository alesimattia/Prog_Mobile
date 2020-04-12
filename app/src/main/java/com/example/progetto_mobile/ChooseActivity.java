package com.example.progetto_mobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ChooseActivity extends AppCompatActivity implements View.OnClickListener {

    private LicenceActivity customDialog;
    private Dialog_Drive customDialog2;

    private ImageButton hitch;
    private ImageButton driver;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private Object label;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_layout);
        setTitle("Ciao "+user.getDisplayName());

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db=FirebaseFirestore.getInstance();

        hitch = findViewById(R.id.imageButton_hitch);
        driver = findViewById(R.id.imageButton_driver);

        hitch.setOnClickListener(this);
        driver.setOnClickListener(this);
    }

    private boolean check_patente() {

        DocumentReference document = db.collection("utenti").document(user.getUid());
        DocumentSnapshot doc;
        document.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {


            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    doc = task.getResult();
                }
                else Log.d("error", "task non eseguito");
            }
            if (doc.get("patente").toString() !=null)
                    Log.d("user_error", "Documento non trovato");
            return false;
        });
        return true;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.imageButton_hitch:
                break;

            case R.id.imageButton_driver:
                if(check_patente()){
                    Toast.makeText(this, "Inizia a guidare!", Toast.LENGTH_LONG).show();
                    Intent driveIntent=new Intent(this, Dialog_Drive.class);
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

