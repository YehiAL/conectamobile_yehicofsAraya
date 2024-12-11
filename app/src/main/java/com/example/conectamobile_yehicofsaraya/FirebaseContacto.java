package com.example.conectamobile_yehicofsaraya;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class FirebaseContacto {
    private DatabaseReference databaseReference;

    public FirebaseContacto() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("contactos");
    }

    // Agregar un nuevo contacto
    public void agregarContacto(Contacto contacto, FirebaseCallback callback) {
        String id = databaseReference.push().getKey();
        if (id != null) {
            contacto.setId(id);
            databaseReference.child(id).setValue(contacto).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    callback.exito();
                } else {
                    callback.fallo(task.getException());
                }
            });
        }
    }

    // Obtener lista de contactos
    public void obtenerContactos(FirebaseCallbackList callback) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Contacto> contactos = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Contacto contacto = snapshot.getValue(Contacto.class);
                    if (contacto != null) {
                        contactos.add(contacto);
                    }
                }
                callback.onListaObtenida(contactos); // Devolvemos la lista de contactos
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.fallo(databaseError.toException()); // En caso de error, se usa el callback de fallo
            }
        });
    }


    // Interface para manejar resultados de Firebase
    public interface FirebaseCallback {
        void exito();
        void fallo(Exception e);
    }


    public interface FirebaseCallbackList {
        void onListaObtenida(List<Contacto> contactos); // Cuando la lista de contactos se obtiene con éxito
        void fallo(Exception e); // Cuando ocurre un error
    }
}

