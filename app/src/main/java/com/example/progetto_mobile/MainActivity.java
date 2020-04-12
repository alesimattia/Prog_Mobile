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

        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);

        mAuth=FirebaseAuth.getInstance();

        registraBtn = findViewById(R.id.registerBtn);
        loginBtn = findViewById(R.id.loginBtn);
        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);

        registraBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
    }




    public void login(String email,String password) {

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
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
                } catch (NullPointerException e) {   //nel caso di campi vuoti
                    Toast.makeText(MainActivity.this, getString(R.string.emptyField), Toast.LENGTH_LONG).show();
                }

                login(email, password);
                break;
            }
            case R.id.registerBtn: {
                Intent regIntent=new Intent(this, RegisterActivity.class);
                startActivity(regIntent);
                break;
            }
            default:    //per protezione
                Toast.makeText(MainActivity.this, "Non permesso", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
    }

}



