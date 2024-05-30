package es.ifp.labsalut.ui;

import static com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_KEYBOARD;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.time.Instant;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import es.ifp.labsalut.R;
import es.ifp.labsalut.activities.MenuActivity;
import es.ifp.labsalut.databinding.FragmentCitasBinding;
import es.ifp.labsalut.negocio.CitaMedica;
import es.ifp.labsalut.negocio.Usuario;
public class CitasFragment extends Fragment {

    // Constantes para los argumentos del fragmento
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_USER = "USUARIO";
    // Binding para el fragmento
    private FragmentCitasBinding binding;
    // Variables para almacenar los parámetros
    private String mParam1;
    private String mParam2;
    // Usuario actual
    private Usuario user = null;
    private Intent pasarPantalla;

    // Constructor vacío requerido
    public CitasFragment() {
    }

    // Método estático para crear una nueva instancia del fragmento con dos parámetros
    public static CitasFragment newInstance(String param1, String param2) {
        CitasFragment fragment = new CitasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    // Método estático para crear una nueva instancia del fragmento con un usuario
    public static CitasFragment newInstance(Usuario user) {
        CitasFragment fragment = new CitasFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    // Método llamado cuando el fragmento es creado
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            user = (Usuario) getArguments().getSerializable(ARG_USER);
        }
    }

    // Método llamado para crear la vista del fragmento
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCitasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    // Método llamado después de que la vista ha sido creada
    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);
        Context context = requireContext();
        // Configuración del botón de fecha
        binding.textFechaCita.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDatePicker();
            }
        });

        // Configuración del botón de hora
        binding.textHoraCita.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarTimePicker(context);
            }
        });

        // Configuración del checkbox de recordatorio
        binding.checkRecordCita.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.textRecordCita.setEnabled(true);
                } else {
                    binding.textRecordCita.setEnabled(false);
                    binding.recordCita.setText("");
                }
            }
        });

        binding.guardarCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CitaMedica cita = new CitaMedica();
                cita.setNombre(binding.nombreCita.getText().toString());
                cita.setDescripcion(binding.descripCita.getText().toString());
                cita.setRecordatorio(binding.recordCita.getText().toString());
                cita.setFecha(binding.fechaCita.getText().toString());
                cita.setHora(binding.horaCita.getText().toString());
                user.setCitaMedica(cita);
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_menu, HomeFragment.newInstance(user))
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    // Método llamado cuando la vista del fragmento es destruida
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    // Método para mostrar el selector de fecha
    private void mostrarDatePicker() {
        Instant instant = null;
        long utcTimeInMillis = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            instant = Instant.from(Instant.now().plus(1, ChronoUnit.DAYS));
            utcTimeInMillis = instant.toEpochMilli();
        }
        CalendarConstraints constraintsBuilder = new CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.from(utcTimeInMillis)).build();
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

                Locale locale = new Locale("es", "ES");
                Month mMonth = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mMonth = Month.of(month);
                    String monthName = mMonth.getDisplayName(TextStyle.FULL, locale);
                    String fechaCita = "";
                    if (day < 10) {
                        fechaCita = "0" + day + "  /  " + monthName + "  /  " + year;
                    } else {
                        fechaCita = day + "  /  " + monthName + "  /  " + year;
                    }
                    binding.fechaCita.setText(fechaCita);
                }

            }
        });
    }

    // Método para mostrar el selector de hora
    private void mostrarTimePicker(Context context) {

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        int horas = calendar.get(Calendar.HOUR_OF_DAY);
        int minutos = calendar.get(Calendar.MINUTE);

        MaterialTimePicker picker = new MaterialTimePicker.Builder()
                .setInputMode(INPUT_MODE_KEYBOARD)
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(horas)
                .setMinute(minutos)
                .setTitleText("Selecciona una hora")
                .build();

        picker.show(requireActivity().getSupportFragmentManager(), "tag");
        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String minutoFormateado = "";
                String horaFormateada = "";
                if (picker.getMinute() < 10) {
                    minutoFormateado = "0" + picker.getMinute();
                } else {
                    minutoFormateado = String.valueOf(picker.getMinute());
                }
                if (picker.getHour() < 10) {
                    horaFormateada = "0" + picker.getHour();
                } else {
                    horaFormateada = String.valueOf(picker.getHour());
                }
                binding.horaCita.setText(horaFormateada + " : " + minutoFormateado);
            }
        });
    }
}
