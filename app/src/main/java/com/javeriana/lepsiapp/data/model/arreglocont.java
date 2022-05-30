package com.javeriana.lepsiapp.data.model;


import org.osmdroid.util.GeoPoint;

public class arreglocont {

    private String Uid;
    private String Evento;
    private String Fecha;
    private String Fuente;
    private String Userid;
    private String Ubica;

    public arreglocont() {
            }

    public String getUid() { return Uid; }
    public void setUid(String uid) {this.Uid = uid; }

    public String getEvento() {  return Evento; }
    public void setEvento(String evento) { Evento = evento; }

    public String getFecha() { return Fecha;}
    public void setFecha(String fecha) {this.Fecha = fecha;}

    public String getFuente() {return Fuente;}
    public void setFuente(String fuente) {this.Fuente = fuente;}

    public String getUserid() {return Userid;}
    public void setUserid(String userid) { this.Userid = userid;}

    public String getUbica() {return Ubica;}
    public void setUbica(String ubica) { this.Ubica = ubica;}
}
