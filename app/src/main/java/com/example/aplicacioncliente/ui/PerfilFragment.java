package com.example.aplicacioncliente.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.aplicacioncliente.R;
import com.example.aplicacioncliente.modelos.Usuario;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class PerfilFragment extends Fragment {

    final int SELECCIONAR_FOTO = 1;
    Usuario usuarioActual;
    String uidUsuarioActual;
    EditText txNombre, txDireccion, txTelefono;
    Button btGuardarCambios;
    ImageButton btCambiarFoto;
    CircleImageView imageView;


    String fotoUri;

    FirebaseDatabase database;
    DatabaseReference myRef;


    FirebaseStorage storage;
    StorageReference storageRef;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        database = FirebaseDatabase.getInstance();
        uidUsuarioActual = FirebaseAuth.getInstance().getCurrentUser().getUid();
        myRef = database.getReference("usuarios").child(uidUsuarioActual);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference("usuarios");

        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btCambiarFoto = view.findViewById(R.id.btCambiar);
        btGuardarCambios = view.findViewById(R.id.btGuardarCambios);
        txNombre = view.findViewById(R.id.txNombre);
        txDireccion = view.findViewById(R.id.txDireccion);
        txTelefono = view.findViewById(R.id.txTelefono);
        imageView = view.findViewById(R.id.fotoPerfil);
        botones();
        lecturaUsuarioActual();
    }

    void botones(){
        btGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!txNombre.equals("")){
                    usuarioActual.setNombre(txNombre.getText().toString());
                }
                if (!txDireccion.equals("")){
                    usuarioActual.setDireccion(txDireccion.getText().toString());
                }
                if (!txTelefono.equals("")){
                    usuarioActual.setTelefono(txTelefono.getText().toString());
                }

                myRef.setValue(usuarioActual);
            }
        });

        btCambiarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i, SELECCIONAR_FOTO);
            }
        });
    }

    void mostrarDatos(){
        if (usuarioActual.getNombre() != null){
            txNombre.setText(usuarioActual.getNombre());
        }
        if (usuarioActual.getDireccion() != null){
            txDireccion.setText(usuarioActual.getDireccion());
        }
        if (usuarioActual.getTelefono() != null){
            txTelefono.setText(usuarioActual.getTelefono());
        }
        if (usuarioActual.getFotoUsuario() != null && !usuarioActual.getFotoUsuario().equals("")){
            Glide.with(this).load(usuarioActual.getFotoUsuario()).into(imageView);
        }

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


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECCIONAR_FOTO && resultCode == RESULT_OK){
            Uri fotoUriAux = data.getData();
            final StorageReference fotoRef = storageRef.child(fotoUriAux.getLastPathSegment());
            fotoRef.putFile(fotoUriAux).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }

                    return fotoRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        fotoUri = task.getResult().toString();
                        usuarioActual.setFotoUsuario(fotoUri);
                        myRef.setValue(usuarioActual);
                        Toast.makeText(getContext(), "Foto cargada correctamente", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}