package es.ifp.labsalut.ui;

import androidx.appcompat.app.AppCompatActivity;

public class SingletonManager {
    private static AppCompatActivity currentActivity;

    // Método para establecer la actividad actual
    public static void setCurrentActivity(AppCompatActivity activity) {
        currentActivity = activity;
    }

    // Método para obtener la actividad actual
    public static AppCompatActivity getCurrentActivity() {
        return currentActivity;
    }
}