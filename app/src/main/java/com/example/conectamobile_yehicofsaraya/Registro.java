package com.example.conectamobile_yehicofsaraya;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;

public class Registro extends AppCompatActivity {

    private EditText etNombreUsuario, etCorreo, etContrasena, etConfirmar;
    private Button btnRegistrar,btnVolver,btnLimpiar;
    private FirebaseAutent firebaseAutent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        //instancia de etiquetas
        etNombreUsuario = findViewById(R.id.et_usuario_registro);
        etCorreo = findViewById(R.id.et_correo_registro);
        etContrasena = findViewById(R.id.et_contra_registro);
        etConfirmar = findViewById(R.id.et_confirmar);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnVolver = findViewById(R.id.btnVolver);
        btnLimpiar = findViewById(R.id.btnLimpiar);
        //instancia de la logica de firebase
        firebaseAutent = new FirebaseAutent(this);
        //evento del boton registrar
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //obtener los datos de los editText
                String nombreUsuario = etNombreUsuario.getText().toString();
                String correo = etCorreo.getText().toString();
                String contrasena = etContrasena.getText().toString(); //esta es para comparar contraseñas
                String confirmar = etConfirmar.getText().toString();
                //enviarlos al metodo de validacion
                Boolean validacion = validaciones(nombreUsuario, correo, contrasena, confirmar);
                //si la validacion es verdadera:
                if (validacion) {
                    //enviara todos los datos a la logica y obtendra los metodos de la interfaz
                    firebaseAutent.registrarUsuario(nombreUsuario, correo, contrasena, new FirebaseAutent.AuthCallback() {
                        @Override
                        public void exito(FirebaseUser user) {
                            //notificara que fue correcto a traves de un toast
                            Toast.makeText(Registro.this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void fallo(Exception exception) {
                            //notificara que el fallo a traves de un toast
                            Toast.makeText(Registro.this, "Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        //evento del boton limpiar
        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //vaciara los editText
                etNombreUsuario.setText("");
                etCorreo.setText("");
                etContrasena.setText("");
                etConfirmar.setText("");
            }
        });
        //evento del boton volver
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    //metodo con el que se validaran la contraseña para que cumpla con las reglas
    private boolean validaciones(String nombreUsuario, String correo, String contrasena, String confirmar) {
        //si los editText no estan vacios:
        if(!nombreUsuario.isEmpty() && !correo.isEmpty() && !contrasena.isEmpty()) {
            //si la contraseña tiene menos de 4 caracteres
            if (contrasena.length() < 4) {
                //retornara falso y lo notificara a traves de un toast
                Toast.makeText(Registro.this, "La contraseña debe tener minimo 4 caracteres", Toast.LENGTH_SHORT).show();
                return false;
            } else if (!contrasena.equals(confirmar)) {
                //si las contraseña ingresada no es igual a la de confirmacion tambien notificara y devolvera falso
                Toast.makeText(Registro.this, "Las contraseñas ingresadas deben coincidir", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                //en caso de que pase las dos validaciones devolvera verdadero
                return true;
            }
        }else{
            //en caso que este vacio, pedira que se rellenen los datos
            Toast.makeText(Registro.this, "Por favor ingrese todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
