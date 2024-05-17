package es.ifp.labsalut.ui;

import static android.content.Context.MODE_PRIVATE;

import static es.ifp.labsalut.MainActivity.MY_PREFS_USER;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import es.ifp.labsalut.R;
import es.ifp.labsalut.negocio.Usuario;
import es.ifp.labsalut.seguridad.FingerprintHandler;

public class SettingsFragment extends Fragment implements FingerprintHandler.AuthenticationCallback {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_USER = "USUARIO";
    public static final String MY_PREFS_HUELLA = "OPENHUELLA";


    protected Switch huella;
    private String mParam1;
    private String mParam2;
    private Usuario user = null;


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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);
        huella = (Switch) root.findViewById(R.id.switchHuella_ajustes);
        Context context = requireContext();
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_HUELLA, MODE_PRIVATE);
        String huellaActiva = prefs.getString("HUELLA"+user.getNombre(), null);
        if (huellaActiva != null) {
            if (huellaActiva.equals("SI")) {
                huella.setChecked(true);
            } else {
                huella.setChecked(false);
            }
        }
        reiniciarHuellaListener(getActivity());
    }

    @Override
    public void onAuthenticationSucceeded() {
        Context context = requireContext();
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_HUELLA, MODE_PRIVATE).edit();
        SharedPreferences.Editor editor_user = context.getSharedPreferences(MY_PREFS_USER, MODE_PRIVATE).edit();

        if (huella.isChecked()) {
            editor.putString("HUELLA"+user.getNombre(), "SI");
            editor_user.putString("FINGER", "SI");

        } else {
            editor.putString("HUELLA"+user.getNombre(), "NO");
            editor_user.putString("FINGER", "NO");
        }
        editor.apply();
        editor_user.apply();

    }

    @Override
    public void onAuthenticationFailed() {
        Activity activity = getActivity();

        if (activity != null) {
            // Ejecutamos el código en el hilo principal de la actividad
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    huella.setOnCheckedChangeListener(null);
                    if (huella.isChecked()) {
                        huella.setChecked(false);
                    } else {
                        huella.setChecked(true);
                    }
                    reiniciarHuellaListener(activity);
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
                    huella.setOnCheckedChangeListener(null);
                    if (huella.isChecked()) {
                        huella.setChecked(false);
                    } else {
                        huella.setChecked(true);
                    }
                    reiniciarHuellaListener(activity);
                }
            });
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    private void reiniciarHuellaListener(Activity activity) {
        huella.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

                    FingerprintHandler finger = new FingerprintHandler(activity, SettingsFragment.this);
                    finger.startAuth();
                }
            }
        });
    }

}