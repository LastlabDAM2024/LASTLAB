package es.ifp.labsalut.negocio;

import java.util.Date;

public class Suscripcion {

    private String email;
    private boolean esSuscrito;
    private String finSuscripcion;

    public Suscripcion() {
        this.email = "";
        this.esSuscrito = false;
        this.finSuscripcion = "";
    }
    public Suscripcion(String email, boolean esSuscrito, String finSuscripcion) {
        this.email = email;
        this.esSuscrito = esSuscrito;
        this.finSuscripcion = finSuscripcion;
    }

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
}
