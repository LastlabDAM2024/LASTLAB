package es.ifp.labsalut.negocio;

import java.io.Serializable;

public class CitaMedica implements Serializable {
    private int idCita;
    private String nombre;
    private String fecha;
    private String hora;
    private String descripcion;
    private float recordatorio;


    public CitaMedica() {
        this.nombre = "";
        this.fecha = "";
        this.hora = "";
        this.descripcion = "";
        this.recordatorio = 0f;
    }
    public CitaMedica(String nombre, String fecha, String hora, String descripcion, float recordatorio) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.hora = hora;
        this.descripcion = descripcion;
        this.recordatorio = recordatorio;
    }

    public int getIdCita() {
        return idCita;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public void setRecordatorio(float recordatorio) {
        this.recordatorio = recordatorio;
    }

    public float getRecordatorio() {
        return recordatorio;
    }

    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
