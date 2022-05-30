package com.javeriana.lepsiapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Button;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import com.javeriana.lepsiapp.activity.RegistroContact;
import com.javeriana.lepsiapp.ui.login.LoginActivity;
import com.javeriana.lepsiapp.databinding.ActivityMainBinding;

import com.google.firebase.FirebaseApp;

import com.javeriana.lepsiapp.data.model.arreglocont;
import com.javeriana.lepsiapp.ui.login.LoginActivity;


import org.osmdroid.util.GeoPoint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    Toolbar toolbarL;




    private SensorManager sm;
    private Sensor s;
    private SensorEventListener evento;
    private int mov=0;
    Button btonayuda;
    int sevento=1;
    SimpleDateFormat fechaact;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FusedLocationProviderClient fusedLocalizacion;
    String platitud;
    String plongitud;
    GeoPoint startPoint;



    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private StorageReference storageReference;
    private String NOMBRE_USUARIO;
    private String rolval;
    private String uid;
    private String UidMain;
    FirebaseUser firebaseUser;

    ActivityResultLauncher<String> getpermisolocal = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if(result){
                        System.out.println("Se dieron los permisos");
                        starLocationUpdates();

                    }else {
                        //denied
                        System.out.println("No se dieron los permisos");
                        //mapView.setText("No hay permisos para la localización");

                    }
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        // Obtener ID usuario logueado

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        toolbarL = findViewById(R.id.barratool);
        setSupportActionBar(toolbarL);



        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home1, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_chat, R.id.navigation_history)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);



        mAuth = FirebaseAuth.getInstance();
        //******Permisos de locación***********
        fusedLocalizacion= LocationServices.getFusedLocationProviderClient(this);
        getpermisolocal.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        //******************************************




        //Acelerometro
        inicializarFirebase();

        sm=(SensorManager) getSystemService(Context.SENSOR_SERVICE);//Acceder a los sensores
        s=sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);//Valor acelerometro
        if(s==null){
            Toast myToast = Toast.makeText(MainActivity.this, String.valueOf("No tiene acelerometro"), Toast.LENGTH_SHORT);
            myToast.show();
        }
        evento=new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                //Evento que se genera con el acelerometro
                if(sensorEvent.values[0]<=-6&&mov==0) {
                    mov++;
                }else{
                    if(sensorEvent.values[0]<=6&&mov==1) {
                        mov++;
                    } else {if (sensorEvent.values[1]<=6&&mov==2){
                        mov++;
                        System.out.println(mov);
                    }else {if (sensorEvent.values[1]<=-6&&mov==3){
                        mov++;
                        System.out.println(mov);
                    }}}
                }
                if (mov==4){
                    mov=0;
                    fechaact= new SimpleDateFormat("dd/MM/yyyy h:mm a");
                    Date date = new Date();
                    String dateToStr = fechaact.format(date);
                    String sumevento = Integer.toString(GlobalVar.sevento);
                    String fun="Sensor";
                    String Ubicacion=String.valueOf(startPoint);
                    arreglocont a= new arreglocont();
                    a.setUid(UUID.randomUUID().toString());
                    a.setEvento(sumevento);
                    a.setFecha(dateToStr);
                    a.setFuente(fun);
                    a.setUserid(GlobalVar.UidMain);
                    a.setUbica(Ubicacion);
                    databaseReference.child("historial").child(GlobalVar.UidMain).child(a.getUid()).setValue(a);
                    GlobalVar.sevento++;
                    System.out.println("_______________");
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
        sm.registerListener(evento,s,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){

        switch (item.getItemId()) {
            case (R.id.LoGout):
                logOut();
                return true;
            case (R.id.Help):
                help();
                return true;
            case (R.id.Profile):
                //profile();
                return true;
        }
        return false;
    }




    public void logOut() {
        //cierre sesion
        mAuth.signOut();
        //UpdateUi
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void help() {
        Intent intent = new Intent(this, HelpInformation.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
    }
    private void starLocationUpdates(){
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocalizacion.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    platitud=String.valueOf(location.getLatitude());
                    plongitud=String.valueOf(location.getLongitude());
                    startPoint  =  new GeoPoint(location.getLatitude(), location.getLongitude());
                }
            });

        }
    }

}

