package es.ifp.labsalut;


import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import javax.crypto.SecretKey;

import es.ifp.labsalut.db.BaseDatos;
import es.ifp.labsalut.negocio.Usuario;
import es.ifp.labsalut.seguridad.CifradoAES;
import es.ifp.labsalut.ui.CitasFragment;
import es.ifp.labsalut.ui.HomeFragment;
import es.ifp.labsalut.ui.MedicamentosFragment;
import es.ifp.labsalut.ui.SettingsFragment;
import es.ifp.labsalut.ui.SuscripcionFragment;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawerLayout;
    protected FloatingActionButton fab;
    protected TextView titulo;
    protected TextView nombreUsuario_nav;
    protected TextView email_nav;
    private Bundle extras;
    private String email;
    private String pass;
    private BaseDatos db;
    private final Usuario user = new Usuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        titulo = (TextView) findViewById(R.id.toolbar_title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        fab = (FloatingActionButton) findViewById(R.id.fab);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        nombreUsuario_nav = (TextView) headerView.findViewById(R.id.usuario_nav_header);
        email_nav = (TextView) headerView.findViewById(R.id.email_nav_header);

        extras = getIntent().getExtras();

        if (extras != null) {
            email = extras.getString("EMAIL");
            pass = extras.getString("PASS");
            recuperarUsuario();
        }

        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_menu, HomeFragment.newInstance(user)).commit();
            navigationView.setCheckedItem(R.id.nav_menu);
            titulo.setText("Menú principal");
        }

        fab.setOnClickListener(new View.OnClickListener() {
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
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);


        nombreUsuario_nav.setText(user.getNombre());
        email_nav.setText(email);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_menu, HomeFragment.newInstance(user)).commit();
                titulo.setText("Menú principal");
                break;
            case R.id.nav_medicamentos:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_menu, MedicamentosFragment.newInstance(email, pass)).commit();
                titulo.setText("Medicamentos");
                break;
            case R.id.nav_citas:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_menu, CitasFragment.newInstance(email, pass)).commit();
                titulo.setText("Citas Médicas");
                break;
            case R.id.nav_suscripcion:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_menu, SuscripcionFragment.newInstance(email, pass)).commit();
                titulo.setText("Suscripción");
                break;
            case R.id.nav_ajustes:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_menu, SettingsFragment.newInstance(email, pass)).commit();
                titulo.setText("Ajustes");
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private void recuperarUsuario() {
        db = new BaseDatos(this);
        CifradoAES aes = new CifradoAES();
        String semilla = email + pass;
        SecretKey secretKey = aes.generarSecretKey(semilla);
        String encrypt2 = null;
        try {
            encrypt2 = aes.encrypt(email.getBytes(), secretKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Usuario usuario = db.getUser(encrypt2);
        if (encrypt2.equals(usuario.getEmail())) {
            user.setIdUsuario(usuario.getIdUsuario());
            user.setNombre(aes.decrypt(usuario.getNombre(), secretKey));
            user.setFechaNacimiento(usuario.getFechaNacimiento());
            user.setEmail(aes.decrypt(usuario.getEmail(), secretKey));
            user.setContrasena(usuario.getContrasena());
        }
    }


}