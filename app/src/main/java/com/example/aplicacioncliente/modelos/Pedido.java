package com.example.aplicacioncliente.modelos;

import java.io.Serializable;
import java.util.Date;

public class Pedido implements Serializable {

    private String idPedido;
    private String uidUsuario;
    private String estado;
    private Date fecha;
    private String entregaPedido;  // SE ASIGNARA CON tienda (se retirara de tienda) o domicilio (si se le lleva al domicilio)

    public Pedido() {
    }

    public Pedido(String idPedido, String uidUsuario, String estado, Date fecha, String entregaPedido) {
        this.idPedido = idPedido;
        this.uidUsuario = uidUsuario;
        this.estado = estado;
        this.fecha = fecha;
        this.entregaPedido = entregaPedido;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public String getUidUsuario() {
        return uidUsuario;
    }

    public void setUidUsuario(String uidUsuario) {
        this.uidUsuario = uidUsuario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getEntregaPedido() {
        return entregaPedido;
    }

    public void setEntregaPedido(String entregaPedido) {
        this.entregaPedido = entregaPedido;
    }

    @Override
    public String toString() {
        return this.idPedido;
    }
}
