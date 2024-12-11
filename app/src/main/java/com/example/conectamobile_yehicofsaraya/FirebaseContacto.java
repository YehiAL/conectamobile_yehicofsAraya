package com.example.conectamobile_yehicofsaraya;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class FirebaseContacto {
    private DatabaseReference databaseReference; //referencia a la base de datos en firebase

    //inicializa la referencia a la base de datos
    public FirebaseContacto() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("contactos");
    }

    //agrega un nuevo contacto a la base de datos.
    public void agregarContacto(Contacto contacto, FirebaseCallback callback) {
        // genera un id unico para el contacto
        String id = databaseReference.push().getKey();
        if (id != null) {
            // asigna el id al contacto
            contacto.setId(id);
            databaseReference.child(id).setValue(contacto).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    callback.exito(); // Llama al método de exito
                } else {
                    callback.fallo(task.getException()); // Llama al método de fallo
                }
            });
        }
    }

    //obtiene la lista completa de contactos desde la base de datos
    public void obtenerContactos(FirebaseCallbackList callback) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //lista para almacenar los contactos obtenidos
                List<Contacto> contactos = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //convierte cada nodo en un objeto Contacto
                    Contacto contacto = snapshot.getValue(Contacto.class);
                    if (contacto != null) {
                        //agrega el contacto a la lista
                        contactos.add(contacto);
                    }
                }
                //llama al callback con la lista de contactos obtenida
                callback.onListaObtenida(contactos);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //llama al método de fallo si ocurre un error
                callback.fallo(databaseError.toException());
            }
        });
    }

    //Interfaz para manejar resultados de operaciones individuales en Firebase.
    public interface FirebaseCallback {
        void exito(); // Método llamado cuando la operación es exitosa
        void fallo(Exception e); // Método llamado cuando ocurre un error
    }

    //Interfaz para manejar resultados de operaciones que devuelven listas en Firebase.
    public interface FirebaseCallbackList {
        void onListaObtenida(List<Contacto> contactos); // Método llamado cuando se obtiene la lista de contactos con éxito
        void fallo(Exception e); // Método llamado cuando ocurre un error
    }
}


