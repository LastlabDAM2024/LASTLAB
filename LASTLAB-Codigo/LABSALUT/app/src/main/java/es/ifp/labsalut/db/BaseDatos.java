package es.ifp.labsalut.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.DatabaseUtils;


import java.math.BigInteger;

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
        db.execSQL("CREATE TABLE IF NOT EXISTS Usuario (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nombre TEXT, fechaNacimiento TEXT, email TEXT, pass BIGINTEGER, claveE BIGINTEGER, claveN BIGINTEGER)");
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
    Metodo que borra la tabla Medicamentos
     */
    public void borrarAllMedicamentos() {
        db = this.getWritableDatabase();
        db.execSQL("DELETE FROM Medicamento");
    }

    /*
    Metodo que borra la tabla CitaMedica
     */
    public void borrarAllCitas() {
        db = this.getWritableDatabase();
        db.execSQL("DELETE FROM CitaMedica");
    }


    // Método para añadir un usuario a la base de datos
    public void addUser(Usuario usuario) {
        db = this.getWritableDatabase();
        String nombreUser = usuario.getNombre();
        String fechaNacimiento = usuario.getFechaNacimiento();
        String email = usuario.getEmail();
        BigInteger pass = usuario.getContrasena();
        BigInteger claveE = usuario.getE();
        BigInteger claveN = usuario.getN();
        db.execSQL("INSERT INTO Usuario (nombre,fechaNacimiento,email,pass,claveE,claveN) VALUES ('" + nombreUser + "','" + fechaNacimiento + "','" + email + "','" + pass + "','" + claveE + "','" + claveN + "')");
    }

    // Método para añadir un medicamento a la base de datos
    public void addMedicamento(Medicamento medicamento) {
        db = this.getWritableDatabase();
        String nombre = medicamento.getNombre();
        int dosis = medicamento.getDosis();
        float frecuencia = medicamento.getFrecuencia();
        float recordatorio = medicamento.getRecordatorio();
        db.execSQL("INSERT INTO Medicamento (nombre,dosis,frecuencia,recordatorio) VALUES ('" + nombre + "','" + dosis + "','" + frecuencia + "','" + recordatorio + "')");

    }

    // Método para añadir una cita médica a la base de datos
    public void addCita(CitaMedica citaMedica) {
        db = this.getWritableDatabase();
        String nombre = citaMedica.getNombre();
        String fecha = citaMedica.getFecha();
        String hora = citaMedica.getHora();
        String descripcion = citaMedica.getDescripcion();
        float recordatorio = citaMedica.getRecordatorio();
        db.execSQL("INSERT INTO CitaMedica (nombre,fecha,hora,descripcion,recordatorio) VALUES ('" + nombre + "','" + fecha + "','" + hora + "','" + descripcion + "','" + recordatorio + "')");
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
        db.execSQL("UPDATE Suscipcion SET esSuscrito='"+esSuscrito+"' WHERE email="+email);
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
