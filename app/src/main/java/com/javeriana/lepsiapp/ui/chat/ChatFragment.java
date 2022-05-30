package com.javeriana.lepsiapp.ui.chat;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.javeriana.lepsiapp.GlobalVar;
import com.javeriana.lepsiapp.Holder.UsuarioViewHolder;
import com.javeriana.lepsiapp.R;
import com.javeriana.lepsiapp.activity.chatActivity;


import com.javeriana.lepsiapp.databinding.FragmentChatBinding;
import com.javeriana.lepsiapp.entidades.Logica.LUsuario;
import com.javeriana.lepsiapp.entidades.Usuario;

public class ChatFragment extends Fragment {

    private RecyclerView rvUsuarios;
    private FirebaseRecyclerAdapter adapter;

    private FragmentChatBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ChatViewModel chatViewModel = new ViewModelProvider((ViewModelStoreOwner) getContext()).get(ChatViewModel.class);

        binding = FragmentChatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        rvUsuarios = binding.rvUsuarios;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvUsuarios.setLayoutManager(linearLayoutManager);
        Query query = FirebaseDatabase.getInstance().getReference()
                .child("Usuarios");
                //.limitToLast(3); //limite de usuarios

        FirebaseRecyclerOptions<Usuario> options =
                new FirebaseRecyclerOptions.Builder<Usuario>()
                        .setQuery(query, Usuario.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Usuario, UsuarioViewHolder>(options) {
            @Override
            public UsuarioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_usuario, parent, false);
                return new UsuarioViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(UsuarioViewHolder holder, int position, Usuario model) {

                holder.getTxtNombreUsuario().setText(model.getUserName());

                LUsuario lUsuario = new LUsuario(getSnapshots().getSnapshot(position).getKey(),model);


                holder.getLayoutContactosChat().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(getContext(),chatActivity.class);
                        intent.putExtra("key_receptor",lUsuario.getKey());
                        startActivity(intent);

                    }
                });

            }


        };

        rvUsuarios.setAdapter(adapter);



       // butchat1.setOnClickListener(view -> {

         //   Intent intent = new Intent(getActivity(), chatActivity.class);
           // getActivity().startActivity(intent);
        //});

        // final TextView textView = binding.textChat;
        //chatViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
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