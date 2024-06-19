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
    private ArrayList<Serializable> medicamentos; // Lista de medicamentos asociados al usuario
    private ArrayList<Serializable> citaMedica; // Lista de citas médicas asociadas al usuario
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
        return (Medicamento) this.medicamentos.get(idMedicamento);
    }

    // Método setter para añadir un medicamento al usuario
    public void setMedicamentos(Medicamento medicamentos) {
        this.medicamentos.add(medicamentos);
    }

    // Método para eliminar un medicamento del usuario
    public void removeMedicamentos(Medicamento medicamentos) {
        this.medicamentos.remove(medicamentos);
    }

    // Método getter para obtener todos los medicamentos asociados al usuario
    public ArrayList<Medicamento> getAllMedicamentos() {
        ArrayList<Medicamento>listaMed= new ArrayList<Medicamento>();
        for(int i=0;i< this.medicamentos.size();i++){
            if (this.medicamentos.get(i) instanceof Medicamento){
                Medicamento medicamento = (Medicamento) this.medicamentos.get(i);
                listaMed.add(medicamento);
            }
        }

        return listaMed;
    }

    public void setAllMedicamentos (ArrayList<Serializable> list){
        this.medicamentos = list;
    }

    // Método getter para obtener una cita médica específica del usuario según su identificador
    public CitaMedica getCitaMedica(int idCita) {
        return (CitaMedica) this.citaMedica.get(idCita);
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
        ArrayList<CitaMedica>listaCita= new ArrayList<CitaMedica>();
        for(int i=0;i< this.citaMedica.size();i++){
            if (this.citaMedica.get(i) instanceof CitaMedica){
                CitaMedica cita = (CitaMedica) this.citaMedica.get(i);
                listaCita.add(cita);
            }
        }

        return listaCita;
    }
    public void setAllCitas (ArrayList<Serializable> list){
        this.citaMedica = list;
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
