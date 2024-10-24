package es.ifp.labsalut.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

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
        db.execSQL("CREATE TABLE IF NOT EXISTS Medicamento (id INTEGER PRIMARY KEY NOT NULL, nombre TEXT, dosis TEXT, frecuencia TEXT, recordatorio TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS CitaMedica (id INTEGER PRIMARY KEY NOT NULL, nombre TEXT, fecha TEXT,hora TEXT,direccion TEXT, descripcion TEXT, recordatorio TEXT)");
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
    Método que borra un medicamento específico del Usuario usando su ID.
*/
    public void borrarMedicamento(int idMedicamento) {
        db = this.getWritableDatabase();

        // Usamos una transacción para mejorar el rendimiento y la seguridad.
        db.beginTransaction();
        try {
            // Preparamos la consulta SQL
            String sql = "DELETE FROM Medicamentos WHERE id = ?";
            SQLiteStatement statement = db.compileStatement(sql);

            // Asignamos el id del medicamento a la declaración preparada
            statement.bindLong(1, idMedicamento);
            statement.execute();

            db.setTransactionSuccessful(); // Confirmamos la transacción si todo sale bien.
        } catch (Exception e) {
            // Manejo de errores
            e.printStackTrace();
        } finally {
            db.endTransaction(); // Cerramos la transacción.
        }
    }

    /*
    Metodo que borra las medicamentos del Usuario
     */
    public void borrarAllMedicamentos(Usuario user) {
        db = this.getWritableDatabase();
        ArrayList<Serializable> medicamentos = user.getAllMedicamentos();

        // Usamos una transacción para mejorar el rendimiento y la seguridad.
        db.beginTransaction();
        try {
            // Preparamos la consulta SQL
            String sql = "DELETE FROM Medicamentos WHERE id = ?";
            SQLiteStatement statement = db.compileStatement(sql);

            for (Serializable med : medicamentos) {
                Medicamento medicamento = (Medicamento) med;
                int id = medicamento.getIdMedicamento();
                // Asignamos el id a la declaración preparada
                statement.bindLong(1, id);
                statement.execute();
            }

            db.setTransactionSuccessful(); // Confirmamos sitodo sale bien.
        } catch (Exception e) {
            // Manejo de errores
            e.printStackTrace();
        } finally {
            db.endTransaction(); // Cerramos la transacción.
        }
    }

    /*
        Método que borra una citaMedica específica del Usuario usando su ID.
    */
    public void borrarCita(int idCita) {
        db = this.getWritableDatabase();

        // Usamos una transacción para mejorar el rendimiento y la seguridad.
        db.beginTransaction();
        try {
            // Preparamos la consulta SQL
            String sql = "DELETE FROM CitaMedica WHERE id = ?";
            SQLiteStatement statement = db.compileStatement(sql);

            // Asignamos el id de la cita a la declaración preparada
            statement.bindLong(1, idCita);
            statement.execute();

            db.setTransactionSuccessful(); // Confirmamos la transacción si todo sale bien.
        } catch (Exception e) {
            // Manejo de errores
            e.printStackTrace();
        } finally {
            db.endTransaction(); // Cerramos la transacción.
        }
    }

    /*
    Metodo que borra las citasMedicas del Usuario
     */
    public void borrarAllCitas(Usuario user) {
        db = this.getWritableDatabase();
        ArrayList<Serializable> citas = user.getAllCitas();

        // Usamos una transacción para mejorar el rendimiento y la seguridad.
        db.beginTransaction();
        try {
            // Preparamos la consulta SQL
            String sql = "DELETE FROM CitaMedica WHERE id = ?";
            SQLiteStatement statement = db.compileStatement(sql);

            for (Serializable cit : citas) {
                CitaMedica cita = (CitaMedica) cit;
                int id = cita.getIdCita();
                // Asignamos el id a la declaración preparada
                statement.bindLong(1, id);
                statement.execute();
            }

            db.setTransactionSuccessful(); // Confirmamos la transacción si todo sale bien.
        } catch (Exception e) {
            // Manejo de errores
            e.printStackTrace();
        } finally {
            db.endTransaction(); // Cerramos la transacción.
        }
    }



    // Método para añadir un usuario a la base de datos
    public int addUser(Usuario usuario) {
        db = this.getWritableDatabase();
        int id = -1;

        // Usamos una transacción para mejorar el rendimiento y la seguridad.
        db.beginTransaction();
        try {
            // Preparamos la consulta de inserción
            String sql = "INSERT INTO Usuario (nombre, fechaNacimiento, email, pass) VALUES (?, ?, ?, ?)";
            SQLiteStatement statement = db.compileStatement(sql);

            // Asignamos los valores a la declaración preparada
            statement.bindString(1, usuario.getNombre());
            statement.bindString(2, usuario.getFechaNacimiento());
            statement.bindString(3, usuario.getEmail());
            statement.bindString(4, usuario.getContrasena());

            // Ejecutamos la inserción
            statement.executeInsert();

            // Obtener el ID del nuevo usuario insertado
            String query = "SELECT id FROM Usuario WHERE nombre = ? AND email = ?";
            Cursor resultado = db.rawQuery(query, new String[]{usuario.getNombre(), usuario.getEmail()});

            if (resultado.moveToFirst()) {
                id = resultado.getInt(resultado.getColumnIndex("id"));
            }

            db.setTransactionSuccessful(); // Confirmamos la transacción si todo sale bien.
        } catch (Exception e) {
            // Manejo de errores
            e.printStackTrace();
        } finally {
            db.endTransaction(); // Cerramos la transacción.
        }

        return id;
    }


    public Usuario getUser(String email) {
        Cursor resultado = null;
        Usuario contenido = new Usuario();
        if (this.numTotalUsers() > 0) {
            db = this.getReadableDatabase();

            // Usamos una consulta preparada para evitar inyecciones SQL.
            String query = "SELECT * FROM Usuario WHERE email = ?";
            resultado = db.rawQuery(query, new String[]{email});

            if (resultado.moveToFirst()) {
                contenido.setIdUsuario(resultado.getInt(resultado.getColumnIndex("id")));
                contenido.setNombre(resultado.getString(resultado.getColumnIndex("nombre")));
                contenido.setFechaNacimiento(resultado.getString(resultado.getColumnIndex("fechaNacimiento")));
                contenido.setEmail(resultado.getString(resultado.getColumnIndex("email")));
                contenido.setContrasena(resultado.getString(resultado.getColumnIndex("pass")));
            }
        }

        if (resultado != null) {
            resultado.close(); // Cerramos el cursor para liberar recursos.
        }

        return contenido; // Retorna el usuario o null si no se encontró.
    }



    public void addUserMedi(Usuario user, Medicamento medicamento) {
        db = this.getWritableDatabase();
        int idUser = user.getIdUsuario();
        int idMedi = medicamento.getIdMedicamento();

        // Usamos una consulta preparada para evitar inyecciones SQL.
        String query = "INSERT INTO UsuarioMedicamento (idUser, idMedicamento) VALUES (?, ?)";
        db.execSQL(query, new Object[]{idUser, idMedi});
    }

    /*
    Método que elimina un medicamento específico asociado a un usuario.
*/
    public void eliminarUserMedi(Usuario user, Medicamento medicamento) {
        db = this.getWritableDatabase();
        int idUser = user.getIdUsuario();
        int idMedi = medicamento.getIdMedicamento();

        // Usamos una consulta preparada para evitar inyecciones SQL.
        String query = "DELETE FROM UsuarioMedicamento WHERE idUser = ? AND idMedicamento = ?";
        db.execSQL(query, new Object[]{idUser, idMedi});
    }

    /*
   Método que elimina una cita médica específica asociada a un usuario.
   */
    public void eliminarUserAllMedi(Usuario user, ArrayList<Serializable> medicamentos) {

        db = this.getWritableDatabase();
        int idUser = user.getIdUsuario();

        for(int i=0; i < medicamentos.size();i++){
            Medicamento medicamento = (Medicamento) medicamentos.get(i);
            int idMedi = medicamento.getIdMedicamento();

            // Usamos una consulta preparada para evitar inyecciones SQL.
            String query = "DELETE FROM UsuarioMedicamento WHERE idUser = ? AND idCitaMedica = ?";
            db.execSQL(query, new Object[]{idUser, idMedi});
        }
    }


    public void addUserCita(Usuario user, CitaMedica cita) {
        db = this.getWritableDatabase();
        int idUser = user.getIdUsuario();
        int idCita = cita.getIdCita();

        // Usamos una consulta preparada para evitar inyecciones SQL.
        String query = "INSERT INTO UsuarioCitaMedica (idUser, idCitaMedica) VALUES (?, ?)";
        db.execSQL(query, new Object[]{idUser, idCita});
    }

    /*
    Método que elimina una cita médica específica asociada a un usuario.
    */
    public void eliminarUserCita(Usuario user, CitaMedica cita) {
        db = this.getWritableDatabase();
        int idUser = user.getIdUsuario();
        int idCita = cita.getIdCita();

        // Usamos una consulta preparada para evitar inyecciones SQL.
        String query = "DELETE FROM UsuarioCitaMedica WHERE idUser = ? AND idCitaMedica = ?";
        db.execSQL(query, new Object[]{idUser, idCita});
    }

    /*
    Método que elimina una cita médica específica asociada a un usuario.
    */
    public void eliminarUserAllCitas(Usuario user, ArrayList<Serializable> citas) {

        db = this.getWritableDatabase();
        int idUser = user.getIdUsuario();

        for(int i=0; i < citas.size();i++){
            CitaMedica cita = (CitaMedica) citas.get(i);
            int idCita = cita.getIdCita();

            // Usamos una consulta preparada para evitar inyecciones SQL.
            String query = "DELETE FROM UsuarioCitaMedica WHERE idUser = ? AND idCitaMedica = ?";
            db.execSQL(query, new Object[]{idUser, idCita});
        }
    }


    public void addMedicamento(Medicamento medicamento) {
        // Abrir la base de datos en modo escritura
        db = this.getWritableDatabase();

        // Usar ContentValues para evitar inyecciones SQL
        ContentValues values = new ContentValues();
        values.put("id", medicamento.getIdMedicamento());
        values.put("nombre", medicamento.getNombre());
        values.put("dosis", medicamento.getDosis());
        values.put("frecuencia", medicamento.getFrecuencia());
        values.put("recordatorio", medicamento.getRecordatorio());

        // Insertar el medicamento en la base de datos
        db.insert("Medicamento", null, values);
    }

    // Método para añadir una cita médica a la base de datos
    public void addCita(CitaMedica citaMedica) {
        db = this.getWritableDatabase();

        // Usar ContentValues para evitar inyecciones SQL
        ContentValues values = new ContentValues();
        values.put("id", citaMedica.getIdCita());
        values.put("nombre", citaMedica.getNombre());
        values.put("fecha", citaMedica.getFecha());
        values.put("hora", citaMedica.getHora());
        values.put("direccion", citaMedica.getDireccion());
        values.put("descripcion", citaMedica.getDescripcion());
        values.put("recordatorio", citaMedica.getRecordatorio());


        // Insertar el medicamento en la base de datos
        db.insert("CitaMedica", null, values);

    }



    // Método para añadir una suscripción a la base de datos
    public void addSuscripcion(Suscripcion suscripcion) {
        db = this.getWritableDatabase();
        String email = suscripcion.getEmail();
        String finSuscripcion = suscripcion.getFinSuscripcion();
        boolean esSuscrito = suscripcion.getEsSuscrito();

        // Usamos una consulta preparada para evitar inyecciones SQL
        String insertQuery = "INSERT INTO Suscripcion (email, esSuscrito, finSuscripcion) VALUES (?, ?, ?)";
        db.execSQL(insertQuery, new Object[]{email, esSuscrito ? 1 : 0, finSuscripcion});
    }


    // Método para actualizar una suscripción en la base de datos
    public void updateSuscripcion(Suscripcion suscripcion) {
        db = this.getWritableDatabase();
        String email = suscripcion.getEmail();
        boolean esSuscrito = suscripcion.getEsSuscrito();

        // Usamos una consulta preparada para evitar inyecciones SQL
        String updateQuery = "UPDATE Suscripcion SET esSuscrito = ? WHERE email = ?";
        db.execSQL(updateQuery, new Object[]{esSuscrito ? 1 : 0, email});
    }



    public ArrayList<Serializable> getAllMedicamentos(Usuario user) {
        ArrayList<Serializable> listMedi = new ArrayList<>();
        int id = user.getIdUsuario();
        Cursor resultado = null;

        // Verificar si hay medicamentos
        if (this.numTotalMedicamentos() > 0) {
            db = this.getReadableDatabase();

            // Usar JOIN para obtener los medicamentos de un usuario específico
            resultado = db.rawQuery(
                    "SELECT m.* FROM Medicamento m " +
                            "INNER JOIN UsuarioMedicamento um ON m.id = um.idMedicamento " +
                            "WHERE um.idUser = ? " +
                            "ORDER BY m.id ASC",
                    new String[]{String.valueOf(id)} // Usar parámetros para evitar inyección SQL
            );

            // Iterar a través de los resultados
            if (resultado != null && resultado.moveToFirst()) {
                do {
                    // Crear una nueva instancia de Medicamento por cada registro
                    Medicamento contenido = new Medicamento();
                    contenido.setIdMedicamento(resultado.getInt(resultado.getColumnIndex("id")));
                    contenido.setNombre(resultado.getString(resultado.getColumnIndex("nombre")));
                    contenido.setDosis(resultado.getString(resultado.getColumnIndex("dosis")));
                    contenido.setFrecuencia(resultado.getString(resultado.getColumnIndex("frecuencia")));
                    contenido.setRecordatorio(resultado.getString(resultado.getColumnIndex("recordatorio")));

                    // Agregar el nuevo medicamento a la lista
                    listMedi.add(contenido);
                } while (resultado.moveToNext()); // Cambiar a moveToNext() aquí
            }

            // Cerrar el cursor al final
            if (resultado != null) {
                resultado.close();
            }
        }
        return listMedi;
    }


    public ArrayList<String> getAllNombreMedi(Usuario user) {
        ArrayList<String> listNombresMedi = new ArrayList<>();
        int id = user.getIdUsuario();
        Cursor resultado = null;

        if (this.numTotalMedicamentos() > 0) {
            db = this.getReadableDatabase();

            // Consulta mejorada para evitar inyecciones SQL
            String query = "SELECT nombre FROM Medicamento WHERE id IN (SELECT idMedicamento FROM UsuarioMedicamento WHERE idUser = ?) ORDER BY id DESC";

            resultado = db.rawQuery(query, new String[]{String.valueOf(id)});
            resultado.moveToFirst();

            while (!resultado.isAfterLast()) {
                String contenido = resultado.getString(resultado.getColumnIndex("nombre"));
                listNombresMedi.add(contenido);
                resultado.moveToNext();
            }
        }

        // Cerrar el cursor después de usarlo para liberar recursos
        if (resultado != null) {
            resultado.close();
        }

        return listNombresMedi;
    }



    public ArrayList<Serializable> getAllCitasMedicas(Usuario user) {
        ArrayList<Serializable> listCitas = new ArrayList<>();
        int id = user.getIdUsuario();
        Cursor resultado = null;

        // Verificar si hay citas médicas
        if (this.numTotalCitas() > 0) {
            db = this.getReadableDatabase();

            // Usar JOIN para obtener las citas médicas del usuario específico
            resultado = db.rawQuery(
                    "SELECT cm.* FROM CitaMedica cm " +
                            "INNER JOIN UsuarioCitaMedica uc ON cm.id = uc.idCitaMedica " +
                            "WHERE uc.idUser = ? " +
                            "ORDER BY cm.id ASC",
                    new String[]{String.valueOf(id)} // Usar parámetros para evitar inyección SQL
            );

            // Iterar a través de los resultados
            if (resultado != null && resultado.moveToFirst()) {
                do {
                    // Crear una nueva instancia de CitaMedica por cada registro
                    CitaMedica contenido = new CitaMedica();
                    contenido.setIdCita(resultado.getInt(resultado.getColumnIndex("id")));
                    contenido.setNombre(resultado.getString(resultado.getColumnIndex("nombre")));
                    contenido.setFecha(resultado.getString(resultado.getColumnIndex("fecha")));
                    contenido.setHora(resultado.getString(resultado.getColumnIndex("hora")));
                    contenido.setDireccion(resultado.getString(resultado.getColumnIndex("direccion")));
                    contenido.setDescripcion(resultado.getString(resultado.getColumnIndex("descripcion")));
                    contenido.setRecordatorio(resultado.getString(resultado.getColumnIndex("recordatorio")));

                    // Agregar la nueva cita a la lista
                    listCitas.add(contenido);
                } while (resultado.moveToNext()); // Cambiar a moveToNext() aquí
            }

            // Cerrar el cursor al final
            if (resultado != null) {
                resultado.close();
            }
        }
        return listCitas;
    }


    public ArrayList<String> getAllNombreCitas(Usuario user) {
        ArrayList<String> listNombresCitas = new ArrayList<>();
        int id = user.getIdUsuario();
        Cursor resultado = null;

        if (this.numTotalCitas() > 0) { // Cambia aquí para verificar el número de citas
            db = this.getReadableDatabase();

            // Consulta mejorada para evitar inyecciones SQL
            String query = "SELECT nombre FROM CitaMedica WHERE id IN (SELECT idCitaMedica FROM UsuarioCitaMedica WHERE idUser = ?) ORDER BY id DESC";

            resultado = db.rawQuery(query, new String[]{String.valueOf(id)});
            resultado.moveToFirst();

            while (!resultado.isAfterLast()) {
                String contenido = resultado.getString(resultado.getColumnIndex("nombre"));
                listNombresCitas.add(contenido);
                resultado.moveToNext();
            }
        }

        // Cerrar el cursor después de usarlo para liberar recursos
        if (resultado != null) {
            resultado.close();
        }

        return listNombresCitas;
    }


    public void cerrarDB() {
        db.close();
    }
}
