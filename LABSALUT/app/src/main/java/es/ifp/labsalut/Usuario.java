package es.ifp.labsalut;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class Usuario extends AppCompatActivity {
    private int idUsuario;
    private String nombre;
    private int fechaNacimiento;
    private List medicamentos;
    private List citaMedica;

    public Usuario(int idUsuario, String nombre, int fechaNacimiento, List medicamentos, List citaMedica) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.medicamentos = medicamentos;
        this.citaMedica = citaMedica;
    }

}
