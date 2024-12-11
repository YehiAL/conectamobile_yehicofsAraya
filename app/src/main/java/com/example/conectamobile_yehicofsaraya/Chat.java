package com.example.conectamobile_yehicofsaraya;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;

public class Chat extends AppCompatActivity {

    private Mqtt mqttHelper;
    private EditText etMensaje;
    private Button btnEnviar;
    private RecyclerView rvChat;
    private MensajeAdapter adapter;
    private List<Mensaje> listaMensajes;

    private String topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Inicializar las vistas
        etMensaje = findViewById(R.id.et_mensaje);
        btnEnviar = findViewById(R.id.btnEnviar);
        rvChat = findViewById(R.id.rv_chat);

        listaMensajes = new ArrayList<>();
        adapter = new MensajeAdapter(listaMensajes);
        rvChat.setLayoutManager(new LinearLayoutManager(this));
        rvChat.setAdapter(adapter);

        // Obtener datos del intent
        String nombre = getIntent().getStringExtra("nombre");
        String correo = getIntent().getStringExtra("correo");
        String userId = "user1";
        String contactId = correo.replace("@", "_").replace(".", "_");
        topic = "chat/" + userId + "/" + contactId;

        setTitle("Chat con " + nombre);

        // Inicializar MQTT
        mqttHelper = new Mqtt(this, userId);

        // Manejar conexión exitosa
        mqttHelper.setOnConnected(() -> {
            Log.d("MQTT", "Conexión lista. Suscribiéndose al topic...");
            mqttHelper.subscribeToTopic(topic);
        });

        // Manejar mensajes entrantes
        mqttHelper.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.e("MQTT", "Conexión perdida");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                String mensajeRecibido = new String(message.getPayload());
                Log.d("MQTT", "Mensaje recibido: " + mensajeRecibido);
                runOnUiThread(() -> {
                    listaMensajes.add(new Mensaje(mensajeRecibido, false));
                    adapter.notifyDataSetChanged();
                });
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.d("MQTT", "Mensaje entregado");
            }
        });

        btnEnviar.setOnClickListener(v -> {
            String mensaje = etMensaje.getText().toString().trim();
            if (!mensaje.isEmpty()) {
                enviarMensaje(mensaje);
            } else {
                Toast.makeText(this, "Escribe un mensaje", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configurarRecepcionMensajes() {
        mqttHelper.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.e("MQTT", "Conexión perdida");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                String mensajeRecibido = new String(message.getPayload());
                Log.d("MQTT", "Mensaje recibido: " + mensajeRecibido);
                runOnUiThread(() -> {
                    listaMensajes.add(new Mensaje(mensajeRecibido, false));
                    adapter.notifyDataSetChanged();
                });
            }

            @Override
            public void deliveryComplete(org.eclipse.paho.client.mqttv3.IMqttDeliveryToken token) {
                Log.d("MQTT", "Mensaje entregado");
            }
        });
    }

    private void enviarMensaje(String mensaje) {
        mqttHelper.publishMessage(topic, mensaje);
        listaMensajes.add(new Mensaje(mensaje, true));
        adapter.notifyDataSetChanged();
        etMensaje.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mqttHelper != null) {
            mqttHelper.disconnect();
        }
    }
}
