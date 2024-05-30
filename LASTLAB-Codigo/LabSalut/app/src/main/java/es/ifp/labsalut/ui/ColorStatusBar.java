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

    // Método para cambiar el color de la barra de estado dinámicamente según el color de fondo
    public static void colorDinamicStatusBar(Activity activity, int colorBackground){
        boolean isLightBackground = isLightColor(colorBackground);  // Determina si el color de fondo es claro
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Ajusta la visibilidad del sistema UI basado en la claridad del color de fondo
            if (isLightBackground) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                activity.getWindow().getDecorView().setSystemUiVisibility(0);
            }
        }
    }

    // Método privado para determinar si un color es claro
    private static boolean isLightColor(int color) {
        int red = Color.red(color);    // Extrae el componente rojo del color
        int green = Color.green(color); // Extrae el componente verde del color
        int blue = Color.blue(color);  // Extrae el componente azul del color

        // Calcula el brillo del color utilizando la fórmula de luminosidad
        double brightness = (red * 299 + green * 587 + blue * 114) / 1000;

        return brightness > 128; // Devuelve true si el color es claro
    }

    // Método para obtener el color de fondo de la Toolbar
    public static int obtenerColorToolbar(Toolbar toolbar) {
        Drawable background = toolbar.getBackground();

        if (background instanceof ColorDrawable) {
            return ((ColorDrawable) background).getColor(); // Devuelve el color si el fondo es un ColorDrawable
        } else {
            return Color.TRANSPARENT; // Devuelve transparente si el fondo no es un ColorDrawable
        }
    }

    // Método para obtener el color de fondo de la actividad
    public static int obtenerColorBackground(Activity activity) {
        View rootView = activity.getWindow().getDecorView().getRootView();
        Drawable background = rootView.getBackground();
        if (background instanceof ColorDrawable) {
            return ((ColorDrawable) background).getColor(); // Devuelve el color si el fondo es un ColorDrawable
        } else {
            return Color.TRANSPARENT; // Devuelve transparente si el fondo no es un ColorDrawable
        }
    }

    // Método para obtener el color de la barra de estado
    public static int obtenerColorStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            if (window != null) {
                return window.getStatusBarColor(); // Devuelve el color de la barra de estado
            }
        }
        return 0; // Devuelve 0 si no se puede obtener el color de la barra de estado
    }
}
