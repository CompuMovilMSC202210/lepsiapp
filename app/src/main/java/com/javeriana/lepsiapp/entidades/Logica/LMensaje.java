package com.javeriana.lepsiapp.entidades.Logica;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.javeriana.lepsiapp.entidades.Mensaje;

import org.ocpsoft.prettytime.PrettyTime;

/**
 * Created by user on 28/08/2018. 28
 */
public class LMensaje {

    private String key;
    private Mensaje mensaje;
    private LUsuario lUsuario;


    public LMensaje(String key, Mensaje mensaje) {
        this.key = key;
        this.mensaje = mensaje;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Mensaje getMensaje() {
        return mensaje;
    }

    public void setMensaje(Mensaje mensaje) {
        this.mensaje = mensaje;
    }

    public long getCreatedTimestampLong() {
        return (long) mensaje.getCreatedTimestamp();
    }

    public LUsuario getlUsuario() {
        return lUsuario;
    }

    public void setlUsuario(LUsuario lUsuario) {
        this.lUsuario = lUsuario;
    }

    public String fechaDeCreacionDelMensaje(){
        Date date = new Date(getCreatedTimestampLong());
        PrettyTime prettyTime = new PrettyTime(new Date(),Locale.getDefault());
        return prettyTime.format(date);}



}
