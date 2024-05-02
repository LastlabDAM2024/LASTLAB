package es.ifp.labsalut.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.math.BigInteger;

import es.ifp.labsalut.R;
import es.ifp.labsalut.negocio.Usuario;
import es.ifp.labsalut.seguridad.CifradoRSA;

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
    private CifradoRSA rsa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
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
                // Asignacion de la fecha en un String y cifrado de la contraseña en RSA con la clave publica y privada para su descifrado.
                fechaNacimiento = dia.getSelectedItem().toString() + "/" + mes.getSelectedItem().toString() + "/" + anyo.getSelectedItem().toString();
                rsa = new CifradoRSA(1024);
                BigInteger cifrado = rsa.cifrar(rsa.convertirString(pass.getText().toString()));
                Usuario user = new Usuario(nombre.getText().toString(), fechaNacimiento, email.getText().toString(), cifrado, rsa.getE(), rsa.getN());
            }
        });

    }
}