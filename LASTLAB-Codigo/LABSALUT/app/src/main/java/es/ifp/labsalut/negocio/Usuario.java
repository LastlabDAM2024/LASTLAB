package es.ifp.labsalut.negocio;

import java.math.BigInteger;
import java.util.ArrayList;

public class Usuario {
    private int idUsuario;
    private String nombre;
    private String fechaNacimiento;
    private String email;
    private BigInteger contrasena;
    private BigInteger e;
    private BigInteger n;
    private ArrayList<Medicamento> medicamentos;
    private ArrayList<CitaMedica> citaMedica;
    private Suscripcion suscripcion;

    public Usuario(String nombre, String fechaNacimiento, String email, BigInteger pass, BigInteger claveE, BigInteger claveN) {
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.email = email;
        this.contrasena = pass;
        this.e = claveE;
        this.n = claveN;
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

    public void removeMedicamentos(Medicamento medicamentos) {

        this.medicamentos.remove(medicamentos);
    }

    public CitaMedica getCitaMedica(int idCita) {
        return this.citaMedica.get(idCita);
    }

    public void setCitaMedica(CitaMedica citaMedica) {
        this.citaMedica.add(citaMedica);
    }

    public void removeCitaMedica(CitaMedica citaMedica) {
        this.citaMedica.remove(citaMedica);
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

    public BigInteger getE() {
        return e;
    }

    public void setE(BigInteger e) {
        this.e = e;
    }

    public BigInteger getN() {
        return n;
    }

    public void setN(BigInteger n) {
        this.n = n;
    }
}