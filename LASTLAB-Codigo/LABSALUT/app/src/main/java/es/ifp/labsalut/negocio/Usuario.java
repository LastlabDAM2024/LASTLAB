package es.ifp.labsalut.negocio;

import java.io.Serializable;
import java.util.ArrayList;

// Clase Usuario que representa a un usuario del sistema
public class Usuario implements Serializable {
    private int idUsuario; // Identificador único del usuario
    private String nombre; // Nombre del usuario
    private String fechaNacimiento; // Fecha de nacimiento del usuario
    private String email; // Correo electrónico del usuario
    private String contrasena; // Contraseña del usuario
    private ArrayList<Medicamento> medicamentos; // Lista de medicamentos asociados al usuario
    private ArrayList<CitaMedica> citaMedica; // Lista de citas médicas asociadas al usuario
    private Suscripcion suscripcion; // Suscripción del usuario

    // Constructor por defecto de la clase Usuario
    public Usuario() {
        this.nombre = "";
        this.fechaNacimiento = "";
        this.email = "";
        this.contrasena = "";
        this.medicamentos = new ArrayList<>();
        this.citaMedica = new ArrayList<>();
        this.suscripcion = new Suscripcion();
    }

    // Constructor parametrizado de la clase Usuario
    public Usuario(String nombre, String fechaNacimiento, String email, String pass) {
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.email = email;
        this.contrasena = pass;
        this.medicamentos = new ArrayList<>();
        this.citaMedica = new ArrayList<>();
        this.suscripcion = new Suscripcion();
    }

    // Método getter para obtener el identificador único del usuario
    public int getIdUsuario() {
        return idUsuario;
    }

    // Método setter para establecer el identificador único del usuario
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    // Método getter para obtener el nombre del usuario
    public String getNombre() {
        return nombre;
    }

    // Método setter para establecer el nombre del usuario
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Método getter para obtener la fecha de nacimiento del usuario
    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    // Método setter para establecer la fecha de nacimiento del usuario
    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    // Método getter para obtener un medicamento específico del usuario según su identificador
    public Medicamento getMedicamentos(int idMedicamento) {
        if (idMedicamento >= 0 && idMedicamento < medicamentos.size()) {
            return medicamentos.get(idMedicamento);
        }
        return null; // O podrías lanzar una excepción si no se encuentra
    }

    // Método setter para añadir un medicamento al usuario
    public void setMedicamentos(Medicamento medicamento) {
        this.medicamentos.add(medicamento);
    }

    // Método para eliminar un medicamento del usuario
    public void removeMedicamentos(Medicamento medicamento) {
        this.medicamentos.remove(medicamento);
    }

    // Método getter para obtener todos los medicamentos asociados al usuario
    public ArrayList<Medicamento> getAllMedicamentos() {
        return medicamentos;
    }

    // Método getter para obtener una cita médica específica del usuario según su identificador
    public CitaMedica getCitaMedica(int idCita) {
        if (idCita >= 0 && idCita < citaMedica.size()) {
            return citaMedica.get(idCita);
        }
        return null; // O podrías lanzar una excepción si no se encuentra
    }

    // Método setter para añadir una cita médica al usuario
    public void setCitaMedica(CitaMedica citaMedica) {
        this.citaMedica.add(citaMedica);
    }

    // Método para eliminar una cita médica del usuario
    public void removeCitaMedica(CitaMedica citaMedica) {
        this.citaMedica.remove(citaMedica);
    }

    // Método getter para obtener todas las citas médicas asociadas al usuario
    public ArrayList<CitaMedica> getAllCitas() {
        return citaMedica;
    }

    // Método getter para obtener el correo electrónico del usuario
    public String getEmail() {
        return this.email;
    }

    // Método setter para establecer el correo electrónico del usuario
    public void setEmail(String email) {
        this.email = email;
    }

    // Método getter para obtener la contraseña del usuario
    public String getContrasena() {
        return this.contrasena;
    }

    // Método setter para establecer la contraseña del usuario
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    // Método getter para obtener la suscripción del usuario
    public Suscripcion getSuscripcion() {
        return this.suscripcion;
    }

    // Método setter para establecer la suscripción del usuario
    public void setSuscripcion(Suscripcion suscripcion) {
        this.suscripcion = suscripcion;
    }
}
