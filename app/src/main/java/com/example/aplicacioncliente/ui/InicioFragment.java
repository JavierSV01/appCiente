package com.example.aplicacioncliente.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.aplicacioncliente.R;
import com.example.aplicacioncliente.modelos.Producto;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InicioFragment extends Fragment {
    FirebaseDatabase database;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_inicio, container, false);


        DatabaseReference myRefProductos;
        database = FirebaseDatabase.getInstance();
        myRefProductos = database.getReference("productos");
        Producto p = new Producto(1, "1", "salchichca", "perrito caliente", 1.1,"comida", 100, 21, "");

        for (int i = 0; i < 20; i++) {
            myRefProductos.child(String.valueOf(i)).setValue(p);
        }
        return root;
    }
}