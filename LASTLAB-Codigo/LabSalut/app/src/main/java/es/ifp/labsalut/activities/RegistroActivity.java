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
    private ActivityRegistroBinding binding;
    private Intent pasarPantalla;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.appBarReg.toolbarReg.setPopupTheme(com.google.android.material.R.style.Widget_Material3_Light_ActionBar_Solid);
        binding.appBarReg.toolbarReg.setTitle("");
        getSupportFragmentManager().beginTransaction().replace(R.id.content_reg, new RegistroFragment()).commit();
        binding.appBarReg.toolbarTitleReg.setText("Registro usuario");
        cambiarColorStatusBar(this,ColorStatusBar.obtenerColorToolbar(binding.appBarReg.toolbarReg));
        ColorStatusBar.colorDinamicStatusBar(this,ColorStatusBar.obtenerColorToolbar(binding.appBarReg.toolbarReg));
        binding.appBarReg.toolbarTitleReg.setTextColor(ColorStatusBar.obtenerColorBackground(this));

        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                pasarPantalla = new Intent(RegistroActivity.this, MainActivity.class);
                finish();
                startActivity(pasarPantalla);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
    }
    public void cambiarColorStatusBar(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            if (window != null) {
                // Establecer el color de la barra de estado
                window.setStatusBarColor(color);
            }
        }
    }
}