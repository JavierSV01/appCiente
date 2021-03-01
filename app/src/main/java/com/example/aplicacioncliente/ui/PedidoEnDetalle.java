package com.example.aplicacioncliente.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.aplicacioncliente.R;
import com.example.aplicacioncliente.controlador.AdaptadorLineas;
import com.example.aplicacioncliente.controlador.AdaptadorLineasEnDetalle;
import com.example.aplicacioncliente.modelos.Linea_Pedido;
import com.example.aplicacioncliente.modelos.Pedido;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;

public class PedidoEnDetalle extends AppCompatActivity {

    Button btEliminarPedido;
    Pedido pedido;
    ArrayList<Linea_Pedido> listaLineas = new ArrayList<>();

    FirebaseDatabase database;
    DatabaseReference myRefPedidos;

    private RecyclerView.Adapter adaptador;
    private RecyclerView rvLineasEnDetalle;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle parametros = this.getIntent().getExtras();

        database = FirebaseDatabase.getInstance();
        myRefPedidos = database.getReference("pedidos").child("lineasPedido");

        if (parametros != null) {
            pedido = (Pedido) parametros.getSerializable("pedido");

        } else {
            Toast.makeText(this, "Sin productos", Toast.LENGTH_SHORT).show();
            finish();
        }
        cargarLineas();
        setContentView(R.layout.activity_pedido_en_detalle);

    }

    @Override
    protected void onStart() {
        super.onStart();
        btEliminarPedido = findViewById(R.id.btEliminarPedido);
        btEliminarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!pedido.getEstado().equals("apuntado")) {
                    Toast.makeText(PedidoEnDetalle.this, "No puedes borrar el pedido si ha pasado de apuntado", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < listaLineas.size(); i++) {
                        database.getReference("pedidos").child("lineasPedido").child(listaLineas.get(i).getIdLinea()).setValue(null);
                    }
                    database.getReference("pedidos").child("pedido").child(pedido.getIdPedido()).setValue(null);
                    Toast.makeText(PedidoEnDetalle.this, "Pedido borrado", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }



    void cargarLineas() {
        ValueEventListener ev = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaLineas.clear();
                for (DataSnapshot xLineas : snapshot.getChildren()) {
                    Linea_Pedido p = xLineas.getValue(Linea_Pedido.class);
                    if (pedido.getIdPedido().equals(p.getIdPedido())) {
                        listaLineas.add(p);
                    }

                }
                lanzarRV();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        myRefPedidos.addValueEventListener(ev);
    }


    void lanzarRV() {
        try {
            rvLineasEnDetalle = findViewById(R.id.rvLineasPedidoEnDetalle);
            layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            rvLineasEnDetalle.setLayoutManager(layoutManager);
            adaptador = new AdaptadorLineasEnDetalle(getApplicationContext(), listaLineas, pedido);
            rvLineasEnDetalle.setAdapter(adaptador);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}