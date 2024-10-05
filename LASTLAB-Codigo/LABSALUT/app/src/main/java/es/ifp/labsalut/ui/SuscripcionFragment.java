package es.ifp.labsalut.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import es.ifp.labsalut.R;
import es.ifp.labsalut.databinding.FragmentSuscripcionBinding;
import es.ifp.labsalut.negocio.Usuario;

public class SuscripcionFragment extends Fragment {
    private FragmentSuscripcionBinding binding;
    private static final String ARG_USER = "USUARIO";

    private Usuario user = null;

    public SuscripcionFragment() {}

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
        if (getArguments() != null) {
            user = (Usuario) getArguments().getSerializable(ARG_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSuscripcionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Configura el botón de suscripción
        binding.suscripSuscrip.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AgregarSuscripcionActivity.class);
            startActivity(intent);
        });

        // Configura el botón de cancelar suscripción
        binding.cancelarSuscrip.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CancelarSuscripcionActivity.class);
            startActivity(intent);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
