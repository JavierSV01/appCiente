package com.example.aplicacioncliente.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aplicacioncliente.R;
import com.example.aplicacioncliente.controlador.AdaptadorPedidos;
import com.example.aplicacioncliente.controlador.ProductosAdapter;
import com.example.aplicacioncliente.modelos.Pedido;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MisPedidosFragment extends Fragment {

    ArrayList<Pedido> listaPedidos = new ArrayList<>();

    FirebaseDatabase database;
    DatabaseReference myRefPedido;

    View view;

    private RecyclerView.Adapter adaptador;
    private RecyclerView rvPedidos;
    private RecyclerView.LayoutManager layoutManager;

    public MisPedidosFragment() {
        // Required empty public constructor
    }

    public static MisPedidosFragment newInstance(String param1, String param2) {
        MisPedidosFragment fragment = new MisPedidosFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        myRefPedido = database.getReference("pedidos").child("pedido");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mis_pedidos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        lecturaDatos();
    }

    private void lecturaDatos() {

        ValueEventListener postListenerPedido = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listaPedidos.clear();

                for (DataSnapshot xPedido : dataSnapshot.getChildren()) {
                    Pedido p = xPedido.getValue(Pedido.class);
                    if (p.getUidUsuario().equals(FirebaseAuth.getInstance().getUid())){
                        listaPedidos.add(p);
                    }
                }
                lanzarRV();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        myRefPedido.addValueEventListener(postListenerPedido);
    }

    void lanzarRV(){

        rvPedidos = view.findViewById(R.id.rvMisPedidos);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvPedidos.setLayoutManager(layoutManager);
        adaptador = new AdaptadorPedidos(listaPedidos, getContext());
        rvPedidos.setAdapter(adaptador);

    }


}