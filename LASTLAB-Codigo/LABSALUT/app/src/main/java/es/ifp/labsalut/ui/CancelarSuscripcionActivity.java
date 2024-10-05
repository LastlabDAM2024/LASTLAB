package es.ifp.labsalut.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import es.ifp.labsalut.R;

public class CancelarSuscripcionActivity extends AppCompatActivity {

    private Button confirmarCancelacionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelar_suscripcion);

        confirmarCancelacionButton = findViewById(R.id.confirmar_cancelacion);

        confirmarCancelacionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para cancelar la suscripción

                // Regresar a SuscripcionFragment
                finish();
            }
        });
    }
}
