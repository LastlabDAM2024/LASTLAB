# INTRODUCCIÓN DEL PROYECTO LABSALUT


## 🚀💊 LABSALUT - Gestión Médica Revolucionaria 🚀💊


### Descripción
¡Bienvenidos al futuro de la gestión de la salud! 🌍 En un mundo donde la tecnología avanza a pasos agigantados, LABSALUT emerge como una solución revolucionaria para transformar la forma en que las personas, especialmente nuestros queridos adultos mayores, cuidan de su bienestar. ❤️👵👴

Imagina una aplicación móvil que no solo te recuerde tus medicamentos, sino que también te proporcione información detallada sobre dosis, interacciones y efectos secundarios. 💡 Visualiza un sistema de citas médicas intuitivo y fácil de usar, eliminando el estrés de programar y asistir a consultas. 🗓️ LABSALUT no solo es una aplicación, es un aliado de salud personalizado y accesible para todos. 🤝📱

Con un enfoque centrado en el usuario, LABSALUT no solo busca solucionar problemas, sino también empoderar a sus usuarios. Desde recordatorios inteligentes hasta acceso rápido a servicios médicos de emergencia, 🚑 nuestra aplicación está diseñada para mejorar la calidad de vida y promover la independencia de quienes más lo necesitan. 🎯

Nos enorgullece presentar un proyecto que no solo promete innovación, sino también un cambio significativo en la forma en que gestionamos nuestra salud. LABSALUT no solo es el futuro, ¡es el presente de la salud y el bienestar! 💪🌟

### Estructura del Proyecto
La estructura del proyecto sigue el patrón MVVC para la organización del código y las funcionalidades:


    - /activities
      - MainActivity.java
      - MenuActivity.java
      - MenuBottomActivity.java
      - RegistroActivity.java
    - /db
      - BaseDatos.java
    - /negocio
      - CitaMedica.java
      - Medicamento.java
      - ModListAdapter.java
      - Suscripcion.java
      - Usuario.java
    - /seguridad
      - CifradoAES.java
      - FingerPrintHandler.java
    - /ui
      - AgregarSuscripcionActivity.java
      - CancelarSuscripcionActivity.java
      - CitasFragment.java
      - MedicamentosFragment.java
      - RegistroFragment.java
      - SettingsFragment.java


### Componentes Principales


    MainActivity: Punto de entrada de la aplicación.
    Fragments: Manejan las diferentes vistas de la app, como CitasFragment y MedicamentosFragment.
    Adaptador (ModListAdapter): Se encarga de mostrar los datos en las vistas mediante la interacción con la base de datos.
    SQLite Database (BaseDatos.java): Administra el almacenamiento local de los datos de usuarios, citas, medicamentos, etc.


### Diagrama de Despliegue


![Diagrama de Despliegue](https://github.com/LastlabDAM2024/LASTLAB/blob/main/DIAGRAMA_DESPLIEGUE.png)


        Explicación de los componentes:
        
        Cliente (Dispositivo Móvil):
        
        Representa el dispositivo del usuario final donde se ejecuta la aplicación.
        
        
        MainActivity:
        
        Es la actividad principal de la aplicación Android.
        Sirve como punto de entrada y controla la navegación general.
        
        
        CitasFragment:
        
        Un fragmento que maneja la interfaz de usuario para la gestión de citas.
        Probablemente muestra una lista de citas y permite interacciones como agregar, editar o eliminar citas.
        
        
        ModListAdapter:
        
        Adaptador personalizado para la lista de citas.
        Se encarga de vincular los datos de las citas con la vista de lista en la interfaz de usuario.
        
        
        SQLite:
        
        Base de datos local utilizada para almacenar y gestionar los datos de las citas en el dispositivo.
        Proporciona persistencia de datos y operaciones CRUD (Crear, Leer, Actualizar, Eliminar).


### Instalación

1) Clonar el repositorio:

        git clone https://github.com/usuario/labsalut-app.git

2)Abrir el proyecto en Android Studio:

      Una vez clonado el repositorio, abre Android Studio y selecciona la opción "Open an existing Android Studio project". Navega hasta el directorio donde clonaste el repositorio y ábrelo.

3)Configurar las dependencias:

      Asegúrate de tener configurado el SDK de Android y las versiones necesarias de las dependencias que se encuentran en el archivo build.gradle.

4) Ejecutar la aplicación:

        Conecta un dispositivo móvil o utiliza un emulador y ejecuta la aplicación desde Android Studio.


### Uso

     - Registro:
      Los nuevos usuarios pueden registrarse proporcionando su información personal, que se almacena de forma segura en la base de datos SQLite. 🛡️
      
     - Gestión de Citas:
      Los usuarios pueden agregar, editar y eliminar citas médicas. Estas acciones son gestionadas por el fragmento CitasFragment y el adaptador ModListAdapter que interactúa con SQLite.
      
      - Gestión de Medicamentos:
       Similar a la gestión de citas, el usuario puede administrar sus medicamentos utilizando MedicamentosFragment. 💊
      
     - Seguridad:  
      Se incluye un sistema de seguridad básico con cifrado AES (CifradoAES.java) para proteger la información sensible de los usuarios. 🔒


### Contribuciones

Pasos para contribuir a este proyecto:

1)Haz un fork del repositorio.


2)Crea una nueva rama para tus características:

      git checkout -b feature/nueva-caracteristica
3)Realiza los cambios y haz commit:

      git commit -m "Descripción de los cambios"
Envía tu rama a GitHub:

      git push origin feature/nueva-caracteristica
Abre un pull request para revisar los cambios.


### Licencia
Este proyecto está bajo la licencia MIT. Puedes ver los detalles en el archivo LICENSE.


### Recursos Adicionales

      Documentación oficial de Android
      Guía del patrón MVVC
      Documentación SQLite para Android

## Gitbook 

[Documentación oficial](https://documentation-31.gitbook.io/documentation-docs) 


### Autores

        -Teresa Herrera
        -Ornella Sofía Gigante
        -Benjamín José Ruiz Amara
        -José Antonio Moreno Fernández
