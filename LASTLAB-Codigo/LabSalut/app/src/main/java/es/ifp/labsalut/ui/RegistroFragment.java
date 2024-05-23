package es.ifp.labsalut.ui;

import static es.ifp.labsalut.ui.SettingsFragment.MY_PREFS_HUELLA;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.Month;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import javax.crypto.SecretKey;

import es.ifp.labsalut.activities.MainActivity;
import es.ifp.labsalut.databinding.FragmentRegistroBinding;
import es.ifp.labsalut.db.BaseDatos;
import es.ifp.labsalut.negocio.Usuario;
import es.ifp.labsalut.seguridad.CifradoAES;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistroFragment extends Fragment {

    private FragmentRegistroBinding binding;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private String fechaNacimiento;
    private CifradoAES aes;
    private BaseDatos db;
    private int mesNacimiento;
    private Intent pasarPantalla;
    private SharedPreferences prefs_huella = null;


    public RegistroFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistroFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistroFragment newInstance(String param1, String param2) {
        RegistroFragment fragment = new RegistroFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegistroBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);

        Context context = root.getContext();
        Activity activity = getActivity();

        db = new BaseDatos(context);
        binding.textoNaciReg.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDatePicker();
            }
        });

        binding.aceptarReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.nombreReg.getText().toString().isEmpty() || binding.mailReg.getText().toString().isEmpty()
                        || fechaNacimiento.equals("dd  /  mes  /  aaaa") || binding.passReg.getText().toString().isEmpty()
                        || binding.passRepReg.getText().toString().isEmpty()) {
                    Snackbar.make(binding.fragmentRegistro, "Todos los campos son obligatorios", Snackbar.LENGTH_LONG).show();
                } else {
                    try {
                        // Crear MasterKey para EncryptedSharedPreferences
                        MasterKey masterKey = new MasterKey.Builder(activity)
                                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                                .build();
                        // Crear EncryptedSharedPreferences para MY_PREFS_HUELLA
                        prefs_huella = EncryptedSharedPreferences.create(
                                activity,
                                MY_PREFS_HUELLA,
                                masterKey,
                                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                        );
                    } catch (GeneralSecurityException | IOException e) {
                        e.printStackTrace();
                    }

                    SharedPreferences.Editor editor_huella = prefs_huella.edit();

                    Calendar calendar = Calendar.getInstance();
                    int anyo = calendar.get(Calendar.YEAR);
                    int mes = calendar.get(Calendar.MONTH) + 1;
                    int dia = calendar.get(Calendar.DAY_OF_MONTH);

                    String[] fechaNaci = fechaNacimiento.split(" {2}/ {2}");
                    if (Integer.parseInt(fechaNaci[2]) <= anyo) {
                        if (mesNacimiento <= mes) {
                            if (Integer.parseInt(fechaNaci[0]) <= dia) {
                                if (!esCorreoValido(binding.mailReg.getText().toString())) {
                                    Snackbar.make(binding.fragmentRegistro, "El formato de correo electronico no es válido", Snackbar.LENGTH_LONG).show();
                                } else {
                                    Usuario usuario = db.getUser(binding.mailReg.getText().toString());
                                    if (!usuario.getEmail().isEmpty()) {
                                        Snackbar.make(binding.fragmentRegistro, "El correo electronico ya esta registrado", Snackbar.LENGTH_LONG).show();
                                    } else {
                                        if (!binding.passReg.getText().toString().equals(binding.passRepReg.getText().toString())) {
                                            Snackbar.make(binding.fragmentRegistro, "Las contraseñas no coinciden", Snackbar.LENGTH_LONG).show();
                                        } else {
                                            aes = new CifradoAES();
                                            String semilla = binding.mailReg.getText().toString() + binding.passReg.getText().toString();
                                            SecretKey secretKey = aes.generarSecretKey(semilla);
                                            String encryptNombre = "";
                                            String encryptEmail = "";
                                            String encryptPass = "";
                                            String encryptFecha = "";

                                            try {
                                                encryptNombre = aes.encrypt(binding.nombreReg.getText().toString().getBytes(), secretKey);
                                                encryptEmail = aes.encrypt(binding.mailReg.getText().toString().getBytes(), secretKey);
                                                encryptPass = aes.encrypt(binding.passReg.getText().toString().getBytes(), secretKey);
                                                encryptFecha = aes.encrypt(fechaNacimiento.getBytes(), secretKey);
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                            Usuario user = new Usuario(encryptNombre, encryptFecha, encryptEmail, encryptPass);
                                            user.setIdUsuario(db.addUser(user));
                                            editor_huella.putString("PRIMERAVEZ" + binding.nombreReg.getText().toString(), "SI");
                                            editor_huella.apply();
                                            pasarPantalla = new Intent(activity, MainActivity.class);
                                            activity.startActivity(pasarPantalla);
                                            activity.finish();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });

        binding.cancelarReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasarPantalla = new Intent(activity, MainActivity.class);
                activity.finish();
                activity.startActivity(pasarPantalla);
            }
        });

    }

    private boolean esCorreoValido(String email) {
        String emailREGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailREGEX);
        if (email == null) {
            return false;
        }
        return pattern.matcher(email).matches();
    }

    private void mostrarDatePicker() {

        Instant instant = null;
        long utcTimeInMillis = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            instant = Instant.from(Instant.now().minus(1, ChronoUnit.DAYS));
            utcTimeInMillis = instant.toEpochMilli();
        }

        CalendarConstraints constraintsBuilder = new CalendarConstraints.Builder()
                .setValidator(DateValidatorPointBackward.before(utcTimeInMillis)).build();
        MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Selecciona una fecha")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(constraintsBuilder)
                .build();
        picker.show(requireActivity().getSupportFragmentManager(), "tag");
        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long aLong) {
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.setTimeInMillis(aLong);

                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH) + 1; // Enero es 0
                int year = calendar.get(Calendar.YEAR);

                mesNacimiento = month;
                Locale locale = new Locale("es", "ES");
                Month mMonth = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    mMonth = Month.of(month);
                    String monthName = mMonth.getDisplayName(TextStyle.FULL, locale);
                    if (day < 10) {
                        fechaNacimiento = "0" + day + "  /  " + monthName + "  /  " + year;
                    } else {
                        fechaNacimiento = day + "  /  " + monthName + "  /  " + year;
                    }
                    binding.nacimientoReg.setText(fechaNacimiento);
                }

            }
        });
    }

}
