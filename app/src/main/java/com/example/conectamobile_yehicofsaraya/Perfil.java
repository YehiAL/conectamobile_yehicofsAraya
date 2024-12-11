package com.example.conectamobile_yehicofsaraya;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

public class Perfil extends AppCompatActivity {


    private EditText etNombreUsuario, etCorreo, etContrasena;
    private Button btnVolver, btnEditar;

    private FirebaseAutent firebaseAutent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        etNombreUsuario = findViewById(R.id.et_usuario_perfil);
        etCorreo = findViewById(R.id.et_correo_perfil);
        etContrasena = findViewById(R.id.et_contra_perfil);
        btnVolver = findViewById(R.id.btnVolver_perfil);
        btnEditar = findViewById(R.id.btnEditar_perfil);

        // Inicialización del helper de Firebase
        firebaseAutent = new FirebaseAutent(this);

        // Cargar la información del usuario al abrir la pantalla
        cargarDatosUsuario();

        // Botón para volver a la pantalla anterior
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Botón para editar la información del perfil
        btnEditar.setOnClickListener(v -> actualizarPerfil());
    }

    //Cargar la información del usuario de Firebase Authentication y la base de datos.
    private void cargarDatosUsuario() {
        firebaseAutent.obtenerPerfilUsuario(new FirebaseAutent.AuthCallback() {
            @Override
            public void exito(FirebaseUser user) {
                // Mostrar el correo desde Firebase Authentication
                etCorreo.setText(user.getEmail());

                // Obtener el nombre del usuario desde la base de datos
                String userId = user.getUid();
                firebaseAutent.obtenerNombreUsuario(userId, new FirebaseAutent.PerfilCallback() {
                    @Override
                    public void nombreObtenido(String nombre) {
                        etNombreUsuario.setText(nombre);
                    }

                    @Override
                    public void nombreActualizado() {
                        // No se usa aquí
                    }

                    @Override
                    public void error(Exception exception) {
                        Toast.makeText(Perfil.this, "Error al obtener el nombre de usuario", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void fallo(Exception exception) {
                Toast.makeText(Perfil.this, "Error al cargar el perfil: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    //Actualizar la información del perfil (nombre de usuario y contraseña).
    private void actualizarPerfil() {
        String nuevoNombre = etNombreUsuario.getText().toString().trim();
        String nuevaContrasena = etContrasena.getText().toString().trim();

        if (!nuevoNombre.isEmpty()) {
            String userId = firebaseAutent.autenticacion.getCurrentUser().getUid();
            firebaseAutent.actualizarNombreUsuario(userId, nuevoNombre, new FirebaseAutent.PerfilCallback() {
                @Override
                public void nombreObtenido(String nombre) { }

                @Override
                public void nombreActualizado() {
                    Toast.makeText(Perfil.this, "Nombre actualizado", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void error(Exception exception) {
                    Toast.makeText(Perfil.this, "Error al actualizar el nombre: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (!nuevaContrasena.isEmpty()) {
            firebaseAutent.actualizarContrasena(nuevaContrasena, new FirebaseAutent.AuthCallback() {
                @Override
                public void exito(FirebaseUser user) {
                    Toast.makeText(Perfil.this, "Contraseña actualizada", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void fallo(Exception exception) {
                    Toast.makeText(Perfil.this, "Error al actualizar la contraseña: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
