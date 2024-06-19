package es.ifp.labsalut.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.ChangeBounds;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.transition.platform.MaterialContainerTransform;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

import es.ifp.labsalut.R;
import es.ifp.labsalut.activities.MainActivity;
import es.ifp.labsalut.activities.MenuActivity;
import es.ifp.labsalut.databinding.FragmentHomeBinding;
import es.ifp.labsalut.negocio.CitaMedica;
import es.ifp.labsalut.negocio.Medicamento;
import es.ifp.labsalut.negocio.ModListAdapter;
import es.ifp.labsalut.negocio.Usuario;
import es.ifp.labsalut.seguridad.FingerprintHandler;

public class HomeFragment extends Fragment implements ModListAdapter.OnItemClickListener {

    // Argumentos para la instancia del fragmento
    private static final String ARG_PARAM1 = "EMAIL";
    private static final String ARG_PARAM2 = "PASS";
    private static final String ARG_USER = "USUARIO";
    // Vinculación de la vista del fragmento
    private FragmentHomeBinding binding;
    // Variables de instancia
    private String email;
    private String pass;
    private Usuario user = null;
    private ModListAdapter adapterMed = null;
    private ModListAdapter adapterCita = null;
    private SparseBooleanArray arrayBoolean = null;

    // Constructor por defecto del fragmento
    public HomeFragment() {
        // Constructor público vacío necesario
    }

    // Método estático para crear una nueva instancia del fragmento con parámetros de correo electrónico y contraseña
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    // Método estático para crear una nueva instancia del fragmento con un objeto Usuario
    public static HomeFragment newInstance(Usuario user) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    // Método llamado cuando el fragmento está siendo creado
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Comprueba si hay argumentos pasados al fragmento y los asigna a las variables de instancia
        if (getArguments() != null) {
            email = getArguments().getString(ARG_PARAM1);
            pass = getArguments().getString(ARG_PARAM2);
            user = (Usuario) getArguments().getSerializable(ARG_USER);
        }
    }

    // Método llamado para crear y devolver la vista asociada con el fragmento
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla el diseño de la vista del fragmento
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    // Método llamado después de que la vista haya sido creada
    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);
        Context context = root.getContext();
        // Crea listas de datos de medicamentos y citas médicas
        ArrayList<Serializable> dataMed = new ArrayList<>();
        // Agrega datos de ejemplo a la lista de medicamentos
        dataMed.add(new Medicamento("Paracetamol", "500mg", "Toma cada 8 horas", "5 min antes"));
        dataMed.add(new Medicamento("Ibuprofeno", "1000mg", "Toma cada 8 horas", "5 min antes"));
        dataMed.add(new Medicamento("Desketoprofeno", "200mg", "Toma cada 8 horas", "5 min antes"));
        dataMed.add(new Medicamento("Aerius", "1000mg", "Toma cada 8 horas", "5 min antes"));
        dataMed.add(new Medicamento("Ibuprofeno", "1000mg", "Toma cada 8 horas", "5 min antes"));
        dataMed.add(new Medicamento("Desketoprofeno", "200mg", "Toma cada 8 horas", "5 min antes"));
        dataMed.add(new Medicamento("Aerius", "1000mg", "Toma cada 8 horas", "5 min antes"));


        ArrayList<Serializable> dataCita = new ArrayList<>();

        // Agrega datos de ejemplo a la lista de citas médicas
        dataCita.add(new CitaMedica("Neurologo", "12/05/2024", "08:00", "Ir en ayunas", "24 horas antes"));
        dataCita.add(new CitaMedica("Endocrino", "27/05/2024", "09:35", "Ir en ayunas", "24 horas antes"));
        dataCita.add(new CitaMedica("Gastroscopia", "12/08/2024", "12:30", "Ir en ayunas", "24 horas antes"));
        dataCita.add(new CitaMedica("Ambulatorio", "27/05/2024", "09:35", "Ir en ayunas", "24 horas antes"));
        dataCita.add(new CitaMedica("Endocrino", "27/05/2024", "09:35", "Ir en ayunas", "24 horas antes"));
        dataCita.add(new CitaMedica("Gastroscopia", "12/08/2024", "12:30", "Ir en ayunas", "24 horas antes"));
        dataCita.add(new CitaMedica("Ambulatorio", "27/05/2024", "09:35", "Ir en ayunas", "24 horas antes"));

        // Crea adaptadores para las listas de medicamentos y citas médicas
        adapterMed = new ModListAdapter(context, dataMed);
        adapterCita = new ModListAdapter(context, dataCita);

        // Inicializa las listas en la interfaz de usuario
        iniciarLista(binding.recyclerViewMediHome, adapterMed);
        iniciarLista(binding.recyclerViewCitaHome, adapterCita);
        // Ajusta el tamaño de las listas en la interfaz de usuario
        ajustarSizeList();

        // Maneja los clics en los botones para eliminar elementos de las listas de medicamentos y citas médicas
        binding.deleteItemListMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtiene los elementos seleccionados y los elimina de la lista de medicamentos
                arrayBoolean = adapterMed.devolverSelect();
                if (arrayBoolean.size() != 0) {
                    for (int i = arrayBoolean.size() - 1; i >= 0; i--) {
                        adapterMed.removeItem(arrayBoolean.keyAt(i));
                    }
                }
                adapterMed.reiniciarSparse();
                binding.deleteItemListMed.setVisibility(View.GONE);
            }
        });

        // Maneja los clics en los botones para eliminar elementos de las listas de citas médicas
        binding.deleteItemListCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtiene los elementos seleccionados y los elimina de la lista de citas médicas
                arrayBoolean = adapterCita.devolverSelect();
                if (arrayBoolean.size() != 0) {
                    for (int i = arrayBoolean.size() - 1; i >= 0; i--) {
                        adapterCita.removeItem(arrayBoolean.keyAt(i));
                    }
                }
                adapterCita.reiniciarSparse();
                binding.deleteItemListCita.setVisibility(View.GONE);
            }
        });

        // Establece un listener para detectar cambios en la selección de elementos en la lista de medicamentos
        adapterMed.setOnDataChangeListener(new ModListAdapter.OnDataChangeListener() {
            @Override
            public void onDataChanged(boolean select) {
                // Muestra u oculta el botón de eliminación según si hay elementos seleccionados
                if (select) {
                    binding.deleteItemListMed.setVisibility(View.VISIBLE);
                } else {
                    binding.deleteItemListMed.setVisibility(View.GONE);
                }
            }
        });

        // Establece un listener para detectar cambios en la selección de elementos en la lista de citas médicas
        adapterCita.setOnDataChangeListener(new ModListAdapter.OnDataChangeListener() {
            @Override
            public void onDataChanged(boolean select) {
                // Muestra u oculta el botón de eliminación según si hay elementos seleccionados
                if (select) {
                    binding.deleteItemListCita.setVisibility(View.VISIBLE);
                } else {
                    binding.deleteItemListCita.setVisibility(View.GONE);
                }
            }
        });

        // Maneja el comportamiento al presionar el botón de retroceso del dispositivo
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (binding.deleteItemListCita.getVisibility() == View.VISIBLE) {
                    // Si hay elementos seleccionados en la lista de citas médicas, cancela la selección
                    adapterCita.reiniciarSparse();
                    binding.deleteItemListCita.setVisibility(View.GONE);
                } else if (binding.deleteItemListMed.getVisibility() == View.VISIBLE) {
                    // Si hay elementos seleccionados en la lista demedicamentos, cancela la selección
                    adapterMed.reiniciarSparse();
                    binding.deleteItemListMed.setVisibility(View.GONE);
                } else {
                    // Si no hay elementos seleccionados en ninguna lista, finaliza la actividad actual
                    FragmentActivity activity = getActivity();
                    if (activity != null) {
                        activity.finish();
                    }
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), onBackPressedCallback);
    }


    // Activa la funcionalidad de deslizamiento en la lista y maneja el deslizamiento de elementos
    private void activarDeslizar(RecyclerView recyclerView, ModListAdapter mAdapter) {
        DeslizarParaAccion deslizarParaAccion = new DeslizarParaAccion(getContext(), (DeslizarParaAccion.ItemTouchHelperContract) mAdapter) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Obtiene la posición del elemento deslizado y el elemento correspondiente
                final int position = viewHolder.getAdapterPosition();
                final Serializable item = mAdapter.getData().get(position);

                // Elimina el elemento deslizado de la lista
                mAdapter.removeItem(position);

                // Muestra una Snackbar para deshacer la acción de eliminación
                Snackbar snackbar = Snackbar
                        .make(binding.fragmentHome, "El elemento ha sido borrado", Snackbar.LENGTH_LONG);
                snackbar.setAction("Deshacer", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Restaura el elemento eliminado a su posición original
                        mAdapter.restoreItem(item, position);
                        recyclerView.scrollToPosition(position);
                    }
                });

                // Configura el color del texto de la acción de la Snackbar
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
        };

        // Asocia la funcionalidad de deslizamiento con el RecyclerView
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(deslizarParaAccion);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    // Inicializa una lista en la interfaz de usuario
    private void iniciarLista(RecyclerView recyclerView, ModListAdapter adapter) {
        Context context = getContext();
        adapter.setOnItemClickListener(this);
        adapter.setRecyclerView(recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        // Activa la funcionalidad de deslizamiento en la lista
        activarDeslizar(recyclerView, adapter);
        recyclerView.setAdapter(adapter);
    }

    // Ajusta el tamaño de las listas en la interfaz de usuario
    private void ajustarSizeList() {
        int itemHeightInDp = 77;
        int numberOfVisibleItems = 4;
        float scale = getResources().getDisplayMetrics().density;
        int itemHeightInPx = (int) (itemHeightInDp * scale);
        int totalHeight = itemHeightInPx * numberOfVisibleItems;
        // Ajusta el tamaño de la lista de medicamentos
        ViewGroup.LayoutParams params = binding.recyclerViewMediHome.getLayoutParams();
        params.height = totalHeight;
        binding.recyclerViewMediHome.setLayoutParams(params);
        // Ajusta el tamaño de la lista de citas médicas
        ViewGroup.LayoutParams params2 = binding.recyclerViewCitaHome.getLayoutParams();
        params2.height = totalHeight;
        binding.recyclerViewCitaHome.setLayoutParams(params2);
    }

    // Método llamado cuando la vista del fragmento está siendo destruida
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onItemClick(View view, int position) {

        if (binding.deleteItemListCita.getVisibility() == View.VISIBLE) {

            adapterCita.performClickCheckBox(position);
        } else {
            CitaMedica cita = user.getCitaMedica(position);

            CardDialogFragment dialog = CardDialogFragment.newInstance(cita);
            dialog.show(requireActivity().getSupportFragmentManager(), "dialog");

        }/*
        if (binding.deleteItemListMed.getVisibility() == View.VISIBLE) {
            adapterMed.performClickCheckBox(position);
        } else {
            CitaMedica cita = user.getCitaMedica(position);

            CardDialogFragment dialog = CardDialogFragment.newInstance(cita);
            dialog.show(requireActivity().getSupportFragmentManager(), "dialog");
        }
        */
    }


}