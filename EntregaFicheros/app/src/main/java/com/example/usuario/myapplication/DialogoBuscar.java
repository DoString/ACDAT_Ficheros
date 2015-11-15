package com.example.usuario.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Created by juan on 29/10/2015.
 */
public class DialogoBuscar extends DialogFragment {
    public enum TipoDato{
        NOMBRE,
        TELEFONO,
        EMAIL
    }

    public DialogoBuscar(){
        //Constructor vacio necesario
    }

    public interface OnDialogoBuscarListener{
        void OnBuscar(DialogInterface dialogo, String dato, TipoDato tipo);
        void OnCancelar(DialogInterface dialogo);
    }

    private OnDialogoBuscarListener buscar;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View vista = inflater.inflate(R.layout.layout_dialogo_buscar, null);
        final TextView texto = (TextView) vista.findViewById(R.id.diaBuscar);
        final RadioGroup r = (RadioGroup) vista.findViewById(R.id.diaRG);

        builder.setView(vista)
                .setTitle("Buscar contacto")
                .setPositiveButton("Buscar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TipoDato t = TipoDato.NOMBRE;
                        switch (r.getCheckedRadioButtonId()) {
                            case R.id.rb1: t = TipoDato.NOMBRE;
                                break;
                            case R.id.rb2: t = TipoDato.TELEFONO;
                                break;
                            case R.id.rb3: t = TipoDato.EMAIL;
                                break;
                            default:
                                break;
                        }
                        buscar.OnBuscar(dialog, texto.getText().toString(), t);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        buscar.OnCancelar(dialog);
                    }
                });
                return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            buscar = (OnDialogoBuscarListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " necesita implementar la interfaz OnDialogBuscarListener");
        }
    }
}
