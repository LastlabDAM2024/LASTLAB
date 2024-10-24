package es.ifp.labsalut.negocio;

import java.io.Serializable;


// Clase CitaMedica que representa una cita médica
public class CitaMedica implements Serializable {
    private int idCita; // Identificador único de la cita médica
    private String nombre; // Nombre de la cita médica
    private String fecha; // Fecha de la cita médica
    private String hora; // Hora de la cita médica
    private String direccion; // Direccion de la cita médica
    private String descripcion; // Descripción de la cita médica
    private String recordatorio; // Recordatorio asociado a la cita médica

    // Constructor por defecto de la clase CitaMedica
    public CitaMedica() {
        this.nombre = "";
        this.fecha = "";
        this.hora = "";
        this.descripcion = "";
        this.recordatorio = "";
        this.direccion = "";
    }

    // Constructor parametrizado de la clase CitaMedica
    public CitaMedica(int id, String nombre, String fecha, String hora, String descripcion, String recordatorio) {
        this.idCita = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.hora = hora;
        this.descripcion = descripcion;
        this.recordatorio = recordatorio;
        this.direccion = "";
    }


    // Constructor parametrizado de la clase CitaMedica
    public CitaMedica(int id,String nombre, String fecha, String hora, String direccion, String descripcion, String recordatorio) {
        this.idCita = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.hora = hora;
        this.descripcion = descripcion;
        this.recordatorio = recordatorio;
        this.direccion = direccion;
    }
    // Constructor parametrizado de la clase CitaMedica
    public CitaMedica(String nombre) {
        this.nombre = nombre;
        this.fecha = "fecha";
        this.hora = "hora";
        this.descripcion = "descripcion";
        this.recordatorio = "recordatorio";
        this.direccion = "direccion";
    }

    // Método getter para obtener el ID de la cita médica
    public int getIdCita() {
        return idCita;
    }

    // Método setter para establecer el ID de la cita médica
    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }

    // Método getter para obtener el nombre de la cita médica
    public String getNombre() {
        return nombre;
    }

    // Método setter para establecer el nombre de la cita médica
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Método getter para obtener la fecha de la cita médica
    public String getFecha() {
        return fecha;
    }

    // Método setter para establecer la fecha de la cita médica
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    // Método getter para obtener la hora de la cita médica
    public String getHora() {
        return hora;
    }

    // Método setter para establecer la hora de la cita médica
    public void setHora(String hora) {
        this.hora = hora;
    }

    // Método getter para obtener la descripción de la cita médica
    public String getDescripcion() {
        return descripcion;
    }

    // Método setter para establecer la descripción de la cita médica
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // Método getter para obtener el recordatorio asociado a la cita médica
    public String getRecordatorio() {
        return recordatorio;
    }

    // Método setter para establecer el recordatorio asociado a la cita médica
    public void setRecordatorio(String recordatorio) {
        this.recordatorio = recordatorio;
    }

    // Método getter para obtener la dirección asociada a la cita médica
    public String getDireccion() {
        return direccion;
    }

    // Método setter para establecer la dirección asociada a la cita médica
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
