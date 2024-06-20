package es.ifp.labsalut.ui;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.Gravity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import es.ifp.labsalut.R;
import es.ifp.labsalut.databinding.FragmentCardDialogBinding;
import es.ifp.labsalut.negocio.CitaMedica;

public class CardDialogFragment extends DialogFragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private static final String ARG_CITA = "CITA";
    private static final int ANCHO_DIALOG = 750;
    private FragmentCardDialogBinding binding;
    private CitaMedica cita = null;
    private GoogleMap mMap;
    private static final String TAG = "CardDialogFragment";
    private double latitud;
    private double longitud;

    public CardDialogFragment() {
    }

    public static CardDialogFragment newInstance(CitaMedica cita) {
        CardDialogFragment fragment = new CardDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CITA, cita);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Recupera los argumentos pasados al fragmento
        if (getArguments() != null) {
            cita = (CitaMedica) getArguments().getSerializable(ARG_CITA);
        }

        // Inflar el layout del fragmento
        binding = FragmentCardDialogBinding.inflate(getLayoutInflater());

        // Configurar otros elementos de UI con datos de la cita
        binding.fotoCard.setImageResource(R.drawable.ic_action_fingerprint);
        binding.fechaCard.setText("Fecha: " + cita.getFecha());
        binding.horaCard.setText("Hora: " + cita.getHora());
        binding.nombreCard.setText(cita.getNombre());
        binding.descripCard.setText(cita.getDescripcion());
        binding.recordCard.setText("Recordatorio: " + cita.getRecordatorio());

        // Configurar el diálogo
        Dialog dialog = new Dialog(requireActivity());
        dialog.setContentView(binding.getRoot());
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setLayout(ANCHO_DIALOG, WRAP_CONTENT);
        dialog.getWindow().setWindowAnimations(R.style.DialogExpandFromTouchAnimation);

        // Configurar el fragmento de mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_dialog);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Log.e(TAG, "Error al obtener el SupportMapFragment.");
        }

        return dialog;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);

        // Obtener la dirección como cadena
        String direccion = cita.getDireccion();

        // Inicializar el objeto Geocoder
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());

        try {
            // Obtener la lista de direcciones a partir de la cadena
            List<Address> addresses = geocoder.getFromLocationName(direccion, 1);

            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                latitud = address.getLatitude();
                longitud = address.getLongitude();

                // Crear la posición del marcador
                LatLng markerPosition = new LatLng(latitud, longitud);

                // Agregar el marcador al mapa
                mMap.addMarker(new MarkerOptions().position(markerPosition).title(direccion));

                // Mover la cámara a la posición del marcador con un nivel de zoom
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, 12f));
            } else {
                // Manejar el caso donde no se encontraron direcciones válidas
                Log.e(TAG, "No se encontraron direcciones para: " + direccion);
            }
        } catch (IOException e) {
            // Manejar errores de geocodificación
            e.printStackTrace();
            Log.e(TAG, "Error al geocodificar la dirección: " + e.getMessage());
        }
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {

        // Abre Google Maps con la ubicación de la cita y permite al usuario elegir el modo de transporte
        String label = cita.getDireccion();
        Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=" + latitud + "," + longitud + "&travelmode=driving");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps"); // Establece el paquete de Google Maps

        // Verifica si Google Maps está instalado en el dispositivo
        if (mapIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Log.e(TAG, "Google Maps no está instalado en tu dispositivo");
            // Aquí podrías mostrar un mensaje al usuario indicando que necesita instalar Google Maps
        }
    }
}