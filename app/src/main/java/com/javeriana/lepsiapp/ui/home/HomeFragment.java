package com.javeriana.lepsiapp.ui.home;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.SENSOR_SERVICE;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.javeriana.lepsiapp.R;
import com.javeriana.lepsiapp.data.model.arreglocont;
import com.javeriana.lepsiapp.databinding.FragmentHomeBinding;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.library.BuildConfig;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.TilesOverlay;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;


public class HomeFragment extends Fragment {

    MapView map;

    double latitude = 4.78;
    double longitude = -74.14;

    double newlatitude = 4.78;
    double newlongitude = -74.14;

    double latitude1 = 4.646631;
    double longitude1 = -74.069015; //4.646631, -74.069015

    double latitude2 = 4.622133;
    double longitude2 = -74.083210; // 4.622133, -74.083210

    double latitude3 = 4.634281;
    double longitude3 = -74.115100; //4.634281, -74.115100

    public static double RADIUS_OF_EARTH_KM = 6371;

    GeoPoint startPoint = new GeoPoint(latitude, longitude);

    GeoPoint person1 = new GeoPoint(latitude1, longitude1);
    GeoPoint person2 = new GeoPoint(latitude2, longitude2);
    GeoPoint person3 = new GeoPoint(latitude3, longitude3);

    Marker per1Market;
    Marker per2Market;
    Marker per3Market;

    IMapController mapController;

    Marker longPressedMarker;

    Marker startMarket;

    Geocoder mGeocoder;

    TextView Txlatitude, txt_permission, luxometro, orientacionx;
    ImageButton btHeld;

    LocationRequest locationRequest;
    LocationCallback locationCallback;
    Location lastLocation;

    GeoPoint gps_geo;

    boolean settingsOK = false;


    SensorManager sensorManager;

    Sensor lightSensor;
    Sensor acelSensor;
    Sensor oriSensor;

    SensorEventListener lightSensorListener;
    SensorEventListener acelSensorListener;
    SensorEventListener oriSensorListener;

    RoadManager roadManager;
    Polyline roadOverlay;
    boolean markerstatus =false;

    int zoom;
    private int mov=0;
    Button btonayuda;
    int sevento=1;
    SimpleDateFormat fechaact;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    FusedLocationProviderClient fusedLocationProviderClient;

    ActivityResultLauncher<String> getLocationPermission = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if (!result) {
                        txt_permission.setText("No hay Permiso");
                    } else {
                        startLocationUpdates();
                    }
                }
            }

    );

    ActivityResultLauncher<IntentSenderRequest> getLocationSettings = registerForActivityResult(
            new ActivityResultContracts.StartIntentSenderForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()== RESULT_OK){
                        settingsOK =true;
                        startLocationUpdates();
                    }
                    else{
                        txt_permission.setText("GPS is off");
                    }
                }
            }
    );


    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textHome;
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(binding.getRoot().getContext());

        map = binding.osmMap;
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.getOverlays().add(createOverlayEvents());

        Txlatitude =binding.txtlatitude;
        orientacionx= binding.txtOrix;
        luxometro =binding.txtLux;

        btHeld = binding.btnVoyMain;
        txt_permission =binding.textPermission;

        mapController = map.getController();

        sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);

        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        acelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        oriSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        locationRequest = createLocationRequest();
        locationCallback = createLocationCallback();

        getLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        checkLocationSettings();
        inicializarFirebase();

        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

        lightSensorListener = new SensorEventListener() {
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {}
            @Override
            public void onSensorChanged(SensorEvent event) {
                //luxometro.setText(String.valueOf(event.values[0]));
                if (event.values[0] < 30) {
                    map.getOverlayManager().getTilesOverlay().setColorFilter(TilesOverlay.INVERT_COLORS);
                } else {
                    map.getOverlayManager().getTilesOverlay().setColorFilter(null);
                }
            }
        };

        oriSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float[] rotationMatrix = new float[16];
                SensorManager.getRotationMatrixFromVector(
                        rotationMatrix, event.values);
                float[] remappedRotationMatrix = new float[16];
                SensorManager.remapCoordinateSystem(rotationMatrix,
                        SensorManager.AXIS_X,
                        SensorManager.AXIS_Z,
                        remappedRotationMatrix);

                float[] orientations = new float[3];
                SensorManager.getOrientation(remappedRotationMatrix, orientations);

                for(int i = 0; i < 3; i++) {
                    orientations[i] = (int)(Math.toDegrees(orientations[i]))*-1;
                }


                if (orientations[0] < 0 && orientations[0] < 360 ){
                    orientations[0] = orientations[0] + 360;
                }

                if (orientations[1] < 0 && orientations[1] < 360){
                    orientations[1] = orientations[1] + 360;
                }

                if (orientations[2] < 0 && orientations[2] < 360){
                    orientations[2] = orientations[2] + 360;
                }

               //orientacionx.setText(String.valueOf(orientations[0]));
                map.setMapOrientation(orientations[0]);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        btHeld.setOnClickListener(view -> {

            double distance1 = distance(startPoint.getLatitude(), startPoint.getLongitude(), latitude1, longitude1);
            double distance2 = distance(startPoint.getLatitude(), startPoint.getLongitude(), latitude2, longitude2);
            double distance3 = distance(startPoint.getLatitude(), startPoint.getLongitude(), latitude3, longitude3);

            double zoom_ =(-1.161*Math.log(distance1)+14.729)*1.09;

            per1Market = createMarker(person1, "Familia1 "+ distance1+ " km", null, R.drawable.marker11);
            per2Market = createMarker(person2, "Familia2 "+ distance2+ " km", null, R.drawable.marker11);
            per3Market = createMarker(person3, "Familia3 "+ distance3+ " km", null, R.drawable.marker11);
            map.getOverlays().add(per1Market);
            map.getOverlays().add(per2Market);
            map.getOverlays().add(per3Market);

            mapController = map.getController();
            mapController.setZoom(zoom_);
            mapController.setCenter(startPoint);

            drawRoute(startPoint, person1);
                    gps_geo = person1;
                    markerstatus = true;

                    //Boton de ayuda
                    fechaact= new SimpleDateFormat("dd/MM/yyyy h:mm a");
                    Date date = new Date();
                    String dateToStr = fechaact.format(date);
                    String sumevento = Integer.toString(sevento);
                    String fun="Boton";
                    arreglocont a= new arreglocont(sumevento, dateToStr, fun);
                    a.setUid(UUID.randomUUID().toString());
                    a.setEvento(sumevento);
                    a.setFecha(dateToStr);
                    a.setFuente(fun);
                    Toast myToast = Toast.makeText(getContext(), String.valueOf("Boton"), Toast.LENGTH_SHORT);
                    myToast.show();
                    System.out.println(sumevento);
                    System.out.println(fun);
                    databaseReference.child("arreglocont").child(a.getUid()).setValue(a);
                    sevento++;

        }
        );

        roadManager = new OSRMRoadManager(binding.getRoot().getContext(), "ANDROID");

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        return root;
    }


    private void inicializarFirebase() {
        FirebaseApp.initializeApp(getContext());
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
    }


    private MapEventsOverlay createOverlayEvents() {
        MapEventsOverlay overlayEventos = new MapEventsOverlay(new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                return false;
            }
            @Override
            public boolean longPressHelper(GeoPoint p) {
                longPressOnMap(p);
                return true;
            }
        });
        return overlayEventos;

    }

    private void longPressOnMap(GeoPoint p)  {
        if(longPressedMarker!=null)
            map.getOverlays().remove(longPressedMarker);

        String namepoint = null;

        try {
            namepoint = mGeocoder.getFromLocation(p.getLatitude(),p.getLongitude(),2).get(0).getAddressLine(0).toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (settingsOK) {
            drawRoute(startPoint, p);
            gps_geo = p;
            double distance1 = distance(startPoint.getLatitude(), startPoint.getLongitude(), p.getLatitude(), p.getLongitude());
            longPressedMarker = createMarker(p, namepoint + " Distancia " +distance1+ "KM", null, R.drawable.marker11);
            Toast.makeText(binding.getRoot().getContext(), "La distancia entre los dos puntos es " + String.valueOf(distance1) + "KM", Toast.LENGTH_LONG).show();
            markerstatus = true;
        }
        else{
            longPressedMarker = createMarker(p, namepoint, null, R.drawable.marker11);
        }
        map.getOverlays().add(longPressedMarker);
    }

    private Marker createMarker(GeoPoint p, String title, String desc, int iconID){

        Marker marker = null;

        if(map!=null) {
            marker = new Marker(map);
            if (title != null) marker.setTitle(title);
            if (desc != null) marker.setSubDescription(desc);
            if (iconID != 0) {
                Drawable myIcon = getResources().getDrawable(iconID, binding.getRoot().getContext().getTheme());
                marker.setIcon(myIcon);
            }
            marker.setPosition(p);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        }
        return marker;
    }


    private void checkLocationSettings(){
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(binding.getRoot().getContext());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Log.i("LepsiApp", "GPS is ON");
                settingsOK = true;
                startLocationUpdates();
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(((ApiException) e).getStatusCode() == CommonStatusCodes.RESOLUTION_REQUIRED){
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    IntentSenderRequest isr = new IntentSenderRequest.Builder(resolvable.getResolution()).build();
                    getLocationSettings.launch(isr);
                }else {
                    txt_permission.setText("No GPS available");
                }
            }
        });
    }

    private LocationRequest createLocationRequest(){
        LocationRequest locationRequest = LocationRequest.create()
                .setInterval(10000)
                .setFastestInterval(5000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    private LocationCallback createLocationCallback(){
        return new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                lastLocation= locationResult.getLastLocation();
                if(lastLocation!=null){

                    zoom++;

                    newlatitude=latitude;
                    latitude = lastLocation.getLatitude();

                    newlongitude =longitude;
                    longitude = lastLocation.getLongitude();


                    if(zoom == 1){
                        startPoint  =  new GeoPoint(latitude, longitude);
                        startMarket = createMarker(startPoint, "Estoy aqui ", null, R.drawable.marker11);
                        map.getOverlays().add(startMarket);

                        mapController = map.getController();
                        mapController.setZoom(18.0);
                        mapController.setCenter(startPoint);
                    }
                    if (zoom > 4){
                        zoom = 2;
                    }

                    double distanceRT = distance(newlatitude,newlongitude,latitude,longitude);


                    if(startMarket!=null)
                        map.getOverlays().remove(startMarket);

                    startPoint  =  new GeoPoint(latitude, longitude);

                    startMarket = createMarker(startPoint, "Estoy aqui ", null, R.drawable.marker11);
                    map.getOverlays().add(startMarket);

                    if(markerstatus && distanceRT>0.02){
                        drawRoute(startPoint, gps_geo);
                    }

                    if (distanceRT>3)
                    {
                        mapController = map.getController();
                        mapController.setZoom(18.0);
                        mapController.setCenter(startPoint);
                    }


                    //elevation = lastLocation.getAltitude();
                }
            }
        };

    }

    private void startLocationUpdates () {
        if (ActivityCompat.checkSelfPermission(binding.getRoot().getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (settingsOK) {
                fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,null);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        map.onResume();

        mapController = map.getController();
        mapController.setZoom(18.0);
        mapController.setCenter(this.startPoint);

        sensorManager.registerListener(lightSensorListener, lightSensor,
                SensorManager.SENSOR_DELAY_NORMAL);

        sensorManager.registerListener(acelSensorListener, acelSensor,
                SensorManager.SENSOR_DELAY_NORMAL);

        sensorManager.registerListener(oriSensorListener, oriSensor,
                SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        sensorManager.unregisterListener(lightSensorListener);
        sensorManager.unregisterListener(acelSensorListener);
        sensorManager.unregisterListener(oriSensorListener);
    }

    public double distance(double lat1, double long1, double lat2, double long2) {
        double latDistance = Math.toRadians(lat1 - lat2);
        double lngDistance = Math.toRadians(long1 - long2);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double result = RADIUS_OF_EARTH_KM * c;
        return Math.round(result*100.0)/100.0;
    }

    private void drawRoute(GeoPoint start, GeoPoint finish){
        ArrayList<GeoPoint> routePoints = new ArrayList<>();
        routePoints.add(start);
        routePoints.add(finish);
        Road road = roadManager.getRoad(routePoints);
        Log.i("LepsiApp", "Route length: "+road.mLength+" klm");
        Log.i("LepsiApp", "Duration: "+road.mDuration/60+" min");
        if(map!=null){
            if(roadOverlay!=null){
                map.getOverlays().remove(roadOverlay);
            }
            roadOverlay = RoadManager.buildRoadOverlay(road);
            roadOverlay.getOutlinePaint().setColor(Color.BLUE);
            roadOverlay.getOutlinePaint().setStrokeWidth(10);
            map.getOverlays().add(roadOverlay);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}