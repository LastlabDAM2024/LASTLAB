package es.ifp.labsalut.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import es.ifp.labsalut.R;
import es.ifp.labsalut.databinding.FragmentSettingsBinding;
import es.ifp.labsalut.databinding.FragmentSuscripcionBinding;
import es.ifp.labsalut.negocio.Usuario;

public class SuscripcionFragment extends Fragment {
    private FragmentSuscripcionBinding binding;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_USER = "USUARIO";

    // Parámetros de instancia
    private String mParam1;
    private String mParam2;
    private Usuario user = null;

    // Constructor público requerido
    public SuscripcionFragment() {}

    // Método de fábrica para crear una nueva instancia del fragmento utilizando parámetros de tipo String
    public static SuscripcionFragment newInstance(String param1, String param2) {
        SuscripcionFragment fragment = new SuscripcionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    // Método de fábrica para crear una nueva instancia del fragmento utilizando un objeto Usuario
    public static SuscripcionFragment newInstance(Usuario user) {
        SuscripcionFragment fragment = new SuscripcionFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Recupera los argumentos pasados al fragmento
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla el diseño para este fragmento utilizando ViewBinding
        binding = FragmentSuscripcionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
