package com.javeriana.lepsiapp.ui.eventsHistory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.javeriana.lepsiapp.GlobalVar;
import com.javeriana.lepsiapp.Holder.arreglocontViewHolder;
import com.javeriana.lepsiapp.R;
import com.javeriana.lepsiapp.databinding.FragmentHistoryBinding;
import com.javeriana.lepsiapp.data.model.arreglocont;

public class History extends Fragment {

    private RecyclerView hist_list_even;
    private FirebaseRecyclerAdapter adapter;

    private FragmentHistoryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HistoryViewModel historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);


        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        hist_list_even = binding.histListEven;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        hist_list_even.setLayoutManager(linearLayoutManager);
        Query query = FirebaseDatabase.getInstance().getReference()
                .child("historial").child(GlobalVar.UidMain);
        System.out.println(query);

        FirebaseRecyclerOptions< arreglocont > options =
                new FirebaseRecyclerOptions.Builder<arreglocont>()
                        .setQuery(query,arreglocont.class )
                        .build();
        System.out.println(options);

        adapter = new FirebaseRecyclerAdapter<arreglocont, arreglocontViewHolder>(options) {
            @Override
            public arreglocontViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.histograma_cam, parent, false);
                return new arreglocontViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(arreglocontViewHolder holder, int position, arreglocont m) {
                holder.getIdfecha_histo().setText(m.getFecha());
                holder.getIdsensor_hist().setText(m.getFuente());
                holder.getIdcont_histo().setText(m.getEvento());
            }


        };
        hist_list_even.setAdapter(adapter);

       // final TextView textView = binding.textHistory;
      //  historyViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }
    @Override
    public void onStart(){
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop(){
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}