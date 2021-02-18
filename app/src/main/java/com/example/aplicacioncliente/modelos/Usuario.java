package com.example.aplicacioncliente.modelos;

import java.io.Serializable;

public class Usuario implements Serializable {
    private String userId;
    private String email;
    private String nombre;
    private float saldo;
    private String direccion;
    private String telefono;
    private String fotoUsuario;


    public Usuario(String userId, String email, String nombre, float saldo, String direccion, String telefono, String fotoUsuario) {
        this.userId = userId;
        this.email = email;
        this.nombre = nombre;
        this.saldo = saldo;
        this.direccion = direccion;
        this.telefono = telefono;
        this.fotoUsuario = fotoUsuario;
    }

    public Usuario(String userId, String email, float saldo) {
        this.userId = userId;
        this.email = email;
        this.saldo = saldo;
        this.telefono = "null";
        this.nombre = "null";
        this.direccion = "null";
        this.fotoUsuario ="null";
    }

    public Usuario() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFotoUsuario() {
        return fotoUsuario;
    }

    public void setFotoUsuario(String fotoUsuario) {
        this.fotoUsuario = fotoUsuario;
    }


    @Override
    public String toString() {
        return this.userId;
    }
}
