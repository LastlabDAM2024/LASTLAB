package es.ifp.labsalut.db;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;
import java.util.ArrayList;

import es.ifp.labsalut.negocio.CitaMedica;
import es.ifp.labsalut.negocio.Medicamento;
import es.ifp.labsalut.negocio.Suscripcion;
import es.ifp.labsalut.negocio.Usuario;

public class BaseDatos extends SQLiteOpenHelper {

    protected SQLiteDatabase db;

    public BaseDatos(Context context) {
        super(context, "BaseDatos", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Aquí se crean las tablas si no existen
        db.execSQL("CREATE TABLE IF NOT EXISTS Usuario (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nombre TEXT, fechaNacimiento TEXT, email TEXT, pass TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Medicamento (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nombre TEXT, dosis TEXT, frecuencia TEXT, recordatorio TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS CitaMedica (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nombre TEXT, fecha TEXT,hora TEXT,direccion TEXT, descripcion TEXT, recordatorio TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Suscripcion (email TEXT, esSuscrito BOOLEAN, finSuscripcion TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS UsuarioMedicamento (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, idUser INTEGER , idMedicamento INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS UsuarioCitaMedica (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, idUser INTEGER , idCitaMedica INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Se ejecuta si hay una actualización de la base de datos
        db.execSQL("DROP TABLE IF EXISTS Usuario");
        db.execSQL("DROP TABLE IF EXISTS Medicamento");
        db.execSQL("DROP TABLE IF EXISTS CitaMedica");
        db.execSQL("DROP TABLE IF EXISTS Suscripcion");
        db.execSQL("DROP TABLE IF EXISTS UsuarioMedicamento");
        db.execSQL("DROP TABLE IF EXISTS UsuarioCitaMedica");
        onCreate(db);
    }

    /*
   Metodo que ejecuta el onUpgrade para resetear el contador de los Ids si no tiene datos la tabla
    */
    public void reiniciarIDs() {
        onUpgrade(this.db, 1, 1);
    }

    /*
    Metodo que devuelve el numero de usuarios que tiene la BBDD
     */
    public int numTotalUsers() {
        int num = 0;
        db = this.getReadableDatabase();
        num = (int) DatabaseUtils.queryNumEntries(db, "Usuario");
        return num;
    }

    /*
    Metodo que devuelve el numero de Medicamentos que tiene la BBDD
     */
    public int numTotalMedicamentos() {
        int num = 0;
        db = this.getReadableDatabase();
        num = (int) DatabaseUtils.queryNumEntries(db, "Medicamento");
        return num;
    }

    /*
    Metodo que devuelve el numero de Citas que tiene la BBDD
     */
    public int numTotalCitas() {
        int num = 0;
        db = this.getReadableDatabase();
        num = (int) DatabaseUtils.queryNumEntries(db, "CitaMedica");
        return num;
    }

    /*
    Metodo que borra las medicamentos del Usuario
     */
    public void borrarAllMedicamentos(Usuario user) {
        db = this.getWritableDatabase();
        ArrayList<Medicamento> medicamentos = user.getAllMedicamentos();
        int id = 0;
        for (int i = 0; i < medicamentos.size(); i++) {
            id = medicamentos.get(i).getIdMedicamento();
            db.execSQL("DELETE FROM Medicamentos WHERE id=" + id);
        }
    }

    /*
    Metodo que borra las citasMedicas del Usuario
     */
    public void borrarAllCitas(Usuario user) {
        db = this.getWritableDatabase();
        ArrayList<CitaMedica> citas = user.getAllCitas();
        int id = 0;
        for (int i = 0; i < citas.size(); i++) {
            id = citas.get(i).getIdCita();
            db.execSQL("DELETE FROM CitaMedica WHERE id=" + id);
        }
    }


    // Método para añadir un usuario a la base de datos
    public int addUser(Usuario usuario) {
        db = this.getWritableDatabase();
        String nombreUser = usuario.getNombre();
        String fechaNacimiento = usuario.getFechaNacimiento();
        String email = usuario.getEmail();
        String pass = usuario.getContrasena();
        db.execSQL("INSERT INTO Usuario (nombre,fechaNacimiento,email,pass) VALUES ('" + nombreUser + "','" + fechaNacimiento + "','" + email + "','" + pass + "')");
        int id = -1;
        Cursor resultado = null;
        int contenido = -1;
        if (this.numTotalUsers() > 0) {
            db = this.getReadableDatabase();
            resultado = db.rawQuery("SELECT id FROM Usuario WHERE (nombre='" + nombreUser + "' AND email='" + email + "')", null);
            resultado.moveToFirst();
            while (!resultado.isAfterLast()) {
                contenido = resultado.getInt(resultado.getColumnIndex("id"));
                resultado.moveToNext();
                id = contenido;
            }
        }
        return id;
    }

    public Usuario getUser(String email) {

        Cursor resultado = null;
        Usuario contenido = new Usuario();
        if (this.numTotalUsers() > 0) {
            db = this.getReadableDatabase();
            resultado = db.rawQuery("SELECT * FROM Usuario WHERE email='" + email + "'", null);
            resultado.moveToFirst();
            while (!resultado.isAfterLast()) {
                contenido.setIdUsuario(resultado.getInt(resultado.getColumnIndex("id")));
                contenido.setNombre(resultado.getString(resultado.getColumnIndex("nombre")));
                contenido.setFechaNacimiento(resultado.getString(resultado.getColumnIndex("fechaNacimiento")));
                contenido.setEmail(resultado.getString(resultado.getColumnIndex("email")));
                contenido.setContrasena(resultado.getString(resultado.getColumnIndex("pass")));
                resultado.moveToNext();
            }

        }
        return contenido;
    }


    public void addUserMedi(Usuario user, Medicamento medicamento) {
        db = this.getWritableDatabase();
        int idUser = user.getIdUsuario();
        int idMedi = medicamento.getIdMedicamento();
        db.execSQL("INSERT INTO UsuarioMedicamento (idUser,idMedicamento) VALUES ('" + idUser + "','" + idMedi + "')");
    }

    public void addUserCita(Usuario user, CitaMedica cita) {
        db = this.getWritableDatabase();
        int idUser = user.getIdUsuario();
        int idCita = cita.getIdCita();
        db.execSQL("INSERT INTO UsuarioCitaMedica (idUser,idCitaMedica) VALUES ('" + idUser + "','" + idCita + "')");
    }

    // Método para añadir un medicamento a la base de datos
    public int addMedicamento(Medicamento medicamento) {
        db = this.getWritableDatabase();
        String nombre = medicamento.getNombre();
        String dosis = medicamento.getDosis();
        String frecuencia = medicamento.getFrecuencia();
        String recordatorio = medicamento.getRecordatorio();
        db.execSQL("INSERT INTO Medicamento (nombre,dosis,frecuencia,recordatorio) VALUES ('" + nombre + "','" + dosis + "','" + frecuencia + "','" + recordatorio + "')");
        int id = -1;
        Cursor resultado = null;
        int contenido = -1;
        if (this.numTotalMedicamentos() > 0) {
            db = this.getReadableDatabase();
            resultado = db.rawQuery("SELECT id FROM Medicamento WHERE nombre='" + nombre + "'AND dosis='" + dosis + "'AND frecuencia='" + frecuencia + "' ORDER BY id DESC", null);
            resultado.moveToFirst();
            while (!resultado.isAfterLast()) {
                contenido = resultado.getInt(resultado.getColumnIndex("id"));
                resultado.moveToNext();
                id = contenido;
            }
        }
        return id;
    }

    // Método para añadir una cita médica a la base de datos
    public int addCita(CitaMedica citaMedica) {
        db = this.getWritableDatabase();
        String nombre = citaMedica.getNombre();
        String fecha = citaMedica.getFecha();
        String hora = citaMedica.getHora();
        String direccion = citaMedica.getDireccion();
        String descripcion = citaMedica.getDescripcion();
        String recordatorio = citaMedica.getRecordatorio();
        db.execSQL("INSERT INTO CitaMedica (nombre,fecha,hora,direccion,descripcion,recordatorio) VALUES ('" + nombre + "','" + fecha + "','" + hora + "','" + direccion + "','" + descripcion + "','" + recordatorio + "')");
        int id = -1;
        Cursor resultado = null;
        int contenido = -1;
        if (this.numTotalCitas() > 0) {
            db = this.getReadableDatabase();
            resultado = db.rawQuery("SELECT id FROM CitaMedica WHERE nombre='" + nombre + "'AND fecha='" + fecha + "'AND hora='" + hora + "' ORDER BY id DESC", null);
            resultado.moveToFirst();
            while (!resultado.isAfterLast()) {
                contenido = resultado.getInt(resultado.getColumnIndex("id"));
                resultado.moveToNext();
                id = contenido;
            }
        }
        return id;
    }


    // Método para añadir una suscripción a la base de datos
    public void addSuscripcion(Suscripcion suscripcion) {
        db = this.getWritableDatabase();
        String email = suscripcion.getEmail();
        String finSuscripcion = suscripcion.getFinSuscripcion();
        boolean esSuscrito = suscripcion.getEsSuscrito();
        db.execSQL("INSERT INTO Suscripcion (email, esSuscrito, finSuscripcion) VALUES ('" + email + "','" + esSuscrito + "')");
    }

    // Método para actualizar una suscripción en la base de datos
    public void updateSuscripcion(Suscripcion suscripcion) {
        db = this.getWritableDatabase();
        String email = suscripcion.getEmail();
        boolean esSuscrito = suscripcion.getEsSuscrito();
        db.execSQL("UPDATE Suscripcion SET esSuscrito='" + esSuscrito + "' WHERE email='" + email + "'");
    }


    public ArrayList<Serializable> getAllMedicamentos(Usuario user) {
        ArrayList<Serializable> listMedi = new ArrayList<Serializable>();
        int id = user.getIdUsuario();
        Cursor resultado = null;
        Medicamento contenido = new Medicamento();
        if (this.numTotalMedicamentos() > 0) {
            db = this.getReadableDatabase();
            resultado = db.rawQuery("SELECT * FROM Medicamento WHERE (SELECT idMedicamento FROM UsuarioMedicamento WHERE idUser='" + id + "') ORDER BY id DESC", null);
            resultado.moveToFirst();
            while (!resultado.isAfterLast()) {
                contenido.setIdMedicamento(resultado.getInt(resultado.getColumnIndex("id")));
                contenido.setNombre(resultado.getString(resultado.getColumnIndex("nombre")));
                contenido.setDosis(resultado.getString(resultado.getColumnIndex("dosis")));
                contenido.setFrecuencia(resultado.getString(resultado.getColumnIndex("frecuencia")));
                contenido.setRecordatorio(resultado.getString(resultado.getColumnIndex("recordatorio")));
                resultado.moveToNext();
                listMedi.add(contenido);
            }

        }
        return listMedi;
    }


    public ArrayList<String> getAllNombreMedi(Usuario user) {
        ArrayList<String> listNombresMedi = new ArrayList<String>();
        int id = user.getIdUsuario();
        Cursor resultado = null;
        String contenido = "";
        if (this.numTotalMedicamentos() > 0) {
            db = this.getReadableDatabase();
            resultado = db.rawQuery("SELECT nombre FROM Medicamento WHERE (SELECT idMedicamento FROM UsuarioMedicamento WHERE idUser='" + id + "') ORDER BY id DESC", null);
            resultado.moveToFirst();
            while (!resultado.isAfterLast()) {
                contenido = resultado.getString(resultado.getColumnIndex("nombre"));
                resultado.moveToNext();
                listNombresMedi.add(contenido);
            }
        }
        return listNombresMedi;
    }


    public ArrayList<Serializable> getAllCitasMedicas(Usuario user) {
        ArrayList<Serializable> listMedi = new ArrayList<Serializable>();
        int id = user.getIdUsuario();
        Cursor resultado = null;
        CitaMedica contenido = new CitaMedica();
        if (this.numTotalCitas() > 0) {
            db = this.getReadableDatabase();
            resultado = db.rawQuery("SELECT * FROM CitaMedica WHERE (SELECT idCitaMedica FROM UsuarioCitaMedica WHERE idUser='" + id + "') ORDER BY id DESC", null);
            resultado.moveToFirst();
            while (!resultado.isAfterLast()) {
                contenido.setIdCita(resultado.getInt(resultado.getColumnIndex("id")));
                contenido.setNombre(resultado.getString(resultado.getColumnIndex("nombre")));
                contenido.setFecha(resultado.getString(resultado.getColumnIndex("fecha")));
                contenido.setHora(resultado.getString(resultado.getColumnIndex("hora")));
                contenido.setDireccion(resultado.getString(resultado.getColumnIndex("direccion")));
                contenido.setDescripcion(resultado.getString(resultado.getColumnIndex("descripcion")));
                contenido.setRecordatorio(resultado.getString(resultado.getColumnIndex("recordatorio")));
                resultado.moveToNext();
                listMedi.add(contenido);
            }
        }
        return listMedi;
    }

    public ArrayList<String> getAllNombreCitas(Usuario user) {
        ArrayList<String> listNombresCitas = new ArrayList<String>();
        int id = user.getIdUsuario();
        Cursor resultado = null;
        String contenido = "";
        if (this.numTotalMedicamentos() > 0) {
            db = this.getReadableDatabase();
            resultado = db.rawQuery("SELECT * FROM CitaMedica WHERE (SELECT idCitaMedica FROM UsuarioCitaMedica WHERE idUser='" + id + "') ORDER BY id DESC", null);
            resultado.moveToFirst();
            while (!resultado.isAfterLast()) {
                contenido = resultado.getString(resultado.getColumnIndex("nombre"));
                resultado.moveToNext();
                listNombresCitas.add(contenido);
            }
        }
        return listNombresCitas;
    }

    public void cerrarDB() {
        db.close();
    }
}
