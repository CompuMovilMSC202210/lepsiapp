package com.javeriana.lepsiapp.ui.homePatients;

import static android.app.Activity.RESULT_OK;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.javeriana.lepsiapp.GlobalVar;
import com.javeriana.lepsiapp.activity.LoginActivity;
import com.javeriana.lepsiapp.data.model.arreglocont;
import com.javeriana.lepsiapp.databinding.HomePatientsFragmentBinding;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.javeriana.lepsiapp.ui.home.HomeViewModel;

import org.osmdroid.util.GeoPoint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class HomePatientsFragment extends Fragment {

    private HomePatientsViewModel mViewModel;
    private HomePatientsFragmentBinding binding;

    int sevento=1;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ImageButton btHeld;
    SimpleDateFormat fechaact;
    FusedLocationProviderClient fusedLocalizacion;
    String platitud;
    String plongitud;
    GeoPoint startPoint;


    //******Si se dieron los permisos***********
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
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        //******Permisos de locación***********
        fusedLocalizacion= LocationServices.getFusedLocationProviderClient(getContext());
        getpermisolocal.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        //******************************************

        binding = HomePatientsFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        inicializarFirebase();

        btHeld = binding.btnAyudaMain;
        btHeld.setOnClickListener(view -> {

            //Boton de ayuda
            //GeoPoint startPoint = new GeoPoint(latitude, longitude);
            fechaact= new SimpleDateFormat("dd/MM/yyyy h:mm a");
            Date date = new Date();
            String dateToStr = fechaact.format(date);
            String sumevento = Integer.toString(sevento);
            String fun="Boton";
            String Ubicacion=String.valueOf(startPoint);
            arreglocont a= new arreglocont();
            a.setUid(UUID.randomUUID().toString());
            a.setEvento(sumevento);
            a.setFecha(dateToStr);
            a.setFuente(fun);
            a.setUserid(GlobalVar.UidMain);
            Toast myToast = Toast.makeText(getContext(), String.valueOf("Boton"), Toast.LENGTH_SHORT);
            myToast.show();
            System.out.println(sumevento);
            System.out.println(fun);
            databaseReference.child("historial").child(GlobalVar.UidMain).child(a.getUid()).setValue(a);
            sevento++;

                }
        );


        return root;


    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(getContext());
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
    }

    //******Actualización localizacion***********
    private void starLocationUpdates(){
        if (ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
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