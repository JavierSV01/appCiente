package com.example.aplicacioncliente.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.List;

public class RealizarPedidoFragment extends Fragment {

    ArrayList<Comercio> listaComercios = new ArrayList<>();
    ArrayList<Producto> listaProductos = new ArrayList<>();
    ArrayList<List<Integer>> listaProductosComercios = new ArrayList<>();
    ArrayList<Linea_Pedido> listaLineas = new ArrayList<>();

    FirebaseDatabase database;
    DatabaseReference myRefComercios;
    DatabaseReference myRefProductos;
    DatabaseReference myRefPedidos;

    Button btCarrito;
    Spinner spFiltroTiendas;
    ArrayList<String> listaTiendasSpinner = new ArrayList<>();

    private RecyclerView.Adapter adaptador;
    private RecyclerView rvProductos;
    private RecyclerView.LayoutManager layoutManager;

    Pedido pedido;
    String pedidoKey;

    View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_realizarpedido, container, false);

        database = FirebaseDatabase.getInstance();
        myRefComercios = database.getReference("comercios");
        myRefProductos = database.getReference("productos");
        myRefPedidos = database.getReference("pedidos");
        pedidoKey = myRefPedidos.child("lineasPedido").push().getKey();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spFiltroTiendas = view.findViewById(R.id.spFiltroTienda);
        lecturaDatos();
        this.view = view;
        btCarrito = view.findViewById(R.id.btCarrito);


        btCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listaLineas.size() > 0) {
                    for (int i = 0; i < listaLineas.size(); i++) {
                        String keyLinea = myRefPedidos.child("lineasPedido").push().getKey();
                        listaLineas.get(i).setIdLinea(keyLinea);
                        listaLineas.get(i).setIdPedido(pedidoKey);
                        myRefPedidos.child("lineasPedido").child(keyLinea).setValue(listaLineas.get(i));
                    }
                    Intent i = new Intent(getContext(), FinalizarPedido.class);
                    pedido = new Pedido(pedidoKey, FirebaseAuth.getInstance().getUid(), "apuntado", new Date(), "");
                    i.putExtra("pedido", pedido);
                    startActivity(i);
                } else {
                    Toast.makeText(getContext(), "Debes añadir productos al carrito", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void lecturaDatos() {
        ValueEventListener postListenerComercios = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listaComercios.clear();
                listaProductosComercios.clear();
                for (DataSnapshot xComercio : dataSnapshot.getChildren()) {
                    Comercio c = xComercio.getValue(Comercio.class);
                    List<Integer> listaProductos = new ArrayList<>();
                    for (DataSnapshot xProductos : xComercio.child("producto").getChildren()){
                        listaProductos.add(Integer.parseInt("" + xProductos.getValue()));
                    }
                    c.setIdProducto(listaProductos);
                    listaComercios.add(c);

                }



                listaTiendasSpinner.clear();
                listaTiendasSpinner.add("Todas las tiendas");
                for (int i = 0; i < listaComercios.size(); i++) {
                    listaTiendasSpinner.add(listaComercios.get(i).getNombre());
                }
                cargarSpinner();
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
                    listaProductos.add(p);
                }

                lanzarRV(listaProductos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        myRefComercios.addValueEventListener(postListenerComercios);
        myRefProductos.addValueEventListener(postListenerProductos);
    }

    void lanzarRV(ArrayList<Producto> listaProductos) {
        try {
            rvProductos = view.findViewById(R.id.recyclerViewProductos);
            layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            rvProductos.setLayoutManager(layoutManager);
            adaptador = new ProductosAdapter(listaProductos, getContext(), listaLineas, pedidoKey);
            rvProductos.setAdapter(adaptador);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    void cargarSpinner() {

        ArrayAdapter<String> a = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, listaTiendasSpinner);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFiltroTiendas.setAdapter(a);

        spFiltroTiendas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ArrayList<Producto> listaProductosAux = new ArrayList<>();
                String tiendaSeleccionada = spFiltroTiendas.getSelectedItem().toString();

                for (int i = 0; i < listaComercios.size(); i++) {   //recorro los comercios
                    if (listaComercios.get(i).getNombre().equals(tiendaSeleccionada)) {//Encuentro el seleccionado
                        if (listaComercios.get(i).getProducto() != null) {
                            for (int j = 0; j < listaComercios.get(i).getProducto().size(); j++) {  //Recorro sus productos
                                for (int k = 0; k < listaProductos.size(); k++) {   //Reccorro todos los prodcutos cargador
                                    if (listaProductos.get(k).getIdProducto() == listaComercios.get(i).getProducto().get(j)) {   //Guardo solo los de ese comercio
                                        listaProductosAux.add(listaProductos.get(k));
                                    }
                                }
                            }
                        }
                    }
                }

                if (tiendaSeleccionada.equals("Todas las tiendas")) {
                    lanzarRV(listaProductos);
                } else {
                    lanzarRV(listaProductosAux);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


}