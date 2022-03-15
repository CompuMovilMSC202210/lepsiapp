package com.javeriana.lepsiapp.ui.dashboard;

import static android.widget.Toast.makeText;

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

import com.javeriana.lepsiapp.AddContactFragment;
import com.javeriana.lepsiapp.R;
import com.javeriana.lepsiapp.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textContacts;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        Button btnContact =binding.cnt1;

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //makeText(getActivity(), "Debe ingresar un número límite para continuar", Toast.LENGTH_SHORT).show();

                inflater.inflate(R.layout.fragment_add_contact, container, false);

            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}