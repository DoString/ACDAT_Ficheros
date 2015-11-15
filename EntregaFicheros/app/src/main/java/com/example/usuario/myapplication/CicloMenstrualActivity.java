package com.example.usuario.myapplication;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CicloMenstrualActivity extends Activity implements View.OnClickListener {

    private Button anadir;
    private EditText ciclo;
    private DatePicker fecha;
    private TextView desde, hasta, medio1, medio2,diaFertil;
    private Date ultimoDiaR, fechaPrimerDiaF, fechaUltimoDiaF;
    private String tmpFePrimerF, tmpFeUltiF;
    private int cicloMenstrual, primerDiaF, ultimoDiaF;
    private SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd-MM-yyyy");
    private Memoria miMemo = null;
    private String datosGuardar;
    private String nombreFi = "DiasFertiles.txt";
    private final static String COD = "UTF-8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ciclo_menstrual);
        inicializar();
    }

    @Override
    public void onClick(View v) {
        if (v == anadir) {
            RealizarOperaciones();
        }
    }

    private void inicializar() {
        anadir = (Button) findViewById(R.id.btnAdd);
        ciclo = (EditText) findViewById(R.id.edtCiclo);
        fecha = (DatePicker) findViewById(R.id.datFecha);
        desde = (TextView) findViewById(R.id.dia1);
        hasta = (TextView) findViewById(R.id.dia4);
        medio1 = (TextView) findViewById(R.id.dia3);
        medio2 = (TextView) findViewById(R.id.dia2);
        diaFertil = (TextView) findViewById(R.id.diaFertil);

        anadir.setOnClickListener(this);
        miMemo = new Memoria(this);
    }

    private void RealizarOperaciones() {
        try {
            int duracion = Integer.parseInt(ciclo.getText().toString());
            if (duracion < 20 || duracion > 30) {
                Toast.makeText(this, "El ciclo tiene que ser entre 20 y 30 dias inclusives", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "El valor de duración de ciclo es incorrecto", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ciclo.length() == 0) {
            Toast.makeText(this, "Debes introducir un valor de duración de ciclo", Toast.LENGTH_SHORT).show();
            return;
        }

        String ultimoDiaRegla = fecha.getDayOfMonth() + "-" + (fecha.getMonth() + 1) + "-" + fecha.getYear();
        //ultimoDiaRegla =  //SE LE SUMA UN MES MAS POR Q SALE CON UN MES MENOS, NO SE POR QUE..

        //FORMATEAMOS FECHA ULTIMA REGLA
        try {
            ultimoDiaR = formatoDelTexto.parse(ultimoDiaRegla);
        } catch (ParseException e) {
            //nunca va a ocurrir
        }
        cicloMenstrual = Integer.parseInt(ciclo.getText().toString()); //GUARDAMOS LOS CICLOS MENSTRUALES

        int mitadCiclo = cicloMenstrual % 2 == 0 ? (cicloMenstrual / 2) - 1 : (cicloMenstrual / 2) + 1;

        Calendar dia_1 = Calendar.getInstance();
        Calendar dia_2 = Calendar.getInstance();
        Calendar dia_3 = Calendar.getInstance();
        Calendar dia_4 = Calendar.getInstance();

        CalcularDiasFertiles(mitadCiclo, dia_1, dia_2, dia_3, dia_4);
        setDiasFertiles(dia_1, dia_2, dia_3, dia_4);

        if (HoyEsDiaFertil(dia_1, dia_2, dia_3, dia_4))
            diaFertil.setText("Hoy es día fértil");
        else
            diaFertil.setText("Hoy no es día fértil");
        GuardarMemoriaExterna(dia_1, dia_4);
    }

    private void GuardarMemoriaExterna(Calendar dia_1, Calendar dia_4) {
        tmpFePrimerF = formatoDelTexto.format(dia_1.getTime());
        tmpFeUltiF = formatoDelTexto.format(dia_4.getTime());

        datosGuardar = "Desde el " + tmpFePrimerF + " hasta el " + tmpFeUltiF;

        if (miMemo.disponibleEscritura()) {
            if (miMemo.escribirExterna(nombreFi, datosGuardar, true, COD)) {
                Toast.makeText(getApplicationContext(), "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(), "No se han podido guardar los datos", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getApplicationContext(), "Error al acceder a la memoria Sdcard", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean HoyEsDiaFertil(Calendar dia_1, Calendar dia_2, Calendar dia_3, Calendar dia_4) {
        Calendar hoy = Calendar.getInstance();
        hoy.setTime(new Date());
        if((hoy.get(Calendar.YEAR) == dia_1.get(Calendar.YEAR) &&
                hoy.get(Calendar.MONTH) == dia_1.get(Calendar.MONTH) &&
                hoy.get(Calendar.DAY_OF_MONTH) == dia_1.get(Calendar.DAY_OF_MONTH)) ||
                (hoy.get(Calendar.YEAR) == dia_2.get(Calendar.YEAR) &&
                        hoy.get(Calendar.MONTH) == dia_2.get(Calendar.MONTH) &&
                        hoy.get(Calendar.DAY_OF_MONTH) == dia_2.get(Calendar.DAY_OF_MONTH)) ||
                (hoy.get(Calendar.YEAR) == dia_3.get(Calendar.YEAR) &&
                        hoy.get(Calendar.MONTH) == dia_3.get(Calendar.MONTH) &&
                        hoy.get(Calendar.DAY_OF_MONTH) == dia_3.get(Calendar.DAY_OF_MONTH)) ||
                (hoy.get(Calendar.YEAR) == dia_4.get(Calendar.YEAR) &&
                        hoy.get(Calendar.MONTH) == dia_4.get(Calendar.MONTH) &&
                        hoy.get(Calendar.DAY_OF_MONTH) == dia_4.get(Calendar.DAY_OF_MONTH)))
            return true;
        return false;
    }

    private void setDiasFertiles(Calendar dia_1, Calendar dia_2, Calendar dia_3, Calendar dia_4) {
        desde.setText(formatoDelTexto.format(dia_1.getTime()));
        medio1.setText(formatoDelTexto.format(dia_2.getTime()));
        medio2.setText(formatoDelTexto.format(dia_3.getTime()));
        hasta.setText(formatoDelTexto.format(dia_4.getTime()));
    }

    private void CalcularDiasFertiles(int mitadCiclo, Calendar dia_1, Calendar dia_2, Calendar dia_3, Calendar dia_4) {
        dia_1.setTime(ultimoDiaR);
        dia_1.add(Calendar.DAY_OF_YEAR, mitadCiclo);
        dia_1.add(Calendar.DAY_OF_YEAR, -2);

        dia_2.setTime(ultimoDiaR);
        dia_2.add(Calendar.DAY_OF_YEAR, mitadCiclo);
        dia_2.add(Calendar.DAY_OF_YEAR, -1);

        dia_3.setTime(ultimoDiaR);
        dia_3.add(Calendar.DAY_OF_YEAR, mitadCiclo);

        dia_4.setTime(ultimoDiaR);
        dia_4.add(Calendar.DAY_OF_YEAR, mitadCiclo);
        dia_4.add(Calendar.DAY_OF_YEAR, 1);
    }
}
