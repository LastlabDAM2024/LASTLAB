package es.ifp.labsalut.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import es.ifp.labsalut.R;
import es.ifp.labsalut.negocio.Suscripcion;

public class AgregarSuscripcionActivity extends AppCompatActivity {

    private EditText cuentaBancariaEditText;
    private EditText tipoSuscripcionEditText;
    private EditText duracionEditText;
    private Button guardarSuscripcionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_suscripcion);

        cuentaBancariaEditText = findViewById(R.id.cuenta_bancaria);
        tipoSuscripcionEditText = findViewById(R.id.tipo_suscripcion);
        duracionEditText = findViewById(R.id.duracion);
        guardarSuscripcionButton = findViewById(R.id.guardar_suscripcion);

        guardarSuscripcionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cuentaBancaria = cuentaBancariaEditText.getText().toString();
                String tipoSuscripcion = tipoSuscripcionEditText.getText().toString();
                String duracion = duracionEditText.getText().toString();

                // Crear y guardar la suscripción
                Suscripcion nuevaSuscripcion = new Suscripcion("usuario@example.com", true, "2025-12-31", cuentaBancaria, tipoSuscripcion, duracion);
                // Lógica para guardar la suscripción en el sistema

                // Regresar a SuscripcionFragment
                finish();
            }
        });
    }
}
