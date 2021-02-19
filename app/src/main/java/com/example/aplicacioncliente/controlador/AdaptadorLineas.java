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
import com.example.aplicacioncliente.modelos.Producto;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorLineas extends RecyclerView.Adapter<AdaptadorLineas.AdaptadorViewHolder> {
    public Context contexto;
    private List<Linea_Pedido> listaLineasPedido = new ArrayList<>();

    public AdaptadorLineas(Context contexto, List<Linea_Pedido> listaLineasPedido) {
        this.listaLineasPedido = listaLineasPedido;
        this.contexto = contexto;
    }

    @NonNull
    @Override
    public AdaptadorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_linea, parent, false);
        AdaptadorViewHolder adaptador = new AdaptadorViewHolder(itemView);
        return adaptador;
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorViewHolder holder, int position) {
        Linea_Pedido linea = listaLineasPedido.get(position);
        try {
            holder.txSubtotal.setText(String.valueOf(linea.getSubtotalLinea()));
            holder.txCantidad.setText(String.valueOf(linea.getCantidadProducto()));
            holder.txIdProducto.setText(String.valueOf(linea.getIdProducto()));
        } catch (NullPointerException ex) {

        }
    }

    @Override
    public int getItemCount() {
        return listaLineasPedido.size();
    }

    public class AdaptadorViewHolder extends RecyclerView.ViewHolder {
        private TextView txIdProducto, txSubtotal, txCantidad;

        public AdaptadorViewHolder(@NonNull View itemView) {
            super(itemView);
            txCantidad = itemView.findViewById(R.id.txCantidadLinea);
            txIdProducto = itemView.findViewById(R.id.txId);
            txSubtotal = itemView.findViewById(R.id.txSubtotal);
        }


    }
}



