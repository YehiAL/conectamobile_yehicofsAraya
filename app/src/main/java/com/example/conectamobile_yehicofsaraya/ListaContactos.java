package com.example.conectamobile_yehicofsaraya;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

        // inicializar las etiquetas
        lvContactos = findViewById(R.id.lv_contactos);
        svBuscarContacto = findViewById(R.id.svBuscarContacto);
        Button btnAgregarContacto = findViewById(R.id.btn_agregar_contacto);

        // Inicializar el la logica de contactos de firebase
        firebaseHelper = new FirebaseContacto();

        // inicializacion de la lista y el adapter
        contactosList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contactosList);
        lvContactos.setAdapter(adapter);

        // obtener contactos desde Firebase
        cargarContactos();

        // evento del boton agregarContacto
        btnAgregarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //redirige a el activity para registrar contactos
                Intent intent = new Intent(ListaContactos.this, RegistrarContacto.class);
                startActivity(intent);
            }
        });

        // listener para manejar clics en los elementos del ListView
        lvContactos.setOnItemClickListener((parent, view, position, id) -> {
            // obtener el contacto seleccionado
            String contactoSeleccionado = contactosList.get(position);

            // separar el nombre y correo (asumiendo formato "Nombre - Correo")
            String[] datosContacto = contactoSeleccionado.split(" - ");
            String nombre = datosContacto[0];
            String correo = datosContacto[1];

            // pasar los datos del contacto al chat
            Intent intent = new Intent(ListaContactos.this, Chat.class);
            intent.putExtra("nombre", nombre);
            intent.putExtra("correo", correo);
            startActivity(intent);
            Log.d("Chat", "Nombre: " + nombre + ", Correo: " + correo);


        });

        // filtrar la lista de contactos a medida que el usuario escribe
        svBuscarContacto.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //filtrara los datos al presionar el boton
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    //metodo para cargar los contactos en la lista
    private void cargarContactos() {
        //obtener los metodos de la interfaz hecha en la logica
        firebaseHelper.obtenerContactos(new FirebaseContacto.FirebaseCallbackList() {
            @Override
            public void onListaObtenida(List<Contacto> contactos) {
                //limpia la lista anterior
                contactosList.clear();
                //por cada contacto, creara un objeto y obtendra sus datos
                for (Contacto contacto : contactos) {
                    String nombre = contacto.getNombre();
                    String correo = contacto.getCorreo();
                    //en caso de que no sean nulos, los agregara a la lista
                    if (nombre != null && correo != null) {
                        contactosList.add(nombre + " - " + correo);
                    }
                }
                // actualizar la lista en la interfaz
                adapter.notifyDataSetChanged();
            }

            @Override
            public void fallo(Exception e) {
                //en caso de que algo falle notificara a traves de un toast
                Toast.makeText(ListaContactos.this, "Error al cargar los contactos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}




