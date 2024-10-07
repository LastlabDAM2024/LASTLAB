package es.ifp.labsalut.ui;

import static es.ifp.labsalut.activities.MainActivity.MY_PREFS_USER;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.Fragment;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import com.google.android.material.snackbar.Snackbar;
import java.io.IOException;
import java.security.GeneralSecurityException;
import es.ifp.labsalut.activities.MenuBottomActivity;
import es.ifp.labsalut.activities.MenuActivity;
import es.ifp.labsalut.databinding.FragmentSettingsBinding;
import es.ifp.labsalut.negocio.Usuario;
import es.ifp.labsalut.seguridad.FingerprintHandler;

public class SettingsFragment extends Fragment implements FingerprintHandler.AuthenticationCallback {

    private static final String ARG_USER = "USUARIO";
    public static final String MY_PREFS_HUELLA = "OPENHUELLA";
    private FragmentSettingsBinding binding;
    private Usuario user = null;
    private SharedPreferences prefs_user = null;
    private SharedPreferences prefs_huella = null;

    public SettingsFragment() {}

    public static SettingsFragment newInstance(Usuario user) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (Usuario) getArguments().getSerializable(ARG_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Activity activity = getActivity();
        Context context = requireContext();

        initializeSharedPreferences(context);
        setupSwitches(activity);
        setupButtons();
    }

    private void initializeSharedPreferences(Context context) {
        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            prefs_user = EncryptedSharedPreferences.create(
                    context,
                    MY_PREFS_USER,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            prefs_huella = EncryptedSharedPreferences.create(
                    context,
                    MY_PREFS_HUELLA,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    private void setupSwitches(Activity activity) {
        String huellaActiva = prefs_huella.getString("HUELLA" + user.getNombre(), "NO");
        binding.checkHuellaSett.setChecked(huellaActiva.equals("SI"));
        reiniciarHuellaListener(activity);

        String uiSecundaria = prefs_huella.getString("UISECUNDARIA" + user.getNombre(), "NO");
        binding.checkUISett.setChecked(uiSecundaria.equals("SI"));
        uiListener(activity);
    }

    private void setupButtons() {
        binding.userSett.setOnClickListener(v -> {
            // Implementar la lógica para las configuraciones de usuario
            Toast.makeText(getContext(), "Configuraciones de usuario", Toast.LENGTH_SHORT).show();
        });

        binding.appintSett.setOnClickListener(v -> {
            // Implementar la lógica para las configuraciones de citas
            Toast.makeText(getContext(), "Configuraciones de citas", Toast.LENGTH_SHORT).show();
        });

        binding.mediSett.setOnClickListener(v -> {
            // Implementar la lógica para las configuraciones de medicamentos
            Toast.makeText(getContext(), "Configuraciones de medicamentos", Toast.LENGTH_SHORT).show();
        });
    }

    // Los métodos onAuthenticationSucceeded, onAuthenticationFailed, onAuthenticationError
    // se mantienen igual que en tu código original

    private void reiniciarHuellaListener(Activity activity) {
        binding.checkHuellaSett.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                FingerprintHandler finger = new FingerprintHandler(activity, SettingsFragment.this);
                finger.startAuth();
            }
        });
    }

    private void uiListener(Activity activity) {
        binding.checkUISett.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor_huella = prefs_huella.edit();
            Intent pasarPantalla;
            if (isChecked) {
                editor_huella.putString("UISECUNDARIA" + user.getNombre(), "SI");
                pasarPantalla = new Intent(activity, MenuBottomActivity.class);
            } else {
                editor_huella.putString("UISECUNDARIA" + user.getNombre(), "NO");
                pasarPantalla = new Intent(activity, MenuActivity.class);
            }
            editor_huella.apply();
            pasarPantalla.putExtra("USUARIO", user);
            pasarPantalla.putExtra("SETTINGFRAGMENT", "SI");
            activity.finish();
            activity.startActivity(pasarPantalla);
        });
    }

    @Override
    public void onAuthenticationSucceeded() {
        Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Context context = requireContext();
                    SharedPreferences.Editor editor_huella = prefs_huella.edit();
                    SharedPreferences.Editor editor_user = prefs_user.edit();

                    if (binding.checkHuellaSett.isChecked()) {
                        editor_huella.putString("HUELLA" + user.getNombre(), "SI");
                        editor_user.putString("FINGER", "SI");
                        Toast.makeText(context, "Huella digital activada", Toast.LENGTH_SHORT).show();
                    } else {
                        editor_huella.putString("HUELLA" + user.getNombre(), "NO");
                        editor_user.putString("FINGER", "NO");
                        Toast.makeText(context, "Huella digital desactivada", Toast.LENGTH_SHORT).show();
                    }
                    editor_huella.apply();
                    editor_user.apply();
                }
            });
        }
    }

    @Override
    public void onAuthenticationFailed() {
        Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Context context = requireContext();
                    Toast.makeText(context, "Autenticación fallida. Intente de nuevo.", Toast.LENGTH_SHORT).show();

                    // Revertir el estado del switch
                    binding.checkHuellaSett.setChecked(!binding.checkHuellaSett.isChecked());
                }
            });
        }
    }

        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            Activity activity = getActivity();

            if (activity != null) {
                // Ejecutamos el código en el hilo principal de la actividad
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.checkHuellaSett.setOnCheckedChangeListener(null);
                        binding.checkHuellaSett.setChecked(!binding.checkHuellaSett.isChecked());
                        reiniciarHuellaListener(activity);

                        if (errorCode == BiometricPrompt.ERROR_LOCKOUT || errorCode == BiometricPrompt.ERROR_LOCKOUT_PERMANENT) {
                            Snackbar.make(binding.fragmentSettings, "Demasiados intentos. Intente más tarde.", Snackbar.LENGTH_LONG).show();
                        } else if (errorCode != BiometricPrompt.ERROR_USER_CANCELED && errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                            Snackbar.make(binding.fragmentSettings, "Error: " + errString, Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }

    }
