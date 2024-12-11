package com.example.conectamobile_yehicofsaraya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Menu extends AppCompatActivity {

    private Button btnListaContactos, btnPerfil, btnSalir;
    private FirebaseAutent firebaseAutent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //instancia de etiquetas
        btnListaContactos = findViewById(R.id.btnListaContactos);
        btnPerfil = findViewById(R.id.btnPerfil);
        btnSalir = findViewById(R.id.btnSalir);
        //instancia de logica de firebase
        firebaseAutent = new FirebaseAutent(this);


        //evento de boton listaContactos
        btnListaContactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //redirigira al layout de la lista
                Intent intent = new Intent(Menu.this, ListaContactos.class);
                startActivity(intent);
            }
        });

        //evento del boton perfil
        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //direccionara al layout del perfil
                Intent intent = new Intent(Menu.this, Perfil.class);
                startActivity(intent);
            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //llamar a la logica del firebase, deslogear y volver al inicio
                firebaseAutent.salirSesion();
                Intent intent = new Intent(Menu.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}