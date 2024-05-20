package es.ifp.labsalut.negocio;

import java.io.Serializable;

public class Medicamento implements Serializable {
    private int idMedicamento;
    private String nombre;
    private String dosis;
    private String frecuencia;
    private String recordatorio;

    public Medicamento() {
        this.nombre = "";
        this.dosis = "";
        this.frecuencia = "";
        this.recordatorio = "";
    }
    public Medicamento(String nombre, String dosis, String frecuencia, String recordatorio) {
        this.nombre = nombre;
        this.dosis = dosis;
        this.frecuencia = frecuencia;
        this.recordatorio = recordatorio;
    }
    public int getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(int idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    public String getRecordatorio() {
        return recordatorio;
    }

    public void setRecordatorio(String recordatorio) {
        this.recordatorio = recordatorio;
    }


}
