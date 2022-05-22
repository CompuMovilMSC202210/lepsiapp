package com.javeriana.lepsiapp.entidades;


public class ContactUsuario {
    private String nombre;
    private String correo;
    private String rol;
    private String id;
    public ContactUsuario() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getRol() { return rol; }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getId(){ return id; }

    public void setId(String is) {
        this.id = id;
    }
}
