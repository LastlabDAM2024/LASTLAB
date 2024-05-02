package es.ifp.labsalut;
import java.util.ArrayList;

public class Usuario {
    private int idUsuario;
    private String nombre;
    private int fechaNacimiento;
    private ArrayList<Medicamento> medicamentos;
    private ArrayList<CitaMedica> citaMedica;
    private Suscripcion suscripcion;

    public Usuario(int idUsuario, String nombre, int fechaNacimiento) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.medicamentos = new ArrayList<Medicamento>();
        this.citaMedica = new ArrayList<CitaMedica>();
        this.suscripcion = new Suscripcion();
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

    public Medicamento getMedicamentos(int idMedicamento) {
        return this.medicamentos.get(idMedicamento);
    }

    public void setMedicamentos(Medicamento medicamentos) {

        this.medicamentos.add(medicamentos);
    }

    public CitaMedica getCitaMedica(int idCita) {
        return this.citaMedica.get(idCita);
    }

    public void setCitaMedica(CitaMedica citaMedica) {
        this.citaMedica.add(citaMedica);
    }
}