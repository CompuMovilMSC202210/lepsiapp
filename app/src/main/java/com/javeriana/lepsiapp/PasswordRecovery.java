package com.javeriana.lepsiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.javeriana.lepsiapp.ui.login.LoginActivity;


public class PasswordRecovery extends AppCompatActivity {
    EditText sendemail;
    Button btnreset;

    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);


        sendemail = findViewById(R.id.emailUser);
        btnreset = findViewById(R.id.btn_reset);

        firebaseAuth = FirebaseAuth.getInstance();


        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = sendemail.getText().toString();
                if (!isUserNameValid(email)) {
                    Toast.makeText(PasswordRecovery.this, "Por favor ingrese un email válido", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(PasswordRecovery.this, "Por favor revise su email", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(PasswordRecovery.this, LoginActivity.class));
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(PasswordRecovery.this, error, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }

            }

            // A placeholder username validation check
            private boolean isUserNameValid(String username) {
                if (username == null) {
                    return false;
                }
                if (username.contains("@")) {
                    return Patterns.EMAIL_ADDRESS.matcher(username).matches();
                } else {
                    return !username.trim().isEmpty();
                }
            }
        });
    }
}