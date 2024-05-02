package es.ifp.labsalut.negocio;

import java.sql.Time;
import java.util.Date;

public class CitaMedica {
    private int idCita;
    private String nombre;
    private Date fecha;
    private Time hora;
    private String descripcion;
    private float recordatorio;

    public CitaMedica(String nombre, Date fecha, Time hora, String descripcion, float recordatorio) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.hora = hora;
        this.descripcion = descripcion;
        this.recordatorio = recordatorio;
    }

    public int getIdCita() {
        return idCita;
    }

    public Time getHora() {
        return hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }

    public void setRecordatorio(float recordatorio) {
        this.recordatorio = recordatorio;
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
