package com.example.progetto_mobile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;

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

public class ShowRides extends AppCompatActivity  {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private RecyclerView recyclerView;
    private RideAdapter rideAdapter;
    private ArrayList<Ride> corse = new ArrayList<>();
    private ArrayList<String> tel = new ArrayList<>();


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
                if (task.isSuccessful())
                    for (final DocumentSnapshot document : task.getResult().getDocuments()) {
                        tel.add(document.get("telefono").toString());
                        putDocument(document);
                    }
                else   Log.w("tag", "errore accesso db");
            }
        });
    }



    public void putDocument(DocumentSnapshot document) {

        if (! isExpired(document.get("data").toString())) {
            Ride ride = new Ride(document.get("tratta").toString(),
                    getString(R.string.direction) + "\t\t" + document.get("verso").toString(),
                    getString(R.string.seats) + "\t\t" + document.get("posti").toString(),
                    document.get("ora").toString(),
                    document.get("data").toString());
            corse.add(ride);

            rideAdapter.setClickListener(new RideAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + tel.get(position)));
                    startActivity(intent);
                }
            });     /*Il listener che setta il numero di telefono nell'intent, è asincrono rispetto alle letture del documento ->
                          è necessario salvare in una variabile i telefoni estratti dal db,
                          in modo da modificare il comportamento dell'intent per ogni elemento della recyclerView */
            rideAdapter.notifyDataSetChanged();     //aggiorna la recyclerView con le modifiche effettuate alla struttura
        }
    }


    public boolean isExpired(String dataDocumento){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");   //mm minuscolo corrisponde a "minute"
        Date today = Calendar.getInstance().getTime();
        try {
            Date data = format.parse(dataDocumento);      //obbligato in un try-catch
            if (data.after(today)) return false;  //cioè not expired
            return true;
        }
        catch (ParseException e) {
            e.printStackTrace();
            Log.w("tag", "errore lettura data");
            return true;
        }
    }

    //gestione dichiarativa bottone "indietro"
    public void back(View v){
        Intent intent=new Intent(this, ChooseActivity.class);
        startActivity(intent);
    }

}
