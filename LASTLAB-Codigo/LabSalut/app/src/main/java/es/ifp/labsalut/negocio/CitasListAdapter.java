package es.ifp.labsalut.negocio;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;

import es.ifp.labsalut.R;
import es.ifp.labsalut.ui.DeslizarParaAccion;

public class CitasListAdapter extends RecyclerView.Adapter<CitasListAdapter.ListViewHolder> implements DeslizarParaAccion.ItemTouchHelperContract {

    private Context context; // Contexto de la aplicación
    private static ArrayList<Serializable> itemList;// Lista de elementos
    private SparseBooleanArray seleccionados; // Array para elementos seleccionados
    private int colorBack; // Color de fondo
    private boolean select = false; // Bandera para selección múltiple
    private OnDataChangeListener mOnDataChangeListener; // Listener para cambios de datos
    private static OnItemCitaClickListener mListener;
    private RecyclerView recyclerView;

    // Interfaz para el listener de cambios de datos
    public interface OnDataChangeListener {
        void onDataChanged(boolean select);
    }

    public interface OnItemCitaClickListener {
        void onItemCitaClick(Serializable serializable, int position);
    }

    // Constructor del adaptador
    public CitasListAdapter(Context context, ArrayList<Serializable> itemList) {
        this.context = context;
        this.itemList = itemList;
        this.seleccionados = new SparseBooleanArray();
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar la vista del elemento de la lista
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        // Vincular los datos a la vista
        Object item = itemList.get(position);

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.cardView.setTransitionName("shared_element_" + position);
        // Configuración de la vista según el estado de selección
        if (seleccionados.get(position, false)) {
            holder.checkBox.setChecked(true);
            holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.md_theme_secondaryFixedDim));
        } else {
            holder.checkBox.setChecked(false);
            holder.cardView.setBackgroundColor(colorBack);
        }

        // Configuración de la visibilidad de los elementos según el modo de selección
        if (!select) {
            holder.checkBox.setVisibility(View.GONE);
            holder.avatarImage.setVisibility(View.VISIBLE);
            holder.optionButton.setVisibility(View.VISIBLE);
            holder.moverButton.setVisibility(View.GONE);
        } else {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.avatarImage.setVisibility(View.GONE);
            holder.optionButton.setVisibility(View.GONE);
            holder.moverButton.setVisibility(View.VISIBLE);
        }

        CitaMedica citaMedica = (CitaMedica) item;
        holder.titleText.setText(citaMedica.getNombre());
        holder.descriptionText.setText(citaMedica.getHora() + " - " + citaMedica.getDescripcion());
        holder.fechaText.setText(citaMedica.getFecha());
        holder.recordTex.setText(citaMedica.getRecordatorio());
        holder.avatarImage.setImageResource(R.drawable.icon_ubicacion);


        // Listener para el cambio de estado del checkbox
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                seleccionados.put(position, true);
                holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.md_theme_secondaryFixedDim));
            } else {
                seleccionados.delete(position);
                holder.cardView.setBackgroundColor(colorBack);
            }

            // Cambios en el modo de selección según el número de elementos seleccionados
            if (seleccionados.size() == 0) {
                select = false;
                mOnDataChangeListener.onDataChanged(false);
            } else {
                select = true;
                mOnDataChangeListener.onDataChanged(true);
            }
            if (seleccionados.size() > 1) {
                notifyItemChanged(holder.getAbsoluteAdapterPosition());
            } else {
                notifyDataSetChanged();
            }
        });

        // Listener para el botón de opciones
        holder.optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v, R.menu.menu_list, holder);
            }
        });


    }

    // Configuración del listener de cambios de datos
    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener) {
        mOnDataChangeListener = onDataChangeListener;
    }

    // Configuración del listener de cambios de datos
    public void setOnItemClickListener(OnItemCitaClickListener onItemClick) {
        mListener = onItemClick;
    }

    public boolean getFlagMove() {
        return select;
    }

    @Override
    public int getItemCount() {
        return itemList.size(); // Devolver el tamaño de la lista
    }

    // Eliminar un elemento de la lista
    public void removeItem(int position) {
        itemList.remove(position);
        notifyItemRemoved(position);
    }

    // Restaurar un elemento en la lista
    public void restoreItem(Serializable item, int position) {
        itemList.add(position, item);
        notifyItemInserted(position);
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

    public ListViewHolder getViewHolder(int position) {
        RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(position);
        if (holder instanceof ListViewHolder) {
            return (ListViewHolder) holder;
        }
        return null;
    }

    public void performClickCheckBox(int position) {
        ListViewHolder holder = getViewHolder(position);
        if (holder != null) {
            holder.checkBox.performClick();
        }
    }


    // Devolver los datos de la lista
    public ArrayList<Serializable> getData() {
        return itemList;
    }

    // Métodos para mover filas en la lista
    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(itemList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(itemList, i, i - 1);
            }
        }
        if (seleccionados.size() > 0) {
            seleccionados.delete(fromPosition);
            seleccionados.put(toPosition, true);
        }
        notifyItemMoved(fromPosition, toPosition);

    }

    @Override
    public void onRowSelected(MedListAdapter.ListViewHolder myViewHolder) {

    }

    @Override
    public void onRowSelected(CitasListAdapter.ListViewHolder myViewHolder) {

    }

    @Override
    public void onRowClear(MedListAdapter.ListViewHolder myViewHolder) {

    }

    @Override
    public void onRowClear(ListViewHolder myViewHolder) {

    }


    // Clase ViewHolder para la lista
    public static class ListViewHolder extends RecyclerView.ViewHolder {
        protected LinearLayout cardView;
        protected ImageView avatarImage; // Imagen del avatar
        protected MaterialCheckBox checkBox; // Checkbox
        protected TextView titleText; // Texto del título
        protected TextView descriptionText; // Texto de la descripción
        protected TextView fechaText; // Texto de la fecha
        protected TextView recordTex; // Texto del recordatorio
        protected Button optionButton; // Botón de opciones
        protected Button moverButton; // Botón para mover

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Serializable serializable = itemList.get(getAbsoluteAdapterPosition());
                    mListener.onItemCitaClick(serializable, getAbsoluteAdapterPosition());
                }
            });

            cardView = itemView.findViewById(R.id.card_list);
            avatarImage = itemView.findViewById(R.id.foto_list);
            checkBox = itemView.findViewById(R.id.check_list);
            titleText = itemView.findViewById(R.id.nombre_list);
            descriptionText = itemView.findViewById(R.id.descrip_list);
            fechaText = itemView.findViewById(R.id.fecha_list);
            recordTex = itemView.findViewById(R.id.record_list);
            optionButton = itemView.findViewById(R.id.iconButton_list);
            moverButton = itemView.findViewById(R.id.icon_list);
        }
    }

    // Mostrar el menú de opciones
    private void showMenu(View v, @MenuRes int menuRes, ListViewHolder myViewHolder) {
        PopupMenu popup = new PopupMenu(context, v);
        popup.getMenuInflater().inflate(menuRes, popup.getMenu());

        try {
            Field menuField = PopupMenu.class.getDeclaredField("mMenu");
            menuField.setAccessible(true);
            Object menuBuilder = menuField.get(popup);

            Method setOptionalIconsVisibleMethod = menuBuilder.getClass().getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            setOptionalIconsVisibleMethod.setAccessible(true);
            setOptionalIconsVisibleMethod.invoke(menuBuilder, true);

        } catch (NoSuchFieldException | NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException e) {
            Log.e("PopupMenuError", "Error al configurar el menuPop", e);
        }

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.editar_list:
                        myViewHolder.checkBox.performClick();
                        return true;
                    case R.id.ampliar_list:
                        Toast.makeText(context, "Opción 2 seleccionada", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });
        popup.show();
    }
}