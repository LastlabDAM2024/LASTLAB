package es.ifp.labsalut.seguridad;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import es.ifp.labsalut.R;

@RequiresApi(api = Build.VERSION_CODES.P)
public class FingerprintHandler extends android.hardware.biometrics.BiometricPrompt.AuthenticationCallback {

    private Context context;
    private Executor executor;

    // Constructor
    public FingerprintHandler(Context mContext) {
        context = mContext;
        executor = Executors.newSingleThreadExecutor();
    }

    public void startAuth(BiometricPrompt.CryptoObject cryptoObject) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        FragmentActivity activity = (FragmentActivity) context;

        BiometricPrompt biometricPrompt = new BiometricPrompt(activity, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                update("Error de autenticación: \n" + errString, false);
            }

            @Override
            public void onAuthenticationFailed() {
                update("Autenticacion fallida", false);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                update("Autenticacion satisfactoria", true);
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("LastLab")
                .setNegativeButtonText("Usar email y contraseña")
                .build();

        biometricPrompt.authenticate(promptInfo, cryptoObject);
    }

    public void update(String e, Boolean success){
        TextView textView = (TextView) ((Activity) context).findViewById(R.id.errorText_finger);
        textView.setText(e);
        if(success){
            textView.setTextColor(ContextCompat.getColor(context, R.color.successText));
        }
    }
}
