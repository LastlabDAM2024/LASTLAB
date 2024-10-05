package es.ifp.labsalut.ui;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import es.ifp.labsalut.R;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;


public class AgregarSuscripcionActivity extends AppCompatActivity {

    private Spinner tipoSuscripcionSpinner;
    private Spinner duracionSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_suscripcion);

        tipoSuscripcionSpinner = findViewById(R.id.tipo_suscripcion_spinner);
        duracionSpinner = findViewById(R.id.duracion_spinner);
        Button guardarSuscripcion = findViewById(R.id.guardar_suscripcion);

        // Configurar los Spinners
        ArrayAdapter<CharSequence> adapterTipo = ArrayAdapter.createFromResource(this,
                R.array.tipo_suscripcion_array, android.R.layout.simple_spinner_item);
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoSuscripcionSpinner.setAdapter(adapterTipo);

        ArrayAdapter<CharSequence> adapterDuracion = ArrayAdapter.createFromResource(this,
                R.array.duracion_array, android.R.layout.simple_spinner_item);
        adapterDuracion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        duracionSpinner.setAdapter(adapterDuracion);

        // Configura el botón de guardar suscripción
        guardarSuscripcion.setOnClickListener(v -> {
            String tipoSuscripcion = tipoSuscripcionSpinner.getSelectedItem().toString();
            String duracionSuscripcion = duracionSpinner.getSelectedItem().toString();
            // Aquí puedes agregar la lógica para guardar la suscripción

            // Mostrar un mensaje de confirmación
            Toast.makeText(this, "Suscripción realizada con éxito: " + tipoSuscripcion + ", " + duracionSuscripcion, Toast.LENGTH_SHORT).show();
            // Regresar al menú principal si es necesario
            finish(); // O puedes iniciar el menú principal directamente
        });
    }
}
