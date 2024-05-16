package es.ifp.labsalut;

import static es.ifp.labsalut.ui.SettingsFragment.MY_PREFS_HUELLA;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import javax.crypto.SecretKey;

import es.ifp.labsalut.db.BaseDatos;
import es.ifp.labsalut.negocio.Usuario;
import es.ifp.labsalut.seguridad.CifradoAES;
import es.ifp.labsalut.seguridad.FingerprintActivity;
import es.ifp.labsalut.ui.RegistroActivity;

public class MainActivity extends AppCompatActivity {

    public static final String MY_PREFS_USER = "RECORDARUSUARIO";
    protected EditText emailEditText;
    protected EditText passwordEditText;
    protected Button loginButton;
    protected Button registroButton;
    protected TextView titleLabel;
    protected CheckBox recordarUser;
    private Intent pasarPantalla;
    private String email;
    private String password;
    private BaseDatos db;
    private Usuario user = null;
    private Bundle extras;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new BaseDatos(this);
        // Obtener referencias de los elementos de la interfaz
        titleLabel = (TextView) findViewById(R.id.logo_main);
        emailEditText = (EditText) findViewById(R.id.userEmail_main);
        passwordEditText = (EditText) findViewById(R.id.password_main);
        loginButton = (Button) findViewById(R.id.acceptButton_main);
        registroButton = (Button) findViewById(R.id.newUserBoton_main);
        recordarUser = (CheckBox) findViewById(R.id.recordarUsuario_main);

        // Configurar el título de la aplicación centrado en un fondo negro y blanco
        titleLabel.setBackgroundColor(getResources().getColor(R.color.black));
        titleLabel.setTextColor(getResources().getColor(R.color.white));

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_USER, MODE_PRIVATE);
        SharedPreferences prefs_huella = getSharedPreferences(MY_PREFS_HUELLA, MODE_PRIVATE);
        String restoredText = prefs.getString("EMAIL", null);
        if (restoredText != null) {

            recordarUser.setChecked(true);
            String email = prefs.getString("EMAIL", "");
            String password = prefs.getString("PASS", "");
            emailEditText.setText(email);
            passwordEditText.setText(password);
            extras=getIntent().getExtras();
            if(extras!=null){
                SharedPreferences.Editor editor_huella = getSharedPreferences(MY_PREFS_HUELLA, MODE_PRIVATE).edit();
                editor_huella.putString("HUELLA", "NO");
                editor_huella.apply();
            }
            String huellaActiva = prefs_huella.getString("HUELLA", "");
            if (huellaActiva.equals("SI")) {
                try {
                    user = validarCredenciales(db, email, password);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                pasarPantalla = new Intent(MainActivity.this, FingerprintActivity.class);
                pasarPantalla.putExtra("USUARIO", user);
                finish();
                startActivity(pasarPantalla);
            }
        } else {
            recordarUser.setChecked(false);
        }

        // Configurar el botón de inicio de sesión
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Los campos Usuario y Contraseña no pueden estar vacíos", Toast.LENGTH_SHORT).show();
                } else {
                    // Verificar si el usuario y la contraseña son correctos
                    try {
                        user = validarCredenciales(db, email, password);
                        if (user != null) {
                            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_USER, MODE_PRIVATE).edit();
                            if (recordarUser.isChecked()) {
                                editor.putString("EMAIL", user.getEmail());
                                editor.putString("PASS", user.getContrasena());
                            } else {
                                editor.putString("EMAIL", "");
                                editor.putString("PASS", "");
                            }
                            editor.apply();
                            if(extras!=null){
                                SharedPreferences.Editor editor_huella = getSharedPreferences(MY_PREFS_HUELLA, MODE_PRIVATE).edit();
                                editor_huella.putString("HUELLA", "SI");
                                editor_huella.apply();
                            }

                            // Si son correctos, redirigir al usuario a la clase Usuario
                            pasarPantalla = new Intent(MainActivity.this, MenuActivity.class);
                            pasarPantalla.putExtra("USUARIO", user);
                            finish();
                            startActivity(pasarPantalla);
                        } else {
                            // Si no son correctos, mostrar un mensaje de error
                            // (puedes implementar esto según tu preferencia)
                            Toast.makeText(MainActivity.this, "El usuario o la contraseña no es correcta", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        registroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasarPantalla = new Intent(MainActivity.this, RegistroActivity.class);
                finish();
                startActivity(pasarPantalla);
            }
        });

    }

    // Método para verificar las credenciales del usuario
    private Usuario validarCredenciales(BaseDatos db, String email, String password) throws Exception {
        // Lógica para verificar si el email y la contraseña son correctos

        Usuario usuario = null;

        CifradoAES aes = new CifradoAES();
        String semilla = email + password;
        SecretKey secretKey = aes.generarSecretKey(semilla);
        String encrypt = null;
        String encrypt2 = null;
        try {
            encrypt = aes.encrypt(password.getBytes(), secretKey);
            encrypt2 = aes.encrypt(email.getBytes(), secretKey);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        usuario = db.getUser(encrypt2);
        if (encrypt.equals(usuario.getContrasena()) &&
                encrypt2.equals(usuario.getEmail())) {
            usuario.setIdUsuario(usuario.getIdUsuario());
            usuario.setNombre(aes.decrypt(usuario.getNombre(), secretKey));
            usuario.setFechaNacimiento(aes.decrypt(usuario.getFechaNacimiento(), secretKey));
            usuario.setEmail(aes.decrypt(usuario.getEmail(), secretKey));
            usuario.setContrasena(aes.decrypt(usuario.getContrasena(), secretKey));
        }else{
            usuario =null;
        }

        return usuario;
    }

}