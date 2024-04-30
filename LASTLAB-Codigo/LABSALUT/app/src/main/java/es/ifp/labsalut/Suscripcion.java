package es.ifp.labsalut;

import java.util.Date;

public class Suscripcion {

    private String email;
    private boolean esSuscrito;
    private Date finSuscripcion;

    public Suscripcion(String email, boolean esSuscrito, Date finSuscripcion, Usuario usuario) {
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

    public boolean isEsSuscrito() {
        return esSuscrito;
    }

    public void setEsSuscrito(boolean esSuscrito) {
        this.esSuscrito = esSuscrito;
    }

    public Date getFinSuscripcion() {
        return finSuscripcion;
    }

    public void setFinSuscripcion(Date finSuscripcion) {
        this.finSuscripcion = finSuscripcion;
    }
}
