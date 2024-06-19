package es.ifp.labsalut.ui;

import static android.widget.ListPopupWindow.WRAP_CONTENT;
import static android.widget.ListPopupWindow.MATCH_PARENT;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import es.ifp.labsalut.R;
import es.ifp.labsalut.databinding.FragmentCardDialogBinding;
import es.ifp.labsalut.databinding.FragmentHomeBinding;
import es.ifp.labsalut.negocio.CitaMedica;
import es.ifp.labsalut.negocio.Usuario;


public class CardDialogFragment extends DialogFragment {

    private static final String ARG_CITA = "CITA";
    private static final int ANCHO_DIALOG = 800;

    private String mParam1;
    private String mParam2;
    private FragmentCardDialogBinding binding;
    private CitaMedica cita = null;

    public CardDialogFragment() {
    }

    public static CardDialogFragment newInstance(CitaMedica cita) {
        CardDialogFragment fragment = new CardDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CITA, cita);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Recupera los argumentos pasados al fragmento
        if (getArguments() != null) {
            cita = (CitaMedica) getArguments().getSerializable(ARG_CITA);
        }
        binding = FragmentCardDialogBinding.inflate(getLayoutInflater());

        binding.fotoCard.setImageResource(R.drawable.ic_action_fingerprint);
        binding.fechaCard.setText("Fecha: " + cita.getFecha());
        binding.horaCard.setText("Hora: " + cita.getHora());
        binding.nombreCard.setText(cita.getNombre());
        binding.descripCard.setText(cita.getDescripcion());
        binding.recordCard.setText("Recordatorio: " + cita.getRecordatorio());

        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(binding.getRoot());
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setLayout(ANCHO_DIALOG, WRAP_CONTENT);
        dialog.getWindow().setWindowAnimations(R.style.DialogExpandFromTouchAnimation);


        return dialog;
    }

}