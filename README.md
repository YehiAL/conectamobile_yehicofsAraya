LIBRERIAS
Durante el desarrollo de este proyecto se implementaron las siguientes librerias:

Firebase:
'com.google.firebase:firebase-bom:33.7.0')
'com.google.firebase:firebase-analytics'
'com.google.firebase:firebase-auth'
'com.google.firebase:firebase-database'
  -firebase-bom como libreria principal de firebase
  -firebase-analytics para el analisis de datos
  -firebase-auth la cual nos ayudara en la autenticación del usuario
  -firebase-databse la que se encargara de enviar los registros a las base de datos de firebase console

Mqtt:
'org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5'
'org.eclipse.paho:org.eclipse.paho.android.service:1.1.1'
  -org.eclipse.paho.client.mqtt, esta libreria nos ayuda con la logica de conexion de mqtt
  -org.eclipse.paho.android.service es la que se encargara de generer un entorno adecuado para android


AndroidManifest.xml

En el archivo AndroidManifest se agrego lo siguiente:

Permisos de usuario:
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
Estos nos permitiran habilitar el acceso a internet desde la aplicación

Servicios:
<service
            android:name="org.eclipse.paho.android.service.MqttService"
            android:exported="true" />
Se habilito el servicio de Mqtt para trabajar con el de forma adecuada

Posibles mejoras:
-La implementacion correcta de mqtt debido a las fallas ocurridas con el broadCastReciver durante el desarrollo del proyecto
-Agregar la funcionalidad de insertar una imagen de perfil y que esta se guarde en la base de datos de firebase
