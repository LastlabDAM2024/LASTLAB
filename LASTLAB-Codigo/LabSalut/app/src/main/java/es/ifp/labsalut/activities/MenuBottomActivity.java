package es.ifp.labsalut.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationBarView;

import es.ifp.labsalut.R;
import es.ifp.labsalut.databinding.ActivityMainBottomBinding;
import es.ifp.labsalut.databinding.FragmentSettingsBinding;
import es.ifp.labsalut.negocio.Usuario;
import es.ifp.labsalut.ui.CitasFragment;
import es.ifp.labsalut.ui.HomeFragment;
import es.ifp.labsalut.ui.MedicamentosFragment;
import es.ifp.labsalut.ui.SettingsFragment;
public class MenuBottomActivity extends AppCompatActivity {

    // Declaración de variables
    private ActivityMainBottomBinding binding;
    private Usuario user = null;
    private Bundle extras;
    private String settingFragment = "NO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflar el layout usando View Binding
        binding = ActivityMainBottomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtener los extras del Intent
        extras = getIntent().getExtras();
        if (extras != null) {
            user = (Usuario) extras.getSerializable("USUARIO");
            if (extras.size() > 1) {
                settingFragment = extras.getString("SETTINGFRAGMENT");
            }
        }

        // Configurar el listener para la navegación de la barra inferior
        binding.navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                // Seleccionar el fragmento basado en el item seleccionado
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        selectedFragment = HomeFragment.newInstance(user);
                        break;
                    case R.id.navigation_medicamentos:
                        selectedFragment = MedicamentosFragment.newInstance(user);
                        break;
                    case R.id.navigation_citas:
                        selectedFragment = CitasFragment.newInstance(user);
                        break;
                    case R.id.navigation_settings:
                        selectedFragment = SettingsFragment.newInstance(user);
                        break;
                }

                // Reemplazar el fragmento en el contenedor
                if (selectedFragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, selectedFragment);
                    fragmentTransaction.commit();
                } else {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, HomeFragment.newInstance(user));
                    fragmentTransaction.commit();
                }
                return true;
            }
        });

        // Seleccionar el fragmento inicial basado en el valor de settingFragment
        if (settingFragment.equals("SI")) {
            binding.navView.setSelectedItemId(R.id.navigation_settings);
        } else if (settingFragment.equals("NO")) {
            binding.navView.setSelectedItemId(R.id.navigation_home);
        }
    }
}
