package es.ifp.labsalut.ui;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import es.ifp.labsalut.R;
import es.ifp.labsalut.negocio.Usuario;

public class SettingsFragment extends Fragment {

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
        String huellaActiva = prefs.getString("HUELLA",null);
        if (huellaActiva!=null){
            if(huellaActiva.equals("SI")){
                huella.setChecked(true);
            }else{
                huella.setChecked(false);
            }
        }

        huella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = requireContext();
                SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_HUELLA, MODE_PRIVATE).edit();
                if (huella.isChecked()) {
                    editor.putString("HUELLA", "SI");
                } else {
                    editor.putString("HUELLA", "NO");
                }
                editor.apply();
            }
        });
    }


    public void onDestroyView() {
        super.onDestroyView();
    }
}