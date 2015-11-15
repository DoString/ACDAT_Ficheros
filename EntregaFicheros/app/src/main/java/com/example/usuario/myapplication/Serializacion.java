package com.example.usuario.myapplication;

import android.content.Context;
import android.util.Log;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by juan on 27/10/2015.
 */
public class Serializacion {
    private ArrayList<Contacto> _contactos;
    private Context _contexto;

    public Serializacion(Context contexto, ArrayList<Contacto> contactos) {
        this._contactos = contactos;
        this._contexto = contexto;
    }

    public void SerializarContacto(String fichero, Contacto contacto) throws IOException {
        FileOutputStream fos = _contexto.openFileOutput(fichero, Context.MODE_APPEND);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(contacto);
    }

    public void SerializarContactos(String fichero) throws IOException {
        FileOutputStream fos = _contexto.openFileOutput(fichero, Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);

        for (int i = 0; i < _contactos.size(); i++) {
            os.writeObject(_contactos.get(i));
        }
        os.close();
        fos.close();
    }

    public void DeserializarContactos(String fichero) throws IOException, ClassNotFoundException {
        FileInputStream fis = null;
        ObjectInputStream is = null;
        try {
            fis = _contexto.openFileInput(fichero);
            is = new ObjectInputStream(fis);
        } catch (Exception e) {
            Log.v("ser", e.getLocalizedMessage());
            return;
        }

        while (true) {
            try {
                Contacto contacto = (Contacto) is.readObject();
                if (!contacto.get_borrado()) //Solo se añaden a la lista los contactos que no estén borrados.
                    _contactos.add(contacto);
            } catch (Exception e) {
                break;
            }
        }
        is.close();
        fis.close();
    }

    /*public boolean SalvarAgenda (String ficheroAntiguo, String FicheroNuevo, Context contexto) {
        String f = "contactos_back";
        File nuevoFichero = new File(contexto.getFilesDir(), f);
        try {
            while (!nuevoFichero.createNewFile()) {
                nuevoFichero.delete();
            }
            SerializarContactos(f);
            FULL_PATH.renameTo(new File(contexto.getFilesDir(), "contactos_temp"));
            nuevoFichero.renameTo(new File(contexto.getFilesDir(), FICHERO));
            FULL_PATH.renameTo(new File(contexto.getFilesDir(),f));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }*/
}
