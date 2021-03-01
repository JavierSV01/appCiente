package com.example.aplicacioncliente.controlador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicacioncliente.R;
import com.example.aplicacioncliente.modelos.Linea_Pedido;
import com.example.aplicacioncliente.modelos.Pedido;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorLineasEnDetalle extends RecyclerView.Adapter<AdaptadorLineasEnDetalle.AdaptadorViewHolder> {
    public Context contexto;
    private Pedido pedido;
    private List<Linea_Pedido> listaLineasPedido = new ArrayList<>();
    private FirebaseDatabase database;

    public AdaptadorLineasEnDetalle(Context contexto, List<Linea_Pedido> listaLineasPedido, Pedido pedido) {
        this.listaLineasPedido = listaLineasPedido;
        this.contexto = contexto;
        this.pedido = pedido;
        database = FirebaseDatabase.getInstance();
    }

    @NonNull
    @Override
    public AdaptadorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_linea_en_detalle, parent, false);
        AdaptadorViewHolder adaptador = new AdaptadorViewHolder(itemView);
        return adaptador;
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorViewHolder holder, int position) {
        Linea_Pedido linea = listaLineasPedido.get(position);

        try {
            holder.setMyRefLinea(database.getReference("pedidos").child("lineasPedido").child(linea.getIdLinea()));
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
        private Button btBorrarLinea;
        private DatabaseReference myRefLinea;

        public AdaptadorViewHolder(@NonNull View itemView) {
            super(itemView);
            txCantidad = itemView.findViewById(R.id.txCantidad);
            txIdProducto = itemView.findViewById(R.id.txIdProductoLinea);
            txSubtotal = itemView.findViewById(R.id.txSubtotal);
            btBorrarLinea = itemView.findViewById(R.id.btBorrarLinea);
            btBorrarLinea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!pedido.getEstado().equals("apuntado")) {
                        Toast.makeText(contexto, "No puedes modificar el pedido si ha pasado de apuntado", Toast.LENGTH_LONG);
                    } else {
                        Toast.makeText(contexto, "Linea borrada", Toast.LENGTH_LONG);
                        getMyRefLinea().setValue(null);
                    }
                }
            });
        }

        public DatabaseReference getMyRefLinea() {
            return myRefLinea;
        }

        public void setMyRefLinea(DatabaseReference myRefLinea) {
            this.myRefLinea = myRefLinea;
        }
    }
}

