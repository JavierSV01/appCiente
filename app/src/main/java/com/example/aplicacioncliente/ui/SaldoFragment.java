package com.example.aplicacioncliente.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.aplicacioncliente.R;
import com.example.aplicacioncliente.modelos.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SaldoFragment extends Fragment {

    TextView txSaldo;
    Usuario usuarioActual;
    String uidUsuarioActual;
    Button btAñadir, btRetirar;
    EditText txRetirar, txAñadir;

    FirebaseDatabase database;
    DatabaseReference myRef;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_saldo, container, false);
        database = FirebaseDatabase.getInstance();
        uidUsuarioActual = FirebaseAuth.getInstance().getCurrentUser().getUid();
        myRef = database.getReference("usuarios").child(uidUsuarioActual);
        lecturaUsuarioActual();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txSaldo = view.findViewById(R.id.txSaldo);
        btAñadir = view.findViewById(R.id.btAñadir);
        btRetirar = view.findViewById(R.id.btRetirar);
        txAñadir = view.findViewById(R.id.txAñadir);
        txRetirar = view.findViewById(R.id.txRetirar);
        botones();

    }

    public void mostrarDatos() {
        txSaldo.setText(Float.toString(usuarioActual.getSaldo()));
    }

    public void botones() {
        btAñadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!txAñadir.getText().toString().equals("")) {
                    Float cantidadAñadir = Float.parseFloat(txAñadir.getText().toString());
                    if (cantidadAñadir >= 0) {
                        usuarioActual.setSaldo(usuarioActual.getSaldo() + cantidadAñadir);
                        myRef.setValue(usuarioActual);
                    } else {
                        Toast.makeText(getContext(), "Introduce una cantidad positiva", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Introduce la cantidad a añadir", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btRetirar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!txRetirar.getText().toString().equals("")) {
                    Float cantidadRetirar = Float.parseFloat(txRetirar.getText().toString());
                    if (usuarioActual.getSaldo() >0) {
                        usuarioActual.setSaldo(usuarioActual.getSaldo() - cantidadRetirar);
                        myRef.setValue(usuarioActual);
                    } else {
                        Toast.makeText(getContext(), "No puedes retirar dinero si estas en negativo", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Introduce la cantidad a retirar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void lecturaUsuarioActual() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usuarioActual = dataSnapshot.getValue(Usuario.class);
                mostrarDatos();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        myRef.addValueEventListener(postListener);
    }
}