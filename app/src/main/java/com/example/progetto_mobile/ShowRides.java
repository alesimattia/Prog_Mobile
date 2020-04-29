package com.example.progetto_mobile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ShowRides extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private RecyclerView recyclerView;
    private RideAdapter rideAdapter;
    private ArrayList<Ride> corse = new ArrayList<>();
    private View element;


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
                    for (final DocumentSnapshot document : task.getResult().getDocuments())
                        putDocument(document);
                }
                else Log.w("tag", "errore accesso db");
            }
        });
    }



    private void putDocument(final DocumentSnapshot document){

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");   //mm minuscolo corrisponde a "minute"
        Date current = Calendar.getInstance().getTime();
        try {
            Date data = format.parse(document.get("data").toString());
            if (data.after(current)) {
                Ride ride = new Ride(document.get("tratta").toString(),
                        getString(R.string.direction) + "\t\t" + document.get("verso").toString(),
                        getString(R.string.seats) + "\t\t" + document.get("posti").toString(),
                        document.get("ora").toString(),
                        document.get("data").toString());
                corse.add(ride);
                rideAdapter.notifyDataSetChanged();    //aggiorna la recyclerView con le modifiche effettuate alla struttura

                /**NON FUNZIONA**/
                View inflatedView = getLayoutInflater().inflate(R.layout.ride, null);
                Button obj = inflatedView.findViewById(R.id.contact);
                obj.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + document.get("telefono").toString()));
                        startActivity(intent);
                    }
                });
            }
        }
        catch (ParseException e) {
            e.printStackTrace();
            Log.w("tag", "errore lettura data");
        }
    }
}
