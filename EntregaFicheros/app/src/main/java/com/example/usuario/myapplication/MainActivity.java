package com.example.usuario.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button boton1, boton2, boton3, boton4,boton5, boton6;
    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boton1 = (Button) findViewById(R.id.btnPrimera);
        boton1.setOnClickListener(this);
        boton2 = (Button) findViewById(R.id.btnSegunda);
        boton2.setOnClickListener(this);
        boton3 = (Button) findViewById(R.id.btnTercera);
        boton3.setOnClickListener(this);
        boton4 = (Button) findViewById(R.id.btnCuarta);
        boton4.setOnClickListener(this);
        boton5 = (Button) findViewById(R.id.btnQuinta);
        boton5.setOnClickListener(this);
        boton6 = (Button) findViewById(R.id.btnSexta);
        boton6.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onClick(View v) {
        if (v == boton1) {
            i = new Intent(this, ActivityAgenda.class);
            this.startActivity(i);
        } else if (v == boton2) {
            i = new Intent(this, AlarmaActivity.class);
            this.startActivity(i);
        } else if (v == boton3) {
            i = new Intent(this, CicloMenstrualActivity.class);
            this.startActivity(i);
        } else if (v == boton4) {
            i = new Intent(this, ConexionAsincrona.class);
            this.startActivity(i);
        } else if (v == boton5) {
            i = new Intent(this, ManejoImagenes.class);
            this.startActivity(i);
        } else if (v == boton6) {
            i = new Intent(this, ConversorFTP.class);
            this.startActivity(i);
        }
    }
}
