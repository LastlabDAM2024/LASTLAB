package es.ifp.labsalut.ui;

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
import es.ifp.labsalut.R;
import es.ifp.labsalut.negocio.Usuario;

public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "EMAIL";
    private static final String ARG_PARAM2 = "PASS";
    private static final String ARG_USER = "USUARIO";
    private String email;
    private String pass;
    protected Button crearMedicamento;
    protected Button crearCitaMedica;
    protected Button editarMedicamento;
    protected Button editarCitaMedica;
    protected ListView listaMedicamentos;
    protected ListView listaCitas;
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
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);
        crearMedicamento = (Button) root.findViewById(R.id.crearMedicamentoBoton_menu);
        crearCitaMedica = (Button) root.findViewById(R.id.crearCitaMedicaBoton_menu);
        editarMedicamento = (Button) root.findViewById(R.id.editarMedicamentoBoton_main);
        editarCitaMedica = (Button) root.findViewById(R.id.editarCitaMedicaBoton_main);
        listaMedicamentos = (ListView) root.findViewById(R.id.listaMedicamento_menu);
        listaCitas = (ListView) root.findViewById(R.id.listaCitaMedica_Menu);
        if (getArguments() != null) {
            email = getArguments().getString(ARG_PARAM1);
            pass = getArguments().getString(ARG_PARAM2);
            user = (Usuario) getArguments().getSerializable(ARG_USER);

        }

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}