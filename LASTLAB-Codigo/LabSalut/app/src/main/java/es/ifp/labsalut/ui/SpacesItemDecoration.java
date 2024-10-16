package es.ifp.labsalut.ui;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private final int space;
    private final int interSpace;

    public SpacesItemDecoration(int space, int interSpace) {
        this.space = space;
        this.interSpace = interSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int itemCount = state.getItemCount(); // Obtener el número total de elementos

        if (position == 0) {
            outRect.left = space; // Espacio al inicio
        } if (position == itemCount - 1) {
            outRect.right = space; // Espacio al final para el último elemento
        } else {
            outRect.right = interSpace; // Espacio entre elementos
        }
    }
}

