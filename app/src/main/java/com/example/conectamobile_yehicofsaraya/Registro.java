package com.example.conectamobile_yehicofsaraya;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;

public class Registro extends AppCompatActivity {

    private EditText etNombreUsuario, etCorreo, etContrasena, etConfirmar;
    private Button btnRegistrar;
    private FirebaseAutent firebaseAutent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etNombreUsuario = findViewById(R.id.et_usuario_registro);
        etCorreo = findViewById(R.id.et_correo_registro);
        etContrasena = findViewById(R.id.et_contra_registro);
        etConfirmar = findViewById(R.id.et_confirmar);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        firebaseAutent = new FirebaseAutent(this);

        btnRegistrar.setOnClickListener(v -> {
            String nombreUsuario = etNombreUsuario.getText().toString();
            String correo = etCorreo.getText().toString();
            String contrasena = etContrasena.getText().toString();
            String confirmar = etConfirmar.getText().toString();

            Boolean validacion = validaciones(nombreUsuario, correo, contrasena, confirmar);

            if (validacion) {
                firebaseAutent.registrarUsuario(nombreUsuario, correo, contrasena, new FirebaseAutent.AuthCallback() {
                    @Override
                    public void exito(FirebaseUser user) {
                        Toast.makeText(Registro.this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void fallo(Exception exception) {
                        Toast.makeText(Registro.this, "Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private boolean validaciones(String nombreUsuario, String correo, String contrasena, String confirmar) {
        if(!nombreUsuario.isEmpty() && !correo.isEmpty() && !contrasena.isEmpty()) {
            if (contrasena.length() < 4) {
                Toast.makeText(Registro.this, "La contraseña debe tener minimo 4 caracteres", Toast.LENGTH_SHORT).show();
                return false;
            } else if (contrasena.equals(confirmar)) {
                return true;
            } else {
                Toast.makeText(Registro.this, "Las contraseñas ingresadas deben coincidir", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else{
            Toast.makeText(Registro.this, "Por favor ingrese todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
