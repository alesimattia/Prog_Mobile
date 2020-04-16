package com.example.progetto_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

//trasformare in dialog
public class LicenceActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editText_codice;
    private Button okBtn;
    private Button cancBtn;
    private String codice = null;

    private FirebaseUser user;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.licence_layout);


        editText_codice = findViewById(R.id.editText_codice);
        cancBtn = findViewById(R.id.button_canc);
        okBtn = findViewById(R.id.button_ok);

        cancBtn.setOnClickListener(this);
        okBtn.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db=FirebaseFirestore.getInstance();
    }


    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.button_ok:
                codice = editText_codice.getText().toString();
                if(!codice.equals(""))    updateInfo(codice);
                break;

            case R.id.button_canc:  //torna alla selezione delle attività
                Intent intent=new Intent(this, ChooseActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void updateInfo(String codice){

        DocumentReference document = db.collection("utenti").document(user.getUid());
        document.update("patente", codice)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent=new Intent(LicenceActivity.this, Dialog.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("write_error", "Db write error -> patente non aggiunta", e);
                        Toast.makeText(LicenceActivity.this, "Errore nella scrittura della patente", Toast.LENGTH_LONG).show();
                    }
                });
    }


/*
    public class FireMissilesDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.dialog_fire_missiles)
                    .setPositiveButton(R.string.fire, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
*/

}
