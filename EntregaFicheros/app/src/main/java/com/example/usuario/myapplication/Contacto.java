package com.example.usuario.myapplication;

import java.io.Serializable;

/**
 * Created by juan on 27/10/2015.
 */
public class Contacto implements Serializable {
    private String _nombre;
    private String _email;
    private String _telefono;
    private Boolean _borrado;

    public Contacto(String nombre, String telefono, String email) {
        _nombre = nombre;
        _telefono = telefono;
        _email = email;
        _borrado = false;
    }

    public Boolean get_borrado() {
        return _borrado;
    }

    public void set_borrado(Boolean _borrado) {
        this._borrado = _borrado;
    }

    public String get_nombre() {
        return _nombre;
    }

    public void set_nombre(String _nombre) {
        this._nombre = _nombre;
    }

    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public String get_telefono() {
        return _telefono;
    }

    public void set_telefono(String _telefono) {
        this._telefono = _telefono;
    }

    @Override
    public String toString() {
        return _nombre + ", " + _telefono;
    }
}
