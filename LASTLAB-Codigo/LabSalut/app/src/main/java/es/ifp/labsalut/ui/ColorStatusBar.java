package es.ifp.labsalut.ui;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.Window;

import androidx.appcompat.widget.Toolbar;


public class ColorStatusBar {

    public static void colorDinamicStatusBar(Activity activity, int colorBackground){
        boolean isLightBackground = isLightColor(colorBackground);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isLightBackground) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                activity.getWindow().getDecorView().setSystemUiVisibility(0);
            }
        }

    }

    private static boolean isLightColor(int color) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        double brightness = (red * 299 + green * 587 + blue * 114) / 1000;

        return brightness > 128;
    }

    public static int obtenerColorToolbar(Toolbar toolbar) {
        Drawable background = toolbar.getBackground();

        if (background instanceof ColorDrawable) {
            return ((ColorDrawable) background).getColor();
        } else {
            return Color.TRANSPARENT;
        }
    }

    public static int obtenerColorBackground(Activity activity) {
        View rootView = activity.getWindow().getDecorView().getRootView();
        Drawable background = rootView.getBackground();
        if (background instanceof ColorDrawable) {
            return ((ColorDrawable) background).getColor();
        } else {
            return Color.TRANSPARENT;
        }
    }

    public static int obtenerColorStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            if (window != null) {
                // Establecer el color de la barra de estado
                return window.getStatusBarColor();
            }
        }
        return 0;
    }
}
