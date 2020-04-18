package com.example.progetto_mobile;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.atomic.AtomicBoolean;


public class ChooseActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton hitch;
    private ImageButton driver;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;

    private String ris = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_layout);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        setTitle("Ciao  " + user.getEmail());

        hitch = findViewById(R.id.imageButton_hitch);
        driver = findViewById(R.id.imageButton_driver);

        hitch.setOnClickListener(this);
        driver.setOnClickListener(this);
    }


    final AtomicBoolean check = new AtomicBoolean(false);

    public void checkPatente() {

        DocumentReference docRef = db.collection("utenti").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ris = document.getString("patente");
                        if (!ris.equals("") && ris != null)
                            check.set(true);
                    }
                } else Log.w("tag", "errore accesso db");
            }
        });

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.imageButton_hitch:
                Intent showIntent = new Intent(this, ShowRides.class);
                startActivity(showIntent);
                break;

            case R.id.imageButton_driver:
                checkPatente();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (check.get()) {  //se ha completato il recupero della patente
                            showDialog();
                        } else {
                            Toast.makeText(ChooseActivity.this, "Non hai una patente registrata!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ChooseActivity.this, LicenceActivity.class);
                            startActivity(intent);
                        }
                    }
                }, 1000);
                break;
        }
    }



    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChooseActivity.this);
        builder.setMessage(R.string.choose2)
                .setCancelable(false)
                .setPositiveButton(R.string.inserisci, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent m = new Intent(ChooseActivity.this, InsertRide.class);
                        startActivity(m);
                    }
                })
                .setNegativeButton(R.string.elimina, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AlertDialog confirm = new AlertDialog.Builder(ChooseActivity.this).create();
                            confirm.setTitle("Alert");
                            confirm.setMessage(getString(R.string.sure));
                            confirm.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    delete();
                                    recreate();
                                }
                            });
                            confirm.show();
                        }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private void delete(){
        CollectionReference docRef = db.collection("viaggi");
        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot current : task.getResult().getDocuments()) {
                        if(current.getString("user").equals(user.getUid())) {
                            db.collection("viaggi").document(current.getId()).delete();
                        }
                    }
                }
            }
        });
    }


}