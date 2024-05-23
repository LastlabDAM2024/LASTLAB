package es.ifp.labsalut.negocio;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Color;


import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;


import es.ifp.labsalut.R;
import es.ifp.labsalut.ui.DeslizarParaMover;

public class ModListAdapter extends RecyclerView.Adapter<ModListAdapter.ListViewHolder> implements DeslizarParaMover.ItemTouchHelperContract {

    private static final float ICON_MARGIN = 8;
    private Context context;
    private ArrayList<Serializable> itemList;// Usa Object para contener tanto Medicamento como CitaMedica
    private int colorBack;

    public ModListAdapter(Context context, ArrayList<Serializable> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Object item = itemList.get(position);

        if (item instanceof Medicamento) {
            Medicamento medicamento = (Medicamento) item;
            holder.titleText.setText(medicamento.getNombre());
            holder.descriptionText.setText(medicamento.getFrecuencia());
            holder.fechaText.setText(medicamento.getDosis());
            holder.recordTex.setText(medicamento.getRecordatorio());
            holder.avatarImage.setImageResource(R.drawable.ic_launcher_foreground);

        } else if (item instanceof CitaMedica) {
            CitaMedica citaMedica = (CitaMedica) item;
            holder.titleText.setText(citaMedica.getNombre());
            holder.descriptionText.setText(citaMedica.getHora() + " - " + citaMedica.getDescripcion());
            holder.fechaText.setText(citaMedica.getFecha());
            holder.recordTex.setText(citaMedica.getRecordatorio());
            holder.avatarImage.setImageResource(R.drawable.ic_action_fingerprint);
        }
        holder.optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v, R.menu.menu_list);
            }
        });
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public void removeItem(int position) {
        itemList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Serializable item, int position) {
        itemList.add(position, item);
        notifyItemInserted(position);
    }

    public ArrayList<Serializable> getData() {
        return itemList;
    }

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
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(ListViewHolder myViewHolder) {
        colorBack =  myViewHolder.itemView.getDrawingCacheBackgroundColor();
        int color = context.getResources().getColor(R.color.md_theme_secondaryFixedDim);
        myViewHolder.itemView.setBackgroundColor(color);

    }

    @Override
    public void onRowClear(ListViewHolder myViewHolder) {
        myViewHolder.itemView.setBackgroundColor(colorBack);

    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarImage;
        TextView titleText;
        TextView descriptionText;
        TextView fechaText;
        TextView recordTex;
        Button optionButton;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImage = itemView.findViewById(R.id.foto_list);
            titleText = itemView.findViewById(R.id.nombre_list);
            descriptionText = itemView.findViewById(R.id.descrip_list);
            fechaText = itemView.findViewById(R.id.fecha_list);
            recordTex = itemView.findViewById(R.id.record_list);
            optionButton = itemView.findViewById(R.id.iconButton_list);
        }
    }

    private void showMenu(View v, @MenuRes int menuRes) {
        PopupMenu popup = new PopupMenu(context, v);
        popup.getMenuInflater().inflate(menuRes, popup.getMenu());

        try {
            // Use reflection to access MenuBuilder and setOptionalIconsVisible
            Field menuField = PopupMenu.class.getDeclaredField("mMenu");
            menuField.setAccessible(true);
            Object menuBuilder = menuField.get(popup);

            Method setOptionalIconsVisibleMethod = menuBuilder.getClass().getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            setOptionalIconsVisibleMethod.setAccessible(true);
            setOptionalIconsVisibleMethod.invoke(menuBuilder, true);

            Method getVisibleItemsMethod = menuBuilder.getClass().getDeclaredMethod("getVisibleItems");
            getVisibleItemsMethod.setAccessible(true);
            Object visibleItems = getVisibleItemsMethod.invoke(menuBuilder);

            for (Object item : (List<?>) visibleItems) {
                MenuItem menuItem = (MenuItem) item;

                int iconMarginPx = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, ICON_MARGIN, context.getResources().getDisplayMetrics());

                if (menuItem.getIcon() != null) {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                        menuItem.setIcon(new InsetDrawable(menuItem.getIcon(), iconMarginPx, 0, iconMarginPx, 0));
                    } else {
                        menuItem.setIcon(new InsetDrawable(menuItem.getIcon(), iconMarginPx, 0, iconMarginPx, 0) {
                            @Override
                            public int getIntrinsicWidth() {
                                return getIntrinsicHeight() + iconMarginPx + iconMarginPx;
                            }
                        });
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        popup.show();
    }
}


