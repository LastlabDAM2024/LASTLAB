package es.ifp.labsalut.negocio;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * La clase Usuario representa a un usuario del sistema LabSalut.
 *
 * Esta clase contiene información relevante sobre el usuario, incluyendo
 * un identificador único, nombre, fecha de nacimiento, correo electrónico
 * y contraseña. Además, mantiene listas de medicamentos y citas médicas
 * asociadas al usuario, así como un objeto Suscripcion que representa
 * la suscripción del usuario a un servicio.
 *
 * Se ofrecen constructores tanto por defecto como parametrizados para
 * facilitar la creación de instancias de Usuario con o sin valores iniciales.
 *
 * Esta clase también incluye métodos getter y setter para acceder y modificar
 * los atributos de forma segura, así como métodos para añadir, eliminar y
 * obtener medicamentos y citas médicas asociadas al usuario.
 *
 * La implementación de la interfaz Serializable permite que los objetos de
 * esta clase sean serializados, lo que es útil para su almacenamiento o
 * transmisión en redes.
 *
 * Ejemplo de uso:
 * Usuario nuevoUsuario = new Usuario("Juan Pérez", "1990-01-01", "juan@example.com", "contraseña123");
 */

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

    // Método para obtener un medicamento específico del usuario según su identificador
    public Medicamento getMedicamentos(int idMedicamento) {
        if (idMedicamento >= 0 && idMedicamento < medicamentos.size()) {
            return medicamentos.get(idMedicamento);
        }
        return null; // se podria lanzar una excepción si no se encuentra (pendiente hacer eso)
    }

    // Método para añadir un medicamento al usuario
    public void setMedicamentos(Medicamento medicamento) {

        this.medicamentos.add(medicamento);
    }

    // Método para eliminar un medicamento del usuario
    public void removeMedicamentos(Medicamento medicamento) {
        this.medicamentos.remove(medicamento);
    }

    // Método para obtener todos los medicamentos asociados al usuario
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

    // Getters Y Setters

    public ArrayList<CitaMedica> getAllCitas() {

        return citaMedica;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {

        this.email = email;
    }


    public String getContrasena() {
        return this.contrasena;
    }


    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }


    public Suscripcion getSuscripcion() {
        return this.suscripcion;
    }


    public void setSuscripcion(Suscripcion suscripcion) {
        this.suscripcion = suscripcion;
    }


    // Método para modificar la cita Médica
    public void setCitaMedica(CitaMedica citaMedica) {
        this.citaMedica.add(citaMedica);
    }

    //Metodo para eliminar la cita médica
    public void removeCitaMedica(CitaMedica citaMedica) {

        this.citaMedica.remove(citaMedica);
    }

    public void setAllCitas (ArrayList<CitaMedica> list){
        this.citaMedica = list;
    }
}



