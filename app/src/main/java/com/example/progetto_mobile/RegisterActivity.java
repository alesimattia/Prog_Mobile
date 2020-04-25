package com.example.progetto_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity{

    private String nome=null, cognome=null, tel=null;
    private String email = null, password = null, patente=null;

    private EditText editText_email;
    private EditText editText_password;
    private EditText editText_patente;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.getApplicationContext();
        super.onCreate(savedInstanceState);

        setTitle(getResources().getString(R.string.registerTitle));
        setContentView(R.layout.register_layout);


        editText_email=findViewById(R.id.editText_email);
        editText_password=findViewById(R.id.editText_password);
        editText_patente=findViewById(R.id.editText_patente);

        db = FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.placeholder, new RegisterFragment1()).commit();
    }



    public void registration(final String nome, final String cognome, final String tel, final String email, final String password, final String patente) {

         mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() { //operazione asincrona
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {

                    //CollectionReference user = db.collection("utenti");
                    //raccolgo tutti i dati dell'utente
                    Map<String, Object> data = new HashMap<>();
                    data.put("email", email);   //oppure mAuth.getCurrentUser().getEmail();
                    data.put("password", password);
                    data.put("cognome", cognome);
                    data.put("nome", nome);
                    data.put("patente", patente);
                    data.put("telefono", tel);

                    db.collection("utenti").document(mAuth.getCurrentUser().getUid())
                            .set(data, SetOptions.merge()) //senza SetOptions se il file esiste viene sovrascritto
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("db_error", "Errore aggiunta info. utente al db"+e);
                                    Toast.makeText(RegisterActivity.this, getString(R.string.dbWriteErr),Toast.LENGTH_LONG).show();
                                    finish();   //riavvia l'activity
                                    startActivity(getIntent());
                                }
                            })
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent intent = new Intent(RegisterActivity.this, ChooseActivity.class);
                                    startActivity(intent);
                                }
                            });
                }
                else Toast.makeText(RegisterActivity.this, getString(R.string.errorSignup),Toast.LENGTH_SHORT).show();
            }
        });
    }

}