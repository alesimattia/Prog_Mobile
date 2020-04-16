package com.example.progetto_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.atomic.AtomicBoolean;

//Creare un dialog con solo 2 bottoni
public class Dialog extends AppCompatActivity implements View.OnClickListener  {


    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseUser user=mAuth.getCurrentUser();
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private String tel=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);

        findViewById(R.id.button_inserisciviaggio).setOnClickListener(this);
        findViewById(R.id.button_eliminaviaggio).setOnClickListener(this);
}


    final AtomicBoolean check = new AtomicBoolean(false);
    public void getTelefono() {

        DocumentReference docRef = db.collection("utenti").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        tel=document.getString("telefono");
                            check.set(true);
                    }
                }
                else Log.w("tag","errore accesso db");
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_inserisciviaggio:
                Intent m = new Intent(this, InsertRide.class);
                startActivity(m);
                break;

            case R.id.button_eliminaviaggio:
                db.collection("viaggi").whereEqualTo("telefono",tel)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });

                Intent n = new Intent(this, ChooseActivity.class);
                startActivity(n);
                break;
        }
}
}