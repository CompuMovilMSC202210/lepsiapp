package com.javeriana.lepsiapp;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import com.javeriana.lepsiapp.ui.PreferManag;
import com.javeriana.lepsiapp.ui.login.LoginActivity;


import Utils.Constants;

public class Register extends AppCompatActivity {

    ImageView SelectedImage;
    Button btnCamara, btnGalery, btnRegister;
    Uri uriCamera;
    EditText email, pass, username, userphone, usereps, userid, usersurname;

    public static String TAG = "LepsiApp";
    View login_tab_fragment;
    int btnUserid;

    String userEmail, userPwd, userName, userPhone, userSurname, userId, userEps, userTypeId;


    //FirebaseAuth
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    DatabaseReference reference;
    PreferManag preferenceManager;
    FirebaseUser firebaseUser;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreference;




    ActivityResultLauncher<String> getContentGallery = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            result -> loadImage(result,0)
    );

    ActivityResultLauncher<Uri> mGetContentCamera = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    //Load image on a view
                    loadImage(uriCamera, 0);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        btnCamara = findViewById(R.id.BtnCamara);
        btnRegister = findViewById(R.id.btnRegister);

        SelectedImage = findViewById(R.id.imageView);
        File file = new File(getFilesDir(), "picFromCamera");
        uriCamera = FileProvider.getUriForFile(this, 	getApplicationContext().getPackageName() + ".fileprovider", file);

        btnCamara.setOnClickListener(view -> mGetContentCamera.launch(uriCamera));
       // btnGalery.setOnClickListener(view -> getContentGallery.launch("image/*"));



    }

    private void loadImage(Uri uri,int orient){
        final InputStream imageStream;
        try {
            imageStream = getContentResolver().openInputStream(uri);
            final Bitmap Image = BitmapFactory.decodeStream(imageStream);
            SelectedImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
            SelectedImage.setRotation(orient);
            SelectedImage.setImageBitmap(Image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void registrarPressed(View v){
        email=(EditText) findViewById(R.id.txtEmail);
        pass=(EditText) findViewById(R.id.txtPwd);
        username=(EditText) findViewById(R.id.txtName);
        userid=(EditText) findViewById(R.id.txtId);
        userphone=(EditText) findViewById(R.id.txtPhone);
        usersurname=(EditText) findViewById(R.id.txtSurName);
        usereps=(EditText) findViewById(R.id.txtEps);

        userEmail =email.getText().toString();
        userPwd=pass.getText().toString();
        userName=username.getText().toString();
        userEps= usereps.getText().toString();
        userId= userid.getText().toString();
        userPhone= userphone.getText().toString();
        userSurname= usersurname.getText().toString();

        if(validateForm(userEmail,userPwd,userName, userSurname, userPhone, userEps, userId))
        {
            mAuth.createUserWithEmailAndPassword(userEmail,userPwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){

                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        assert firebaseUser != null;
                        String userid = firebaseUser.getUid();

                        DatabaseReference reference = database.getReference("Usuarios/"+firebaseUser.getUid());
                        //reference.setValue(usuario);
                       // finish();

                       // reference = FirebaseDatabase.getInstance().getReference("Usuarios").child(userid);

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id", userid);
                        hashMap.put("userId", userid);
                        hashMap.put("userName", userName);
                        hashMap.put("userSurname", userSurname);
                        hashMap.put("userPhone", userPhone);
                        hashMap.put(Constants.KEY_EMAIL , userEmail);
                        hashMap.put(Constants.KEY_PASSWORD , userPwd);
                        hashMap.put("userEps", userEps);
                        hashMap.put("imageURL", "default");
                        hashMap.put("status", "offline");
                        hashMap.put("search", userEmail.toLowerCase());


                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(Register.this, "Registro existoso, por favor inicie sesión", Toast.LENGTH_SHORT).show();
                                    mAuth.signOut();
                                    Intent intent = new Intent(Register.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();

                                }
                            }
                        });

                    }else{
                        Log.e(TAG,"Autentificación fallida: "+task.getException().toString());
                        Toast.makeText(Register.this, task.getException().toString(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
    private boolean validateForm(String emailS, String passwordS, String usernameS, String userSurnameS, String userPhoneS, String userEpsS, String userIdS)
    {
        boolean esValido=true;

        emailS.trim();
        if(TextUtils.isEmpty(emailS)){
            esValido=false;
            email.setError("Campo Requerido");
        }

        usernameS.trim();

        if(TextUtils.isEmpty(usernameS)){
            esValido=false;
            username.setError("Campo Requerido");
        }
        userSurnameS.trim();
        if(TextUtils.isEmpty(userSurnameS)){
            esValido=false;
            usersurname.setError("Campo Requerido");
        }
        userIdS.trim();
        if(TextUtils.isEmpty(userIdS)){
            esValido=false;
            userid.setError("Campo Requerido");
        }
        userPhoneS.trim();
        if(TextUtils.isEmpty(userPhoneS)){
            esValido=false;
            userphone.setError("Campo Requerido");
        }
        passwordS.trim();
        if(TextUtils.isEmpty(passwordS)){
            esValido=false;
            pass.setError("Campo Requerido");
        }

        if(passwordS.length()<6)
        {
            esValido=false;
            pass.setError("La contraseña debe tener al menos 6 caracteres");
        }

        if(esValido==true && TextUtils.isEmpty(passwordS)!=true){
            if (validarEmail(emailS)==false) {
                esValido=false;
                email.setError("Formato de email incorrecto");
            }
        }

        return esValido;
    }

    private boolean validarEmail(String email) {
        if (!email.contains("@") ||
                !email.contains(".") ||
                email.length() < 6)
            return false;
        return true;
    }

    public void Salir(View v){
        //cierre sesion
        mAuth.signOut();
        //UpdateUi
        Intent intent=new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


}