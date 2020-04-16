package com.example.progetto_mobile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class ShowRides extends AppCompatActivity {

    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseUser user=mAuth.getCurrentUser();
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private LinearLayout layout;
    public LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
    public Task<QuerySnapshot> ris;
    public ArrayList arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showrides_layout);
        layout = findViewById(R.id.linear);

        getData();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (check.get()) {
                    for (DocumentSnapshot document : ris.getResult()) {

                        TextView dynamicTextView = new TextView(ShowRides.this);
                        dynamicTextView.setLayoutParams(params);
                        dynamicTextView.setText(document.get("tratta")+"  "+ document.get("ora")+"  "+document.get("verso")+"  "+document.get("posti"));
                        layout.addView(dynamicTextView);
                    }
                }
            }
        },1000);
    }



    final AtomicBoolean check = new AtomicBoolean(false);
    public void getData() {

        CollectionReference docRef = db.collection("utenti");
        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ris=task;
                    check.set(true);
                }
                else Log.w("tag","errore accesso db");
            }
        });
    }
}
