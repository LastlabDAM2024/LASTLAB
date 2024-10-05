package es.ifp.labsalut.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import es.ifp.labsalut.R;
import es.ifp.labsalut.activities.MainActivity;

public class CancelarSuscripcionActivity extends AppCompatActivity {
    private Button btnConfirmarCancelacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelar_suscripcion);

        btnConfirmarCancelacion = findViewById(R.id.btnConfirmarCancelacion);

        btnConfirmarCancelacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoConfirmacion();
            }
        });
    }

    private void mostrarDialogoConfirmacion() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmación")
                .setMessage("¿Estás seguro de que deseas cancelar tu suscripción?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Aquí puedes añadir la lógica para cancelar la suscripción
                        // Por ejemplo, actualiza el estado de la suscripción en tu modelo de datos

                        // Muestra mensaje de suscripción cancelada
                        Toast.makeText(CancelarSuscripcionActivity.this, "Suscripción cancelada", Toast.LENGTH_SHORT).show();

                        // Regresa al menú principal
                        Intent intent = new Intent(CancelarSuscripcionActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Finaliza la actividad actual
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Muestra mensaje de suscripción no cancelada
                        Toast.makeText(CancelarSuscripcionActivity.this, "Suscripción no cancelada", Toast.LENGTH_SHORT).show();

                        // Regresa al menú principal
                        Intent intent = new Intent(CancelarSuscripcionActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Finaliza la actividad actual
                    }
                })
                .show();
    }
}
