package es.ifp.labsalut.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Locale;

import javax.crypto.SecretKey;

import es.ifp.labsalut.R;
import es.ifp.labsalut.MainActivity;
import es.ifp.labsalut.db.BaseDatos;
import es.ifp.labsalut.negocio.Usuario;
import es.ifp.labsalut.seguridad.CifradoAES;

public class RegistroActivity extends AppCompatActivity {

    protected EditText nombre;
    protected EditText email;
    protected EditText pass;
    protected EditText pass2;
    protected TextView fechaText;
    protected Button calendarioBoton;
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
        fechaText = (TextView) findViewById(R.id.fecha_registro);
        aceptarBoton = (Button) findViewById(R.id.aceptar_registro);
        cancelarBoton = (Button) findViewById(R.id.cancelar_registro);
        calendarioBoton = (Button) findViewById(R.id.calendarioBoton_citas);

        calendarioBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int anyo = calendar.get(Calendar.YEAR);
                int mes = calendar.get(Calendar.MONTH);
                int dia = calendar.get(Calendar.DAY_OF_MONTH);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    DatePickerDialog datePicker = new DatePickerDialog(RegistroActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            Locale locale = new Locale("es", "ES");
                            Month mMonth = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                mMonth = Month.of(month + 1);
                                String monthName = mMonth.getDisplayName(TextStyle.FULL, locale);
                                fechaNacimiento = dayOfMonth + "  /  " + monthName + "  /  " + year;
                                fechaText.setText(String.valueOf(fechaNacimiento));
                            }
                        }
                    }, anyo, mes, dia);
                    datePicker.show();
                }

            }
        });
        aceptarBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    encryptFecha = aes.encrypt(fechaNacimiento.getBytes(), secretKey);
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