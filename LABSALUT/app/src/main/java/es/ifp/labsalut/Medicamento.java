package es.ifp.labsalut;

import androidx.appcompat.app.AppCompatActivity;

public class Medicamento extends AppCompatActivity {

    private int idMedicamento;
    private String nombre;
    private int dosis;
    private int frecuencia;
    private int recordatorio;
    private Usuario usuario;

    public Medicamento(int idMedicamento, String nombre, int dosis, int frecuencia, int recordatorio, Usuario usuario) {
        this.idMedicamento = idMedicamento;
        this.nombre = nombre;
        this.dosis = dosis;
        this.frecuencia = frecuencia;
        this.recordatorio = recordatorio;
        this.usuario = usuario;
    }

    // Getters, Setters
}



