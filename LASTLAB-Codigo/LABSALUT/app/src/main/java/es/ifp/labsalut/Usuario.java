package es.ifp.labsalut;

import java.math.BigInteger;
import java.util.ArrayList;

public class Usuario {
    private int idUsuario;
    private String nombre;
    private String fechaNacimiento;
    private String email;
    private BigInteger contrasena;
    private ArrayList<Medicamento> medicamentos;
    private ArrayList<CitaMedica> citaMedica;
    private Suscripcion suscripcion;

    public Usuario(int idUsuario, String nombre, String fechaNacimiento, String email) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.email = email;
        //Hacer cifrado RSA1024 para contraseña
        this.medicamentos = new ArrayList<Medicamento>();
        this.citaMedica = new ArrayList<CitaMedica>();
        this.suscripcion = new Suscripcion();
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Medicamento getMedicamentos(int idMedicamento) {
        return this.medicamentos.get(idMedicamento);
    }

    public void setMedicamentos(Medicamento medicamentos) {

        this.medicamentos.add(medicamentos);
    }

    public CitaMedica getCitaMedica(int idCita) {
        return this.citaMedica.get(idCita);
    }

    public void setCitaMedica(CitaMedica citaMedica) {
        this.citaMedica.add(citaMedica);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigInteger getContrasena() {
        return contrasena;
    }

    public void setContrasena(BigInteger contrasena) {
        this.contrasena = contrasena;
    }

    public Suscripcion getSuscripcion() {
        return suscripcion;
    }

    public void setSuscripcion(Suscripcion suscripcion) {
        this.suscripcion = suscripcion;
    }
}