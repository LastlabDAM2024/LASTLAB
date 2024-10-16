package es.ifp.labsalut.negocio;

import java.io.Serializable;

// Clase Suscripcion que representa la suscripción de un usuario
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
