package com.javeriana.lepsiapp.ui.chat;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.javeriana.lepsiapp.activity.chatActivity;
import com.javeriana.lepsiapp.R;
import com.javeriana.lepsiapp.databinding.FragmentChatBinding;

public class ChatFragment extends Fragment {
    private FragmentChatBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ChatViewModel chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        binding = FragmentChatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button butchat1 = binding.btnChat1;
        Button but2 = binding.btnChat2;
        Button but3 = binding.btnChat3;

        butchat1.setOnClickListener(view -> {

            Intent intent = new Intent(getActivity(), chatActivity.class);
            getActivity().startActivity(intent);
        });

        // final TextView textView = binding.textChat;
        //chatViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}