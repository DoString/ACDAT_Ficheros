package com.example.usuario.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juan on 28/10/2015.
 */
public class AdaptadorContactos extends ArrayAdapter<Contacto> {
    private ArrayList<Contacto> _contactos;
    public AdaptadorContactos(Context context, ArrayList<Contacto> objects) {
        super(context, 0, objects);
        _contactos = objects;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        //Obteniendo una instancia del inflater
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Salvando la referencia del View de la fila
        View listItemView = convertView;

        //Comprobando si el View no existe
        if (null == convertView) {
            //Si no existe, entonces inflarlo con image_list_view.xml
            listItemView = inflater.inflate(
                    R.layout.lista_contactos,
                    parent,
                    false);
        }

        //Obteniendo instancias de los elementos
        TextView nombre = (TextView) listItemView.findViewById(R.id.setNombre);
        TextView telefono = (TextView) listItemView.findViewById(R.id.setTelefono);
        TextView email = (TextView) listItemView.findViewById(R.id.setEmail);
        ImageButton borrar = (ImageButton) listItemView.findViewById(R.id.ibtnBorrar);
        borrar.setTag(position);
        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View t = v;
                final Contacto c = getItem(position);
                new AlertDialog.Builder(getContext())
                        .setTitle("Aviso")
                        .setMessage("Esta seguro que desea borrar al contacto: " + c.toString())
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                c.set_borrado(true);
                                int indice = (Integer) t.getTag();
                                _contactos.remove(indice);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });

        nombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView e = (TextView) v;
                final EditText t = new EditText(getContext());
                t.setInputType(InputType.TYPE_CLASS_TEXT);
                t.setHint(((TextView) v).getText().toString());
                new AlertDialog.Builder(getContext())
                        .setView(t)
                        .setTitle("Editar Nombre de contacto")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String cadena = ((EditText) t).getText().toString();
                                if (cadena.isEmpty()) return;
                                _contactos.get(position).set_nombre(cadena);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
        telefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView e = (TextView) v;
                final EditText t = new EditText(getContext());
                t.setInputType(InputType.TYPE_CLASS_NUMBER);
                t.setHint(((TextView) v).getText().toString());
                new AlertDialog.Builder(getContext())
                        .setView(t)
                        .setTitle("Editar Teléfono de contacto")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String cadena = ((EditText) t).getText().toString();
                                if (cadena.isEmpty()) return;
                                _contactos.get(position).set_nombre(cadena);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView e = (TextView) v;
                final EditText t = new EditText(getContext());
                t.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                t.setHint(((TextView) v).getText().toString());
                new AlertDialog.Builder(getContext())
                        .setView(t)
                        .setTitle("Editar Email de contacto")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String cadena = ((EditText) t).getText().toString();
                                if (cadena.isEmpty()) return;
                                _contactos.get(position).set_nombre(cadena);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });

       /* listItemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                int color = v.getSolidColor();
                final View e = v;
                if(hasFocus)
                    v.post(new Runnable() {
                        @Override
                        public void run() {
                            e.setSelected(true);
                            e.requestFocus();
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                //No debería pasar
                            }
                            e.clearFocus();
                            e.setSelected(false);
                        }
                    });
                    //v.setBackgroundColor(Color.argb(0xFF, 0x88, 0x94, 0xAE));
            }
        });*/

        listItemView.setFocusableInTouchMode(true);
        listItemView.setFocusable(true);

        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isActivated())
                    v.setActivated(false);
            }
        });



        //Establecemos un tag para acceder al item.
        listItemView.setTag(position);

        //Obteniendo instancia de la Tarea en la posición actual
        Contacto item = getItem(position);

        nombre.setText(item.get_nombre());
        telefono.setText(item.get_telefono());
        email.setText(item.get_email());

        //Devolver al ListView la fila creada
        return listItemView;
    }
}
