package com.javeriana.lepsiapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.javeriana.lepsiapp.AdapterMensajes;
import com.javeriana.lepsiapp.Entidades.MensajeEnviar;
import com.javeriana.lepsiapp.Entidades.MensajeRecibir;
import com.javeriana.lepsiapp.Entidades.Usuario;
import com.javeriana.lepsiapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.javeriana.lepsiapp.ui.login.LoginActivity;

import java.lang.ref.Reference;

import de.hdodenhof.circleimageview.CircleImageView;





public class specific_chat extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CircleImageView fotoperfil;
    private TextView nombre;
    private RecyclerView rvMensajes;
    private EditText txtMensaje;
    private Button btnEnviar,cerrarSesion;
    private AdapterMensajes adapter;
    private ImageButton btnEnviarFoto;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private static final int PHOTO_SEND=1;
    private static final int PHOTO_PERFIL=2;
    private String fotoperfilCadena;
    private FirebaseAuth mAuth;
    private String NOMBRE_USUARIO;



    public specific_chat() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment specific_chat.
     */
    // TODO: Rename and change types and number of parameters

    public static class MainActivity extends AppCompatActivity {

        private CircleImageView fotoperfil;
        private TextView nombre;
        private RecyclerView rvMensajes;
        private EditText txtMensaje;
        private Button btnEnviar, cerrarSesion;
        private AdapterMensajes adapter;
        private ImageButton btnEnviarFoto;

        private FirebaseDatabase database;
        private DatabaseReference databaseReference;
        private FirebaseStorage storage;
        private StorageReference storageReference;
        private static final int PHOTO_SEND = 1;
        private static final int PHOTO_PERFIL = 2;
        private String fotoperfilCadena;
        private FirebaseAuth mAuth;
        private String NOMBRE_USUARIO;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            fotoperfil = (CircleImageView) findViewById(R.id.fotoperfil);
            nombre = (TextView) findViewById(R.id.nombre);
            rvMensajes = (RecyclerView) findViewById(R.id.rvMensajes);
            txtMensaje = (EditText) findViewById(R.id.txtMensaje);
            btnEnviar = (Button) findViewById(R.id.btnEnviar);
            btnEnviarFoto = (ImageButton) findViewById(R.id.btnEnviarFoto);
            cerrarSesion = (Button) findViewById(R.id.cerrarSesion);
            fotoperfilCadena = "";

            database = FirebaseDatabase.getInstance();
            databaseReference = database.getReference("chatV2 "); //sala de chat nombre version2
            storage = FirebaseStorage.getInstance();
            mAuth = FirebaseAuth.getInstance();


            adapter = new AdapterMensajes(this);
            LinearLayoutManager l = new LinearLayoutManager(this);
            rvMensajes.setLayoutManager(l);
            rvMensajes.setAdapter(adapter);

            btnEnviar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    databaseReference.push().setValue(new MensajeEnviar(txtMensaje.getText().toString(), NOMBRE_USUARIO, fotoperfilCadena, "1", ServerValue.TIMESTAMP));
                    txtMensaje.setText("");
                }
            });
            cerrarSesion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    returnLogin();

                }
            });

            btnEnviarFoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.setType("image/jpeg");
                    i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    startActivityForResult(Intent.createChooser(i, "Selecciona una foto"), PHOTO_SEND);

                }
            });

            fotoperfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.setType("image/jpeg");
                    i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    startActivityForResult(Intent.createChooser(i, "Selecciona una foto"), PHOTO_PERFIL);

                }
            });


            adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);
                    setScrollbar();
                }
            });

            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    MensajeRecibir m = snapshot.getValue(MensajeRecibir.class); //cambie dataSnapshot por snapshot
                    adapter.addMensaje(m);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            verifyStoragePermissions(this);

        }

        private void setScrollbar() {

            rvMensajes.scrollToPosition(adapter.getItemCount() - 1);
        }

        public static boolean verifyStoragePermissions(Activity activity) {
            String[] PERMISSIONS_STORAGE = {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            int REQUEST_EXTERNAL_STORAGE = 1;
            int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        activity,
                        PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE
                );
                return false;
            } else {
                return true;
            }
        }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PHOTO_SEND && resultCode == RESULT_OK) {
                Uri u = data.getData();
                storageReference = storage.getReference("imagenes_chat");//imagenes_chat
                final StorageReference fotoReferencia = storageReference.child(u.getLastPathSegment());
                fotoReferencia.putFile(u).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //  Uri u = taskSnapshot.getDownloadUrl();
                        MensajeEnviar m = new MensajeEnviar(NOMBRE_USUARIO + " te ha enviado a photo", u.toString(), NOMBRE_USUARIO, fotoperfilCadena, "2", ServerValue.TIMESTAMP);
                        databaseReference.push().setValue(m);
                    }

                });
            } else if (requestCode == PHOTO_PERFIL && resultCode == RESULT_OK) {
                Uri u = data.getData();
                storageReference = storage.getReference("fotoperfil");//imagenes_chat
                final StorageReference fotoReferencia = storageReference.child(u.getLastPathSegment());
                fotoReferencia.putFile(u).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //  Uri u = taskSnapshot.getDownloadUrl();
                        fotoperfilCadena = u.toString();
                        MensajeEnviar m = new MensajeEnviar(NOMBRE_USUARIO + "ha actualizado su foto de perfil", u.toString(), NOMBRE_USUARIO, fotoperfilCadena, "2", ServerValue.TIMESTAMP);
                        databaseReference.push().setValue(m);
                        Glide.with(MainActivity.this).load(u.toString()).into(fotoperfil);
                    }

                });


            }
        }

        @Override
        protected void onResume() {
            super.onResume();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                btnEnviar.setEnabled(false);
                DatabaseReference reference = database.getReference("Usuarios/" + currentUser.getUid());
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Usuario usuario = snapshot.getValue(Usuario.class); //cambio dataSnapshot por snapshot
                        NOMBRE_USUARIO = usuario.getNombre();
                        nombre.setText(NOMBRE_USUARIO);
                        btnEnviar.setEnabled(true);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else {
                returnLogin();
            }
        }

        private void returnLogin() {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();

        }


    }}