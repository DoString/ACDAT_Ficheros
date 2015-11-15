package com.example.usuario.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;

import cz.msebera.android.httpclient.Header;

public class ConexionAsincrona extends AppCompatActivity implements View.OnClickListener {
    EditText direccion, file;
    RadioButton radioJava, radioApache, radioAsinc;
    Button conectar, descargar;
    WebView web;
    TextView tiempo;
    long inicio, fin;
    Memoria m;
    static boolean correcto;


    public static final String JAVA = "java";
    public static final String APACHE = "APACHE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conexion_asincrona);
        iniciar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_conexion_asincrona, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void iniciar() {
        direccion = (EditText) findViewById(R.id.aurl);
        file = (EditText) findViewById(R.id.asfichero);
        radioJava = (RadioButton) findViewById(R.id.aradioJava);
        radioApache = (RadioButton) findViewById(R.id.aradioApache);
        radioAsinc = (RadioButton) findViewById(R.id.aradioAsinc);
        conectar = (Button) findViewById(R.id.abtnNavegar);
        conectar.setOnClickListener(this);
        descargar = (Button) findViewById(R.id.asdescargar);
        descargar.setOnClickListener(this);
        //cancelar = (Button) findViewById(R.id.btnCancelar);
        //cancelar.setOnClickListener(this);
        web = (WebView) findViewById(R.id.aweb);
        tiempo = (TextView) findViewById(R.id.aresultado);
        //StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        m = new Memoria(this);
        correcto = false;
    }

    @Override
    public void onClick(View v) {
        if (v == conectar) {
            TareaAsincrona tarea;
            String opcion = APACHE;
            String texto = direccion.getText().toString();

            Resultado resultado = new Resultado();
            if (v == conectar) {
                //No funciona comprobar el estado de la red
                if (true/* isNetworkAvailable() */) {
                    if (radioAsinc.isChecked())
                        androidAsincrono();
                    else {
                        if (radioJava.isChecked())
                            opcion = JAVA;
                        inicio = System.currentTimeMillis();
                        tarea = new TareaAsincrona(this);
                        tarea.execute(opcion, texto);
                    }
                } else {
                    Toast.makeText(ConexionAsincrona.this, "No hay red Disponible", Toast.LENGTH_SHORT).show();
                }

            }
        } else if (v == descargar) {
            if (file.getText().toString().isEmpty()) {
                Toast.makeText(ConexionAsincrona.this, "Debes introducir el nombre del fichero", Toast.LENGTH_SHORT).show();
                return;
            }

            if (correcto) {
                File fichero = new File(Environment.getExternalStorageDirectory(), file.getText().toString());
                if (m.disponibleEscritura()) {
                    web.saveWebArchive(fichero.getAbsolutePath());
                    Toast.makeText(ConexionAsincrona.this, "Fichero guardado correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error al guardar el fichero , la tarjeta SD no existe", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(ConexionAsincrona.this, "La página no se cargó correctamente", Toast.LENGTH_SHORT).show();
            }
        }


    }

    ProgressDialog progreso;

    public void androidAsincrono() {
        String texto = direccion.getText().toString();
        AsyncHttpClient client = new AsyncHttpClient();
        final ProgressDialog progreso = new ProgressDialog(ConexionAsincrona.this);
        client.get(direccion.getText().toString(), new TextHttpResponseHandler() {
                    @Override
                    public void onStart() {                  // called before request is started

                        progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progreso.setMessage("Conectando . . .");
                        progreso.setCancelable(true);
                        progreso.show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String response) {
                        // called when response HTTP status is "200 OK"
                        progreso.dismiss();
                        fin = System.currentTimeMillis();
                        web.loadDataWithBaseURL(null, response, "text/html", "UTF-8", null);
                        tiempo.setText("Duración: " + String.valueOf(fin - inicio) + " milisegundos");
                        correcto = true;
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String response, Throwable t) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        progreso.dismiss();
                        fin = System.currentTimeMillis();
                        web.loadDataWithBaseURL(null, t.getMessage() + "\n" + response, "text/html", "UTF-8", null);
                        tiempo.setText("Duración: " + String.valueOf(fin - inicio) + " milisegundos");
                        correcto = false;
                    }
                }
        );
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(ConexionAsincrona.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    public class TareaAsincrona extends AsyncTask<String, Integer, Resultado> {
        private ProgressDialog progreso;
        private Context contexto;

        public TareaAsincrona(Context contexto) {
            this.contexto = contexto;
        }

        protected void onPreExecute() {
            progreso = new ProgressDialog(ConexionAsincrona.this);
            progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progreso.setMessage("Conectando . . .");
            progreso.setCancelable(true);
            progreso.show();
        }

        protected Resultado doInBackground(String... cadena) {
            Resultado resultado;
            try {
                int i = 1;
                this.publishProgress(i++);
                if (cadena[0].equals(JAVA))
                    resultado = Conexion.conectarJava(cadena[1]);
                else
                    resultado = Conexion.conectarApache(cadena[1]);
            } catch (Exception e) {
                Log.e("HTTP", e.getMessage(), e);
                resultado = null;
                cancel(true);
            }
            return resultado;
        }

        protected void onPostExecute(Resultado resultado) {
            progreso.dismiss();

            fin = System.currentTimeMillis();
            if (resultado.getCodigo()) {
                web.loadDataWithBaseURL(null, resultado.getContenido(), "text/html", "UTF-8", null);
                correcto = true;
            } else {
                web.loadDataWithBaseURL(null, resultado.getMensaje(), "text/html", "UTF-8", null);
                correcto = false;
            }
            tiempo.setText("Duración: " + String.valueOf(fin - inicio) + " milisegundos");
        }

        protected void onCancelled() {
            progreso.dismiss();
            web.loadDataWithBaseURL(null, "cancelado", "text/html", "UTF-8", null);
        }

        protected void onProgressUpdate(Integer... progress) {
            progreso.setMessage("Conectando " + Integer.toString(progress[0]));
        }
    }
}
