package es.ifp.labsalut.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import javax.crypto.SecretKey;

import es.ifp.labsalut.R;
import es.ifp.labsalut.db.BaseDatos;
import es.ifp.labsalut.negocio.Usuario;
import es.ifp.labsalut.seguridad.CifradoAES;

public class RegistroActivity extends AppCompatActivity {

    protected EditText nombre;
    protected EditText email;
    protected EditText pass;
    protected EditText pass2;
    protected Spinner dia;
    protected Spinner mes;
    protected Spinner anyo;
    protected Button aceptarBoton;
    protected Button cancelarBoton;
    private String fechaNacimiento;
    private CifradoAES aes;
    private BaseDatos db;
    private Intent pasarPantalla;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        db = new BaseDatos(this);
        nombre = (EditText) findViewById(R.id.nombreEditText_registro);
        email = (EditText) findViewById(R.id.mailEditText_registro);
        pass = (EditText) findViewById(R.id.passEditText_registro);
        pass2 = (EditText) findViewById(R.id.passEditText2_registro);
        dia = (Spinner) findViewById(R.id.dia_registro);
        mes = (Spinner) findViewById(R.id.mes_registro);
        anyo = (Spinner) findViewById(R.id.anyo_registro);
        aceptarBoton = (Button) findViewById(R.id.aceptar_registro);
        cancelarBoton = (Button) findViewById(R.id.cancelar_registro);

        aceptarBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fechaNacimiento = dia.getSelectedItem().toString() + "/" + mes.getSelectedItem().toString() + "/" + anyo.getSelectedItem().toString();
                aes = new CifradoAES();
                String semilla = email.getText().toString() + pass.getText().toString();
                SecretKey secretKey = aes.generarSecretKey(semilla);
                String encryptNombre = "";
                String encryptEmail = "";
                String encryptPass = "";
                String encryptFecha = "";

                try {
                    encryptNombre = aes.encrypt(nombre.getText().toString().getBytes(), secretKey);
                    encryptEmail = aes.encrypt(email.getText().toString().getBytes(), secretKey);
                    encryptPass = aes.encrypt(pass.getText().toString().getBytes(), secretKey);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                Usuario user = new Usuario(encryptNombre, encryptFecha, encryptEmail, encryptPass);
                user.setIdUsuario(db.addUser(user));
                pasarPantalla = new Intent(RegistroActivity.this, MainActivity.class);
                startActivity(pasarPantalla);
                finish();
            }
        });

        cancelarBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasarPantalla = new Intent(RegistroActivity.this, MainActivity.class);
                startActivity(pasarPantalla);
                finish();
            }
        });

    }
}