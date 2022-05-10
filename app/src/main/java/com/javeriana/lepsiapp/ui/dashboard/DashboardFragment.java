package com.javeriana.lepsiapp.ui.dashboard;

import static android.widget.Toast.makeText;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.javeriana.lepsiapp.activity.RegistroContact;
import com.javeriana.lepsiapp.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    Button but1;

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button but1 = binding.ctBot1;
        Button but2 = binding.ctBot2;
        Button but3 = binding.ctBot3;



        //final TextView textView = binding.textContacts;
        //dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        //Button btnContact =binding.cnt1;

        but1.setOnClickListener(view -> {

            Intent intent = new Intent(getActivity(), RegistroContact.class);
            getActivity().startActivity(intent);
        });



        return root;
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}