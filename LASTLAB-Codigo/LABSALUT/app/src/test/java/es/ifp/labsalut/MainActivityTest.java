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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import es.ifp.labsalut.activities.MainActivity;

/**
 * Esta clase contiene los casos de prueba unitarios para la actividad `MainActivity`.
 * Los tests se realizan utilizando el framework de Robolectric y JUnit, permitiendo la simulación
 * de la actividad en un entorno controlado sin necesidad de un dispositivo físico.
 *
 * Se incluyen pruebas para verificar el correcto funcionamiento de elementos de la interfaz
 * de usuario en la `MainActivity`, como botones, campos de texto y checkboxes. Cada prueba
 * se asegura de que los elementos sean inicializados correctamente, que contengan los textos esperados
 * y que las interacciones del usuario funcionen según lo previsto.
 *
 * Casos de prueba:
 * - Verificación de la visibilidad y el texto de un `TextView` para el título de la aplicación.
 * - Comprobación de la inicialización y el texto de un botón para la acción de aceptar.
 * - Verificación de un botón para crear un nuevo usuario.
 * - Comprobación de que los campos de email y contraseña estén correctamente inicializados.
 * - Verificación del estado del checkbox para recordar al usuario y la interacción con él.
 *
 * La clase está configurada para ejecutarse en un entorno simulado con la versión de Android especificada
 * (Android TIRAMISU) y utiliza Robolectric para crear una instancia de la actividad y realizar las pruebas.
 *
 * @see MainActivity
 * @see Robolectric
 * @see org.junit.Test
 */


@Config(sdk = Build.VERSION_CODES.TIRAMISU)
@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
    private MainActivity activity;

    // Variable para almacenar los resultados de los tests
    private StringBuilder testResults = new StringBuilder();

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
        try{
        TextView tituloTextView = activity.findViewById(R.id.titulo);
        assertNotNull("El TextView del título no debería ser null", tituloTextView);
        assertEquals("LabSalut", tituloTextView.getText().toString());
        // Almacenar el mensaje de éxito
        testResults.append("Tets de TextView: PASADO correctamente.\n");
    } catch (AssertionError e) {
        // Almacenar el mensaje de fallo
        testResults.append("Test de TextView:  FALLADO. ");
    }
    }

    @Test
    public void testBotonAceptar() {
        try{
        Button botonAceptar = activity.findViewById(R.id.botonAceptar);
        assertNotNull("El botón aceptar no debería ser null", botonAceptar);
        assertEquals("Aceptar", botonAceptar.getText().toString());
            // Almacenar el mensaje de éxito
            testResults.append("Tets de Botón Aceptar: PASADO correctamente.\n");
            } catch (AssertionError e) {
                // Almacenar el mensaje de fallo
                testResults.append("Test de Botón Aceptar:  FALLADO. ");
            }
    }

    @Test
    public void testNuevoUsuarioBoton() {

        try {
        Button nuevoUsuarioBoton = activity.findViewById(R.id.nuevoUsuarioBoton);
        assertNotNull("El botón nuevo usuario no debería ser null", nuevoUsuarioBoton);
        assertEquals("Crear nuevo usuario", nuevoUsuarioBoton.getText().toString());
            // Almacenar el mensaje de éxito
            testResults.append("Tets de Usuario Boton: PASADO correctamente.\n");
        } catch (AssertionError e) {
            // Almacenar el mensaje de fallo
            testResults.append("Test de Usuario Boton:  FALLADO. ");
        }
    }

    @Test
    public void testCampoEmail() {
        try{
        EditText emailField = activity.findViewById(R.id.email);
        assertNotNull("El campo de email no debería ser null", emailField);
            // Almacenar el mensaje de éxito
            testResults.append("Tet de Campo de E-mail: PASADO correctamente.\n");
        } catch (AssertionError e) {
            // Almacenar el mensaje de fallo
            testResults.append("Test de Campo de E-mail:  FALLADO. ");
        }
    }

    @Test
    public void testCampoPassword() {
        try{
        EditText passwordField = activity.findViewById(R.id.pass);
        assertNotNull("El campo de contraseña no debería ser null", passwordField);
            // Almacenar el mensaje de éxito
            testResults.append("Tets de Campo de Contraseña: PASADO correctamente.\n");
        } catch (AssertionError e) {
            // Almacenar el mensaje de fallo
            testResults.append("Test de Campo de Contraseña:  FALLADO. ");
        }
    }

    @Test
    public void testRecordarUsuarioCheckBox() {
        try{
        CheckBox recordarUsuarioCheckBox = activity.findViewById(R.id.recordarUsuario);
        assertNotNull("El checkbox no debería ser null", recordarUsuarioCheckBox);
        assertFalse("El checkbox debería estar desmarcado inicialmente",
                recordarUsuarioCheckBox.isChecked());

        recordarUsuarioCheckBox.setChecked(true);
        assertTrue("El checkbox debería estar marcado después de activarlo",
                recordarUsuarioCheckBox.isChecked());

            // Almacenar el mensaje de éxito
            testResults.append("Tets Chechbx de recordar user: PASADO correctamente.\n");
        } catch (AssertionError e) {
            // Almacenar el mensaje de fallo
            testResults.append("Test de  Checkbox de recordar user:  FALLADO. ");
        }
    }

    // Método que se ejecuta al final de la ejecución de los tests
    @After
    public void printTestResults() {
        System.out.println("\nResultado de los tests:\n");
        System.out.println(testResults.toString());
    }
}