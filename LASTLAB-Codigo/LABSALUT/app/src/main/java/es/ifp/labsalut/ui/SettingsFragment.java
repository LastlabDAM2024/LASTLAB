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
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.Fragment;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;

import es.ifp.labsalut.activities.MenuBottomActivity;
import es.ifp.labsalut.activities.MenuActivity;
import es.ifp.labsalut.databinding.FragmentSettingsBinding;
import es.ifp.labsalut.negocio.Usuario;
import es.ifp.labsalut.seguridad.FingerprintHandler;

public class SettingsFragment extends Fragment implements FingerprintHandler.AuthenticationCallback {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_USER = "USUARIO";
    public static final String MY_PREFS_HUELLA = "OPENHUELLA";
    private FragmentSettingsBinding binding;
    private String mParam1;
    private String mParam2;
    private Intent pasarPantalla;
    private Usuario user = null;
    private SharedPreferences prefs_user = null;
    private SharedPreferences prefs_huella = null;


    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            user = (Usuario) getArguments().getSerializable(ARG_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);
        Activity activity = getActivity();
        Context context = requireContext();
        try {
            // Crear MasterKey para EncryptedSharedPreferences
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            // Crear EncryptedSharedPreferences para MY_PREFS_USER
            prefs_user = EncryptedSharedPreferences.create(
                    context,
                    MY_PREFS_USER,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            // Crear EncryptedSharedPreferences para MY_PREFS_HUELLA
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

        String huellaActiva = prefs_huella.getString("HUELLA" + user.getNombre(), "");
        if (huellaActiva.equals("SI")) {
            binding.checkHuellaSett.setChecked(true);
        } else {
            binding.checkHuellaSett.setChecked(false);
        }
        reiniciarHuellaListener(activity);

        String uiSecudaria = prefs_huella.getString("UISECUNDARIA" + user.getNombre(), "");

        if (uiSecudaria.equals("SI")) {
            binding.checkUISett.setChecked(true);
        } else {
            binding.checkUISett.setChecked(false);
        }
        uiListener(activity);
    }

    @Override
    public void onAuthenticationSucceeded() {
        Context context = requireContext();
        SharedPreferences.Editor editor_huella = prefs_huella.edit();
        SharedPreferences.Editor editor_user = prefs_user.edit();

        if (binding.checkHuellaSett.isChecked()) {
            editor_huella.putString("HUELLA" + user.getNombre(), "SI");
            editor_user.putString("FINGER", "SI");

        } else {
            editor_huella.putString("HUELLA" + user.getNombre(), "NO");
            editor_user.putString("FINGER", "NO");
        }
        editor_huella.apply();
        editor_user.apply();

    }

    @Override
    public void onAuthenticationFailed() {

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
                    if (binding.checkHuellaSett.isChecked()) {
                        binding.checkHuellaSett.setChecked(false);
                    } else {
                        binding.checkHuellaSett.setChecked(true);
                    }
                    reiniciarHuellaListener(activity);
                    if (errorCode == BiometricPrompt.ERROR_TIMEOUT || errorCode == BiometricPrompt.ERROR_LOCKOUT) {
                        Toast.makeText(activity, "Vuelva a intentarlo en 30 segundos", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }

    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    private void reiniciarHuellaListener(Activity activity) {
        binding.checkHuellaSett.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

                    FingerprintHandler finger = new FingerprintHandler(activity, SettingsFragment.this);
                    finger.startAuth();
                }
            }
        });
    }

    private void uiListener(Activity activity) {
        binding.checkUISett.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor_huella = prefs_huella.edit();
                if (binding.checkUISett.isChecked()) {
                    editor_huella.putString("UISECUNDARIA" + user.getNombre(), "SI");
                    pasarPantalla = new Intent(activity, MenuBottomActivity.class);
                    pasarPantalla.putExtra("USUARIO", user);
                    pasarPantalla.putExtra("SETTINGFRAGMENT","SI");
                    activity.finish();
                    activity.startActivity(pasarPantalla);
                } else {
                    editor_huella.putString("UISECUNDARIA" + user.getNombre(), "NO");
                    pasarPantalla = new Intent(activity, MenuActivity.class);
                    pasarPantalla.putExtra("USUARIO", user);
                    pasarPantalla.putExtra("SETTINGFRAGMENT","SI");
                    activity.finish();
                    activity.startActivity(pasarPantalla);
                }
                editor_huella.apply();
            }
        });
    }

}