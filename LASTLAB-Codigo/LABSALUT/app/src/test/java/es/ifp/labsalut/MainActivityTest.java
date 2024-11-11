package es.ifp.labsalut;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import android.os.Build;
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
import es.ifp.labsalut.activities.MainActivity;

@Config(sdk = Build.VERSION_CODES.TIRAMISU)
@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
    private MainActivity activity;

    @Before
    public void setUp() {
        try {
            activity = Robolectric.buildActivity(MainActivity.class)
                    .create()
                    .start()
                    .resume()
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTituloTextView() {
        TextView tituloTextView = activity.findViewById(R.id.titulo);
        assertNotNull("El TextView del título no debería ser null", tituloTextView);
        assertEquals("LabSalut", tituloTextView.getText().toString());
    }

    @Test
    public void testBotonAceptar() {
        Button botonAceptar = activity.findViewById(R.id.botonAceptar);
        assertNotNull("El botón aceptar no debería ser null", botonAceptar);
        assertEquals("Aceptar", botonAceptar.getText().toString());
    }

    @Test
    public void testNuevoUsuarioBoton() {
        Button nuevoUsuarioBoton = activity.findViewById(R.id.nuevoUsuarioBoton);
        assertNotNull("El botón nuevo usuario no debería ser null", nuevoUsuarioBoton);
        assertEquals("Crear nuevo usuario", nuevoUsuarioBoton.getText().toString());
    }

    @Test
    public void testCampoEmail() {
        EditText emailField = activity.findViewById(R.id.email);
        assertNotNull("El campo de email no debería ser null", emailField);
    }

    @Test
    public void testCampoPassword() {
        EditText passwordField = activity.findViewById(R.id.pass);
        assertNotNull("El campo de contraseña no debería ser null", passwordField);
    }

    @Test
    public void testRecordarUsuarioCheckBox() {
        CheckBox recordarUsuarioCheckBox = activity.findViewById(R.id.recordarUsuario);
        assertNotNull("El checkbox no debería ser null", recordarUsuarioCheckBox);
        assertFalse("El checkbox debería estar desmarcado inicialmente",
                recordarUsuarioCheckBox.isChecked());

        recordarUsuarioCheckBox.setChecked(true);
        assertTrue("El checkbox debería estar marcado después de activarlo",
                recordarUsuarioCheckBox.isChecked());
    }
}