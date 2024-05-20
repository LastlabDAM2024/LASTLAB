package es.ifp.labsalut.negocio;

import android.content.Context;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.MenuRes;
import androidx.appcompat.widget.PopupMenu;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import es.ifp.labsalut.R;

public class ModListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Serializable> datos;
    private static final int ICON_MARGIN = 8;


    public ModListAdapter(Context context, ArrayList<Serializable> datos) {
        this.context = context;
        this.datos = datos;

    }

    @Override
    public int getCount() {
        return datos.size();
    }

    @Override
    public Object getItem(int position) {
        return datos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        Serializable item = (Serializable) getItem(position);

        ImageView image = convertView.findViewById(R.id.foto_list);
        TextView nombre = convertView.findViewById(R.id.nombre_list);
        TextView frecuencia = convertView.findViewById(R.id.freq_list);
        TextView fecha = convertView.findViewById(R.id.fecha_list);
        TextView record = convertView.findViewById(R.id.record_list);
        Button menuButton = convertView.findViewById(R.id.iconButton_list);

        if (item instanceof Medicamento) {
            Medicamento data = (Medicamento) item;
            image.setImageResource(R.drawable.ic_launcher_foreground);
            nombre.setText(data.getNombre());
            frecuencia.setText(data.getFrecuencia());
            fecha.setText(data.getDosis());
            record.setText(data.getRecordatorio());
        } else if (item instanceof CitaMedica) {
            CitaMedica data = (CitaMedica) item;
            image.setImageResource(R.drawable.ic_action_fingerprint);
            nombre.setText(data.getNombre());
            frecuencia.setText(data.getDescripcion());
            fecha.setText(data.getFecha());
            record.setText(data.getRecordatorio());
        }


        // Configurar el evento del botón de menú
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v, R.menu.menu_list);
            }
        });

        return convertView;
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


