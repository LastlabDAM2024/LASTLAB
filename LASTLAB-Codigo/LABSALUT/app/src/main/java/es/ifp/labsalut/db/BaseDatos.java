package es.ifp.labsalut.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.math.BigInteger;
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
        db.execSQL("CREATE TABLE IF NOT EXISTS Usuario (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nombre TEXT, fechaNacimiento TEXT, email TEXT, pass TEXT, claveE TEXT, claveN TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Medicamento (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nombre TEXT, dosis INTEGER, frecuencia FLOAT, recordatorio FLOAT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS CitaMedica (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nombre TEXT, fecha TEXT,hora TEXT, descripcion TEXT, recordatorio FLOAT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Suscripcion (email TEXT, esSuscrito BOOLEAN, finSuscripcion TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS UsuarioMedicamento (idUser INTEGER PRIMARY KEY, idMedicamento INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS UsuarioCitaMedica (idUser INTEGER PRIMARY KEY, idCitaMedica INTEGER)");
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
        String pass = usuario.getContrasena().toString();
        String claveE = usuario.getE().toString();
        String claveN = usuario.getN().toString();
        db.execSQL("INSERT INTO Usuario (nombre,fechaNacimiento,email,pass,claveE,claveN) VALUES ('" + nombreUser + "','" + fechaNacimiento + "','" + email + "','" + pass + "','" + claveE + "','" + claveN + "')");
        int id = -1;
        Cursor resultado = null;
        int contenido = -1;
        if (this.numTotalUsers() > 0) {
            db = this.getReadableDatabase();
            resultado = db.rawQuery("SELECT id FROM Usuario WHERE (nombre='" + nombreUser + "' AND email='" + email + "')", null);
            resultado.moveToFirst();
            while (resultado.isAfterLast() == false) {
                contenido = resultado.getInt(resultado.getColumnIndex("id"));
                resultado.moveToNext();
                id = contenido;
            }
        }
        return id;
    }

    public Usuario getUser(String nombre) {

        Cursor resultado = null;
        Usuario contenido = new Usuario();
        if (this.numTotalUsers() > 0) {
            db = this.getReadableDatabase();
            resultado = db.rawQuery("SELECT * FROM Usuario WHERE nombre='" + nombre + "'", null);
            resultado.moveToFirst();
            while (resultado.isAfterLast() == false) {
                contenido.setIdUsuario(resultado.getInt(resultado.getColumnIndex("id")));
                contenido.setNombre(resultado.getString(resultado.getColumnIndex("nombre")));
                contenido.setFechaNacimiento(resultado.getString(resultado.getColumnIndex("fechaNacimiento")));
                contenido.setEmail(resultado.getString(resultado.getColumnIndex("email")));
                contenido.setContrasena(new BigInteger(resultado.getString(resultado.getColumnIndex("pass"))));
                contenido.setE(new BigInteger(resultado.getString(resultado.getColumnIndex("claveE"))));
                contenido.setN(new BigInteger(resultado.getString(resultado.getColumnIndex("claveN"))));
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
        int dosis = medicamento.getDosis();
        float frecuencia = medicamento.getFrecuencia();
        float recordatorio = medicamento.getRecordatorio();
        db.execSQL("INSERT INTO Medicamento (nombre,dosis,frecuencia,recordatorio) VALUES ('" + nombre + "','" + dosis + "','" + frecuencia + "','" + recordatorio + "')");
        int id = -1;
        Cursor resultado = null;
        int contenido = -1;
        if (this.numTotalMedicamentos() > 0) {
            db = this.getReadableDatabase();
            resultado = db.rawQuery("SELECT id FROM Medicamento WHERE nombre='" + nombre + "'AND dosis='" + dosis + "'AND frecuencia='" + frecuencia + "' ORDER BY id DESC", null);
            resultado.moveToFirst();
            while (resultado.isAfterLast() == false) {
                contenido = resultado.getInt(resultado.getColumnIndex("id"));
                resultado.moveToNext();
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
        String descripcion = citaMedica.getDescripcion();
        float recordatorio = citaMedica.getRecordatorio();
        db.execSQL("INSERT INTO CitaMedica (nombre,fecha,hora,descripcion,recordatorio) VALUES ('" + nombre + "','" + fecha + "','" + hora + "','" + descripcion + "','" + recordatorio + "')");
        int id = -1;
        Cursor resultado = null;
        int contenido = -1;
        if (this.numTotalCitas() > 0) {
            db = this.getReadableDatabase();
            resultado = db.rawQuery("SELECT id FROM CitaMedica WHERE nombre='" + nombre + "'AND fecha='" + fecha + "'AND hora='" + hora + "' ORDER BY id DESC", null);
            resultado.moveToFirst();
            while (resultado.isAfterLast() == false) {
                contenido = resultado.getInt(resultado.getColumnIndex("id"));
                resultado.moveToNext();
            }
        }
        return id;
    }


    // Método para añadir una suscripción a la base de datos
    public void addSuscripcion(Suscripcion suscripcion) {
        db = this.getWritableDatabase();
        String email = suscripcion.getEmail();
        String fechafin = suscripcion.getFinSuscripcion();
        boolean esSuscrito = suscripcion.getEsSuscrito();
        db.execSQL("INSERT INTO Suscripcion (email,esSuscrito,finSuscripcion) VALUES ('" + email + "','" + esSuscrito + "','" + fechafin + "')");
    }

    // Método para actualizar una suscripción en la base de datos
    public void updateSuscripcion(Suscripcion suscripcion) {
        db = this.getWritableDatabase();
        String email = suscripcion.getEmail();
        boolean esSuscrito = suscripcion.getEsSuscrito();
        db.execSQL("UPDATE Suscipcion SET esSuscrito='" + esSuscrito + "' WHERE email='" + email+"'");
    }


    public ArrayList<Medicamento> getAllMedicamentos(Usuario user) {
        ArrayList<Medicamento> listMedi = new ArrayList<Medicamento>();
        int id = user.getIdUsuario();
        Cursor resultado = null;
        Medicamento contenido = new Medicamento();
        if (this.numTotalMedicamentos() > 0) {
            db = this.getReadableDatabase();
            resultado = db.rawQuery("SELECT * FROM Medicamento WHERE (SELECT idMedicamento FROM UsuarioMedicamento WHERE idUser='" + id + "') ORDER BY id DESC", null);
            resultado.moveToFirst();
            while (resultado.isAfterLast() == false) {
                contenido.setIdMedicamento(resultado.getInt(resultado.getColumnIndex("id")));
                contenido.setNombre(resultado.getString(resultado.getColumnIndex("nombre")));
                contenido.setDosis(resultado.getInt(resultado.getColumnIndex("dosis")));
                contenido.setFrecuencia(resultado.getFloat(resultado.getColumnIndex("frecuencia")));
                contenido.setRecordatorio(resultado.getFloat(resultado.getColumnIndex("recordatorio")));
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
            while (resultado.isAfterLast() == false) {
                contenido = resultado.getString(resultado.getColumnIndex("nombre"));
                resultado.moveToNext();
                listNombresMedi.add(contenido);
            }
        }
        return listNombresMedi;
    }


    public ArrayList<CitaMedica> getAllCitas(Usuario user) {
        ArrayList<CitaMedica> listMedi = new ArrayList<CitaMedica>();
        int id = user.getIdUsuario();
        Cursor resultado = null;
        CitaMedica contenido = new CitaMedica();
        if (this.numTotalCitas() > 0) {
            db = this.getReadableDatabase();
            resultado = db.rawQuery("SELECT * FROM CitaMedica WHERE (SELECT idCitaMedica FROM UsuarioCitaMedica WHERE idUser='" + id + "') ORDER BY id DESC", null);
            resultado.moveToFirst();
            while (resultado.isAfterLast() == false) {
                contenido.setIdCita(resultado.getInt(resultado.getColumnIndex("id")));
                contenido.setNombre(resultado.getString(resultado.getColumnIndex("nombre")));
                contenido.setFecha(resultado.getString(resultado.getColumnIndex("fecha")));
                contenido.setHora(resultado.getString(resultado.getColumnIndex("hora")));
                contenido.setDescripcion(resultado.getString(resultado.getColumnIndex("descripcion")));
                contenido.setRecordatorio(resultado.getFloat(resultado.getColumnIndex("recordatorio")));
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
            while (resultado.isAfterLast() == false) {
                contenido = resultado.getString(resultado.getColumnIndex("nombre"));
                resultado.moveToNext();
                listNombresCitas.add(contenido);
            }
        }
        return listNombresCitas;
    }

    /*

    @NELLA ESTO NO TIENE SENTIDO EN UNA CLASE DE TIPO DE BASE DE DATOS
    ESTO DEBERIA DE IR EN EL .JAVA QUE VAYA VINCULADO A LA ACTIVIDAD SUSCRIPCION, EN ESTE CASO
    SUSCRIPCIONACTIVITY

    // Método para mostrar un diálogo de confirmación de cancelación
    public void confirmarCancelacion(Context context, DialogInterface.OnClickListener confirmListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmar Cancelación");
        builder.setMessage("¿Estás seguro que deseas cancelar la suscripción?");
        builder.setPositiveButton("OK", confirmListener);
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    */
    public void cerrarDB() {
        db.close();
    }
}
