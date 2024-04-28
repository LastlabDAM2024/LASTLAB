package es.ifp.labsalut;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDatos extends SQLiteOpenHelper {


    public BaseDatos(Context context) {
        super(context, "BaseDatos", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Aquí se crean las tablas si no existen
        db.execSQL("CREATE TABLE IF NOT EXISTS Usuario (id INTEGER PRIMARY KEY, nombre TEXT, fechaNacimiento TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Medicamento (id INTEGER PRIMARY KEY, nombre TEXT, dosis INTEGER, frecuencia INTEGER, recordatorio INTEGER, idUsuario INTEGER, FOREIGN KEY(idUsuario) REFERENCES Usuario(id))");
        db.execSQL("CREATE TABLE IF NOT EXISTS CitaMedica (id INTEGER PRIMARY KEY, nombre TEXT, fecha TEXT, descripcion TEXT, recordatorio TEXT, idUsuario INTEGER, FOREIGN KEY(idUsuario) REFERENCES Usuario(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Se ejecuta si hay una actualización de la base de datos
        db.execSQL("DROP TABLE IF EXISTS Usuario");
        db.execSQL("DROP TABLE IF EXISTS Medicamento");
        db.execSQL("DROP TABLE IF EXISTS CitaMedica");
        onCreate(db);
    }

    // Método para añadir un usuario a la base de datos
    public void addUser(Usuario usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Lógica para añadir el usuario a la tabla Usuario
    }

    // Métodos para editar y eliminar usuarios, medicamentos y citas médicas
    // Implementarlos de manera similar al método addUser

    // Método para añadir un medicamento a la base de datos
    public void addMedicamento(Medicamento medicamento) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Lógica para añadir el medicamento a la tabla Medicamento
    }

    // Método para añadir una cita médica a la base de datos
    public void addCita(CitaMedica citaMedica) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Lógica para añadir la cita médica a la tabla CitaMedica
    }
}
