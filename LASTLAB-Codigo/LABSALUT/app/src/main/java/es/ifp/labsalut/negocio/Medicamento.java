package es.ifp.labsalut.negocio;

import java.io.Serializable;

// Clase Medicamento que representa un medicamento
public class Medicamento implements Serializable {
    private int idMedicamento; // Identificador único del medicamento
    private String nombre; // Nombre del medicamento
    private String dosis; // Dosis del medicamento
    private String frecuencia; // Frecuencia del medicamento
    private String recordatorio; // Recordatorio asociado al medicamento

    // Constructor por defecto de la clase Medicamento
    public Medicamento() {
        this.nombre = "";
        this.dosis = "";
        this.frecuencia = "";
        this.recordatorio = "";
    }

    // Constructor parametrizado de la clase Medicamento
    public Medicamento(int id, String nombre, String dosis, String frecuencia, String recordatorio) {
        this.idMedicamento = id;
        this.nombre = nombre;
        this.dosis = dosis;
        this.frecuencia = frecuencia;
        this.recordatorio = recordatorio;
    }

    // Constructor parametrizado de la clase Medicamento
    public Medicamento(String nombre) {
        this.nombre = nombre;
        this.dosis = "dosis";
        this.frecuencia = "frecuencia";
        this.recordatorio = "recordatorio";
    }

    // Método getter para obtener el ID del medicamento
    public int getIdMedicamento() {
        return idMedicamento;
    }

    // Método setter para establecer el ID del medicamento
    public void setIdMedicamento(int idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    // Método getter para obtener el nombre del medicamento
    public String getNombre() {
        return nombre;
    }

    // Método setter para establecer el nombre del medicamento
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Método getter para obtener la dosis del medicamento
    public String getDosis() {
        return dosis;
    }

    // Método setter para establecer la dosis del medicamento
    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    // Método getter para obtener la frecuencia del medicamento
    public String getFrecuencia() {
        return frecuencia;
    }

    // Método setter para establecer la frecuencia del medicamento
    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    // Método getter para obtener el recordatorio asociado al medicamento
    public String getRecordatorio() {
        return recordatorio;
    }

    // Método setter para establecer el recordatorio asociado al medicamento
    public void setRecordatorio(String recordatorio) {
        this.recordatorio = recordatorio;
    }
}
