package com.example.progetto_mobile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ShowRides extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    private LinearLayout layout;
    public LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);

    private String number = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.showrides_layout);
        layout = findViewById(R.id.linear);

        if (ActivityCompat.checkSelfPermission(ShowRides.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ShowRides.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            return;
        }



        CollectionReference docRef = db.collection("viaggi");
        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (final DocumentSnapshot document : task.getResult().getDocuments()) {
                        TextView dynamicTextView = new TextView(ShowRides.this);
                        dynamicTextView.setLayoutParams(params);
                        dynamicTextView.setText(document.get("tratta") + "   |" + document.get("verso") + "|  " +"  Posti: "+document.get("posti")+ "\n" +
                                                document.get("ora") +" -- "+document.get("data"));
                        layout.addView(dynamicTextView);


                        Button btnCall = new Button(ShowRides.this);
                        btnCall.setOnClickListener(new android.view.View.OnClickListener() {
                            String number=document.get("telefono").toString();
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null)));
                            }
                        });
                        btnCall.setText(R.string.call);
                        btnCall.setLayoutParams(params);
                        layout.addView(btnCall);
                    }
                }
                else Log.w("tag","errore accesso db");
            }
        });
    }

}
