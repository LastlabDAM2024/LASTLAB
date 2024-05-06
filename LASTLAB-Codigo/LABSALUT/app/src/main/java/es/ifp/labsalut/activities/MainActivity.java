package es.ifp.labsalut.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

import javax.crypto.SecretKey;

import es.ifp.labsalut.R;
import es.ifp.labsalut.db.BaseDatos;
import es.ifp.labsalut.negocio.Usuario;
import es.ifp.labsalut.seguridad.CifradoAES;

public class MainActivity extends AppCompatActivity {

    protected EditText usernameEditText;
    protected EditText passwordEditText;
    protected Button loginButton;
    protected Button registroButton;
    protected TextView titleLabel;
    private Intent pasarPantalla;
    private String user;
    private String password;
    private BaseDatos db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new BaseDatos(this);
        // Obtener referencias de los elementos de la interfaz
        titleLabel = findViewById(R.id.logo_main);
        usernameEditText = findViewById(R.id.username_main);
        passwordEditText = findViewById(R.id.password_main);
        loginButton = findViewById(R.id.acceptButton_main);
        registroButton = findViewById(R.id.newUserBoton_main);

        // Configurar el título de la aplicación centrado en un fondo negro y blanco
        titleLabel.setBackgroundColor(getResources().getColor(R.color.black));
        titleLabel.setTextColor(getResources().getColor(R.color.white));

        // Configurar el botón de inicio de sesión
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                // Verificar si el usuario y la contraseña son correctos
                try {
                    if (validarCredenciales(user, password)) {
                        // Si son correctos, redirigir al usuario a la clase Usuario
                        pasarPantalla = new Intent(MainActivity.this, MenuActivity.class);
                        startActivity(pasarPantalla);
                    } else {
                        // Si no son correctos, mostrar un mensaje de error
                        // (puedes implementar esto según tu preferencia)
                        // Por ejemplo, un Toast o un TextView debajo del EditText
                        Toast.makeText(MainActivity.this, "La contraseña no es correcta", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        registroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasarPantalla = new Intent(MainActivity.this, RegistroActivity.class);
                startActivity(pasarPantalla);
            }
        });

    }

    // Método para verificar las credenciales del usuario
    private boolean validarCredenciales(String username, String password) throws Exception {
        // Lógica para verificar si el usuario y la contraseña son correctos
        // Puedes implementar la lógica de verificación aquí o en un método separado
        // Por ejemplo, comparar con datos almacenados en la base de datos o en una lista
        boolean result = false;
        if (username.equals("") || password.equals("")) {
            Toast.makeText(this, "Los campos Usuario y Contraseña no pueden estar vacíos", Toast.LENGTH_SHORT).show();
        } else {
            Usuario usuario = db.getUser(username);
            CifradoAES aes = new CifradoAES();
            String semilla = username+password;
            SecretKey secretKey=aes.generarSecretKey(semilla);
            byte[] encrypt=null;
            try {
                encrypt = aes.encrypt(password.getBytes(), secretKey);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (Arrays.toString(encrypt).equals(usuario.getContrasena())){
                result = true;
            }
        }
        return result; // En este ejemplo, siempre devuelve true para simplificar
    }
}