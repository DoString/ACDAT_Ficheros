package com.example.usuario.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.zip.Inflater;

/**
 * Created by usuario on 29/10/15.
 */
public class DialogoAnadir extends DialogFragment {
    private EditText _nombre;
    private EditText _telefono;
    private EditText _email;
    private Context contexto;

    public interface DialogAnadirListener {
        void OnclickOK(DialogInterface dialogo, Contacto contacto);
        void NotificarAnadido();
        void OnclickCancelar(DialogInterface dialogo, boolean cerrar);
    }

    public DialogoAnadir(Context contexto) {
        this.contexto=contexto;
    }

    private DialogAnadirListener anadir;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder((getActivity()));
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View vista = inflater.inflate(R.layout.layout_dialogo, null);

        _nombre = (EditText) vista.findViewById(R.id.diaNombre);
        _telefono = (EditText) vista.findViewById(R.id.diaTel);
        _email = (EditText) vista.findViewById(R.id.diaMail);


        final DialogFragment dia = this;
        builder.setView(vista)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Contacto c = new Contacto(_nombre.getText().toString(),
                                        _telefono.getText().toString(),
                                        _email.getText().toString());
                                if (c.get_nombre().isEmpty() || c.get_telefono().isEmpty() ||
                                        c.get_email().isEmpty()) {
                                    c = null;
                                    Toast.makeText(contexto.getApplicationContext(), "No hay datos de contacto", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                anadir.OnclickOK(dialog, c);
                                anadir.NotificarAnadido();
                                Toast.makeText(contexto, "Contacto añadido correctamente", Toast.LENGTH_SHORT).show();
                            }
                        }
                )
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                anadir.OnclickCancelar(dialog, true);
                            }
                        }
                );
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            anadir = (DialogAnadirListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " Necesita implementar la interfaz DialogAnadirListener");
        }
    }
}
