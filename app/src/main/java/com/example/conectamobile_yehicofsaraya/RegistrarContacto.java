package com.example.conectamobile_yehicofsaraya;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegistrarContacto extends AppCompatActivity {
    private EditText etNombreContacto, etCorreoContacto;
    private Button btnAgregarContacto,btnLimpiar,btnVolver;
    private FirebaseContacto firebasecontacto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_contacto);
        //inicializacion de etiquetas
        etNombreContacto = findViewById(R.id.et_nombre_contacto);
        etCorreoContacto = findViewById(R.id.et_correo_contacto);
        btnAgregarContacto = findViewById(R.id.btnAgregarContacto);
        btnLimpiar = findViewById(R.id.btnLimpiar);
        btnVolver = findViewById(R.id.btnVolver);
        //inicializacion de logica
        firebasecontacto = new FirebaseContacto();

        //evento del boton para agregar un contacto en firebase
        btnAgregarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarContacto();
            }
        });
        //evento que limpiara los editText
        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limpiarCampos();
            }
        });
        //evento que finalizara el layout actual para volver a la lista
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //metodo para agregar contactos
    private void agregarContacto() {
        //obtener datos de los editText
        String nombre = etNombreContacto.getText().toString().trim();
        String correo = etCorreoContacto.getText().toString().trim();

        //en caso de que los editText esten vacios, notificara por un toast
        if (nombre.isEmpty() || correo.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        //crea un objeto contacto con el nombre y correo
        Contacto contacto = new Contacto(null, nombre, correo);
        //llama a la interfaz de la logica en el metodo de agregarContacto
        firebasecontacto.agregarContacto(contacto, new FirebaseContacto.FirebaseCallback() {
            @Override
            public void exito() {
                //en caso de exito notificara y vaciara los campos
                Toast.makeText(RegistrarContacto.this, "Contacto agregado con éxito", Toast.LENGTH_SHORT).show();
                limpiarCampos();
            }

            @Override
            public void fallo(Exception e) {
                //notificara el error
                Toast.makeText(RegistrarContacto.this, "Error al agregar contacto: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //limpiar los campos en caso de que presione el respectivo boton
    private void limpiarCampos() {
        etNombreContacto.setText("");
        etCorreoContacto.setText("");
    }
}
