package es.ifp.labsalut;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

import android.content.Intent;
import android.widget.TextView;

import androidx.activity.OnBackPressedDispatcher;
import androidx.fragment.app.Fragment;
import androidx.test.core.app.ActivityScenario;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import es.ifp.labsalut.activities.MainActivity;
import es.ifp.labsalut.activities.RegistroActivity;
import es.ifp.labsalut.ui.ColorStatusBar;
import es.ifp.labsalut.ui.RegistroFragment;

/**
 * Esta clase contiene los casos de prueba unitarios para la actividad `RegistroActivity`.
 * Los tests se realizan utilizando Robolectric y JUnit para simular el comportamiento de la actividad
 * y sus elementos de interfaz de usuario en un entorno controlado sin necesidad de un dispositivo físico.
 *
 * Los casos de prueba verifican el correcto comportamiento de la actividad `RegistroActivity` al
 * cargar el fragmento correspondiente (`RegistroFragment`), configurar los colores de la interfaz,
 * y comprobar la correcta inicialización de los elementos de la interfaz como botones, campos de texto,
 * y la barra de estado. También incluye pruebas de la interacción con el botón de retroceso para asegurar
 * que la actividad navegue correctamente hacia la actividad principal (`MainActivity`).
 *
 * Casos de prueba:
 * - Verificación de que el fragmento `RegistroFragment` está cargado correctamente y que el color
 *   de la barra de estado coincide con el color de la toolbar.
 * - Comprobación de que el título de la toolbar es el esperado.
 * - Verificación de la funcionalidad del botón de retroceso, asegurando que al presionarlo se navegue
 *   a `MainActivity`.
 * - Comprobación de la correcta inicialización de los campos de texto y botones en `RegistroFragment`.
 *
 * La clase está configurada para ejecutarse en un entorno simulado con la versión de Android especificada
 * (SDK 33) y utiliza Robolectric para crear una instancia de la actividad y realizar las pruebas.
 *
 * @see RegistroActivity
 * @see RegistroFragment
 * @see Robolectric
 * @see ActivityScenario
 */

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 33)
public class RegistroActivityTest {

    @Test
    public void testRegistroActivityLoadsFragmentAndSetsColors() {
        // Iniciar RegistroActivity
        ActivityScenario<RegistroActivity> scenario = ActivityScenario.launch(RegistroActivity.class);

        scenario.onActivity(activity -> {
            // Verificar que RegistroFragment está cargado
            Fragment fragment = activity.getSupportFragmentManager().findFragmentById(R.id.content_reg);
            assertNotNull("RegistroFragment debería estar cargado", fragment);
            assertTrue("El fragmento debería ser una instancia de RegistroFragment", fragment instanceof RegistroFragment);

            // Verificar que el color de la barra de estado se ha cambiado
            int expectedStatusBarColor = ColorStatusBar.obtenerColorToolbar(activity.findViewById(R.id.toolbar_reg));
            assertEquals("El color de la barra de estado debería coincidir con el color de la toolbar",
                    expectedStatusBarColor, activity.getWindow().getStatusBarColor());

            // Verificar el título de la toolbar
            TextView toolbarTitle = activity.findViewById(R.id.toolbar_title_reg);
            assertNotNull("El título de la toolbar debería estar inicializado", toolbarTitle);
            assertEquals("Registro usuario", toolbarTitle.getText().toString());
        });
    }

    @Test
    public void testBackButtonPressOpensMainActivity() {
        // Iniciar RegistroActivity
        ActivityScenario<RegistroActivity> scenario = ActivityScenario.launch(RegistroActivity.class);

        scenario.onActivity(activity -> {
            // Simular el botón de retroceso
            OnBackPressedDispatcher dispatcher = activity.getOnBackPressedDispatcher();
            dispatcher.onBackPressed();

            // Verificar que MainActivity ha sido lanzada
            Intent expectedIntent = new Intent(activity, MainActivity.class);
            Intent actualIntent = shadowOf(activity).getNextStartedActivity();
            assertEquals("Debería lanzarse MainActivity al presionar el botón de retroceso",
                    expectedIntent.getComponent(), actualIntent.getComponent());
        });
    }

    @Test
    public void testFragmentUIElements() {
        // Iniciar RegistroActivity
        ActivityScenario<RegistroActivity> scenario = ActivityScenario.launch(RegistroActivity.class);

        scenario.onActivity(activity -> {
            // Verificar los campos de texto en RegistroFragment
            TextView nombreText = activity.findViewById(R.id.nombre_reg);
            assertNotNull("El campo de texto para el nombre debería estar inicializado", nombreText);
            assertEquals("Nombre", ((TextInputLayout) activity.findViewById(R.id.textnombre_reg)).getHint());

            TextView nacimientoText = activity.findViewById(R.id.nacimiento_reg);
            assertNotNull("El campo de texto para fecha de nacimiento debería estar inicializado", nacimientoText);

            TextView mailText = activity.findViewById(R.id.mail_reg);
            assertNotNull("El campo de texto para el email debería estar inicializado", mailText);

            TextView passText = activity.findViewById(R.id.pass_reg);
            assertNotNull("El campo de texto para la contraseña debería estar inicializado", passText);

            TextView passRepText = activity.findViewById(R.id.passRep_reg);
            assertNotNull("El campo de texto para repetir la contraseña debería estar inicializado", passRepText);

            // Verificar el botón Aceptar
            MaterialButton aceptarButton = activity.findViewById(R.id.aceptar_reg);
            assertNotNull("El botón Aceptar debería estar inicializado", aceptarButton);
            assertEquals("Aceptar", aceptarButton.getText().toString());

            // Verificar el botón Cancelar
            MaterialButton cancelarButton = activity.findViewById(R.id.cancelar_reg);
            assertNotNull("El botón Cancelar debería estar inicializado", cancelarButton);
            assertEquals("Cancelar", cancelarButton.getText().toString());
        });
    }
}
