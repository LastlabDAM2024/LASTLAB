package es.ifp.labsalut.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.biometric.BiometricPrompt;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import javax.crypto.SecretKey;

import es.ifp.labsalut.R;
import es.ifp.labsalut.databinding.ActivityMainBinding;
import es.ifp.labsalut.databinding.ActivityMenuBinding;
import es.ifp.labsalut.db.BaseDatos;
import es.ifp.labsalut.negocio.Usuario;
import es.ifp.labsalut.seguridad.CifradoAES;
import es.ifp.labsalut.seguridad.FingerprintHandler;
import es.ifp.labsalut.ui.CitasFragment;
import es.ifp.labsalut.ui.HomeFragment;
import es.ifp.labsalut.ui.MedicamentosFragment;
import es.ifp.labsalut.ui.SettingsFragment;
import es.ifp.labsalut.ui.SuscripcionFragment;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMenuBinding binding;
    protected TextView nombreUsuario_nav;
    protected TextView email_nav;
    protected FloatingActionButton fab;
    private Bundle extras;
    private Usuario user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.appBarMain.toolbar.setPopupTheme(com.google.android.material.R.style.Widget_Material3_Light_ActionBar_Solid);
        binding.appBarMain.toolbar.setTitle("");


        fab = (FloatingActionButton) findViewById(R.id.fab);
        View headerView = binding.navView.getHeaderView(0);
        nombreUsuario_nav = (TextView) headerView.findViewById(R.id.usuario_nav_header);
        email_nav = (TextView) headerView.findViewById(R.id.email_nav_header);
        extras = getIntent().getExtras();

        if (extras != null) {
            user = (Usuario) extras.getSerializable("USUARIO");
        }

        binding.navView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.appBarMain.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_menu, HomeFragment.newInstance(user)).commit();
            binding.navView.setCheckedItem(R.id.nav_menu);
            binding.appBarMain.toolbarTitle.setText("Menú principal");
        }

        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.fab).show();
            }
        });

        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                }else if(!binding.appBarMain.toolbarTitle.getText().toString().equals("Menú principal")){
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_menu, HomeFragment.newInstance(user)).commit();
                    binding.appBarMain.toolbarTitle.setText("Menú principal");
                }else{
                    finish();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);


        nombreUsuario_nav.setText(user.getNombre());
        email_nav.setText(user.getEmail());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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

        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }



}