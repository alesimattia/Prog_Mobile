package com.example.progetto_mobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

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
        setContentView(R.layout.profile_layout);
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
                break;

            case R.id.imageButton_edit:
                showDialog();
                break;
        }
    }


    private void showDialog() {
        View inflated = LayoutInflater.from(ProfileActivity.this).inflate(R.layout.profile_fields,
                                            (ViewGroup) findViewById(android.R.id.content).getRootView(),false);

        final EditText inputEmail  = inflated.findViewById(R.id.email);
        final EditText inputPassword  = inflated.findViewById(R.id.password);
        final EditText inputTel  = inflated.findViewById(R.id.tel);

        final AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);

        builder.setView(inflated);
        builder.setMessage(R.string.edit)
                .setCancelable(true)
                .setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();    //toglie dalla memoria il precendente dialog
                            final AlertDialog confirm = new AlertDialog.Builder(ProfileActivity.this).create();
                            confirm.setTitle(getString(R.string.save));
                            confirm.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String pass=inputPassword.getText().toString();
                                    if(pass != null && !pass.isEmpty() && pass.length() < 6) {
                                        confirm.cancel();
                                        showDialog();
                                        Toast.makeText(ProfileActivity.this, getString(R.string.passAlert), Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        updateInfo(inputEmail.getText().toString(), pass, inputTel.getText().toString());
                                        recreate();
                                        Toast.makeText(ProfileActivity.this, getString(R.string.updated), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            confirm.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.back), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    confirm.cancel();
                                    showDialog();
                                }
                            });
                            confirm.show();
                    }
                })
                .setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {    dialog.cancel();    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }


    private void updateInfo(String email, String password, String tel) {
        if (email != null && !email.isEmpty())
            user.updateEmail(email)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {   failMsg();  }
                });
        //se l'utente non ha inserito il campo non lo aggiorna

        if (password.length() >= 6) {
            user.updatePassword(password)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {   failMsg();  }
                    });
            db.collection("utenti").document(user.getUid())
                    .update("password", password);
        }

        if (tel != null && !tel.isEmpty())
            db.collection("utenti").document(user.getUid())
                .update("telefono", tel)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {   failMsg();  }
                });
    }

    public void failMsg(){
        Toast.makeText(ProfileActivity.this, getString(R.string.dbWriteErr), Toast.LENGTH_LONG).show();
        Log.w("error", "Errore scrittura db");
        return;
    }

}
