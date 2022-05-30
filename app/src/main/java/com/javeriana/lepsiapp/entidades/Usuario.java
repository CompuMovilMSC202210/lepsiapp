package com.javeriana.lepsiapp.entidades;


public class Usuario {
    private String userName;
    private String email;
    private String rol;

    public String getFotoPerfilURL() {
        return fotoPerfilURL;
    }

    public void setFotoPerfilURL(String fotoPerfilURL) {
        this.fotoPerfilURL = fotoPerfilURL;
    }

    private String fotoPerfilURL;

    public Usuario() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() { return rol; }

    public void setRol(String rol) {
        this.rol = rol;
    }
}



