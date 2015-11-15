package com.example.usuario.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.File;

public class ConversorFTP extends Activity implements View.OnClickListener {

    public final static String FICHERO = "cambio.txt";
    public final static String DIRECTORIO = "./ftp";
    public final static String HOST = "192.168.2.64";
    public final static int PUERTO = 21;
    public final static String USER = "alumno";
    public final static String PASS = "malaga";
    public static double cambio;
    public static boolean correcto;
    public final static String COD = "UTF-8";
    private int esperar = 0;

    EditText euros, dolares;
    RadioButton eurDolar, dolarEur;
    Button convertir;
    Conversor conversor;
    Memoria memoria;
    FTPConnection conexionFtp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversor_ftp);
        inicializar();
    }

    public void inicializar() {
        euros = (EditText) findViewById(R.id.editText1);
        dolares = (EditText) findViewById(R.id.editText2);
        eurDolar = (RadioButton) findViewById(R.id.radio0);
        eurDolar.setChecked(true); // PARA QUE ESTE PUESTO POR DEFECTO EN ESTE RADIOBUTTON
        dolarEur = (RadioButton) findViewById(R.id.radio1);
        convertir = (Button) findViewById(R.id.fconvertir);
        convertir.setOnClickListener(this);
        memoria = new Memoria(this);
        conexionFtp = new FTPConnection();
        correcto = false;
    }

    @Override
    public void onClick(View v) {
        if (v == convertir) {
            TareaAsincrona descarga = new TareaAsincrona(this);
            descarga.execute();





        }
    }

    public class TareaAsincrona extends AsyncTask<String, Integer, Resultado> {
        private ProgressDialog progreso;
        private Context contexto;

        public TareaAsincrona(Context contexto) {
            this.contexto = contexto;
        }

        protected void onPreExecute() {
            progreso = new ProgressDialog(ConversorFTP.this);
            progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progreso.setMessage("Conectando . . .");
            progreso.setCancelable(true);
            progreso.show();
        }

        protected Resultado doInBackground(String... cadena) {
            Resultado resultado = null;
            if (conexionFtp.ftpConnect(HOST, PUERTO, USER, PASS)) {
                if (conexionFtp.ftpDownload(FICHERO, DIRECTORIO, new File(getApplicationContext().getFilesDir(), FICHERO))) {
                    resultado = memoria.leerInterna(FICHERO, COD);
                }
            }
            return resultado;
        }

        protected void onPostExecute(Resultado resultado) {
            progreso.dismiss();
            correcto = false;
            if (resultado != null) {
                if (resultado.getCodigo()) {
                    cambio = Double.parseDouble(resultado.getContenido());
                    correcto = true;
                }
            }
            if (correcto)
                conversor = new Conversor(cambio);
            else
                conversor = new Conversor();

            if (eurDolar.isChecked()) {
                if (!euros.getText().toString().isEmpty())
                    dolares.setText(conversor.convertirADolares(euros.getText().toString()));
                else
                    Toast.makeText(ConversorFTP.this, "Debes introducir un valor de euros", Toast.LENGTH_SHORT).show();
            }

            if (dolarEur.isChecked()) {
                if (!dolares.getText().toString().isEmpty())
                    euros.setText(conversor.convertirAEuros(dolares.getText().toString()));
                else
                    Toast.makeText(ConversorFTP.this, "Debes introducir un valor de dólares", Toast.LENGTH_SHORT).show();
            }
        }

        protected void onCancelled() {
            progreso.dismiss();
        }

        protected void onProgressUpdate(Integer... progress) {
            progreso.setMessage("Conectando " + Integer.toString(progress[0]));
        }
    }
}
