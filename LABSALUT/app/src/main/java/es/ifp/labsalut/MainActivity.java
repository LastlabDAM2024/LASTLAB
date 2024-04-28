package es.ifp.labsalut;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {

    EditText usernameEditText, passwordEditText;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtener referencias de los elementos de la interfaz
        TextView titleLabel = findViewById(R.id.titleLabel);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        // Configurar el título de la aplicación centrado en un fondo azul y blanco
        titleLabel.setText("LABSALUT");
        titleLabel.setBackgroundColor(getResources().getColor(R.color.blue));
        titleLabel.setTextColor(getResources().getColor(R.color.white));

        // Configurar el mensaje de bienvenida
        TextView welcomeMessageTextView = findViewById(R.id.welcomeMessageTextView);
        welcomeMessageTextView.setText("¡Bienvenido a LABSALUT!");

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
                    Intent intent = new Intent(MainActivity.this, Usuario.class);
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
}