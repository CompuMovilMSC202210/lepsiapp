package com.javeriana.lepsiapp.entidades;


public class ContactUsuario {
    private String userName;
    private String email;
    private String userPhone;
    private String rol;
    private String id;
    private String pacid;
    public ContactUsuario() {
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

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getRol() { return rol; }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getId(){ return id; }

    public void setId(String is) {
        this.id = id;
    }

    public String getPacId(){ return pacid; }

    public void setPacId(String pacid) {
        this.pacid = pacid;
    }
}
