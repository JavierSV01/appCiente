package com.example.aplicacioncliente.modelos;
import java.io.Serializable;

public class Linea_Pedido implements Serializable {

    private String idPedido;
    private String idLinea;
    private int idProducto;
    private int idComercio;
    private int cantidadProducto;
    private float subtotalLinea;

    public Linea_Pedido(String idPedido, String idLinea, int idProducto, int idComercio, int cantidadProducto, float subtotalLinea) {
        this.idPedido = idPedido;
        this.idLinea = idLinea;
        this.idProducto = idProducto;
        this.idComercio = idComercio;
        this.cantidadProducto = cantidadProducto;
        this.subtotalLinea = subtotalLinea;
    }
    public Linea_Pedido(String idLinea, int idProducto, int cantidadProducto, float subtotalLinea) {
        this.idLinea = idLinea;
        this.idProducto = idProducto;
        this.cantidadProducto = cantidadProducto;
        this.subtotalLinea = subtotalLinea;
    }

    public Linea_Pedido() {
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public String getIdLinea() {
        return idLinea;
    }

    public void setIdLinea(String idLinea) {
        this.idLinea = idLinea;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdComercio() {
        return idComercio;
    }

    public void setIdComercio(int idComercio) {
        this.idComercio = idComercio;
    }

    public int getCantidadProducto() {
        return cantidadProducto;
    }

    public void setCantidadProducto(int cantidadProducto) {
        this.cantidadProducto = cantidadProducto;
    }

    public float getSubtotalLinea() {
        return subtotalLinea;
    }

    public void setSubtotalLinea(float subtotalLinea) {
        this.subtotalLinea = subtotalLinea;
    }

    @Override
    public String toString() {
        return this.idLinea;
    }
}

