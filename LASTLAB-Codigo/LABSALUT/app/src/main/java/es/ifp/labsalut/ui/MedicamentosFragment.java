package es.ifp.labsalut.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import javax.crypto.SecretKey;

import es.ifp.labsalut.R;
import es.ifp.labsalut.databinding.FragmentMedicamentosBinding;
import es.ifp.labsalut.db.BaseDatos;
import es.ifp.labsalut.negocio.Medicamento;
import es.ifp.labsalut.negocio.Usuario;
import es.ifp.labsalut.seguridad.CifradoAES;

public class MedicamentosFragment extends Fragment {

    // Argumentos estáticos para pasar parámetros al fragmento
    private static final String ARG_USER = "USUARIO";

    // Enlace de vista para el fragmento
    private FragmentMedicamentosBinding binding;
    private Usuario user = null;
    private BaseDatos db;
    private CifradoAES aes;

    // Constructor vacío requerido
    public MedicamentosFragment() {
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
            user = (Usuario) getArguments().getSerializable(ARG_USER);
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

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);
        Context context = root.getContext();
        db = new BaseDatos(context);

        binding.checkRecordMed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.textRecordMed.setEnabled(true);
                } else {
                    binding.textRecordMed.setEnabled(false);
                    binding.recordMed.setText("");
                }
            }
        });

        binding.guardarMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Medicamento medicamento = new Medicamento();

                medicamento.setIdMedicamento(user.getAllMedicamentos().size()+1);
                medicamento.setNombre(binding.nombreMed.getText().toString());
                medicamento.setDosis(binding.dosisMed.getText().toString());
                medicamento.setFrecuencia(binding.frecuenciaMed.getText().toString());
                medicamento.setRecordatorio(binding.recordMed.getText().toString());

                aes = new CifradoAES();
                String semilla = user.getEmail() + user.getContrasena();
                SecretKey secretKey = aes.generarSecretKey(semilla);
                String encryptNombreMed = "";
                String encryptDosisMed = "";
                String encryptFrecuenciaMed = "";
                String encryptRecordMed = "";

                // Cifrado de datos
                try {
                    // Encripta los datos del usuario
                    encryptNombreMed = aes.encrypt(medicamento.getNombre().getBytes(), secretKey);
                    encryptDosisMed = aes.encrypt(medicamento.getDosis().getBytes(), secretKey);
                    encryptFrecuenciaMed = aes.encrypt(medicamento.getFrecuencia().getBytes(), secretKey);
                    encryptRecordMed = aes.encrypt(medicamento.getRecordatorio().getBytes(), secretKey);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                Medicamento encrypMed = new Medicamento(medicamento.getIdMedicamento(),encryptNombreMed,encryptDosisMed,encryptFrecuenciaMed,encryptRecordMed);
                user.setMedicamento(medicamento);
                encrypMed.setIdMedicamento(medicamento.getIdMedicamento());
                db.addMedicamento(encrypMed);
                db.addUserMedi(user, encrypMed);

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
        });

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
                    AppCompatActivity activity = SingletonManager.getCurrentActivity();
                    TextView toolbarText = activity.findViewById(R.id.app_bar_main).findViewById(R.id.toolbar).findViewById(R.id.toolbar_title);
                    toolbarText.setText("Menú Principal");

                    if (homeMenuItem != null) {
                        homeMenuItem.setChecked(true);
                    }
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), onBackPressedCallback);
    }


    // Método llamado cuando la vista del fragmento se destruye
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        // Aquí se pueden liberar recursos si es necesario
    }
}
