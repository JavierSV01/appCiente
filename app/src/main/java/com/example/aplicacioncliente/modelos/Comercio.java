package com.example.aplicacioncliente.modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Comercio implements Serializable {

    private int id;
    private String nombre;
    private String direccion;
    private String categoria;
    private List<Integer> productos;
    private String fotoComercio;
    private String telefono;

    public Comercio() {
    }

    public Comercio(int id, String nombre, String direccion, String categoria, List<Integer> productos, String fotoComercio, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.categoria = categoria;
        this.productos = productos;
        this.fotoComercio = fotoComercio;
        this.telefono = telefono;
    }

    public Comercio(int id, String nombre, String direccion, String categoria, String fotoComercio, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.categoria = categoria;
        this.productos = new ArrayList<>();
        this.fotoComercio = fotoComercio;
        this.telefono = telefono;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public List<Integer> getProducto() {
        return productos;
    }

    public void setIdProducto(List<Integer> productos) {
        this.productos = productos;
    }

    public String getFotoComercio() {
        return fotoComercio;
    }

    public void setFotoComercio(String fotoComercio) {
        this.fotoComercio = fotoComercio;
    }

    @Override
    public String toString() {
        return Integer.toString(this.id);
    }
}

