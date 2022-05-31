package com.javeriana.lepsiapp.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.javeriana.lepsiapp.GlobalVar;
import com.javeriana.lepsiapp.entidades.ContactUsuario;
import com.javeriana.lepsiapp.R;

public class RegistroContact extends AppCompatActivity {

    private EditText txtContactoNombre,txtContactoId,txtContactoEmail,txtContactoPhone;
    private Button btnRegistrar;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    DatabaseReference databaseReference;
    DatabaseReference mDatabase;

    private String Name_fire ;
    private String email_fire;
    private String phone_fire;
    private String rol_fire;

    private ContactUsuario usuarioPac = new ContactUsuario();



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
                String userName = txtContactoNombre.getText().toString();
                String id = txtContactoId.getText().toString();
                String email = txtContactoEmail.getText().toString();
                String userPhone = txtContactoPhone.getText().toString();
                String password ="Lepsiapp2022";
                String rol ="contacto";
                String pacid="";


                if (isValidEmail(email)&& validarId(id) && validarName(userName)&& validartelefono(userPhone)) {
                     mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegistroContact.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        // firebease llama a  roll
                                        mDatabase= FirebaseDatabase.getInstance().getReference();
                                        mDatabase.child("Usuarios").child(GlobalVar.UidMain).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()){

                                                    Name_fire = snapshot.child("userName").getValue().toString();
                                                    email_fire = snapshot.child("email").getValue().toString();
                                                    phone_fire = snapshot.child("userPhone").getValue().toString();
                                                    rol_fire = snapshot.child("rol").getValue().toString();


                                                    usuarioPac.setEmail(email_fire);
                                                    usuarioPac.setUserName(Name_fire);
                                                    usuarioPac.setUserPhone(phone_fire);
                                                    usuarioPac.setRol(rol_fire);
                                                    usuarioPac.setPacId(GlobalVar.UidMain);

                                                    database.getReference("Contactos/"+mAuth.getCurrentUser().getUid()+"/"+GlobalVar.UidMain).setValue(usuarioPac);

                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });




                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(RegistroContact.this,"Se registro correctamente", Toast.LENGTH_SHORT).show();
                                        ContactUsuario usuario = new ContactUsuario();
                                        usuario.setEmail(email);
                                        usuario.setUserName(userName);
                                        usuario.setUserPhone(userPhone);
                                        usuario.setRol(rol);
                                        usuario.setPacId(GlobalVar.UidMain);
                                        usuario.setId(id);
                                        FirebaseUser currentUser = mAuth.getCurrentUser();
                                        DatabaseReference reference = database.getReference("Usuarios/"+currentUser.getUid());
                                        reference.setValue(usuario);
                                        database.getReference("Contactos/"+GlobalVar.UidMain+"/"+mAuth.getCurrentUser().getUid()).setValue(usuario);
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


