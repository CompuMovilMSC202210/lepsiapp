package com.javeriana.lepsiapp.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.javeriana.lepsiapp.Entidades.ContactUsuario;
import com.javeriana.lepsiapp.R;

public class RegistroContact extends AppCompatActivity {

    private EditText txtContactoNombre,txtContactoId,txtContactoEmail,txtContactoPhone;
    private Button btnRegistrar;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_contact);
        txtContactoNombre =(EditText) findViewById(R.id.txtContactName);
        txtContactoId =(EditText) findViewById(R.id.txtContactID);
        txtContactoEmail =(EditText) findViewById(R.id.txtContactEmail);
        txtContactoPhone =(EditText) findViewById(R.id.txtContactPhone);
        btnRegistrar = (Button) findViewById(R.id.btnContacRegister);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();



        //Registro de los usuarios.

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = txtContactoNombre.getText().toString();
                String id = txtContactoId.getText().toString();
                String correo = txtContactoEmail.getText().toString();
                String telefono = txtContactoPhone.getText().toString();
                String Contraseña ="Lepsiapp2022";
                String Tipo ="Contacto";

                if (isValidEmail(correo)&& validarId(id) && validarName(nombre)&& validartelefono(telefono)) {
                     mAuth.createUserWithEmailAndPassword(correo, Contraseña)
                            .addOnCompleteListener(RegistroContact.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(RegistroContact.this,"Se registro correctamente", Toast.LENGTH_SHORT).show();
                                        ContactUsuario usuario = new ContactUsuario();
                                        usuario.setCorreo(correo);
                                        usuario.setNombre(nombre);
                                        usuario.setTipo(Tipo);
                                        FirebaseUser currentUser = mAuth.getCurrentUser();
                                        DatabaseReference reference = database.getReference("Usuarios/"+currentUser.getUid());
                                        reference.setValue(usuario);
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(RegistroContact.this,"Error al registrarse", Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
                }else {
                    Toast.makeText(RegistroContact.this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }


    public boolean validarName(String nombre){
        return !nombre.isEmpty();
    }
    public boolean validarId(String id){ return !id.isEmpty();}
    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    public boolean validartelefono(String nombre){
        return !nombre.isEmpty();
    }
}

