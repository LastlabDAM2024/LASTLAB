package es.ifp.labsalut.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import java.io.Serializable;
import java.util.ArrayList;

import es.ifp.labsalut.R;
import es.ifp.labsalut.databinding.FragmentHomeBinding;
import es.ifp.labsalut.databinding.FragmentSettingsBinding;
import es.ifp.labsalut.negocio.CitaMedica;
import es.ifp.labsalut.negocio.Medicamento;
import es.ifp.labsalut.negocio.ModListAdapter;
import es.ifp.labsalut.negocio.Usuario;

public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "EMAIL";
    private static final String ARG_PARAM2 = "PASS";
    private static final String ARG_USER = "USUARIO";
    private FragmentHomeBinding binding;
    private String email;
    private String pass;

    private Usuario user = null;


    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static HomeFragment newInstance(Usuario user) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            email = getArguments().getString(ARG_PARAM1);
            pass = getArguments().getString(ARG_PARAM2);
            user = (Usuario) getArguments().getSerializable(ARG_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);
        Context context = requireContext();
        if (getArguments() != null) {
            email = getArguments().getString(ARG_PARAM1);
            pass = getArguments().getString(ARG_PARAM2);
            user = (Usuario) getArguments().getSerializable(ARG_USER);
        }
        ArrayList<Serializable> dataMed = new ArrayList<>();
        dataMed.add(new Medicamento("Paracetamol", "500mg", "Toma cada 8 horas", "5 min antes"));
        dataMed.add(new Medicamento("Ibuprofeno", "1000mg", "Toma cada 8 horas", "5 min antes"));
        dataMed.add(new Medicamento("Desketoprofeo", "200mg", "Toma cada 8 horas", "5 min antes"));

        ModListAdapter adapterMed = new ModListAdapter(context, dataMed);
        binding.listMediHome.setAdapter(adapterMed);

        ArrayList<Serializable> dataCita = new ArrayList<>();
        dataCita.add(new CitaMedica("Neurologo","12/05/2024","08:00","Ir en ayunas","24 horas antes"));
        dataCita.add(new CitaMedica("Endocrino","27/05/2024","09:35","Ir en ayunas","24 horas antes"));
        dataCita.add(new CitaMedica("Gastroscopia","12/08/2024","12:30","Ir en ayunas","24 horas antes"));

        ModListAdapter adapterCita = new ModListAdapter(context, dataCita);
        binding.listCitaHome.setAdapter(adapterCita);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}