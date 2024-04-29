package es.ifp.labsalut;


import java.util.ArrayList;

public class Usuario {
    private int idUsuario;
    private String nombre;
    private int fechaNacimiento;
    private ArrayList medicamentos;
    private ArrayList citaMedica;

    public Usuario(int idUsuario, String nombre, int fechaNacimiento, ArrayList medicamentos, ArrayList citaMedica) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.medicamentos = medicamentos;
        this.citaMedica = citaMedica;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(int fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public ArrayList getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(ArrayList medicamentos) {
        this.medicamentos = medicamentos;
    }

    public ArrayList getCitaMedica() {
        return citaMedica;
    }

    public void setCitaMedica(ArrayList citaMedica) {
        this.citaMedica = citaMedica;
    }
}