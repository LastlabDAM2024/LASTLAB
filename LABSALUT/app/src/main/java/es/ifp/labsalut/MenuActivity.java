package es.ifp.labsalut;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class MenuActivity extends AppCompatActivity {

    protected Button crearMedicamento;
    protected Button crearCitaMedica;
    protected Button editarMedicamento;
    protected Button editarCitaMedica;
    protected TextView nombreUsuario;
    protected ListView listaMedicamentos;
    protected ListView listaCitas;
    private Intent pasarPantalla;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        crearMedicamento = (Button) findViewById(R.id.crearMedicamentoBoton_menu);
        crearCitaMedica = (Button) findViewById(R.id.crearCitaMedicaBoton_menu);
        editarMedicamento = (Button) findViewById(R.id.editarMedicamentoBoton_main);
        editarCitaMedica = (Button) findViewById(R.id.editarCitaMedicaBoton_main);
        nombreUsuario = (TextView) findViewById(R.id.nombreUser_menu);
        listaMedicamentos = (ListView) findViewById(R.id.listaMedicamento_menu);
        listaCitas = (ListView) findViewById(R.id.listaCitaMedica_Menu);

        crearMedicamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasarPantalla = new Intent(MenuActivity.this, MedicamentoActivity.class);
                startActivity(pasarPantalla);
            }
        });

        crearCitaMedica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasarPantalla = new Intent(MenuActivity.this, CitaMedicaActivity.class);
                startActivity(pasarPantalla);
            }
        });

    }
}