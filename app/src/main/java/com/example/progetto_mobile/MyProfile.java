package com.example.progetto_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyProfile extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;

    private ImageButton btnBack;
    private ImageButton btnEdit;
    private TextView textView_nome;
    private TextView textView_cognome;
    private TextView textView_patente;
    private TextView textView_email;
    private TextView textView_telefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile_layout);
        setTitle(R.string.edit);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        btnBack = findViewById(R.id.imageButton_back);
        btnEdit = findViewById(R.id.imageButton_edit);

        textView_nome = findViewById(R.id.textViev_nome);
        textView_cognome = findViewById(R.id.textView_cognome);
        textView_patente = findViewById(R.id.textView_patente);
        textView_email = findViewById(R.id.textViev_email);
        textView_telefono = findViewById(R.id.textView_telefono);

        btnBack.setOnClickListener(this);
        btnEdit.setOnClickListener(this);

        DocumentReference docRef = db.collection("utenti").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        textView_nome.setText(document.get("nome").toString());
                        textView_cognome.setText(document.get("cognome").toString());
                        textView_email.setText( document.get("email").toString());
                        textView_patente.setText(getString(R.string.licence2) +":\t\t" + document.get("patente").toString());
                        textView_telefono.setText(getString(R.string.telephone) +":\t\t" + document.get("telefono").toString());
                    }
                }
                else   Log.w("tag", "errore accesso db");
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton_back:
                Intent intent=new Intent(this, ChooseActivity.class);
                startActivity(intent);

            case R.id.imageButton_edit:
                break;
        }
    }

}
