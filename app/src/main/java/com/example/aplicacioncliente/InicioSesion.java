package com.example.aplicacioncliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplicacioncliente.modelos.Usuario;
import com.example.aplicacioncliente.ui.MainActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InicioSesion extends AppCompatActivity {

    Button btAcceder, btRegistrar;
    SignInButton loginGoogle;
    TextView txEmail, txContraseña;
    Context contexto;

    FirebaseDatabase database;
    DatabaseReference myRef;

    final int RC_SIGN_IN = 123;

    public static List<Usuario> listaUsuarios = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        contexto = this;

        loginGoogle = findViewById(R.id.btLoginGoogle);
        btAcceder = findViewById(R.id.btIniciarSesion);
        btRegistrar = findViewById(R.id.btCrearUsuario);
        txEmail = findViewById(R.id.txCorreo);
        txContraseña = findViewById(R.id.txContraseña);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("usuarios");

        lecturaBD();

    }

    @Override
    protected void onStart() {
        super.onStart();
        correoLogin();
        googleLogin();
    }

    public void correoLogin() {
        btRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!txContraseña.getText().toString().equals("") && !txEmail.getText().toString().equals("")) {
                    registrarCuenta();
                } else {
                    Toast.makeText(contexto, "Introduce el email y la contraseña", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btAcceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!txContraseña.getText().toString().equals("") && !txEmail.getText().toString().equals("")) {
                    accederCuenta();
                } else {
                    Toast.makeText(contexto, "Introduce el email y la contraseña", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void registrarCuenta() {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(txEmail.getText().toString(), txContraseña.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Registado correctamente", Toast.LENGTH_SHORT).show();
                            Usuario u = new Usuario(FirebaseAuth.getInstance().getUid(), txEmail.getText().toString(), "null", 0, "null", "null", "https://firebasestorage.googleapis.com/v0/b/asociacioncomercios-aa45a.appspot.com/o/usuarios%2FuserPorDefecto.png?alt=media&token=00475335-5b1f-42d3-b0f0-8f9952174695");

                            myRef.child(FirebaseAuth.getInstance().getUid()).setValue(u);
                        } else {
                            Toast.makeText(getApplicationContext(), "Error al registrar", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void accederCuenta() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(txEmail.getText().toString(), txContraseña.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Acceso correcto", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("idUsuarioActual", FirebaseAuth.getInstance().getUid());
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Error al acceder", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    public void lecturaBD() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listaUsuarios.clear();

                for (DataSnapshot xUsuarios : dataSnapshot.getChildren()) {
                    Usuario u = xUsuarios.getValue(Usuario.class);
                    listaUsuarios.add(u);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        myRef.addValueEventListener(postListener);
    }


    public void googleLogin() {

        loginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<AuthUI.IdpConfig> providers = Arrays.asList(
                        new AuthUI.IdpConfig.GoogleBuilder().build()
                );
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build(),
                        RC_SIGN_IN);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Toast.makeText(this, "Sesion iniciada correctamente", Toast.LENGTH_SHORT).show();
                Usuario u = new Usuario(FirebaseAuth.getInstance().getUid(),  user.getEmail(), "null", 0, "null", "null", "https://firebasestorage.googleapis.com/v0/b/asociacioncomercios-aa45a.appspot.com/o/usuarios%2FuserPorDefecto.png?alt=media&token=00475335-5b1f-42d3-b0f0-8f9952174695");
                Boolean nuevoUsuario = true;
                for (int i = 0; i< listaUsuarios.size(); i++){
                    if (listaUsuarios.get(i).getUserId().equals(u.getUserId())){
                        nuevoUsuario = false;
                    }
                }
                if (nuevoUsuario){
                    myRef.child(FirebaseAuth.getInstance().getUid()).setValue(u);
                }

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Error al iniciar sesion", Toast.LENGTH_SHORT).show();
            }
        }
    }
}