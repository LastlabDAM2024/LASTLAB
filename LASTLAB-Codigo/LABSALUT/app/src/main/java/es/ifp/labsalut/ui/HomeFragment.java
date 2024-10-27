package es.ifp.labsalut.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.util.ArrayList;

import javax.crypto.SecretKey;

import es.ifp.labsalut.R;
import es.ifp.labsalut.databinding.AppBarMenuBinding;
import es.ifp.labsalut.databinding.FragmentHomeBinding;
import es.ifp.labsalut.db.BaseDatos;
import es.ifp.labsalut.negocio.CitaMedica;
import es.ifp.labsalut.negocio.CitasListAdapter;
import es.ifp.labsalut.negocio.Medicamento;
import es.ifp.labsalut.negocio.MedListAdapter;
import es.ifp.labsalut.negocio.Usuario;
import es.ifp.labsalut.seguridad.CifradoAES;

public class HomeFragment extends Fragment implements MedListAdapter.OnItemMedClickListener, CitasListAdapter.OnItemCitaClickListener {

    // Argumentos para la instancia del fragmento
    private static final String ARG_PARAM1 = "EMAIL";
    private static final String ARG_PARAM2 = "PASS";
    private static final String ARG_USER = "USUARIO";
    // Vinculación de la vista del fragmento
    private FragmentHomeBinding binding;
    // Variables de instancia
    private String email;
    private String pass;
    private BaseDatos db;
    private Usuario user = null;
    private MedListAdapter adapterMed = null;
    private CitasListAdapter adapterCita = null;
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
        db = new BaseDatos(requireActivity());

        ArrayList<Serializable> dataMed = new ArrayList<>();
        ArrayList<Serializable> dataCita= new ArrayList<>();
        // Crea listas de datos de medicamentos y citas médicasç
        if(user!=null){
            if(!user.getAllMedicamentos().isEmpty()){
                dataMed = user.getAllMedicamentos();

            }
            else{
                for (int i = 0; i < 4; i++) {
                    Medicamento medi = new Medicamento("VACIO");
                    dataMed.add(medi);
                }
            }

            if(!user.getAllCitas().isEmpty()){
                dataCita = user.getAllCitas();
            }else{
                for (int i = 0; i < 4; i++) {
                    CitaMedica cita = new CitaMedica("VACIO");
                    dataCita.add(cita);
                }
            }
        }

        adapterCita = new CitasListAdapter(context, dataCita);
        iniciarLista(binding.recyclerViewCitaHome, adapterCita);
        adapterMed = new MedListAdapter(context, dataMed);
        iniciarLista(binding.recyclerViewMediHome, adapterMed);
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
                        db.borrarMedicamento(i);
                        db.eliminarUserMedi(user,user.getMedicamento(i));
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
                        db.borrarCita(i);
                        db.eliminarUserCita(user,user.getCitaMedica(i));
                    }
                }
                adapterCita.reiniciarSparse();
                binding.deleteItemListCita.setVisibility(View.GONE);
            }
        });

        // Establece un listener para detectar cambios en la selección de elementos en la lista de medicamentos
        adapterMed.setOnDataChangeListener(new MedListAdapter.OnDataChangeListener() {
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
        adapterCita.setOnDataChangeListener(new CitasListAdapter.OnDataChangeListener() {
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
    private void activarDeslizar(RecyclerView recyclerView, MedListAdapter mAdapter) {
        DeslizarParaAccion deslizarParaAccion = new DeslizarParaAccion(getContext(), (DeslizarParaAccion.ItemTouchHelperContract) mAdapter) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Obtiene la posición del elemento deslizado y el elemento correspondiente
                final int position = viewHolder.getBindingAdapterPosition();
                final Serializable item = mAdapter.getData().get(position);

                // Elimina el elemento deslizado de la lista
                mAdapter.removeItem(position);
                db.borrarMedicamento(position);
                db.eliminarUserMedi(user,user.getMedicamento(position));

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

            @Override
            public void onMoved(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, int fromPos, @NonNull RecyclerView.ViewHolder target, int toPos, int x, int y) {
                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);

                ArrayList<Serializable> medicamentos = mAdapter.getData();
                db.borrarAllMedicamentos(user);
                db.eliminarUserAllMedi(user,medicamentos);
                CifradoAES aes = new CifradoAES();
                String semilla = user.getEmail() + user.getContrasena();
                SecretKey secretKey = aes.generarSecretKey(semilla);
                for(int i=0;i < medicamentos.size();i++){
                    Medicamento medicamento = (Medicamento) medicamentos.get(i);
                    medicamento.setIdMedicamento(i+1);
                    String encryptNombreMed = "";
                    String encryptDosisMed = "";
                    String encryptFrecuenciaMed = "";
                    String encryptRecordMed = "";
                    // Cifrado de datos
                    try {
                        // Encripta los datos del usuario
                        encryptNombreMed = aes.encrypt(medicamento.getNombre().getBytes(), secretKey);
                        encryptDosisMed = aes.encrypt(medicamento.getDosis().getBytes(), secretKey);
                        encryptFrecuenciaMed = aes.encrypt(medicamento.getFrecuencia().getBytes(), secretKey);
                        encryptRecordMed = aes.encrypt(medicamento.getRecordatorio().getBytes(), secretKey);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    Medicamento encrypMed = new Medicamento(medicamento.getIdMedicamento(),encryptNombreMed,encryptDosisMed,encryptFrecuenciaMed,encryptRecordMed);
                    db.addMedicamento(encrypMed);
                    db.addUserMedi(user,encrypMed);
                }
                user.setAllMedicamentos(medicamentos);
            }
        };
        // Asocia la funcionalidad de deslizamiento con el RecyclerView
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(deslizarParaAccion);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    // Activa la funcionalidad de deslizamiento en la lista y maneja el deslizamiento de elementos
    private void activarDeslizar(RecyclerView recyclerView, CitasListAdapter mAdapter) {
        DeslizarParaAccion deslizarParaAccion = new DeslizarParaAccion(getContext(), (DeslizarParaAccion.ItemTouchHelperContract) mAdapter) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Obtiene la posición del elemento deslizado y el elemento correspondiente
                final int position = viewHolder.getBindingAdapterPosition();
                final Serializable item = mAdapter.getData().get(position);

                // Elimina el elemento deslizado de la lista
                mAdapter.removeItem(position);
                db.borrarCita(position);
                db.eliminarUserCita(user,user.getCitaMedica(position));

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

            @Override
            public void onMoved(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, int fromPos, @NonNull RecyclerView.ViewHolder target, int toPos, int x, int y) {
                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
                ArrayList<Serializable> citas = mAdapter.getData();
                db.borrarAllCitas(user);
                db.eliminarUserAllCitas(user,citas);
                CifradoAES aes = new CifradoAES();
                String semilla = user.getEmail() + user.getContrasena();
                SecretKey secretKey = aes.generarSecretKey(semilla);
                for(int i=0;i<citas.size();i++){
                    CitaMedica citaMedica = (CitaMedica) citas.get(i);
                    citaMedica.setIdCita(i+1);
                    String encryptNombreCita = "";
                    String encryptDecrip = "";
                    String encryptRecord = "";
                    String encryptDireccion = "";
                    String encryptFecha = "";
                    String encryptHora = "";
                    // Cifrado de datos
                    try {
                        // Encripta los datos del usuario
                        encryptNombreCita = aes.encrypt(citaMedica.getNombre().getBytes(), secretKey);
                        encryptDecrip = aes.encrypt(citaMedica.getDescripcion().getBytes(), secretKey);
                        encryptRecord = aes.encrypt(citaMedica.getRecordatorio().getBytes(), secretKey);
                        encryptDireccion = aes.encrypt(citaMedica.getDireccion().getBytes(), secretKey);
                        encryptFecha = aes.encrypt(citaMedica.getFecha().getBytes(), secretKey);
                        encryptHora = aes.encrypt(citaMedica.getHora().getBytes(), secretKey);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    CitaMedica encrypCita = new CitaMedica(citaMedica.getIdCita(),encryptNombreCita,encryptDecrip,encryptRecord,encryptDireccion,encryptFecha,encryptHora);
                    db.addCita(encrypCita);
                    db.addUserCita(user,encrypCita);
                }
                user.setAllCitas(citas);
            }
        };

        // Asocia la funcionalidad de deslizamiento con el RecyclerView
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(deslizarParaAccion);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    // Inicializa una lista en la interfaz de usuario
    private void iniciarLista(RecyclerView recyclerView, MedListAdapter adapter) {
        Context context = getContext();
        adapter.setOnItemClickListener(this);
        adapter.setRecyclerView(recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        // Activa la funcionalidad de deslizamiento en la lista
        activarDeslizar(recyclerView, adapter);
        recyclerView.setAdapter(adapter);
    }

    private void iniciarLista(RecyclerView recyclerView, CitasListAdapter adapter) {
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

    @Override
    public void onItemCitaClick(Serializable serializable, int position) {
        if (binding.deleteItemListCita.getVisibility() == View.VISIBLE) {

            adapterCita.performClickCheckBox(position);
        } else {
            if (position<user.getAllCitas().size()) {
                CitaMedica cita = user.getCitaMedica(position);

                CardDialogFragment dialog = CardDialogFragment.newInstance(cita);
                dialog.show(requireActivity().getSupportFragmentManager(), "dialog");
            }
        }
    }

    @Override
    public void onItemMedClick(Serializable serializable, int position) {
        if (binding.deleteItemListMed.getVisibility() == View.VISIBLE) {

            adapterMed.performClickCheckBox(position);
        } else {
            if (position<user.getAllMedicamentos().size()) {
                Medicamento medicamento = user.getMedicamento(position);

                CardDialogFragment dialog = CardDialogFragment.newInstance(medicamento);
                dialog.show(requireActivity().getSupportFragmentManager(), "dialog");
            }
        }
    }


    // Método llamado cuando la vista del fragmento está siendo destruida
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding=null;
    }


}