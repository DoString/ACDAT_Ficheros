package com.example.usuario.myapplication;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ActivityAgenda extends AppCompatActivity implements DialogoAnadir.DialogAnadirListener,
        DialogoBuscar.OnDialogoBuscarListener {
    private ListView _vistaContactos;
    private ArrayList<Contacto> _contactos;
    private ImageButton Anadir, Buscar;
    private static final String FICHERO = "contactos";
    private File FULL_PATH;
    private String[] _nombres = {"José", "Francisco", "María", "Juan", "Alfredo"};
    private String[] _telefonos = {"606345678", "654123564", "674234565", "634214678", "650123456"};
    private String[] _emails = {"Jose1234@Gmail.com", "Fran2365@Yahoo.es", "Mari1a@Gmail.com", "JohnDoe@Gmail.com", "Alfred008@Gmail.com"};
    private Serializacion ser;
    private AdaptadorContactos adaptador;
    private DialogFragment dialogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_agenda);


        _contactos = new ArrayList<Contacto>();
        adaptador = new AdaptadorContactos(this, _contactos);
        ser = new Serializacion(this.getApplicationContext(), _contactos);

        FULL_PATH = new File(getFilesDir(), FICHERO);

        Anadir = (ImageButton) findViewById(R.id.btnAnadir);
        Buscar = (ImageButton) findViewById(R.id.btnBuscar);
        _vistaContactos = (ListView) findViewById(R.id.listaContactos);


        Anadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                new DialogoAnadir(getApplicationContext()).show(ft, "miDialogo");
            }
        });
        Buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                new DialogoBuscar().show(ft, "miDialogo2");
            }
        });

        long size = FULL_PATH.length();
        if (!FULL_PATH.exists() || FULL_PATH.length() <= 4){
            CrearCincoContactos();
            try {
                ser.SerializarContactos(FICHERO);
            } catch (IOException e) {
                Log.v("ser", e.getLocalizedMessage());
            }
        }

        try {
            if (_contactos.size() <= 0 ) {
                ser.DeserializarContactos(FICHERO);
                Toast.makeText(ActivityAgenda.this, "Deserialización de contactos correcta", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.v("ser", e.getLocalizedMessage());
            CrearCincoContactos();
        }

        _vistaContactos.setItemsCanFocus(true);
        _vistaContactos.setClickable(true);
        //_vistaContactos.setItemsCanFocus(true);

        _vistaContactos.setAdapter(adaptador);

    }

    private void CrearCincoContactos() {
        for (int i = 0; i < 5; i++) {
            adaptador.add(new Contacto(_nombres[i], _telefonos[i], _emails[i]));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            ser.SerializarContactos(FICHERO);
            Toast.makeText(ActivityAgenda.this, "Serialización de contactos correcta", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.v("ser", e.getLocalizedMessage());
        }
    }

    @Override
    public void OnclickOK(DialogInterface dialogo, Contacto contacto) {
        adaptador.add(contacto);
    }

    @Override
    public void NotificarAnadido() {
        adaptador.notifyDataSetChanged();
    }

    @Override
    public void OnclickCancelar(DialogInterface dialogo, boolean cerrar) {
        if (cerrar)
            dialogo.dismiss();
    }

    @Override
    public void OnBuscar(DialogInterface dialogo, String dato, DialogoBuscar.TipoDato t) {
        dialogo.dismiss();
        int pos = -1;
        switch (t) {
            case NOMBRE:
                for (int i = 0; i < _contactos.size(); i++) {
                    if (_contactos.get(i).get_nombre().toLowerCase().equals(dato.toLowerCase()))
                        pos = i;
                }
                break;
            case TELEFONO:
                for (int i = 0; i < _contactos.size(); i++) {
                    if (_contactos.get(i).get_telefono().toLowerCase().equals(dato.toLowerCase()))
                        pos = i;
                }
                break;
            case EMAIL:
                for (int i = 0; i < _contactos.size(); i++) {
                    if (_contactos.get(i).get_email().toLowerCase().equals(dato.toLowerCase()))
                        pos = i;
                }
                break;
            default:
                break;
        }
        View fila;
        if (pos != -1) {
            final int position = pos;
            final View vista = getViewByPosition(position, _vistaContactos);
            //vista.requestFocusFromTouch();
            _vistaContactos.setSelection(position);
            _vistaContactos.post(new Runnable() {
                @Override
                public void run() {
                    _vistaContactos.setSelection(position);
                }
            });

            _vistaContactos.setItemChecked(position, true);

        } else
            Toast.makeText(ActivityAgenda.this, "No se ha encontrado ningún contacto", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnCancelar(DialogInterface dialogo) {
        dialogo.dismiss();
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    public void cambiarColor(Runnable r) {
        r.run();
    }
}
