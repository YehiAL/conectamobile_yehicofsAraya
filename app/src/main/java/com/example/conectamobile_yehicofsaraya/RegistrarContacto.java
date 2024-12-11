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

        etNombreContacto = findViewById(R.id.et_nombre_contacto);
        etCorreoContacto = findViewById(R.id.et_correo_contacto);
        btnAgregarContacto = findViewById(R.id.btnAgregarContacto);
        btnLimpiar = findViewById(R.id.btnLimpiar);
        btnVolver = findViewById(R.id.btnVolver);

        firebasecontacto = new FirebaseContacto();

        btnAgregarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarContacto();
            }
        });
        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limpiarCampos();
            }
        });
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void agregarContacto() {
        String nombre = etNombreContacto.getText().toString().trim();
        String correo = etCorreoContacto.getText().toString().trim();

        if (nombre.isEmpty() || correo.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Contacto contacto = new Contacto(null, nombre, correo);
        firebasecontacto.agregarContacto(contacto, new FirebaseContacto.FirebaseCallback() {
            @Override
            public void exito() {
                Toast.makeText(RegistrarContacto.this, "Contacto agregado con éxito", Toast.LENGTH_SHORT).show();
                limpiarCampos();
            }

            @Override
            public void fallo(Exception e) {
                Toast.makeText(RegistrarContacto.this, "Error al agregar contacto: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void limpiarCampos() {
        etNombreContacto.setText("");
        etCorreoContacto.setText("");
    }
}
