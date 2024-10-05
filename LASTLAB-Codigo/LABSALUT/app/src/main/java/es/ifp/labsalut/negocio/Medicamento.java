package es.ifp.labsalut.negocio;

import java.io.Serializable;

/**
 * Esta clase `Medicamento` representa un medicamento en el sistema. Es una clase que implementa la interfaz
 * `Serializable`, lo que permite que las instancias de `Medicamento` se puedan convertir en un flujo de bytes
 * para ser almacenadas o transmitidas de manera más sencilla. A continuación se detallan las características
 * y funcionalidades de esta clase:
 *
 * 1. **Atributos**:
 *    - `idMedicamento`: Un entero que sirve como identificador único del medicamento en la base de datos.
 *    - `nombre`: Una cadena que almacena el nombre del medicamento.
 *    - `dosis`: Una cadena que indica la dosis recomendada del medicamento.
 *    - `frecuencia`: Una cadena que describe la frecuencia con la que debe administrarse el medicamento.
 *    - `recordatorio`: Una cadena que contiene un recordatorio asociado al medicamento, útil para el usuario.
 *
 * 2. **Constructores**:
 *    - Se proporciona un constructor por defecto que inicializa los atributos a cadenas vacías.
 *    - Un constructor parametrizado que permite crear un objeto `Medicamento` con valores específicos para
 *      los atributos `nombre`, `dosis`, `frecuencia` y `recordatorio`.
 *
 * 3. **Métodos Getters y Setters**:
 *    - Se implementan métodos `get` y `set` para cada uno de los atributos. Estos métodos permiten acceder
 *      y modificar los valores de los atributos de forma controlada, lo que fomenta el encapsulamiento.
 *
 * Esta clase se utiliza para crear y gestionar objetos de tipo `Medicamento` dentro de la aplicación,
 * facilitando la manipulación de la información relacionada con los medicamentos de los usuarios.
 */

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
    public Medicamento(String nombre, String dosis, String frecuencia, String recordatorio) {
        this.nombre = nombre;
        this.dosis = dosis;
        this.frecuencia = frecuencia;
        this.recordatorio = recordatorio;
    }

    // Getters y Setters

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
