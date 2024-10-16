package es.ifp.labsalut.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import es.ifp.labsalut.R;
import es.ifp.labsalut.databinding.FragmentSettingsBinding;
import es.ifp.labsalut.databinding.FragmentSuscripcionBinding;
import es.ifp.labsalut.negocio.Usuario;

public class SuscripcionFragment extends Fragment {
    private FragmentSuscripcionBinding binding;
    private static final String ARG_USER = "USUARIO";
    private Usuario user = null;

    // Constructor público requerido
    public SuscripcionFragment() {}


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
            user = (Usuario) getArguments().getSerializable(ARG_USER);
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Maneja el comportamiento al presionar el botón de retroceso del dispositivo
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_menu, HomeFragment.newInstance(user))
                        .addToBackStack(null)
                        .commit();

                // Actualiza el Navigation Drawer
                DrawerLayout drawerLayout = requireActivity().findViewById(R.id.drawer_layout);
                NavigationView navigationView = requireActivity().findViewById(R.id.nav_view);

                if (drawerLayout != null && navigationView != null) {
                    // Cerrar el drawer si está abierto
                    drawerLayout.closeDrawer(GravityCompat.START);

                    // Obtener el menú y marcar el elemento correcto
                    Menu menu = navigationView.getMenu();
                    MenuItem homeMenuItem = menu.findItem(R.id.nav_menu); // Asegúrate de que el ID del menú sea correcto
                    if (homeMenuItem != null) {
                        homeMenuItem.setChecked(true);
                    }
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), onBackPressedCallback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
