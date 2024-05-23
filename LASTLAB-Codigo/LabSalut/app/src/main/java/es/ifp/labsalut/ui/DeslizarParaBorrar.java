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
import android.graphics.drawable.ColorDrawable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import es.ifp.labsalut.R;

abstract class DeslizarParaBorrar extends ItemTouchHelper.SimpleCallback {

        Context mContext;
        private Paint mClearPaint;
        private ColorDrawable mBackground;
        private int backgroundColor;
        private Drawable deleteDrawable;
        private int intrinsicWidth;
        private int intrinsicHeight;


        DeslizarParaBorrar(Context context) {
            super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            mContext = context;
            mBackground = new ColorDrawable();
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


        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            return makeMovementFlags(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
            return false;
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            View itemView = viewHolder.itemView;
            int itemHeight = itemView.getHeight();

            boolean isCancelled = dX == 0 && !isCurrentlyActive;

            if (isCancelled) {
                clearCanvas(c, itemView.getLeft() + dX, (float) itemView.getTop(), (float) itemView.getRight() + dX, (float) itemView.getBottom());
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                return;
            }

            GradientDrawable shapeDrawable = new GradientDrawable();
            shapeDrawable.setColor(backgroundColor);
            shapeDrawable.setCornerRadius(500);

            if (dX > 0) { // Swipe to the right
                shapeDrawable.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + (int) dX, itemView.getBottom());
                deleteDrawable.setBounds(itemView.getLeft() + deleteDrawable.getIntrinsicWidth(), itemView.getTop() + (itemHeight - intrinsicHeight) / 2, itemView.getLeft() + deleteDrawable.getIntrinsicWidth() + intrinsicWidth, itemView.getTop() + (itemHeight + intrinsicHeight) / 2);
            } else if (dX < 0) { // Swipe to the left
                shapeDrawable.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                deleteDrawable.setBounds(itemView.getRight() - deleteDrawable.getIntrinsicWidth() - intrinsicWidth, itemView.getTop() + (itemHeight - intrinsicHeight) / 2, itemView.getRight() - deleteDrawable.getIntrinsicWidth(), itemView.getTop() + (itemHeight + intrinsicHeight) / 2);
            }

            shapeDrawable.draw(c);
            deleteDrawable.draw(c);

        }

        private void clearCanvas(Canvas c, Float left, Float top, Float right, Float bottom) {
            c.drawRect(left, top, right, bottom, mClearPaint);

        }

        @Override
        public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
            return 0.7f;
        }
    }
