package com.example.aplicacioncliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.aplicacioncliente.controlador.AdaptadorLineas;
import com.example.aplicacioncliente.modelos.Linea_Pedido;
import com.example.aplicacioncliente.modelos.Pedido;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FinalizarPedido extends AppCompatActivity {

    Pedido pedido;

    ArrayList<Linea_Pedido> listaLineas = new ArrayList<>();

    Button btRealizarPedido;
    RadioButton rdDomicilio;
    RadioButton rdRecogida;

    FirebaseDatabase database;
    DatabaseReference myRefLineas;
    DatabaseReference myRefPedidos;

    private RecyclerView.Adapter adaptador;
    private RecyclerView rvLineas;
    private RecyclerView.LayoutManager layoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle parametros = this.getIntent().getExtras();
        if (parametros != null){
            pedido = (Pedido) parametros.getSerializable("pedido");
        }else{
            Toast.makeText(this, "Sin productos", Toast.LENGTH_SHORT).show();
            finish();
        }
        database = FirebaseDatabase.getInstance();
        myRefLineas = database.getReference("pedidos").child("lineasPedido");
        myRefPedidos= database.getReference("pedidos").child("pedido");
        setContentView(R.layout.activity_finalizar_pedido);
    }

    @Override
    protected void onResume() {
        super.onResume();
        lecturaDatos();
    }

    @Override
    protected void onStart() {
        super.onStart();
        btRealizarPedido = findViewById(R.id.btRealizarPedido);
        rdDomicilio = findViewById(R.id.rdEntrega);
        rdRecogida = findViewById(R.id.rdRecogida);

        btRealizarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rdDomicilio.isChecked()){
                    pedido.setEntregaPedido("domicilio");
                    myRefPedidos.push().setValue(pedido);
                }
                if (rdRecogida.isChecked()){
                    pedido.setEntregaPedido("recogida");
                    myRefPedidos.push().setValue(pedido);
                }
            }
        });
    }


    private void lecturaDatos() {
        ValueEventListener postListenerLineas = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listaLineas.clear();
                for (DataSnapshot xLineas : dataSnapshot.getChildren()) {
                    Linea_Pedido p = xLineas.getValue(Linea_Pedido.class);
                    System.out.println("id pedido " + pedido.getIdPedido());
                    System.out.println("id linea " + p.getIdPedido());
                    if (pedido.getIdPedido().equals(p.getIdPedido())){
                        listaLineas.add(p);
                    }

                }
                lanzarRV();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        myRefLineas.addValueEventListener(postListenerLineas);
    }

    void lanzarRV() {
        try {
            rvLineas = findViewById(R.id.rvLineas);
            layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            rvLineas.setLayoutManager(layoutManager);
            adaptador = new AdaptadorLineas(this, listaLineas);
            rvLineas.setAdapter(adaptador);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


}