package com.javeriana.lepsiapp.data.model;


public class arreglocont {

    private String Uid;
    private String Evento;
    private String Fecha;
    private String Fuente;

    public arreglocont(String evento, String fecha, String fuente) {
        Evento = evento;
        Fecha = fecha;
        Fuente = fuente;
    }

    public String getUid() { return Uid; }
    public void setUid(String uid) {this.Uid = uid; }

    public String getEvento() {  return Evento; }
    public void setEvento(String evento) { Evento = evento; }

    public String getFecha() { return Fecha;}
    public void setFecha(String fecha) {this.Fecha = fecha;}

    public String getFuente() {return Fuente;}
    public void setFuente(String fuente) {this.Fuente = fuente;}

}
