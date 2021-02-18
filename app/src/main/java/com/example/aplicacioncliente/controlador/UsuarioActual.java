package com.example.aplicacioncliente.controlador;

import android.widget.EditText;

import com.example.aplicacioncliente.modelos.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UsuarioActual {
    Usuario usuarioActual;
    String uidUsuarioActual;


    FirebaseDatabase database;
    DatabaseReference myRef;

    public UsuarioActual(){
        this.database = FirebaseDatabase.getInstance();
        this.uidUsuarioActual = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.myRef = database.getReference("usuarios").child(uidUsuarioActual);

    }

    public void lecturaUsuarioActual() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usuarioActual = dataSnapshot.getValue(Usuario.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        myRef.addValueEventListener(postListener);
    }

}
