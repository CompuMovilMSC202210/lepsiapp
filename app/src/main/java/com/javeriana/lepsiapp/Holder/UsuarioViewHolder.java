package com.javeriana.lepsiapp.Holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.javeriana.lepsiapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsuarioViewHolder extends RecyclerView.ViewHolder {

private CircleImageView civFotoPerfil;
private TextView txtNombreUsuario;
private LinearLayout layoutContactosChat;


    public UsuarioViewHolder(@NonNull View itemView) {
        super(itemView);
        civFotoPerfil=itemView.findViewById(R.id.civFotoPerfil);
        txtNombreUsuario=itemView.findViewById(R.id.txtNombreUsuario);
        layoutContactosChat=itemView.findViewById(R.id.layoutContactosChat);


    }

    public CircleImageView getCivFotoPerfil() {
        return civFotoPerfil;
    }

    public void setCivFotoPerfil(CircleImageView civFotoPerfil) {
        this.civFotoPerfil = civFotoPerfil;
    }

    public TextView getTxtNombreUsuario() {
        return txtNombreUsuario;
    }

    public void setTxtNombreUsuario(TextView txtNombreUsuario) {
        this.txtNombreUsuario = txtNombreUsuario;
    }

    public LinearLayout getLayoutContactosChat() {
        return layoutContactosChat;
    }

    public void setLayoutContactosChat(LinearLayout layoutContactosChat) {
        this.layoutContactosChat = layoutContactosChat;
    }
}
