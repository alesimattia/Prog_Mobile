package com.example.progetto_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.atomic.AtomicBoolean;


public class ChooseActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton hitch;
    private ImageButton driver;
    private ImageButton myRides;
    private ImageButton myProfile;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;

    private Boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_layout);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        setTitle("Ciao  \t" + user.getEmail());

        hitch = findViewById(R.id.imageButton_hitch);
        driver = findViewById(R.id.imageButton_driver);
        myRides = findViewById(R.id.imageButton_my_rides);
        myProfile = findViewById(R.id.imageButton_my_profile);

        hitch.setOnClickListener(this);
        driver.setOnClickListener(this);
        myRides.setOnClickListener(this);
        myProfile.setOnClickListener(this);
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
                        String ris = document.getString("patente");
                        if (!ris.equals("") && ris != null)
                            flag=true;  //c'è una patente registrata
                        check.set(true);  //ha completato la lettura
                    }
                }
                else Log.w("tag", "errore accesso db");
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
                            if (flag) {
                                Intent m = new Intent(ChooseActivity.this, InsertRide.class);
                                startActivity(m);
                            }
                            else {
                                Toast.makeText(ChooseActivity.this, "Non hai una patente registrata!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ChooseActivity.this, LicenceActivity.class);
                                startActivity(intent);
                            }
                        }
                    }
                 }, 1500);
                break;

            case R.id.imageButton_my_rides:
                Intent myRidesIntent=new Intent(this, MyRides.class);
                startActivity(myRidesIntent);
                break;

            case R.id.imageButton_my_profile:
                Intent myProfileIntent=new Intent(this, ProfileActivity.class);
                startActivity(myProfileIntent);
                break;
        }
    }


}