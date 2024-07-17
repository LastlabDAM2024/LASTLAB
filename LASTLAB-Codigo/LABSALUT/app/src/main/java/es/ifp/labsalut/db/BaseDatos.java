package es.ifp.labsalut.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
        db.execSQL("CREATE TABLE IF NOT EXISTS CitaMedica (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nombre TEXT, fecha TEXT,hora TEXT, descripcion TEXT, recordatorio TEXT)");
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
        SQLiteDatabase db = this.getWritableDatabase();
        String nombreUser = usuario.getNombre();
        String fechaNacimiento = usuario.getFechaNacimiento();
        String email = usuario.getEmail();
        String pass = usuario.getContrasena();

        ContentValues values = new ContentValues();
        values.put("nombre", nombreUser);
        values.put("fechaNacimiento", fechaNacimiento);
        values.put("email", email);
        values.put("pass", pass);

        long id = db.insert("Usuario", null, values);

        db.close();

        // Convertir el long id a int
        if (id == -1) {
            // Si hubo un error al insertar, devolver un valor que indique error
            return -1;
        } else {
            // Si se insertó correctamente, convertir el long a int
            return (int) id;
        }
    }



    public Usuario getUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        Usuario usuario = new Usuario();

        try {
            cursor = db.rawQuery("SELECT * FROM Usuario WHERE email=?", new String[]{email});

            if (cursor != null && cursor.moveToFirst()) {
                usuario.setIdUsuario(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                usuario.setNombre(cursor.getString(cursor.getColumnIndexOrThrow("nombre")));
                usuario.setFechaNacimiento(cursor.getString(cursor.getColumnIndexOrThrow("fechaNacimiento")));
                usuario.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
                usuario.setContrasena(cursor.getString(cursor.getColumnIndexOrThrow("pass")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return usuario;
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

    public int addMedicamento(Medicamento medicamento) {
        SQLiteDatabase db = this.getWritableDatabase();
        String nombre = medicamento.getNombre();
        String dosis = medicamento.getDosis();
        String frecuencia = medicamento.getFrecuencia();
        String recordatorio = medicamento.getRecordatorio();

        // Insertar el medicamento en la tabla Medicamento
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("dosis", dosis);
        values.put("frecuencia", frecuencia);
        values.put("recordatorio", recordatorio);

        long id = db.insert("Medicamento", null, values);

        // Verificar si la inserción fue exitosa
        if (id == -1) {
            // Manejar el caso de inserción fallida si es necesario
            return -1;
        }

        // Obtener el ID del medicamento insertado
        int insertedId = -1;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT id FROM Medicamento WHERE nombre=? AND dosis=? AND frecuencia=? ORDER BY id DESC", new String[]{nombre, dosis, frecuencia});

            if (cursor != null && cursor.moveToFirst()) {
                insertedId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return insertedId;
    }

    // Método para añadir una cita médica a la base de datos
    public int addCita(CitaMedica citaMedica) {
        db = this.getWritableDatabase();
        String nombre = citaMedica.getNombre();
        String fecha = citaMedica.getFecha();
        String hora = citaMedica.getHora();
        String descripcion = citaMedica.getDescripcion();
        String recordatorio = citaMedica.getRecordatorio();

        // Ejecutar la inserción de la cita médica
        db.execSQL("INSERT INTO CitaMedica (nombre,fecha,hora,descripcion,recordatorio) VALUES ('" + nombre + "','" + fecha + "','" + hora + "','" + descripcion + "','" + recordatorio + "')");

        int id = -1;
        Cursor resultado = null;

        // Obtener el ID de la cita médica insertada
        try {
            resultado = db.rawQuery("SELECT last_insert_rowid()", null);
            if (resultado.moveToFirst()) {
                id = resultado.getInt(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resultado != null) {
                resultado.close();
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
        db.execSQL("INSERT INTO Suscripcion (email, esSuscrito, finSuscripcion) VALUES (?, ?, ?)", new Object[]{email, esSuscrito, finSuscripcion});
    }

    // Método para actualizar una suscripción en la base de datos
    public void updateSuscripcion(Suscripcion suscripcion) {
        db = this.getWritableDatabase();
        String email = suscripcion.getEmail();
        boolean esSuscrito = suscripcion.getEsSuscrito();
        db.execSQL("UPDATE Suscripcion SET esSuscrito='" + esSuscrito + "' WHERE email='" + email + "'");
    }


    public ArrayList<Medicamento> getAllMedicamentos(Usuario user) {
        ArrayList<Medicamento> listMedi = new ArrayList<>();
        int id = user.getIdUsuario();
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM Medicamento WHERE id IN (SELECT idMedicamento FROM UsuarioMedicamento WHERE idUser='" + id + "') ORDER BY id DESC", null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Medicamento medicamento = new Medicamento();
                    medicamento.setIdMedicamento(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                    medicamento.setNombre(cursor.getString(cursor.getColumnIndexOrThrow("nombre")));
                    medicamento.setDosis(cursor.getString(cursor.getColumnIndexOrThrow("dosis")));
                    medicamento.setFrecuencia(cursor.getString(cursor.getColumnIndexOrThrow("frecuencia")));
                    medicamento.setRecordatorio(cursor.getString(cursor.getColumnIndexOrThrow("recordatorio")));
                    listMedi.add(medicamento);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return listMedi;
    }



    public ArrayList<String> getAllNombreMedi(Usuario user) {
        ArrayList<String> listNombresMedi = new ArrayList<>();
        int id = user.getIdUsuario();
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery("SELECT nombre FROM Medicamento WHERE id IN (SELECT idMedicamento FROM UsuarioMedicamento WHERE idUser='" + id + "') ORDER BY id DESC", null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String nombreMedicamento = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                    listNombresMedi.add(nombreMedicamento);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return listNombresMedi;
    }



    public ArrayList<CitaMedica> getAllCitas(Usuario user) {
        ArrayList<CitaMedica> listCitas = new ArrayList<>();
        int id = user.getIdUsuario();
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM CitaMedica WHERE id IN (SELECT idCitaMedica FROM UsuarioCitaMedica WHERE idUser='" + id + "') ORDER BY id DESC", null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    CitaMedica cita = new CitaMedica();
                    cita.setIdCita(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                    cita.setNombre(cursor.getString(cursor.getColumnIndexOrThrow("nombre")));
                    cita.setFecha(cursor.getString(cursor.getColumnIndexOrThrow("fecha")));
                    cita.setHora(cursor.getString(cursor.getColumnIndexOrThrow("hora")));
                    cita.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow("descripcion")));
                    cita.setRecordatorio(cursor.getString(cursor.getColumnIndexOrThrow("recordatorio")));

                    listCitas.add(cita);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return listCitas;
    }

    public ArrayList<String> getAllNombreCitas(Usuario user) {
        ArrayList<String> listNombresCitas = new ArrayList<>();
        int id = user.getIdUsuario();
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM CitaMedica WHERE id IN (SELECT idCitaMedica FROM UsuarioCitaMedica WHERE idUser='" + id + "') ORDER BY id DESC", null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String nombreCita = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                    listNombresCitas.add(nombreCita);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return listNombresCitas;
    }
}