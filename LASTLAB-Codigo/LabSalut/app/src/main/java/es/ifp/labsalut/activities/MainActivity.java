package es.ifp.labsalut.activities;

import static es.ifp.labsalut.ui.SettingsFragment.MY_PREFS_HUELLA;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.crypto.SecretKey;

import es.ifp.labsalut.R;
import es.ifp.labsalut.databinding.ActivityMainBinding;
import es.ifp.labsalut.db.BaseDatos;
import es.ifp.labsalut.negocio.Usuario;
import es.ifp.labsalut.seguridad.CifradoAES;
import es.ifp.labsalut.seguridad.FingerprintHandler;
import es.ifp.labsalut.ui.ColorStatusBar;

public class MainActivity extends AppCompatActivity implements FingerprintHandler.AuthenticationCallback {

    public static final String MY_PREFS_USER = "RECORDARUSUARIO";
    private ActivityMainBinding binding;

    private Intent pasarPantalla;
    private String email;
    private String password;
    private BaseDatos db;
    private Usuario user = null;
    private FingerprintHandler finger = null;
    private boolean activacionHuella = false;
    private SharedPreferences prefs_user = null;
    private SharedPreferences prefs_huella = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ColorStatusBar.colorDinamicStatusBar(this,ColorStatusBar.obtenerColorBackground(this));

        db = new BaseDatos(this);

        // Configurar el título de la aplicación centrado en un fondo negro y blanco
        binding.titulo.setBackgroundColor(getResources().getColor(R.color.md_theme_onPrimaryFixedVariant));
        binding.titulo.setTextColor(getResources().getColor(R.color.md_theme_onPrimary));

        try {
            // Crear MasterKey para EncryptedSharedPreferences
            MasterKey masterKey = new MasterKey.Builder(this)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            // Crear EncryptedSharedPreferences para MY_PREFS_USER
            prefs_user = EncryptedSharedPreferences.create(
                    this,
                    MY_PREFS_USER,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            // Crear EncryptedSharedPreferences para MY_PREFS_HUELLA
            prefs_huella = EncryptedSharedPreferences.create(
                    this,
                    MY_PREFS_HUELLA,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        SharedPreferences.Editor editor_user = prefs_user.edit();
        String restoredText = prefs_user.getString("EMAIL", null);

        if (restoredText != null && !restoredText.isEmpty()) {
            binding.textoPass.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);
            binding.recordarUsuario.setChecked(true);
            String email = prefs_user.getString("EMAIL", "");
            String password = prefs_user.getString("PASS", "");
            binding.email.setText(email);
            binding.pass.setText(password);

            String huellaActiva = prefs_user.getString("FINGER", "");
            if (huellaActiva.equals("SI")) {
                activacionHuella = false;
                try {
                    user = validarCredenciales(db, email, password);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    finger = new FingerprintHandler(this, this);
                    finger.startAuth();
                }

            }
        } else {
            binding.recordarUsuario.setChecked(false);
            binding.textoPass.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
        }

        binding.pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.pass.getText().toString().isEmpty()) {
                    binding.textoPass.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);
                } else {
                    binding.textoPass.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
                }
            }
        });

        // Configurar el botón de inicio de sesión
        binding.botonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = binding.email.getText().toString();
                password = binding.pass.getText().toString();
                if (email.isEmpty() || password.isEmpty()) {
                    Snackbar.make(binding.activityMain, "Los campos Usuario y Contraseña no pueden estar vacíos", Snackbar.LENGTH_LONG).show();
                } else {
                    // Verificar si el usuario y la contraseña son correctos
                    try {
                        user = validarCredenciales(db, email, password);
                        if (user != null) {
                            if (binding.recordarUsuario.isChecked()) {
                                editor_user.putString("EMAIL", user.getEmail());
                                editor_user.putString("PASS", user.getContrasena());
                            } else {
                                editor_user.putString("EMAIL", "");
                                editor_user.putString("PASS", "");
                            }
                            editor_user.apply();

                            String primeravezHuella = prefs_huella.getString("PRIMERAVEZ" + user.getNombre(), "");

                            if (primeravezHuella.equals("SI")) {

                                new MaterialAlertDialogBuilder(MainActivity.this)
                                        .setTitle("Huella digital")
                                        .setMessage("¿Quiere activarla para iniciar sesión?")
                                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                                                    activacionHuella = true;
                                                    MainActivity.this.runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            FingerprintHandler fingerFirst = new FingerprintHandler(MainActivity.this, MainActivity.this);
                                                            fingerFirst.startAuth();
                                                        }
                                                    });
                                                }
                                            }
                                        })
                                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                pasarPantalla = new Intent(MainActivity.this, MenuActivity.class);
                                                pasarPantalla.putExtra("USUARIO", user);
                                                activacionHuella = false;
                                                finish();
                                                startActivity(pasarPantalla);
                                            }
                                        })
                                        .show();

                            } else {
                                String uiSecundaria = prefs_huella.getString("UISECUNDARIA" + user.getNombre(),"");
                                if(uiSecundaria.equals("SI")){
                                    pasarPantalla = new Intent(MainActivity.this, MenuBottomActivity.class);

                                }else{
                                    pasarPantalla = new Intent(MainActivity.this, MenuActivity.class);

                                }
                                pasarPantalla.putExtra("USUARIO", user);
                                activacionHuella = false;
                                String huella = prefs_huella.getString("HUELLA" + user.getNombre(), "");
                                if (huella.equals("SI")) {
                                    editor_user.putString("FINGER", "SI");
                                }
                                editor_user.apply();
                                finish();
                                startActivity(pasarPantalla);
                            }
                        } else {
                            // Si no son correctos, mostrar un mensaje de error
                            Snackbar.make(binding.activityMain, "El usuario o la contraseña no es correcta", Snackbar.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        binding.nuevoUsuarioBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasarPantalla = new Intent(MainActivity.this, RegistroActivity.class);
                finish();
                startActivity(pasarPantalla);
            }
        });

        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);

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
        } else {
            usuario = null;
        }

        return usuario;
    }

    @Override
    public void onAuthenticationSucceeded() {
        SharedPreferences.Editor editor_huella = prefs_huella.edit();
        SharedPreferences.Editor editor_user = prefs_user.edit();
        if (activacionHuella) {
            editor_huella.putString("HUELLA" + user.getNombre(), "SI");
            editor_huella.putString("PRIMERAVEZ" + user.getNombre(), "NO");
            editor_user.putString("FINGER", "SI");
            activacionHuella = false;
        }
        editor_huella.apply();
        editor_user.apply();
        String uiSecundaria = prefs_huella.getString("UISECUNDARIA" + user.getNombre(),"");
        if(uiSecundaria.equals("SI")){
            pasarPantalla = new Intent(MainActivity.this, MenuBottomActivity.class);

        }else{
            pasarPantalla = new Intent(MainActivity.this, MenuActivity.class);

        }
        pasarPantalla.putExtra("USUARIO", user);
        finish();
        startActivity(pasarPantalla);
    }

    @Override
    public void onAuthenticationFailed() {

    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        // Handle authentication error
        if (errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON && errorCode != BiometricPrompt.ERROR_USER_CANCELED) {
            if (activacionHuella) {
                pasarPantalla = new Intent(MainActivity.this, MenuActivity.class);
                pasarPantalla.putExtra("USUARIO", user);
                finish();
                startActivity(pasarPantalla);
            }
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(binding.activityMain, "Vuelva a intentarlo en 30 segundos", Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }


}