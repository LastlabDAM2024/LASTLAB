package es.ifp.labsalut.negocio;

import java.io.Serializable;

/**
 * La clase Suscripcion representa la suscripción de un usuario a un servicio.
 *
 * Esta clase incluye atributos que almacenan información relevante sobre la
 * suscripción, como el correo electrónico del usuario, un indicador de si
 * el usuario está suscrito y la fecha de finalización de la suscripción.
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

    // Constructor por defecto de la clase Suscripcion
    public Suscripcion() {
        this.email = "";
        this.esSuscrito = false;
        this.finSuscripcion = "";
    }

    // Constructor parametrizado de la clase Suscripcion
    public Suscripcion(String email, boolean esSuscrito, String finSuscripcion) {
        this.email = email;
        this.esSuscrito = esSuscrito;
        this.finSuscripcion = finSuscripcion;
    }

    // Método getter para obtener el correo electrónico del usuario
    public String getEmail() {
        return email;
    }

    // Método setter para establecer el correo electrónico del usuario
    public void setEmail(String email) {
        this.email = email;
    }

    // Método getter para obtener si el usuario está suscrito o no
    public boolean getEsSuscrito() {
        return esSuscrito;
    }

    // Método setter para establecer si el usuario está suscrito o no
    public void setEsSuscrito(boolean esSuscrito) {
        this.esSuscrito = esSuscrito;
    }

    // Método getter para obtener la fecha de finalización de la suscripción
    public String getFinSuscripcion() {
        return finSuscripcion;
    }

    // Método setter para establecer la fecha de finalización de la suscripción
    public void setFinSuscripcion(String finSuscripcion) {
        this.finSuscripcion = finSuscripcion;
    }
}
