package com.example.aplicacioncliente.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicacioncliente.FinalizarPedido;
import com.example.aplicacioncliente.R;
import com.example.aplicacioncliente.controlador.ProductosAdapter;
import com.example.aplicacioncliente.modelos.Comercio;
import com.example.aplicacioncliente.modelos.Linea_Pedido;
import com.example.aplicacioncliente.modelos.Pedido;
import com.example.aplicacioncliente.modelos.Producto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class RealizarPedidoFragment extends Fragment {

    ArrayList<Comercio> listaComercios = new ArrayList<>();
    ArrayList<Producto> listaProductos = new ArrayList<>();
    ArrayList<Linea_Pedido> listaLineas = new ArrayList<>();

    FirebaseDatabase database;
    DatabaseReference myRefComercios;
    DatabaseReference myRefProductos;
    DatabaseReference myRefPedidos;

    Button btCarrito;

    private RecyclerView.Adapter adaptador;
    private RecyclerView rvProductos;
    private RecyclerView.LayoutManager layoutManager;

    View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_realizarpedido, container, false);

        database = FirebaseDatabase.getInstance();
        myRefComercios = database.getReference("comercios");
        myRefProductos = database.getReference("productos");
        myRefPedidos = database.getReference("pedidos");
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lecturaDatos();
        this.view = view;
        btCarrito = view.findViewById(R.id.btCarrito);


        btCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listaLineas.size() > 0){
                    String pedidoKey = myRefPedidos.child("lineasPedido").push().getKey();

                    for (int i = 0; i < listaLineas.size(); i++) {
                        listaLineas.get(i).setIdPedido(pedidoKey);
                        myRefPedidos.child("lineasPedido").push().setValue(listaLineas.get(i));
                    }
                    Intent i = new Intent(getContext(), FinalizarPedido.class);
                    Pedido pedido = new Pedido(pedidoKey, FirebaseAuth.getInstance().getUid(), "Apuntado", new Date(), "");
                    i.putExtra("pedido", pedido);
                    startActivity(i);
                }else{
                    Toast.makeText(getContext(), "Ddebes añadir productos al carrito", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void lecturaDatos() {
        ValueEventListener postListenerComercios = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listaComercios.clear();

                for (DataSnapshot xComercio : dataSnapshot.getChildren()) {
                    Comercio c = xComercio.getValue(Comercio.class);
                    listaComercios.add(c);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        ValueEventListener postListenerProductos = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listaProductos.clear();

                for (DataSnapshot xProducto : dataSnapshot.getChildren()) {
                    Producto p = xProducto.getValue(Producto.class);
                    System.out.println(p.getNombre());
                    listaProductos.add(p);
                }

                lanzarRV();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        myRefComercios.addValueEventListener(postListenerComercios);
        myRefProductos.addValueEventListener(postListenerProductos);
    }

    void lanzarRV() {
        try {
            rvProductos = view.findViewById(R.id.recyclerViewProductos);
            layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            rvProductos.setLayoutManager(layoutManager);
            adaptador = new ProductosAdapter(listaProductos, getContext(), listaLineas);
            rvProductos.setAdapter(adaptador);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*
    void cargarProductos(ArrayList<Comercio> listaComercios){
        for (int i = 0; i <listaComercios.size(); i++) {
            for (int j = 0; j < listaComercios.get(i).getProducto(); j++) {
                listaProductos.add(lista)
            }
        }
    }
    */


}