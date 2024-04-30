package es.ifp.labsalut;
import android.os.Bundle;
import android.widget.Button;

import java.util.Date;

public class Suscripcion {
    private String email;
    private boolean esSuscrito;
    private Date finSuscripcion;

    private Button btnCancelarSuscripcion;
    private Usuario usuario; // Relación con la clase Usuario

    public Suscripcion(String email, boolean esSuscrito, Date finSuscripcion, Usuario usuario) {
        this.email = email;
        this.esSuscrito = esSuscrito;
        this.finSuscripcion = finSuscripcion;
        this.usuario = usuario;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }


}
