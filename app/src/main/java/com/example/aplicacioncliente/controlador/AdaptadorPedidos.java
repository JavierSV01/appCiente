package com.example.aplicacioncliente.controlador;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aplicacioncliente.R;
import com.example.aplicacioncliente.modelos.Linea_Pedido;
import com.example.aplicacioncliente.modelos.Pedido;
import com.example.aplicacioncliente.modelos.Producto;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorPedidos extends RecyclerView.Adapter<AdaptadorPedidos.AdaptadorViewHolder> {
    public List<Pedido> listaPedidos = new ArrayList<>();
    public Context contexto;

    public AdaptadorPedidos(List<Pedido> lista, Context contexto) {
        this.listaPedidos = lista;
        this.contexto = contexto;
    }

    @NonNull
    @Override
    public AdaptadorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido, parent, false);
        AdaptadorViewHolder adaptador = new AdaptadorViewHolder(itemView);
        return adaptador;
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorViewHolder holder, int position) {
        Pedido pedido = listaPedidos.get(position);
        try {
            holder.txtTipo.setText(pedido.getEntregaPedido());
            holder.txtId.setText(String.valueOf(pedido.getIdPedido()));
            holder.txtFecha.setText(pedido.getFecha().getDate() + "/" + (pedido.getFecha().getMonth() + 1) + "/" + (pedido.getFecha().getYear() + 1900));
            holder.txtEstado.setText(pedido.getEstado());

        } catch (NullPointerException ex) {

        }
    }

    @Override
    public int getItemCount() {
        return listaPedidos.size();
    }

    public class AdaptadorViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTipo, txtId, txtFecha, txtEstado;

        public AdaptadorViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFecha = itemView.findViewById(R.id.txFechaPedido);
            txtTipo = itemView.findViewById(R.id.txTipo);
            txtId = itemView.findViewById(R.id.txIdPEdido);
            txtEstado = itemView.findViewById(R.id.txEstadoPedido);
        }
    }
}



