package es.ifp.labsalut.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import es.ifp.labsalut.R;
import es.ifp.labsalut.databinding.ActivityRegistroBinding;
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

}