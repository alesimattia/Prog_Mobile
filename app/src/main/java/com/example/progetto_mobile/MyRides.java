package com.example.progetto_mobile;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MyRides extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;

    private Button btnDeleteAll;
    private ImageButton btnBack;

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

        btnDeleteAll = findViewById(R.id.button_delete_all);
        btnBack = findViewById(R.id.imageButton_back);
        btnDeleteAll.setOnClickListener(this);
        btnBack.setOnClickListener(this);

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
                        showDocuments(document);
                    }
                else   Log.w("tag", "errore accesso db");
            }
        });
    }


    private void showDocuments(final DocumentSnapshot document) {
            Ride    ride = new     Ride(document.get("tratta").toString(),
                getString(R.string.direction) + "\t\t" + document.get("verso"),
                    getString(R.string.seats) + "\t\t" + document.get("posti"),
                                        document.get("ora").toString(),
                                        document.get("data").toString());
            corse.add(ride);

            //aggiunge un comportamento ad ogni "box" della RecyclerView
            rideAdapter.setClickListener(new RideAdapter.OnItemClickListener() {
                @Override                                               //Il listener che setta il numero di telefono nell'intent, è asincrono rispetto alle letture del documento
                public void onItemClick(View view, int position) {      //è necessario salvare in una variabile i telefoni estratti dal db,
                                                                        //in modo da modificare il comportamento dell'intent per ogni elemento della recyclerView
                    final AlertDialog confirm = new AlertDialog.Builder(MyRides.this).create();
                    confirm.setTitle(R.string.sure);
                    confirm.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            db.collection("viaggi").document(document.getId()).delete();
                            Intent intent=new Intent(MyRides.this, ChooseActivity.class);
                            startActivity(intent);
                            Toast.makeText(MyRides.this, R.string.all_deleted, Toast.LENGTH_SHORT).show();
                        }
                    });
                    confirm.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.back), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            confirm.cancel();
                        }
                    });
                    confirm.show();
                }
            });
            rideAdapter.notifyDataSetChanged();     //aggiorna la recyclerView con le modifiche effettuate alla struttura
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.imageButton_back:
                Intent intent = new Intent(this, ChooseActivity.class);
                startActivity(intent);
                break;

            case R.id.button_delete_all:
                showDialog();
                break;
        }
    }


    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyRides.this);
        builder.setMessage(R.string.sure_detail)
                .setCancelable(true)
                .setPositiveButton(R.string.elimina, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final AlertDialog confirm = new AlertDialog.Builder(MyRides.this).create();
                            confirm.setTitle(R.string.sure);
                            confirm.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteAll();
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


    private void deleteAll(){
        CollectionReference docRef = db.collection("viaggi");
        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                    for (DocumentSnapshot current : task.getResult().getDocuments())
                        if(current.getString("user").equals(user.getUid()))
                            db.collection("viaggi").document(current.getId()).delete();
            }
        });
    }
}
