package es.ifp.labsalut;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.sql.Date;

public class BaseDatos extends SQLiteOpenHelper {

    protected SQLiteDatabase db;

    public BaseDatos(Context context) {
        super(context, "BaseDatos", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Aquí se crean las tablas si no existen
        db.execSQL("CREATE TABLE IF NOT EXISTS Usuario (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nombre TEXT, fechaNacimiento TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Medicamento (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nombre TEXT, dosis INTEGER, frecuencia FLOAT, recordatorio FLOAT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS CitaMedica (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nombre TEXT, fecha TEXT, descripcion TEXT, recordatorio FLOAT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Suscripcion (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, email TEXT, esSuscrito INTEGER, finSuscripcion TEXT)");
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

    // Método para añadir un usuario a la base de datos
    public void addUser(Usuario usuario) {
        db = this.getWritableDatabase();
        // Lógica para añadir el usuario a la tabla Usuario
    }

    // Método para añadir un medicamento a la base de datos
    public void addMedicamento(Medicamento medicamento) {
        db = this.getWritableDatabase();
        // Lógica para añadir el medicamento a la tabla Medicamento
    }

    // Método para añadir una cita médica a la base de datos
    public void addCita(CitaMedica citaMedica) {
        db = this.getWritableDatabase();
        // Lógica para añadir la cita médica a la tabla CitaMedica
    }

    // Método para añadir una suscripción a la base de datos
    public void addSuscripcion(Suscripcion suscripcion) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", suscripcion.getEmail());
        values.put("esSuscrito", suscripcion.isEsSuscrito() ? 1 : 0); // Convertir booleano a entero
        values.put("finSuscripcion", suscripcion.getFinSuscripcion().toString()); // Suponiendo que finSuscripcion es un objeto Date

        db.insert("Suscripcion", null, values);
        db.close();
    }

    // Método para cancelar una suscripción en la base de datos
    public void cancelSuscripcion(String email) {
        db = this.getWritableDatabase();
        db.delete("Suscripcion", "email = ?", new String[]{email});
        db.close();
    }

    // Método para actualizar una suscripción en la base de datos
    public void updateSuscripcion(Suscripcion suscripcion) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("esSuscrito", suscripcion.isEsSuscrito() ? 1 : 0); // Convertir booleano a entero
        values.put("finSuscripcion", suscripcion.getFinSuscripcion().toString()); // Suponiendo que finSuscripcion es un objeto Date

        db.update("Suscripcion", values, "email = ?", new String[]{suscripcion.getEmail()});
        db.close();
    }

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
}
