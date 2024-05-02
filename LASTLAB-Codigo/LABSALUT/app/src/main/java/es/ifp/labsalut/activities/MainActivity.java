package es.ifp.labsalut.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import es.ifp.labsalut.R;

public class MainActivity extends AppCompatActivity {

    protected EditText usernameEditText;
    protected EditText passwordEditText;
    protected Button loginButton;
    protected TextView titleLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtener referencias de los elementos de la interfaz
        titleLabel = findViewById(R.id.logo_main);
        usernameEditText = findViewById(R.id.username_main);
        passwordEditText = findViewById(R.id.password_main);
        loginButton = findViewById(R.id.acceptButton_main);

        // Configurar el título de la aplicación centrado en un fondo negro y blanco
        titleLabel.setBackgroundColor(getResources().getColor(R.color.black));
        titleLabel.setTextColor(getResources().getColor(R.color.white));

        // Configurar el botón de inicio de sesión
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verificar el usuario y la contraseña
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Verificar si el usuario y la contraseña son correctos
                if (isValidCredentials(username, password)) {
                    // Si son correctos, redirigir al usuario a la clase Usuario
                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                    startActivity(intent);
                } else {
                    // Si no son correctos, mostrar un mensaje de error
                    // (puedes implementar esto según tu preferencia)
                    // Por ejemplo, un Toast o un TextView debajo del EditText
                }
            }
        });
    }

    // Método para verificar las credenciales del usuario
    private boolean isValidCredentials(String username, String password) {
        // Lógica para verificar si el usuario y la contraseña son correctos
        // Puedes implementar la lógica de verificación aquí o en un método separado
        // Por ejemplo, comparar con datos almacenados en la base de datos o en una lista
        return true; // En este ejemplo, siempre devuelve true para simplificar
    }
}