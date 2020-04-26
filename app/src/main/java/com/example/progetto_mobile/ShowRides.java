package com.example.progetto_mobile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ShowRides extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private RecyclerView recyclerView;
    private RideAdapter rideAdapter;
    private ArrayList<Ride> corse = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.showrides_layout);
        setTitle(R.string.ridesTitle);


        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);     //dimensioni contenuto fisse -> migliora performance
        recyclerView.setLayoutManager(new LinearLayoutManager(ShowRides.this));

        rideAdapter = new RideAdapter(corse);
        recyclerView.setAdapter(rideAdapter);



        CollectionReference docRef = db.collection("viaggi");
        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (final DocumentSnapshot document : task.getResult().getDocuments()) {
                        Ride ride=new Ride(document.get("tratta").toString(), document.get("verso").toString(),
                                            document.get("posti").toString(), document.get("ora").toString(), document.get("data").toString());
                        corse.add(ride);
                        rideAdapter.notifyDataSetChanged();      //aggiorna la recyclerView con le modifiche effettuate alla struttura

                    }
                }
                else Log.w("tag", "errore accesso db");
            }
        });
    }


}
