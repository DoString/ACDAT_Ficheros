package com.example.usuario.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
//http://pastebin.com/3Q3swcj6          http://pastebin.com/download.php?i=3Q3swcj6
public class ManejoImagenes extends AppCompatActivity implements View.OnClickListener{

    EditText texto;
    Button boton, sig, ant;
    ImageView imagen;
    AsyncHttpClient cliente;
    ArrayList<String> fotos;
    int indice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manejo_imagenes);
        texto = (EditText) findViewById(R.id.imgURl);
        boton = (Button) findViewById(R.id.btnCargar);
        boton.setOnClickListener(this);
        ant = (Button) findViewById(R.id.iant);
        ant.setOnClickListener(this);
        sig = (Button) findViewById(R.id.isig);
        sig.setOnClickListener(this);
        imagen = (ImageView) findViewById(R.id.Visor);
        cliente = new AsyncHttpClient();
        fotos = new ArrayList<String>();
        int indice = 0;
    }
    @Override
    public void onClick(View v) {
        final ProgressDialog progreso = new ProgressDialog(this);
        String url;
        if (v == boton) {
            url = texto.getText().toString();

            if (url.isEmpty()){
                Toast.makeText(ManejoImagenes.this, "Debes introducir una url primero", Toast.LENGTH_SHORT).show();
                return;
            }

            if (fotos.size() > 0){
                Picasso.with(this)
                        .load(fotos.get(0))
                        .error(R.drawable.error)
                        .into(imagen);
                return;
            }

            final Activity a = this;
            cliente.get(url, new FileAsyncHttpResponseHandler(this) {
                @Override
                public void onStart() {
                    super.onStart();
                    progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progreso.setMessage("Conectando . . .");
                    progreso.setCancelable(false);
                    progreso.show();
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                    progreso.dismiss();
                    Toast.makeText(a, "Se ha producido un error al acceder al fichero:\n" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onSuccess(int statusCode, Header[] headers, File response) {
                    progreso.dismiss();
                    //texto.setText("");
                    try {
                        /*InputStream file = openFileInput(response.getName());
                        InputStreamReader contenido = new InputStreamReader(file);
                        BufferedReader br = new BufferedReader(contenido);*/

                        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(response)));
                        String linea;
                        try {
                            while ((linea = br.readLine()) != null) {
                                //texto.append(linea);
                                fotos.add(linea);
                            }
                            br.close();
                            if (fotos.size() > 0) {
                                Picasso.with(a)
                                        .load(fotos.get(0))
                                        .into(imagen);
                            } else {
                                imagen.setImageResource(R.drawable.error);
                            }
                        } catch (IOException e) {
                            Log.d("ERROR", e.getMessage());
                        }

                    } catch (FileNotFoundException e) {
                        Log.d("ERROR", e.getMessage());
                    }
                }
            });
        } else if (v == ant) {
            if (fotos.size() > 0) {
                indice = (indice + fotos.size() - 1) % fotos.size();
                Picasso.with(this)
                        .load(fotos.get(indice))
                        .into(imagen);
            } else {
                imagen.setImageResource(R.drawable.error);
            }
        }else if (v == sig) {
            if (fotos.size() > 0) {
                indice = (indice + 1) % fotos.size();
                Picasso.with(this)
                        .load(fotos.get(indice))
                        .into(imagen);
            } else {
                imagen.setImageResource(R.drawable.error);
            }
        }
    }
}
