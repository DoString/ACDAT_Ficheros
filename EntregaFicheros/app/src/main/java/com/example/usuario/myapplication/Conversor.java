package com.example.usuario.myapplication;

public class Conversor {
    private double cambio;
    private static final double VALOR = 0.92;

    public Conversor(double cambio) {
        this.cambio = cambio;
    }

    public Conversor() {
        this.cambio = VALOR;
    }

    public String convertirADolares(String cantidad) {
        double valor = Double.parseDouble(cantidad) / cambio;
        return String.format("%.3f", valor);
    }

    public String convertirAEuros(String cantidad) {
        double valor = Double.parseDouble(cantidad) * cambio;
        return String.format("%.3f", valor);
    }
}
