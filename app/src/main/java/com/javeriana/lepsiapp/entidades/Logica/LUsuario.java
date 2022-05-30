package com.javeriana.lepsiapp.entidades.Logica;

import com.google.firebase.database.Exclude;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.javeriana.lepsiapp.entidades.Usuario;

/**
 * Created by user on 28/08/2018. 28
 */
public class LUsuario {

    private String key;
    private Usuario usuario;
    public LUsuario(String key, Usuario usuario) {
        this.key = key;
        this.usuario = usuario;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }



}


