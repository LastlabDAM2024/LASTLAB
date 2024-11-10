package es.ifp.labsalut;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import es.ifp.labsalut.activities.MainActivity;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 33)
public class MainActivityTest {

    private MainActivity activity;

    @Before
    public void setUp() {
        // Se crea una instancia de la actividad
        activity = Robolectric.buildActivity(MainActivity.class).create().get();
    }

    // Test que verifica el texto de un TextView
    @Test
    public void testTituloTextView() {
        TextView tituloTextView = activity.findViewById(R.id.titulo);
        assertNotNull(tituloTextView);  // Verifica que el TextView no sea null
        assertEquals("LabSalut", tituloTextView.getText().toString());
    }

    // Test para verificar el funcionamiento de los botones
    @Test
    public void testBotonAceptar() {
        Button botonAceptar = activity.findViewById(R.id.botonAceptar);
        assertNotNull(botonAceptar);  // Verifica que el botón no sea null
        assertEquals("Aceptar", botonAceptar.getText().toString());
    }

    @Test
    public void testNuevoUsuarioBoton() {
        Button nuevoUsuarioBoton = activity.findViewById(R.id.nuevoUsuarioBoton);
        assertNotNull(nuevoUsuarioBoton);  // Verifica que el botón no sea null
        assertEquals("Crear nuevo usuario", nuevoUsuarioBoton.getText().toString());
    }

    // Test para verificar los campos de texto
    @Test
    public void testCampoEmail() {
        EditText emailField = activity.findViewById(R.id.email);
        assertNotNull(emailField);  // Verifica que el campo de texto para email no sea null
    }

    @Test
    public void testCampoPassword() {
        EditText passwordField = activity.findViewById(R.id.pass);
        assertNotNull(passwordField);  // Verifica que el campo de texto para contraseña no sea null
    }

    // Test para verificar la casilla de verificación
    @Test
    public void testRecordarUsuarioCheckBox() {
        CheckBox recordarUsuarioCheckBox = activity.findViewById(R.id.recordarUsuario);
        assertNotNull(recordarUsuarioCheckBox);  // Verifica que la casilla no sea null
        assertFalse(recordarUsuarioCheckBox.isChecked());  // Verifica que inicialmente no esté marcada

        // Si lo marcamos, verificamos que el estado cambie
        recordarUsuarioCheckBox.setChecked(true);
        assertTrue(recordarUsuarioCheckBox.isChecked());  // Verifica que ahora esté marcada
    }
}
