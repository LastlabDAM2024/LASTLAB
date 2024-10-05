package es.ifp.labsalut.seguridad;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
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
import java.util.concurrent.Executors;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * La clase FingerprintHandler gestiona la autenticación biométrica en dispositivos Android
 * utilizando huellas dactilares. Esta implementación se basa en la API de BiometricPrompt
 * para ofrecer una experiencia de usuario segura y fluida al acceder a la aplicación.
 *
 * La clase proporciona métodos para iniciar el proceso de autenticación, generar
 * claves criptográficas y manejar el cifrado de datos utilizando el almacén de claves de
 * Android (AndroidKeyStore).
 *
 * Características principales:
 * - **Autenticación Biométrica**: Permite a los usuarios desbloquear la aplicación
 *   utilizando su huella dactilar, mejorando la seguridad de la aplicación.
 * - **Gestión de Claves**: Genera y almacena claves criptográficas en el AndroidKeyStore,
 *   asegurando que las claves nunca abandonen el dispositivo.
 * - **Callbacks de Autenticación**: Proporciona un mecanismo para manejar eventos de
 *   autenticación, permitiendo a los desarrolladores reaccionar a resultados exitosos o
 *   fallidos.
 *
 * Métodos destacados:
 * - `startAuth()`: Inicia el proceso de autenticación biométrica.
 * - `generateKey()`: Genera una clave criptográfica en el AndroidKeyStore.
 * - `cipherInit()`: Inicializa el cifrado utilizando la clave generada.
 *
 */

@RequiresApi(api = Build.VERSION_CODES.P)
public class FingerprintHandler extends BiometricPrompt.AuthenticationCallback {

    private Context context; // Contexto de la aplicación
    private Executor executor; // Ejecutor para manejar hilos
    private KeyStore keyStore; // Almacen de claves
    private static final String KEY_NAME = "lectorHuella"; // Nombre de la clave
    private Cipher cipher; // Cifrado
    private AuthenticationCallback callback; // Callback para manejar eventos de autenticación

    // Interface para callbacks de autenticación
    public interface AuthenticationCallback {
        void onAuthenticationSucceeded(); // Método llamado cuando la autenticación es exitosa
        void onAuthenticationFailed(); // Método llamado cuando la autenticación falla
        void onAuthenticationError(int errorCode, CharSequence errString); // Método llamado cuando ocurre un error en la autenticación
    }

    // Constructor
    public FingerprintHandler(Context mContext, AuthenticationCallback cback) {
        context = mContext;
        callback = cback;
        executor = Executors.newSingleThreadExecutor(); // Inicializa el ejecutor con un solo hilo
    }

    // Método para iniciar la autenticación biométrica
    public void startAuth() {
        // Verifica el permiso para usar la biometría
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        generateKey(); // Genera la clave
        cipherInit(); // Inicializa el cifrado
        FragmentActivity activity = (FragmentActivity) context;

        // Crea una instancia de BiometricPrompt
        BiometricPrompt biometricPrompt = new BiometricPrompt(activity, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                callback.onAuthenticationError(errorCode, errString); // Llama al callback de error
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                callback.onAuthenticationFailed(); // Llama al callback de fallo
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                callback.onAuthenticationSucceeded(); // Llama al callback de éxito
            }
        });

        // Configura la información del prompt
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Desbloquea LabSalut para poder usarlo")
                .setNegativeButtonText("Cancelar")
                .build();

        // Inicia la autenticación biométrica
        biometricPrompt.authenticate(promptInfo, new BiometricPrompt.CryptoObject(cipher));
    }

    // Método para generar la clave en el AndroidKeyStore
    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore"); // Obtiene una instancia del KeyStore
        } catch (Exception e) {
            e.printStackTrace();
        }

        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore"); // Obtiene una instancia del KeyGenerator
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Error al obtener la instancia de KeyGenerator", e);
        }

        try {
            keyStore.load(null); // Carga el KeyStore
            keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey(); // Genera la clave
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException |
                 IOException | CertificateException e) {
            throw new RuntimeException(e);
        }
    }

    // Método para inicializar el cifrado
    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7); // Obtiene una instancia del cifrado
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Error al obtener el cifrado", e);
        }

        try {
            keyStore.load(null); // Carga el KeyStore
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME, null); // Obtiene la clave
            cipher.init(Cipher.ENCRYPT_MODE, key); // Inicializa el cifrado en modo de encriptación
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException |
                 IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Error al iniciar el cifrado", e);
        }
    }
}
