package com.example.conectamobile_yehicofsaraya;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private EditText etCorreo, etContrasena;
    private Button btnIniciarSesion, btnRegistrar;
    private FirebaseAutent firebaseAutent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etCorreo = findViewById(R.id.et_correo_login);
        etContrasena = findViewById(R.id.et_contraseña_login);
        btnIniciarSesion = findViewById(R.id.btnIrIniciar);
        btnRegistrar = findViewById(R.id.btnIrRegistrar);

        firebaseAutent = new FirebaseAutent(this);

        btnIniciarSesion.setOnClickListener(v -> {
            String correo = etCorreo.getText().toString().trim();
            String contrasena = etContrasena.getText().toString().trim();

            if (!correo.isEmpty() && !contrasena.isEmpty()) {
                firebaseAutent.iniciarSesion(correo, contrasena, new FirebaseAutent.AuthCallback() {
                    @Override
                    public void exito(FirebaseUser user) {
                        // Redirigir al perfil del usuario
                        Intent intent = new Intent(Login.this, Menu.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void fallo(Exception exception) {
                        Toast.makeText(Login.this, "Error al iniciar sesión: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(Login.this, "Por favor ingrese todos los campos", Toast.LENGTH_SHORT).show();
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Registro.class);
                startActivity(intent);
            }
        });

    }
}
