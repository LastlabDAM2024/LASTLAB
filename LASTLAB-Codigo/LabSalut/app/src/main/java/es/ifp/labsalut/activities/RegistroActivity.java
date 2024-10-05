package es.ifp.labsalut.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import es.ifp.labsalut.R;
import es.ifp.labsalut.databinding.ActivityRegistroBinding;
import es.ifp.labsalut.ui.ColorStatusBar;
import es.ifp.labsalut.ui.RegistroFragment;

/**
 * La clase RegistroActivity extiende AppCompatActivity y se encarga de gestionar el registro de un nuevo
 * usuario en la aplicación.
 *
 * Esta actividad proporciona una interfaz para que los usuarios puedan ingresar sus datos de registro y
 * manejar la transición entre la actividad de registro y la actividad principal de la aplicación.
 *
 * 1. **View Binding**:
 *    - Utiliza la clase generada `ActivityRegistroBinding` para acceder a los elementos del diseño
 *      de manera eficiente y evitar el uso de `findViewById()`.
 *
 * 2. **Configuración de la Barra de Herramientas**:
 *    - Configura la barra de herramientas al inflar el layout, estableciendo su tema y título.
 *
 * 3. **Carga del Fragmento de Registro**:
 *    - Reemplaza el contenido del contenedor de fragmentos (`R.id.content_reg`) con una instancia del
 *      `RegistroFragment`, que se encarga de manejar la lógica específica para el registro del usuario.
 *
 * 4. **Configuración del Título y Color de la Barra de Estado**:
 *    - Establece el título de la barra de herramientas como "Registro usuario" y cambia el color de la
 *      barra de estado y del texto del título de la barra de herramientas para mejorar la experiencia
 *      visual del usuario.
 *
 * 5. **Manejo del Botón de Retroceso**:
 *    - Configura un callback para el botón de retroceso que permite al usuario regresar a la
 *      `MainActivity` (pantalla principal) al finalizar la actividad de registro.
 *    - Utiliza un Intent para iniciar la `MainActivity` y finaliza la actividad de registro.
 *
 * 6. **Método para Cambiar el Color de la Barra de Estado**:
 *    - Implementa el método `cambiarColorStatusBar` para modificar el color de la barra de estado en
 *      función de la versión de Android que esté utilizando el dispositivo.
 *
 * `RegistroActivity`, entonces, lo que haría es proporcionar una interfaz para el registro de nuevos usuarios, facilitando
 * la navegación a la pantalla principal y personalizando la apariencia de la aplicación.
 */

public class RegistroActivity extends AppCompatActivity {

    // Declaración de variables
    private ActivityRegistroBinding binding;
    private Intent pasarPantalla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflar el layout usando View Binding
        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configurar la barra de herramientas
        binding.appBarReg.toolbarReg.setPopupTheme(com.google.android.material.R.style.Widget_Material3_Light_ActionBar_Solid);
        binding.appBarReg.toolbarReg.setTitle("");

        // Reemplazar el contenido del fragmento con RegistroFragment
        getSupportFragmentManager().beginTransaction().replace(R.id.content_reg, new RegistroFragment()).commit();

        // Configurar el título de la barra de herramientas
        binding.appBarReg.toolbarTitleReg.setText("Registro usuario");

        // Cambiar el color de la barra de estado y el texto del título de la barra de herramientas
        cambiarColorStatusBar(this, ColorStatusBar.obtenerColorToolbar(binding.appBarReg.toolbarReg));
        ColorStatusBar.colorDinamicStatusBar(this, ColorStatusBar.obtenerColorToolbar(binding.appBarReg.toolbarReg));
        binding.appBarReg.toolbarTitleReg.setTextColor(ColorStatusBar.obtenerColorBackground(this));

        // Configurar el callback para el botón de retroceso
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Intent para pasar a la pantalla principal
                pasarPantalla = new Intent(RegistroActivity.this, MainActivity.class);
                finish();  // Finalizar la actividad actual
                startActivity(pasarPantalla);  // Iniciar la actividad principal
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
    }

    // Método para cambiar el color de la barra de estado
    public void cambiarColorStatusBar(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            if (window != null) {
                window.setStatusBarColor(color);  // Establecer el color de la barra de estado
            }
        }
    }
}
