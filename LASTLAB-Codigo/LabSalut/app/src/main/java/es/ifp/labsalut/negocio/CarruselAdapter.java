package es.ifp.labsalut.negocio;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import es.ifp.labsalut.R;

public class CarruselAdapter extends RecyclerView.Adapter<CarruselAdapter.CarruselViewHolder> implements OnMapReadyCallback {

    private FragmentManager fragmentManager; // Contexto de la aplicación
    private Context context;
    private ArrayList<Serializable> itemList; // Lista de elementos
    private SparseBooleanArray seleccionados; // Array para elementos seleccionados
    private GoogleMap mMap;
    private int colorBack; // Color de fondo
    private boolean select = false; // Bandera para selección múltiple
    private RecyclerView recyclerView;
    private double latitud;
    private double longitud;
    private CitaMedica citaMedica = null;
    private static final String TAG = "Carrusel";

    public CarruselAdapter(Context context, FragmentManager fragmentManager, ArrayList<Serializable> itemList) {
        this.fragmentManager = fragmentManager;
        this.context = context;
        this.itemList = itemList;
        this.seleccionados = new SparseBooleanArray();
    }

    @NonNull
    @Override
    public CarruselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carrusel, parent, false);
        return new CarruselViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarruselViewHolder holder, int position) {
        // Vincular los datos a la vista
        Object item = itemList.get(position);

        citaMedica = (CitaMedica) item;
        holder.nombre.setText(citaMedica.getNombre());
        holder.descrip.setText(citaMedica.getDescripcion());
        holder.fecha.setText("Fecha: " + citaMedica.getFecha());
        holder.hora.setText("Hora: " + citaMedica.getHora());
        holder.recordatorio.setText("Recordatorio: " + citaMedica.getRecordatorio());

        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.width = (int) (recyclerView.getWidth() * 0.85); // Ocupa el 85% del ancho del RecyclerView
        holder.itemView.setLayoutParams(layoutParams);
        // Configurar el mapa
        holder.map.onCreate(null);
        holder.map.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Obtener la dirección como cadena
        String direccion = citaMedica.getDireccion();

        // Inicializar el objeto Geocoder
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

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
    public void onViewRecycled(@NonNull CarruselViewHolder holder) {
        super.onViewRecycled(holder);
        holder.map.onDestroy();
    }

    @Override
    public int getItemCount() {
        return itemList.size(); // Devolver el tamaño de la lista
    }

    // Devolver los elementos seleccionados
    public SparseBooleanArray devolverSelect() {
        return seleccionados;
    }

    // Reiniciar la selección de elementos
    public void reiniciarSparse() {
        seleccionados.clear();
        select = false;
        notifyDataSetChanged();
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    // Devolver los datos de la lista
    public ArrayList<Serializable> getData() {
        return itemList;
    }

    public static class CarruselViewHolder extends RecyclerView.ViewHolder {
        protected MapView map;
        protected TextView nombre;
        protected TextView descrip;
        protected TextView hora;
        protected TextView fecha;
        protected TextView recordatorio;

        public CarruselViewHolder(@NonNull View itemView) {
            super(itemView);
            map = itemView.findViewById(R.id.map_carrusel);
            nombre = itemView.findViewById(R.id.nombre_carrusel);
            descrip = itemView.findViewById(R.id.descrip_carrusel);
            recordatorio = itemView.findViewById(R.id.record_carrusel);
            fecha = itemView.findViewById(R.id.fecha_carrusel);
            hora = itemView.findViewById(R.id.hora_carrusel);
        }
    }
}
