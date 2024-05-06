package es.ifp.labsalut.negocio;

import java.math.BigInteger;
import java.util.ArrayList;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Usuario {
    private int idUsuario;
    private String nombre;
    private String fechaNacimiento;
    private String email;
    private String contrasena;
    private SecretKey key;
    private ArrayList<Medicamento> medicamentos;
    private ArrayList<CitaMedica> citaMedica;
    private Suscripcion suscripcion;


    public Usuario() {
        this.nombre = "";
        this.fechaNacimiento = "";
        this.email = "";
        this.contrasena = "";
        this.key = null;
        this.medicamentos = new ArrayList<Medicamento>();
        this.citaMedica = new ArrayList<CitaMedica>();
        this.suscripcion = new Suscripcion();
    }
    public Usuario(String nombre, String fechaNacimiento, String email, String pass, SecretKey clave) {
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.email = email;
        this.contrasena = pass;
        this.key = clave;
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

    public ArrayList<Medicamento> getAllMedicamentos() {
        return this.medicamentos;
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

    public ArrayList<CitaMedica> getAllCitas() {
        return this.citaMedica;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Suscripcion getSuscripcion() {
        return suscripcion;
    }

    public void setSuscripcion(Suscripcion suscripcion) {
        this.suscripcion = suscripcion;
    }

    public SecretKey getKey() {
        return key;
    }

    public void setKey(String e) {
        this.key= new SecretKeySpec(e.getBytes(), "AES");;
    }

}