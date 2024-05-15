package es.ifp.labsalut.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Locale;

import es.ifp.labsalut.R;

public class CitasFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    protected TextView fecha;
    protected TextView hora;
    protected EditText nombre;
    protected EditText descrip;
    protected Button calendarioBoton;
    protected Button horaBoton;


    public CitasFragment() {
        // Required empty public constructor
    }

    public static CitasFragment newInstance(String param1, String param2) {
        CitasFragment fragment = new CitasFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_citas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);
        fecha = (TextView) root.findViewById(R.id.fecha_cita);
        hora = (TextView) root.findViewById(R.id.hora_cita);
        nombre = (EditText) root.findViewById(R.id.nombreEditText_Cita);
        descrip = (EditText) root.findViewById(R.id.descripEditText_Cita);
        calendarioBoton = (Button) root.findViewById(R.id.calendarioBoton_citas);
        horaBoton = (Button) root.findViewById(R.id.horarioBoton_citas);

        calendarioBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int anyo = calendar.get(Calendar.YEAR);
                int mes = calendar.get(Calendar.MONTH);
                int dia = calendar.get(Calendar.DAY_OF_MONTH);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    DatePickerDialog datePicker = new DatePickerDialog(root.getContext(), R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            Locale locale = new Locale("es", "ES");
                            Month mMonth = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                mMonth = Month.of(month + 1);
                                String monthName = mMonth.getDisplayName(TextStyle.FULL, locale);
                                String fechaText = dayOfMonth + " / " + monthName + " / " + year;
                                fecha.setText(String.valueOf(fechaText));
                            }
                        }
                    }, anyo, mes, dia);
                    datePicker.show();
                }

            }
        });

        horaBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int horas = calendar.get(Calendar.HOUR_OF_DAY);
                int minutos = calendar.get(Calendar.MINUTE);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    TimePickerDialog timePicker = new TimePickerDialog(root.getContext(), R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            hora.setText(hourOfDay + " : " + minute);
                        }
                    }, horas, minutos, true);
                    timePicker.show();
                }

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}