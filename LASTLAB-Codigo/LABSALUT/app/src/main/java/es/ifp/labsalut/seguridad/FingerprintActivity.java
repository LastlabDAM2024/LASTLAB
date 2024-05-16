package es.ifp.labsalut.seguridad;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.concurrent.Executor;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import es.ifp.labsalut.MainActivity;
import es.ifp.labsalut.MenuActivity;
import es.ifp.labsalut.R;
import es.ifp.labsalut.negocio.Usuario;

public class FingerprintActivity extends AppCompatActivity {

    private KeyStore keyStore;
    private static final String KEY_NAME = "pruebaHuella";
    private Cipher cipher;
    private TextView textView;
    private Executor executor;
    private Bundle extras;
    private Usuario user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint);

        textView = findViewById(R.id.errorText_finger);

        executor = ContextCompat.getMainExecutor(this);

        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        BiometricManager biometricManager = BiometricManager.from(this);

        switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                textView.setText("El sensor biometrico está disponible");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                textView.setText("Este dispositivo no tiene funciones biometricas");
                return;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                textView.setText("Las funciones biometricas no están disponibles actualmente");
                return;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                textView.setText("No hay credenciales biometricas en el dispositivo");
                return;
            default:
                textView.setText("El sensor biometrico no está disponible");
                return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED) {
            textView.setText("El permiso para la autenticación biometrica no está habilitado");
        } else {
            if (!keyguardManager.isKeyguardSecure()) {
                textView.setText("La seguridad de pantalla de bloqueo no esta habilitada en ajustes");
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    generateKey();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (cipherInit()) {
                        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                                .setTitle("LastLab")
                                .setNegativeButtonText("Usar email y contraseña")
                                .build();

                        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
                            @Override

                            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                                super.onAuthenticationError(errorCode, errString);
                                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                                    Intent pasarPantalla = new Intent(FingerprintActivity.this, MainActivity.class);
                                    pasarPantalla.putExtra("HUELLA","SI");
                                    finish();
                                    startActivity(pasarPantalla);
                                } else {
                                    textView.setText("Error de autenticación: " + errString);
                                }
                            }

                            @Override
                            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                                super.onAuthenticationSucceeded(result);
                                textView.setText("Autenticacion satisfactoria");
                                extras = getIntent().getExtras();

                                if (extras != null) {
                                    user = (Usuario) extras.getSerializable("USUARIO");
                                }
                                Intent pasarPantalla = new Intent(FingerprintActivity.this, MenuActivity.class);
                                pasarPantalla.putExtra("USUARIO", user);
                                finish();
                                startActivity(pasarPantalla);
                            }

                            @Override
                            public void onAuthenticationFailed() {
                                super.onAuthenticationFailed();
                                textView.setText("Autenticacion fallida");
                            }
                        });

                        biometricPrompt.authenticate(promptInfo, new BiometricPrompt.CryptoObject(cipher));
                    }
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }

        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Error al obtener la instancia de KeyGenerator", e);
        }

        try {
            keyStore.load(null);
            keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException |
                 IOException | CertificateException e) {
            throw new RuntimeException(e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Error al obtener el cifrado", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException |
                 IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Error al iniciar el cifrado", e);
        }
    }
}