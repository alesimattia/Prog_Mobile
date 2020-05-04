package com.example.progetto_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import fragment.InsertFragment1;


public class InsertRide extends AppCompatActivity {

    private FirebaseUser user;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private String phone=null;


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_layout);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db=FirebaseFirestore.getInstance();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.placeholder, new InsertFragment1()).commit();
    }


    final AtomicBoolean check = new AtomicBoolean(false);
    public void getNumber() {
        DocumentReference docRef = db.collection("utenti").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        phone=document.getString("telefono");
                        check.set(true);
                    }
                }
                else Log.w("tag","errore accesso db");
            }
        });
    }


    public void sumbit(final String tratta, final String verso, final String posti, final String ora, final String data){
        getNumber();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (check.get()) {  //attende la lettura dal db del telefono
                    Map<String, Object> pack = new HashMap<>();
                    pack.put("tratta", tratta);
                    pack.put("ora", ora);
                    pack.put("data", data);
                    pack.put("verso", verso);
                    pack.put("posti", posti);
                    pack.put("telefono", phone);
                    pack.put("user", user.getUid());

                    db.collection("viaggi").document(/*radndom UID*/)   //un documento per viaggio
                            .set(pack)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("db_error", "Errore aggiunta viaggio al db " + e);
                                    Toast.makeText(InsertRide.this, getString(R.string.dbWriteErr), Toast.LENGTH_LONG).show();
                                    getParentActivityIntent();
                                }
                            });

                    Toast.makeText(InsertRide.this, getString(R.string.added), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(InsertRide.this, ChooseActivity.class);
                    startActivity(intent);
                }
            }
        }, 1000);
    }


}

