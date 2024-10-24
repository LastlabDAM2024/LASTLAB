package es.ifp.labsalut.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import es.ifp.labsalut.R;
import es.ifp.labsalut.negocio.CitasListAdapter;
import es.ifp.labsalut.negocio.MedListAdapter;

public class DeslizarParaAccion extends ItemTouchHelper.SimpleCallback {

    private Context mContext;  // Contexto de la actividad
    private Paint mClearPaint;  // Pintura para limpiar el canvas
    private int backgroundColor;  // Color de fondo para la acción de deslizar
    private Drawable deleteDrawable;  // Drawable para el icono de eliminación
    private int intrinsicWidth;  // Ancho intrínseco del icono de eliminación
    private int intrinsicHeight;  // Altura intrínseca del icono de eliminación
    private final ItemTouchHelperContract mAdapter;  // Interfaz para manejar las acciones de movimiento y selección
    private MedListAdapter adaptadorMed;
    private CitasListAdapter adaptadorCitas;
    private boolean flagMove = false;

    // Constructor de la clase
    DeslizarParaAccion(Context context, ItemTouchHelperContract adapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mContext = context;
        mAdapter = adapter;
        if (adapter instanceof MedListAdapter) {
            adaptadorMed = (MedListAdapter) adapter;
        } else {
            adaptadorCitas = (CitasListAdapter) adapter;

        }
        backgroundColor = Color.parseColor("#b80f0a");
        mClearPaint = new Paint();
        mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        deleteDrawable = ContextCompat.getDrawable(mContext, R.drawable.recycler_bin);
        if (deleteDrawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) deleteDrawable).getBitmap();
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 50, 75, false);
            deleteDrawable = new BitmapDrawable(mContext.getResources(), scaledBitmap);
        }
        intrinsicWidth = deleteDrawable.getIntrinsicWidth();
        intrinsicHeight = deleteDrawable.getIntrinsicHeight();
    }

    // Método para obtener los flags de movimiento permitidos
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        final int[] dragFlags = {0}; // Array para almacenar dragFlags
        final int[] swipeFlags = {0}; // Array para almacenar swipeFlags
        dragFlags[0] = ItemTouchHelper.UP | ItemTouchHelper.DOWN; // Permitir movimientos hacia arriba y abajo por defecto
        swipeFlags[0] = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT; // Permitir deslizamientos hacia la izquierda y derecha por defecto

        if (adaptadorMed != null) {
            if (adaptadorMed.getFlagMove()) {
                // Permitir solo movimientos hacia arriba y abajo cuando está seleccionado
                dragFlags[0] = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                // No permitir deslizamientos cuando está seleccionado
                swipeFlags[0] = 0;
                // Flags iniciales

            } else {
                // Permitir deslizamientos hacia la izquierda y derecha cuando no está seleccionado
                swipeFlags[0] = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                // No permitir movimientos cuando no está seleccionado
                dragFlags[0] = 0;
            }
        } else {

            if (adaptadorCitas.getFlagMove()) {
                // Permitir solo movimientos hacia arriba y abajo cuando está seleccionado
                dragFlags[0] = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                // No permitir deslizamientos cuando está seleccionado
                swipeFlags[0] = 0;
                // Flags iniciales

            } else {
                // Permitir deslizamientos hacia la izquierda y derecha cuando no está seleccionado
                swipeFlags[0] = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                // No permitir movimientos cuando no está seleccionado
                dragFlags[0] = 0;
            }
        }
        // Retornar los flags de movimiento actualizados
        return makeMovementFlags(dragFlags[0], swipeFlags[0]);
    }

    // Método para manejar el movimiento de los elementos en el RecyclerView
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        mAdapter.onRowMoved(viewHolder.getAbsoluteAdapterPosition(), target.getAbsoluteAdapterPosition());
        return true;
    }


    // Método para establecer las direcciones de deslizamiento por defecto
    @Override
    public void setDefaultSwipeDirs(int defaultSwipeDirs) {
        super.setDefaultSwipeDirs(defaultSwipeDirs);
    }

    // Método para establecer las direcciones de arrastre por defecto
    @Override
    public void setDefaultDragDirs(int defaultDragDirs) {
        super.setDefaultDragDirs(defaultDragDirs);
    }

    // Método para dibujar la vista mientras se está deslizando
    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        int constant = 60;
        View itemView = viewHolder.itemView;
        int itemHeight = itemView.getHeight();

        boolean isCancelled = dX == 0 && !isCurrentlyActive;
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (isCancelled) {
                clearCanvas(c, itemView.getLeft() + dX + constant, (float) itemView.getTop(), (float) itemView.getRight() + dX - constant, (float) itemView.getBottom());
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                return;
            }
            GradientDrawable shapeDrawable = new GradientDrawable();
            shapeDrawable.setColor(backgroundColor);
            shapeDrawable.setCornerRadius(60f);

            if (dX > 0) {
                shapeDrawable.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + (int) dX + constant, itemView.getBottom());
                deleteDrawable.setBounds(itemView.getLeft() + deleteDrawable.getIntrinsicWidth(), itemView.getTop() + (itemHeight - intrinsicHeight) / 2, itemView.getLeft() + deleteDrawable.getIntrinsicWidth() + intrinsicWidth, itemView.getTop() + (itemHeight + intrinsicHeight) / 2);
            } else if (dX < 0) {
                shapeDrawable.setBounds(itemView.getRight() + (int) dX - constant, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                deleteDrawable.setBounds(itemView.getRight() - deleteDrawable.getIntrinsicWidth() - intrinsicWidth, itemView.getTop() + (itemHeight - intrinsicHeight) / 2, itemView.getRight() - deleteDrawable.getIntrinsicWidth(), itemView.getTop() + (itemHeight + intrinsicHeight) / 2);
            }

            shapeDrawable.draw(c);
            deleteDrawable.draw(c);
        }
    }

    // Método para limpiar el canvas
    private void clearCanvas(Canvas c, Float left, Float top, Float right, Float bottom) {
        c.drawRect(left, top, right, bottom, mClearPaint);
    }

    // Método para obtener el umbral de deslizamiento
    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return 0.6f;
    }

    // Método para manejar el evento de deslizamiento
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        // Implementar lógica cuando se deslice un elemento
    }

    // Método para manejar el cambio de selección
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder,
                                  int actionState) {
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            if (viewHolder instanceof MedListAdapter.ListViewHolder) {
                MedListAdapter.ListViewHolder myViewHolder =
                        (MedListAdapter.ListViewHolder) viewHolder;
                mAdapter.onRowSelected(myViewHolder);
            }
            if (viewHolder instanceof CitasListAdapter.ListViewHolder) {
                CitasListAdapter.ListViewHolder myViewHolder =
                        (CitasListAdapter.ListViewHolder) viewHolder;
                mAdapter.onRowSelected(myViewHolder);
            }
        }
        // Configurar los flags de arrastre y deslizamiento según la selección en el adaptador
        super.onSelectedChanged(viewHolder, actionState);
    }

    // Método para limpiar la vista
    @Override
    public void clearView(RecyclerView recyclerView,
                          RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if (viewHolder instanceof MedListAdapter.ListViewHolder) {
            MedListAdapter.ListViewHolder myViewHolder =
                    (MedListAdapter.ListViewHolder) viewHolder;
            mAdapter.onRowClear(myViewHolder);
        }
        if (viewHolder instanceof CitasListAdapter.ListViewHolder) {
            CitasListAdapter.ListViewHolder myViewHolder =
                    (CitasListAdapter.ListViewHolder) viewHolder;
            mAdapter.onRowClear(myViewHolder);
        }
    }

    // Interfaz para manejar los eventos de movimiento y selección
    public interface ItemTouchHelperContract {

        void onRowMoved(int fromPosition, int toPosition);

        void onRowSelected(MedListAdapter.ListViewHolder myViewHolder);

        void onRowSelected(CitasListAdapter.ListViewHolder myViewHolder);

        void onRowClear(MedListAdapter.ListViewHolder myViewHolder);

        void onRowClear(CitasListAdapter.ListViewHolder myViewHolder);
    }
}
