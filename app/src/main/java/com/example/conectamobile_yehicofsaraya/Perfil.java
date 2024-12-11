package com.example.conectamobile_yehicofsaraya;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseUser;

public class Perfil extends AppCompatActivity {

    private EditText etNombreUsuario, etCorreo, etContrasena;
    private Button btnVolver, btnEditar;

    private FirebaseAutent firebaseAutent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        //instancia de etiquetas
        etNombreUsuario = findViewById(R.id.et_usuario_perfil);
        etCorreo = findViewById(R.id.et_correo_perfil);
        etContrasena = findViewById(R.id.et_contra_perfil);
        btnVolver = findViewById(R.id.btnVolver_perfil);
        btnEditar = findViewById(R.id.btnEditar_perfil);

        // inicializacion de la logica de Firebase
        firebaseAutent = new FirebaseAutent(this);

        // cargar la información del usuario al abrir la pantalla
        cargarDatosUsuario();

        // boton para volver a la pantalla anterior
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // boton para editar la informacion del perfil
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizarPerfil();
            }
        });
    }

    //cargar la informacion del usuario de Firebase Authentication y la base de datos.
    private void cargarDatosUsuario() {
        firebaseAutent.obtenerPerfilUsuario(new FirebaseAutent.AuthCallback() {
            @Override
            public void exito(FirebaseUser user) {
                // mostrar el correo desde Firebase Authentication
                etCorreo.setText(user.getEmail());

                // obtener el nombre del usuario desde la base de datos
                String userId = user.getUid();
                //utilizar el metodo de la clase logica de firebase
                firebaseAutent.obtenerNombreUsuario(userId, new FirebaseAutent.PerfilCallback() {
                    @Override
                    public void nombreObtenido(String nombre) {
                        etNombreUsuario.setText(nombre);
                    }

                    @Override
                    public void nombreActualizado() {
                        // no se usa aqui
                    }
                    @Override
                    public void error(Exception exception) {
                        //notifica a traves de un toast
                        Toast.makeText(Perfil.this, "Error al obtener el nombre de usuario", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void fallo(Exception exception) {
                //notifica a traves de un toast
                Toast.makeText(Perfil.this, "Error al cargar el perfil: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    //actualizar la informacion del perfil (nombre de usuario y contraseña).
    private void actualizarPerfil() {
        //obtener los datos de los editText
        String nuevoNombre = etNombreUsuario.getText().toString().trim();
        String nuevaContrasena = etContrasena.getText().toString().trim();

        //en caso de que cambien el usuario
        if (!nuevoNombre.isEmpty()) {
            //obtendra el id del usuario actual
            String userId = firebaseAutent.autenticacion.getCurrentUser().getUid();
            //y los enviara a la logica, obtiendo los metodos de la interfaz
            firebaseAutent.actualizarNombreUsuario(userId, nuevoNombre, new FirebaseAutent.PerfilCallback() {
                @Override
                public void nombreObtenido(String nombre) { }

                //notificara a traves de un toast
                @Override
                public void nombreActualizado() {
                    Toast.makeText(Perfil.this, "Nombre actualizado", Toast.LENGTH_SHORT).show();
                }

                //de la misma forma notificara los errores
                @Override
                public void error(Exception exception) {
                    Toast.makeText(Perfil.this, "Error al actualizar el nombre: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        //en caso de que desee cambiar la contraseña
        if (!nuevaContrasena.isEmpty()) {
            //enviara la nueva contraseña a la logica
            firebaseAutent.actualizarContrasena(nuevaContrasena, new FirebaseAutent.AuthCallback() {
                @Override
                public void exito(FirebaseUser user) {
                    //notificara con un toast
                    Toast.makeText(Perfil.this, "Contraseña actualizada", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void fallo(Exception exception) {
                    //notificara con un toast en caso de fallo
                    Toast.makeText(Perfil.this, "Error al actualizar la contraseña: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
