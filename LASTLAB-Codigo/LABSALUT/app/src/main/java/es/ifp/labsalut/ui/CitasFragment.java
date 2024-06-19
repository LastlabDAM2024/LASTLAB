package es.ifp.labsalut.ui;

import static com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_KEYBOARD;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.time.Instant;
import java.time.Month;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import es.ifp.labsalut.R;
import es.ifp.labsalut.databinding.FragmentCitasBinding;
import es.ifp.labsalut.db.BaseDatos;
import es.ifp.labsalut.negocio.CitaMedica;
import es.ifp.labsalut.negocio.Usuario;

public class CitasFragment extends Fragment {

    // Constantes para los argumentos del fragmento
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_USER = "USUARIO";
    private static final String TAG = "CitasFragment";
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;

    // Binding para el fragmento
    private FragmentCitasBinding binding;
    // Variables para almacenar los parámetros
    // Usuario actual
    private Usuario user = null;
    private BaseDatos db;
    protected AutocompleteSupportFragment autocompleteDireccion;

    // Constructor vacío requerido
    public CitasFragment() {
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
        Context context = root.getContext();
        db = new BaseDatos(context);
        autocompleteDireccion = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_direccion);

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
                CitaMedica cita = new CitaMedica("Neurologo", "12/05/2024", "08:00", "Ir en ayunas", "24 horas antes");



                /*
                cita.setNombre(binding.nombreCita.getText().toString());
                cita.setDescripcion(binding.descripCita.getText().toString());
                cita.setRecordatorio(binding.recordCita.getText().toString());
                cita.setFecha(binding.fechaCita.getText().toString());
                cita.setHora(binding.horaCita.getText().toString());

                 */

                // FALTA CIFRAR DATOS DE LAS CITAS
                cita.setIdCita(db.addCita(cita));
                user.setCitaMedica(cita);
                db.addUserCita(user, cita);
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_menu, HomeFragment.newInstance(user))
                        .addToBackStack(null)
                        .commit();
            }
        });

        new CitaMedica("Neurologo", "12/05/2024", "08:00", "Ir en ayunas", "24 horas antes");
        new CitaMedica("Endocrino", "27/05/2024", "09:35", "Ir en ayunas", "24 horas antes");
        new CitaMedica("Gastroscopia", "12/08/2024", "12:30", "Ir en ayunas", "24 horas antes");
        new CitaMedica("Ambulatorio", "27/05/2024", "09:35", "Ir en ayunas", "24 horas antes");
        new CitaMedica("Endocrino", "27/05/2024", "09:35", "Ir en ayunas", "24 horas antes");
        new CitaMedica("Gastroscopia", "12/08/2024", "12:30", "Ir en ayunas", "24 horas antes");
        new CitaMedica("Ambulatorio", "27/05/2024", "09:35", "Ir en ayunas", "24 horas antes");

        // Configuración del autocompletado de lugares en el campo de dirección
        binding.textDireccionCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //Iniciar el fragment de autocompletado
                toggleAutocompleteVisibility();
                startAutocomplete();
            }
        });

        if (autocompleteDireccion != null) {
            autocompleteDireccion.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS));
            autocompleteDireccion.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    // Manejar el lugar seleccionado
                    binding.direccionCita.setText(place.getAddress());
                }

                @Override
                public void onError(@NonNull Status status) {
                    // Manejar el error
                    Log.i(TAG, "Ocurrió un error: " + status);
                }
            });
        }

    }


    // Manejar el resultado de la actividad de autocompletado de lugares
    private ActivityResultLauncher<Intent> autocompleteLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Manejar el resultado de la actividad aquí
                        Intent data = result.getData();
                        if (data != null) {
                            Place place = Autocomplete.getPlaceFromIntent(data);
                            binding.direccionCita.setText(place.getAddress());
                        }
                    } else if (result.getResultCode() == AutocompleteActivity.RESULT_ERROR) {
                        // Manejar errores
                        Intent data = result.getData();
                        if (data != null) {
                            Status status = Autocomplete.getStatusFromIntent(data);
                            Log.i(TAG, status.getStatusMessage());
                        }
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        // El usuario canceló la operación.
                    }
                }
            }
    );


    // Método para iniciar Autocomplete
    private void startAutocomplete() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .build(requireActivity());
        autocompleteLauncher.launch(intent);
    }


    // Método para cambiar la visibilidad de AutocompleteSupportFragment
    private void toggleAutocompleteVisibility() {
        if (autocompleteDireccion != null && autocompleteDireccion.getView() != null) {
            int visibility = autocompleteDireccion.getView().getVisibility();
            if (visibility == View.VISIBLE) {
                autocompleteDireccion.getView().setVisibility(View.GONE);
            } else {
                autocompleteDireccion.getView().setVisibility(View.VISIBLE);
            }
        }
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
