package com.javeriana.lepsiapp.ui.homePatients;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.DownloadManager;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.Manifest;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.api.HttpRule;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.javeriana.lepsiapp.GlobalVar;
import com.javeriana.lepsiapp.Holder.arreglocontViewHolder;
import com.javeriana.lepsiapp.R;
import com.javeriana.lepsiapp.ui.login.LoginActivity;
import com.javeriana.lepsiapp.data.model.arreglocont;
import com.javeriana.lepsiapp.databinding.HomePatientsFragmentBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.javeriana.lepsiapp.ui.home.HomeViewModel;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HomePatientsFragment extends Fragment {

    private HomePatientsViewModel mViewModel;
    private HomePatientsFragmentBinding binding;

    int sevento=1;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ImageButton btHeld;

    BarChart grafica1;
    SimpleDateFormat fechaact;
    FusedLocationProviderClient fusedLocalizacion;
    String platitud;
    String plongitud;
    GeoPoint startPoint;
    ArrayList barArrayList;



    //******Si se dieron los permisos**********
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
        //******Permisos de locación***********
        binding = HomePatientsFragmentBinding.inflate(inflater, container, false);
        View graf=inflater.inflate(R.layout.home_patients_fragment,container,false);
        grafica1 = binding.grafica;
        getData();
        BarDataSet barDataSet=new BarDataSet(barArrayList,"Fecha");
        BarData barData=new BarData(barDataSet);
        grafica1.setData(barData);
        barDataSet.setColors(R.color.ocre_claro);
        barData.setValueTextColor(R.color.ocre_claro);
        barDataSet.setValueTextColor(R.color.ocre_claro);
        barDataSet.setValueTextSize(16f);
        grafica1.getDescription().setEnabled(true);

        View root = binding.getRoot();
        inicializarFirebase();


        btHeld = binding.btnAyudaMain;


        btHeld.setOnClickListener(view -> {


            //Notificacion

            llamarespecifico();

            //Boton de ayuda
            //GeoPoint startPoint = new GeoPoint(latitude, longitude);
            fechaact= new SimpleDateFormat("dd/MM/yyyy h:mm a");
            Date date = new Date();
            String dateToStr = fechaact.format(date);
            String sumevento = String.valueOf(GlobalVar.sevento);
            String fun="Boton";
            String Ubicacion=String.valueOf(startPoint);
            arreglocont a= new arreglocont();
            a.setUid(UUID.randomUUID().toString());
            a.setEvento(sumevento);
            a.setFecha(dateToStr);
            a.setFuente(fun);
            a.setUserid(GlobalVar.UidMain);
            a.setUbica(Ubicacion);
            Toast myToast = Toast.makeText(getContext(), String.valueOf("Boton"), Toast.LENGTH_SHORT);
            myToast.show();
            System.out.println(sumevento);
            System.out.println(fun);
            databaseReference.child("historial").child(GlobalVar.UidMain).child(a.getUid()).setValue(a);
            GlobalVar.sevento++;

            arreglocont ubicaUser = new arreglocont();

            ubicaUser.setUbica(Ubicacion);

            databaseReference.child("Ubicacion").child(GlobalVar.UidMain).setValue(ubicaUser);

                }
        );
        return root;
       }

    private void llamarespecifico() {

        RequestQueue myrequest = Volley.newRequestQueue(getContext());
        JSONObject json = new JSONObject();
        try {
            String token = "dGu8QCu6QpiZo8gCqanqJp:APA91bFJ7EdSBmdPv6ZhrtfUwn-A7V5IFz7A4oXS7sKa8otJvffy8wHBxwyvgLMatbjE-HdBpwF3Lqom7Cb5J4W3aZ3gp4YBVL7qjB_p6t6h0evpX8zonFU7GHhwQz44W6_ohWplvUyy";
            json.put("to",token);
            JSONObject notificacion = new JSONObject();
            notificacion.put("titulo","Ayuda");
            notificacion.put("detalle","Su contacto de LepsiApp ha presentado un episodio");

            json.put("data",notificacion);

            String URL= "https://fcm.googleapis.com/fcm/send";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,URL,json,null,null){
                @Override
                public Map<String, String> getHeaders()  {
                    Map<String,String> header = new HashMap<>();

                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAA81g1HtM:APA91bGulMnFIavkCqNjlIayZVcceTL7-YVMZPirO8mE4uC2SOqkllJTcYrlymQ8R7xgkUWEoFRWppoQMTi3Wo47HBccMen_UdwCzKhEZApR-M6ceif5EmnkBKqmg72f6Hk2_cPnmJFN ");

                    return header;
                }
            };

            myrequest.add(request);


        }catch (JSONException e){

            e.printStackTrace();
        }
    }

    private void getData() {
        barArrayList=new ArrayList();
        barArrayList.add(new BarEntry(2f,10));
        barArrayList.add(new BarEntry(3f,30));
        barArrayList.add(new BarEntry(4f,20));
        barArrayList.add(new BarEntry(5f,50));

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