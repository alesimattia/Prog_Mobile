package com.example.progetto_mobile;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MyRides extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;

    private RecyclerView recyclerView;
    private RideAdapter rideAdapter;
    private ArrayList<Ride> corse = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_rides_layout);
        setTitle(R.string.myrides);

        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        user=mAuth.getCurrentUser();

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);     //dimensioni contenuto fisse -> migliora performance
        recyclerView.setLayoutManager(new LinearLayoutManager(MyRides.this));

        rideAdapter = new RideAdapter(corse);
        recyclerView.setAdapter(rideAdapter);

        showRides(user);
    }

    private void showRides(FirebaseUser user){

        Query docRef = db.collection("viaggi").whereEqualTo("user", user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                    for (final DocumentSnapshot document : task.getResult().getDocuments()) {
                        document.getId();
                        putDocument(document);
                    }
                else   Log.w("tag", "errore accesso db");
            }
        });
    }


    private void putDocument(final DocumentSnapshot document) {

            Ride ride = new Ride(document.get("tratta").toString(),
                    getString(R.string.direction) + "\t\t" + document.get("verso").toString(),
                    getString(R.string.seats) + "\t\t" + document.get("posti").toString(),
                    document.get("ora").toString(),
                    document.get("data").toString());
            corse.add(ride);

            rideAdapter.setClickListener(new RideAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    showDialog(document.getId());
                }
            });     /*Il listener che setta il numero di telefono nell'intent, è asincrono rispetto alle letture del documento ->
                          è necessario salvare in una variabile i telefoni estratti dal db,
                          in modo da modificare il comportamento dell'intent per ogni elemento della recyclerView */
            rideAdapter.notifyDataSetChanged();     //aggiorna la recyclerView con le modifiche effettuate alla struttura
    }


    private void showDialog(final String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyRides.this);
        builder.setMessage(R.string.delete)
                .setCancelable(true)
                .setPositiveButton(R.string.elimina, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final AlertDialog confirm = new AlertDialog.Builder(MyRides.this).create();
                            confirm.setTitle(R.string.sure);
                            confirm.setMessage(getString(R.string.sure));

                            confirm.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    db.collection("viaggi").document(id).delete();
                                    confirm.cancel();
                                    recreate();
                                }
                            });
                            confirm.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.back), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    confirm.cancel();
                                }
                            });
                            confirm.show();
                        }
                })
                .setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }


    //gestione dichiarativa bottone "indietro"
    public void back(View v){
        Intent intent=new Intent(this, ChooseActivity.class);
        startActivity(intent);
    }

}
