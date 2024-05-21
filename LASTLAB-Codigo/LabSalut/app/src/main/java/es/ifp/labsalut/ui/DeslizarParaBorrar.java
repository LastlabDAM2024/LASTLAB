package es.ifp.labsalut.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import es.ifp.labsalut.R;

public class DeslizarParaBorrar extends ItemTouchHelper.SimpleCallback {

    private RecyclerView.Adapter adapter;
    private ColorDrawable background;


    public DeslizarParaBorrar(RecyclerView.Adapter adapter,  SwipeButtonClickListener listener) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
        background = new ColorDrawable(Color.RED);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            View itemView = viewHolder.itemView;
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            View swipeButton = LayoutInflater.from(itemView.getContext()).inflate(R.layout.boton_borrado, null, false);
            swipeButton.setLayoutParams(params);

            if (dX > 0) { // Swiping to the right
                swipeButton.setBackgroundResource(R.color.md_theme_error);
                ((Button) swipeButton).setText("Borrar");
                swipeButton.setTranslationX(0);
            } else if (dX < 0) { // Swiping to the left
                swipeButton.setBackgroundResource(R.color.md_theme_error);
                ((Button) swipeButton).setText("Borrar");
                swipeButton.setTranslationX(dX);
            }

            c.save();
            if (dX > 0) {
                c.clipRect(itemView.getLeft(), itemView.getTop(), (int) dX, itemView.getBottom());
                swipeButton.layout(itemView.getLeft(), itemView.getTop(), (int) dX, itemView.getBottom());
            } else {
                c.clipRect(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                swipeButton.layout(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
            }
            swipeButton.draw(c);
            c.restore();
        }
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        adapter.notifyItemRemoved(position);

        // borrar del datalist
    }
    public interface SwipeButtonClickListener {
        void onSwipe(int position);
    }

}
