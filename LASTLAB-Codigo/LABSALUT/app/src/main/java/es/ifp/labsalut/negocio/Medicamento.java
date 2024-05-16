package es.ifp.labsalut.negocio;

import java.io.Serializable;

public class Medicamento implements Serializable {
    private int idMedicamento;
    private String nombre;
    private int dosis;
    private float frecuencia;
    private float recordatorio;

    public Medicamento() {
        this.nombre = "";
        this.dosis = 0;
        this.frecuencia = 0f;
        this.recordatorio = 0f;
    }
    public Medicamento(String nombre, int dosis, float frecuencia, float recordatorio) {
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

    public int getDosis() {
        return dosis;
    }

    public void setDosis(int dosis) {
        this.dosis = dosis;
    }

    public float getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(float frecuencia) {
        this.frecuencia = frecuencia;
    }

    public float getRecordatorio() {
        return recordatorio;
    }

    public void setRecordatorio(float recordatorio) {
        this.recordatorio = recordatorio;
    }


}
