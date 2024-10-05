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


/**
 * La clase MenuBottomActivity extiende AppCompatActivity y es responsable de gestionar la navegación en la
 * aplicación mediante una barra de navegación inferior (Bottom Navigation Bar).
 *
 * Esta actividad permite a los usuarios acceder a diferentes secciones de la aplicación mediante la selección
 * de fragmentos que representan las siguientes características:
 *
 * 1. **View Binding**:
 *    - Utiliza la clase generada `ActivityMainBottomBinding` para acceder a los elementos del diseño (layout)
 *      de manera más eficiente y segura, evitando el uso de `findViewById()`.
 *
 * 2. **Manejo de Usuario**:
 *    - Recupera información del usuario a través de un objeto `Usuario` que se pasa a través de un "Intent" al
 *      iniciar la actividad. Esto permite personalizar la experiencia del usuario en la aplicación.
 *
 * 3. **Navegación**:
 *    - Configura un listener para la barra de navegación inferior que detecta qué elemento ha sido seleccionado
 *      por el usuario y reemplaza el fragmento visible en la pantalla. Los fragmentos que se pueden seleccionar son:
 *        - `HomeFragment`: Pantalla principal de la aplicación.
 *        - `MedicamentosFragment`: Sección dedicada a la gestión de medicamentos.
 *        - `CitasFragment`: Sección donde se gestionan las citas médicas.
 *        - `SettingsFragment`: Sección de configuración de la aplicación.
 *
 * 4. **Transiciones de Fragmentos**:
 *    - Al seleccionar un elemento en la barra de navegación, la actividad utiliza `FragmentManager` y `FragmentTransaction`
 *      para reemplazar el fragmento actual con el fragmento correspondiente al elemento seleccionado, asegurando
 *      que la interfaz de usuario se actualice correctamente.
 *
 * 5. **Selección Inicial de Fragmento**:
 *    - Al iniciar la actividad, se determina qué fragmento se debe mostrar inicialmente en función del valor de
 *      la variable `settingFragment`. Si este valor es "SI", se selecciona el `SettingsFragment`; de lo contrario,
 *      se selecciona el `HomeFragment`.
 *
 * `MenuBottomActivity` lo que hace es gestionar la navegación principal de la aplicación a través de una interfaz
 * de usuario intuitiva que permite a los usuarios acceder a diferentes funcionalidades mediante una barra de
 * navegación inferior. Esta estructura mejora la experiencia del usuario y organiza la navegación en la
 * aplicación de manera eficiente.
 */

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
