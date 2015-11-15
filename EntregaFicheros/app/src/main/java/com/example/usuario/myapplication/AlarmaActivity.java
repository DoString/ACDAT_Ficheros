package com.example.usuario.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class AlarmaActivity extends Activity implements View.OnClickListener {
    private ImageButton boton;
    private TextView crono, res;
    private int[] tiempos;
    private String[] textos;
    private CountDownTimer[] alarmas;
    private final static int INTERVALO = 1000;
    private final static int UNSEGUNDO = 1000;
    private int remAlarm;
    private int lastAlarm;
    private Memoria mem;
    private Resultado r;
    private final static String FICHERO = "alarmas.txt";
    private final static String COD = "UTF-8";
    private final static int MILISEC = 60000;
    private MediaPlayer mp;
    public AlertDialog d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarma);
        InicializarCampos();

        if (!EscribirFichero() || !LeerFichero()) {
            this.finish();
            return;
        }

        CrearAlarmas();
    }

    private void InicializarCampos() {
        mp = MediaPlayer.create(this, R.raw.alarma);
        mem = new Memoria(this);
        r = new Resultado();

        boton = (ImageButton) findViewById(R.id.aComenzar);
        boton.setOnClickListener(this);
        crono = (TextView) findViewById(R.id.acrono);
        crono.setText("00:00");
        res = (TextView) findViewById(R.id.aRes);


        final Activity a = this;
        d = new AlertDialog.Builder(this)
                .setTitle("Fin de Alarmas")
                .setMessage("Desea reiniciar?")
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setBotonEnabled(true);
                        //InicializarCampos();
                        remAlarm = tiempos.length;
                        setAlarmasRestantes(remAlarm);
                        alarmas[0].start();

                    }
                })
                .setNegativeButton("SALIR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        a.finish();
                    }
                }).create();
    }

    private void setBotonEnabled(boolean enabled) {
        boton.setEnabled(enabled);
    }

    private void setAlarmasRestantes(int restantes) {
        res.setText(String.valueOf(restantes));
    }

    private boolean EscribirFichero() {
        if (mem.disponibleEscritura()) {
            mem.escribirExterna(FICHERO, "4,paso por el primer kilómetro\r\n", false, COD);
            mem.escribirExterna(FICHERO, "5,paso por el segundo kilómetro\r\n", true, COD);
            mem.escribirExterna(FICHERO, "7,paso por el tercer kilómetro\r\n", true, COD);
            mem.escribirExterna(FICHERO, "9,paso por el cuarto kilómetro\r\n", true, COD);
        } else {
            Toast.makeText(AlarmaActivity.this, "No hay permiso de escritura en SDcard", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean LeerFichero() {
        try {
            if (mem.disponibleLectura()) {
                r = mem.leerExterna("alarmas.txt", "UTF-8");
                if (r.getCodigo()) {
                    String cont = r.getContenido();
                    if (cont != null && cont.length() > 0) {
                        String lineas[] = cont.split("\r\n");

                        remAlarm = lineas.length;
                        lastAlarm = lineas.length;
                        tiempos = new int[lineas.length];
                        textos = new String[lineas.length];

                        for (int i = 0; i < lineas.length; i++) {
                            int n = Integer.parseInt(lineas[i].split(",")[0])  * MILISEC;
                            String t = lineas[i].split(",")[1];
                            tiempos[i] = n;
                            textos[i] = t;
                        }
                    }
                }
            } else {
                Toast.makeText(AlarmaActivity.this, "Error al acceder a la memoria externa", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(AlarmaActivity.this, "Error: El fichero tiene un formato desconocido", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void CrearAlarmas() {
        alarmas = new CountDownTimer[tiempos.length];
        remAlarm = tiempos.length;
        setAlarmasRestantes(remAlarm);
        for (int i = 0; i < tiempos.length; i++) {
            final int a = i;
            alarmas[i] = new CountDownTimer(tiempos[i], INTERVALO) {

                public void onTick(long millisUntilFinished) {
                    String min, sec;
                    min = String.valueOf(((millisUntilFinished / UNSEGUNDO) / 60) % 60 < 10 ?
                            "0" + ((millisUntilFinished / UNSEGUNDO) / 60) % 60 : ((millisUntilFinished / UNSEGUNDO) / 60) % 60);
                    sec = String.valueOf((millisUntilFinished / UNSEGUNDO) % 60 < 10 ?
                            "0" + (millisUntilFinished / UNSEGUNDO) % 60 : (millisUntilFinished / UNSEGUNDO) % 60);
                    crono.setText(min + ":" + sec);
                }

                public void onFinish() {
                    setAlarmasRestantes(--remAlarm);

                    if (a <= tiempos.length - 1)
                        Toast.makeText(AlarmaActivity.this, textos[a], Toast.LENGTH_LONG).show();
                    mp.start();

                    if (a + 1 < lastAlarm)
                        alarmas[a + 1].start();
                    else
                        d.show();
                }
            };
        }
    }

    @Override
    public void onClick(View v) {

        if (v == boton) {
            alarmas[0].start();
            v.setEnabled(false);
            res.setText(String.valueOf(lastAlarm));
        }
    }
}

