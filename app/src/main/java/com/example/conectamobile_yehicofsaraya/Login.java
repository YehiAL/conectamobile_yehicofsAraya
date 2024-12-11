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

        //instancia de etiquetas utilizadas en el layout
        etCorreo = findViewById(R.id.et_correo_login);
        etContrasena = findViewById(R.id.et_contraseña_login);
        btnIniciarSesion = findViewById(R.id.btnIrIniciar);
        btnRegistrar = findViewById(R.id.btnIrRegistrar);
        //Instancia de la clase de logica de firebase
        firebaseAutent = new FirebaseAutent(this);

        //evento del boton iniciar sesion
        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //obtener los textos de los editText
                String correo = etCorreo.getText().toString().trim();
                String contrasena = etContrasena.getText().toString().trim();
                //en caso de que NO esten vacios:
                if (!correo.isEmpty() && !contrasena.isEmpty()) {
                    //enviar el correo y la contraseña a la logica de la firebase
                    firebaseAutent.iniciarSesion(correo, contrasena, new FirebaseAutent.AuthCallback() {
                        //la interfaz de la logica actuara en caso de que sea exitoso o falle
                        @Override
                        public void exito(FirebaseUser user) {
                            //redirigir al perfil del usuario
                            Intent intent = new Intent(Login.this, Menu.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void fallo(Exception exception) {
                            //notificar a traves de un toast
                            Toast.makeText(Login.this, "Error al iniciar sesión: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    //notificar a traves de un toast
                    Toast.makeText(Login.this, "Por favor ingrese todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //evento del boton registrar
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //redigirigira al layout registro
                Intent intent = new Intent(Login.this, Registro.class);
                startActivity(intent);
            }
        });

    }
}
