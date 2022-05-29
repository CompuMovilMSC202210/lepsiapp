package com.javeriana.lepsiapp.Holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.javeriana.lepsiapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsuarioViewHolder extends RecyclerView.ViewHolder {

private CircleImageView civFotoPerfil;
private TextView txtNombreUsuario;


    public UsuarioViewHolder(@NonNull View itemView) {
        super(itemView);
        civFotoPerfil=itemView.findViewById(R.id.civFotoPerfil);
        txtNombreUsuario=itemView.findViewById(R.id.txtNombreUsuario);


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

}
