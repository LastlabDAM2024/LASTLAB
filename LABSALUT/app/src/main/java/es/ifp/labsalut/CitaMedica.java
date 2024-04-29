package es.ifp.labsalut;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class CitaMedica extends AppCompatActivity {

    private int idCita;
    private String nombre;
    private Date fecha;
    private String descripcion;
    private Date recordatorio;

    public CitaMedica(int idCita, String nombre, Date fecha, String descripcion, Date recordatorio, Usuario usuario) {
        this.idCita = idCita;
        this.nombre = nombre;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.recordatorio = recordatorio;
    }

    // Getters, Setters
}


