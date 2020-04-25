package com.example.progetto_mobile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button registraBtn;
    private Button loginBtn;
    private EditText emailText;
    private EditText passwordText;

    public String email = null;
    public String password = null;

    private FirebaseAuth mAuth;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);

        mAuth=FirebaseAuth.getInstance();

        registraBtn = findViewById(R.id.registerBtn);
        loginBtn = findViewById(R.id.loginBtn);
        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);

        registraBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
    }

    public void login(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = mAuth.getCurrentUser();
                            Intent intent = new Intent(MainActivity.this, ChooseActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Log.w("login_error", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.loginBtn: {

                try {
                    email = emailText.getText().toString();
                    password = passwordText.getText().toString();
                }
                catch (NullPointerException e) {   //nel caso di campi vuoti
                    Toast.makeText(this, getString(R.string.emptyField), Toast.LENGTH_LONG).show();
                }

                if(email.isEmpty()){
                    emailText.setError(getResources().getString(R.string.err_richiesto));   //VERIFICA SE BASTA getString(...)
                    emailText.requestFocus();
                    return;
                }
                if(password.isEmpty() || password.length()<6){
                    passwordText.setError(getResources().getString(R.string.passAlert));
                    passwordText.requestFocus();
                    return;
                }

                login(email, password);
                break;
            }

            case R.id.registerBtn: {
                Intent regIntent=new Intent(this, RegisterActivity.class);
                startActivity(regIntent);
                break;
            }
            default:    //per protezione - non tradotto
                Toast.makeText(this, "Not allowed", Toast.LENGTH_LONG).show();
        }
    }




}



