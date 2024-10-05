package es.ifp.labsalut.negocio;

import java.io.Serializable;

/**
 * La clase Suscripcion representa la suscripción de un usuario a un servicio.
 *
 * Esta clase incluye atributos que almacenan información relevante sobre la
 * suscripción, como el correo electrónico del usuario, un indicador de si
 * el usuario está suscrito y la fecha de finalización de la suscripción,
 * la cuenta bancaria, el tipo de suscripción y la duración de la suscripción.
 *
 * Se proporcionan constructores tanto por defecto como parametrizados para
 * permitir la creación de instancias de Suscripcion con o sin valores iniciales.
 * Además, se incluyen métodos getter y setter para acceder y modificar los
 * atributos de manera segura.
 *
 * La implementación de la interfaz Serializable permite que los objetos de
 * esta clase sean fácilmente serializados, facilitando su almacenamiento
 * o transmisión a través de redes.
 *
 * Ejemplo de uso:
 * Suscripcion miSuscripcion = new Suscripcion("usuario@example.com", true, "2025-12-31");
 */

public class Suscripcion implements Serializable {
    private String email; // Correo electrónico del usuario
    private boolean esSuscrito; // Indica si el usuario está suscrito o no
    private String finSuscripcion; // Fecha de finalización de la suscripción
    private String cuentaBancaria; // Cuenta bancaria del usuario
    private String tipoSuscripcion; // Tipo de suscripción (normal o premium)
    private String duracion; // Duración de la suscripción (1 mes, 1 año)

    // Constructor por defecto de la clase Suscripcion
    public Suscripcion() {
        this.email = "";
        this.esSuscrito = false;
        this.finSuscripcion = "";
        this.cuentaBancaria = "";
        this.tipoSuscripcion = "";
        this.duracion = "";
    }

    // Constructor parametrizado de la clase Suscripcion
    public Suscripcion(String email, boolean esSuscrito, String finSuscripcion, String cuentaBancaria, String tipoSuscripcion, String duracion) {
        this.email = email;
        this.esSuscrito = esSuscrito;
        this.finSuscripcion = finSuscripcion;
        this.cuentaBancaria = cuentaBancaria;
        this.tipoSuscripcion = tipoSuscripcion;
        this.duracion = duracion;
    }

    // Métodos getters y setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getEsSuscrito() {
        return esSuscrito;
    }

    public void setEsSuscrito(boolean esSuscrito) {
        this.esSuscrito = esSuscrito;
    }

    public String getFinSuscripcion() {
        return finSuscripcion;
    }

    public void setFinSuscripcion(String finSuscripcion) {
        this.finSuscripcion = finSuscripcion;
    }

    public String getCuentaBancaria() {
        return cuentaBancaria;
    }

    public void setCuentaBancaria(String cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }

    public String getTipoSuscripcion() {
        return tipoSuscripcion;
    }

    public void setTipoSuscripcion(String tipoSuscripcion) {
        this.tipoSuscripcion = tipoSuscripcion;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }
}
