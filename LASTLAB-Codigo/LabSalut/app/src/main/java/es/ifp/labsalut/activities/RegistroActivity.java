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
