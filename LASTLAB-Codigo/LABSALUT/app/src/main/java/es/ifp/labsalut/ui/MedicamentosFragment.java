package es.ifp.labsalut.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import es.ifp.labsalut.R;
import es.ifp.labsalut.databinding.FragmentMedicamentosBinding;
import es.ifp.labsalut.databinding.FragmentSettingsBinding;
import es.ifp.labsalut.negocio.Usuario;

public class MedicamentosFragment extends Fragment {

    // Argumentos estáticos para pasar parámetros al fragmento
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_USER = "USUARIO";

    // Enlace de vista para el fragmento
    private FragmentMedicamentosBinding binding;

    // Variables para almacenar los parámetros pasados
    private String mParam1;
    private String mParam2;

    // Constructor vacío requerido
    public MedicamentosFragment() {
    }

    // Método estático para crear una nueva instancia del fragmento con dos parámetros
    public static MedicamentosFragment newInstance(String param1, String param2) {
        MedicamentosFragment fragment = new MedicamentosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    // Método estático para crear una nueva instancia del fragmento con un usuario
    public static MedicamentosFragment newInstance(Usuario user) {
        MedicamentosFragment fragment = new MedicamentosFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    // Método llamado cuando se crea el fragmento
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // Método para crear y devolver la vista asociada al fragmento
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMedicamentosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    // Método llamado cuando la vista del fragmento se destruye
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Aquí se pueden liberar recursos si es necesario
    }
}
