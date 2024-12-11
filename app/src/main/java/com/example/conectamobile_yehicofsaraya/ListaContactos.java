package com.example.conectamobile_yehicofsaraya;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListaContactos extends AppCompatActivity {
    private FirebaseContacto firebaseHelper;
    private ListView lvContactos;
    private SearchView svBuscarContacto;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> contactosList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contactos);

        // Inicializar los componentes de la vista
        lvContactos = findViewById(R.id.lv_contactos);
        svBuscarContacto = findViewById(R.id.svBuscarContacto);
        Button btnAgregarContacto = findViewById(R.id.btn_agregar_contacto);

        // Inicializar el helper de Firebase
        firebaseHelper = new FirebaseContacto();

        // Lista y adaptador para mostrar los contactos
        contactosList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contactosList);
        lvContactos.setAdapter(adapter);

        // Obtener contactos desde Firebase
        cargarContactos();

        // Botón para agregar un nuevo contacto
        btnAgregarContacto.setOnClickListener(v -> {
            Intent intent = new Intent(ListaContactos.this, RegistrarContacto.class);
            startActivity(intent);
        });

        // Filtrar la lista de contactos a medida que el usuario escribe
        svBuscarContacto.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Filtrar la lista al enviar la consulta
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                /* Filtrar la lista en tiempo real mientras se escribe
                adapter.getFilter().filter(newText)*/
                return false;
            }
        });
    }

    private void cargarContactos() {
        firebaseHelper.obtenerContactos(new FirebaseContacto.FirebaseCallbackList() {
            @Override
            public void onListaObtenida(List<Contacto> contactos) {
                contactosList.clear();
                for (Contacto contacto : contactos) {
                    String nombre = contacto.getNombre();
                    String correo = contacto.getCorreo();
                    if (nombre != null && correo != null) {
                        contactosList.add(nombre + " - " + correo);
                    }
                }
                adapter.notifyDataSetChanged(); // Actualizar la lista en la interfaz
            }

            @Override
            public void fallo(Exception e) {
                Toast.makeText(ListaContactos.this, "Error al cargar los contactos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

