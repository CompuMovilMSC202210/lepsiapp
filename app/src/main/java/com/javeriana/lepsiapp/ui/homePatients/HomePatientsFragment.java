package com.javeriana.lepsiapp.ui.homePatients;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.javeriana.lepsiapp.data.model.arreglocont;
import com.javeriana.lepsiapp.databinding.FragmentHomeBinding;
import com.javeriana.lepsiapp.databinding.HomePatientsFragmentBinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.javeriana.lepsiapp.R;
import com.javeriana.lepsiapp.ui.home.HomeViewModel;

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



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = HomePatientsFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        inicializarFirebase();
        

        btHeld = binding.btnAyudaMain;




        btHeld.setOnClickListener(view -> {

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







        return root;
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(getContext());
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
    }


}