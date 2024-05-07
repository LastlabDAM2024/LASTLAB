package es.ifp.labsalut.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import javax.crypto.SecretKey;

import es.ifp.labsalut.R;
import es.ifp.labsalut.db.BaseDatos;
import es.ifp.labsalut.negocio.Usuario;
import es.ifp.labsalut.seguridad.CifradoAES;


public class MenuActivity extends AppCompatActivity {

    protected Button crearMedicamento;
    protected Button crearCitaMedica;
    protected Button editarMedicamento;
    protected Button editarCitaMedica;
    protected TextView nombreUsuario;
    protected ListView listaMedicamentos;
    protected ListView listaCitas;
    private Intent pasarPantalla;
    private BaseDatos db;
    private Bundle extras;
    private String email;
    private String password;
    private final Usuario user = new Usuario();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        db = new BaseDatos(this);
        crearMedicamento = (Button) findViewById(R.id.crearMedicamentoBoton_menu);
        crearCitaMedica = (Button) findViewById(R.id.crearCitaMedicaBoton_menu);
        editarMedicamento = (Button) findViewById(R.id.editarMedicamentoBoton_main);
        editarCitaMedica = (Button) findViewById(R.id.editarCitaMedicaBoton_main);
        nombreUsuario = (TextView) findViewById(R.id.nombreUser_menu);
        listaMedicamentos = (ListView) findViewById(R.id.listaMedicamento_menu);
        listaCitas = (ListView) findViewById(R.id.listaCitaMedica_Menu);

        extras = getIntent().getExtras();

        if (extras != null) {
            email = extras.getString("EMAIL");
            password = extras.getString("PASS");


            CifradoAES aes = new CifradoAES();
            String semilla = email + password;
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
        nombreUsuario.setText(user.getNombre());
        crearMedicamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasarPantalla = new Intent(MenuActivity.this, MedicamentoActivity.class);
                startActivity(pasarPantalla);
                finish();
            }
        });

        crearCitaMedica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasarPantalla = new Intent(MenuActivity.this, CitaMedicaActivity.class);
                startActivity(pasarPantalla);
                finish();
            }
        });

    }
}