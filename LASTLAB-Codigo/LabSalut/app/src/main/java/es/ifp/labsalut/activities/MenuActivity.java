package es.ifp.labsalut.activities;


import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.view.GravityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;


import es.ifp.labsalut.R;
import es.ifp.labsalut.databinding.ActivityMenuBinding;
import es.ifp.labsalut.negocio.Usuario;

import es.ifp.labsalut.ui.CitasFragment;
import es.ifp.labsalut.ui.ColorStatusBar;
import es.ifp.labsalut.ui.HomeFragment;
import es.ifp.labsalut.ui.MedicamentosFragment;
import es.ifp.labsalut.ui.SettingsFragment;
import es.ifp.labsalut.ui.SuscripcionFragment;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Declaración de variables
    private ActivityMenuBinding binding;
    protected TextView nombreUsuario_nav;
    protected TextView email_nav;
    protected FloatingActionButton fab;
    private Bundle extras;
    private Usuario user = null;
    private String settingFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflar el layout usando View Binding
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configurar la barra de herramientas
        binding.appBarMain.toolbar.setPopupTheme(com.google.android.material.R.style.Widget_Material3_Light_ActionBar_Solid);
        binding.appBarMain.toolbar.setTitle("");

        // Configurar el color de la barra de estado
        ColorStatusBar.colorDinamicStatusBar(this, ColorStatusBar.obtenerColorToolbar(binding.appBarMain.toolbar));

        // Inicializar el FloatingActionButton
        fab = (FloatingActionButton) findViewById(R.id.fab);

        // Obtener la vista de cabecera del NavigationView
        View headerView = binding.navView.getHeaderView(0);
        nombreUsuario_nav = (TextView) headerView.findViewById(R.id.usuario_nav_header);
        email_nav = (TextView) headerView.findViewById(R.id.email_nav_header);

        // Obtener los extras del Intent
        extras = getIntent().getExtras();
        if (extras != null) {
            user = (Usuario) extras.getSerializable("USUARIO");
            settingFragment = extras.getString("SETTINGFRAGMENT");
        }

        // Configurar el listener para los elementos del NavigationView
        binding.navView.setNavigationItemSelectedListener(this);

        // Configurar el ActionBarDrawerToggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.appBarMain.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.md_theme_onPrimary));
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Configurar el color del título de la barra de herramientas
        binding.appBarMain.toolbarTitle.setTextColor(ColorStatusBar.obtenerColorBackground(this));

        // Manejar la lógica para mostrar el fragmento correcto según los extras
        if (settingFragment != null) {
            if (settingFragment.equals("SI")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.content_menu, SettingsFragment.newInstance(user)).commit();
                binding.navView.setCheckedItem(R.id.nav_menu);
                binding.appBarMain.toolbarTitle.setText("Ajustes");
            }
        } else {
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.content_menu, HomeFragment.newInstance(user)).commit();
                binding.navView.setCheckedItem(R.id.nav_menu);
                binding.appBarMain.toolbarTitle.setText("Menú principal");
            }
        }

        // Configurar el listener del FloatingActionButton
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.fab).show();
            }
        });

        // Configurar el callback para el botón de retroceso
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                } else if (!binding.appBarMain.toolbarTitle.getText().toString().equals("Menú principal")) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_menu, HomeFragment.newInstance(user)).commit();
                    binding.appBarMain.toolbarTitle.setText("Menú principal");
                } else {
                    finish();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);

        // Configurar el nombre y el email del usuario en el NavigationView
        nombreUsuario_nav.setText(user.getNombre());
        email_nav.setText(user.getEmail());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Manejar la selección de elementos en el NavigationView
        switch (item.getItemId()) {
            case R.id.nav_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_menu, HomeFragment.newInstance(user)).commit();
                binding.appBarMain.toolbarTitle.setText("Menú principal");
                break;
            case R.id.nav_medicamentos:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_menu, MedicamentosFragment.newInstance(user)).commit();
                binding.appBarMain.toolbarTitle.setText("Medicamentos");
                break;
            case R.id.nav_citas:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_menu, CitasFragment.newInstance(user)).commit();
                binding.appBarMain.toolbarTitle.setText("Citas Médicas");
                break;
            case R.id.nav_suscripcion:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_menu, SuscripcionFragment.newInstance(user)).commit();
                binding.appBarMain.toolbarTitle.setText("Suscripción");
                break;
            case R.id.nav_ajustes:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_menu, SettingsFragment.newInstance(user)).commit();
                binding.appBarMain.toolbarTitle.setText("Ajustes");
                break;
        }

        // Cerrar el drawer después de seleccionar un elemento
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
