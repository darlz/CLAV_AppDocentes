package com.example.tivi.appinicio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tivi.appinicio.Adaptador.AdapterParametros;
import com.example.tivi.appinicio.BaseDatos.baseDatos;
import com.example.tivi.appinicio.Modelo.Parametro;

import java.util.ArrayList;

public class ListaParametro extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private baseDatos dbHelper;
    private AdapterParametros adapter;
    private String filter = "";

    int fkMateria;
    TextView promedioFinConPorcent, PasaNoPasa;
    Button botonPromedioFinal;




    private ArrayList<Parametro> parametro = new ArrayList<Parametro>();

    ArrayList<Double> listaParametro = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_parametro);

        Bundle b = getIntent().getExtras();

        if(b!=null)
        {
            fkMateria = b.getInt("MATERIA_ALUMNO_ID");
        }

        //initialize the variables
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerViewParametros);
        promedioFinConPorcent = (TextView) findViewById(R.id.PromedioFinalPArametrosConPorcentaje);
        PasaNoPasa = (TextView) findViewById(R.id.PasaNoPasa);

        botonPromedioFinal = (Button) findViewById(R.id.botonPromedioFinal);




        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //populate recyclerview
        populaterecyclerView(filter);

        botonPromedioFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConsultarNotas();
                SumaPromediada();
            }
        });


    }

    private void populaterecyclerView(String filter){
        dbHelper = new baseDatos(this);
        adapter = new AdapterParametros(dbHelper.listaParametros(fkMateria), this, mRecyclerView);
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    public void ConsultarNotas() {
        dbHelper = new baseDatos(this);
        parametro.addAll(dbHelper.listaParametros(fkMateria));

        for (int i = 0; i < parametro.size(); i++)
        {
            listaParametro.add(parametro.get(i).getNotaFinal());
            //double notes = listaParametro.get(i);
        }
    }

    public void SumaPromediada()
    {
        double notes=0, n=0, resultado=0;
        for (int i = 0; i < listaParametro.size(); i++)
        {
            notes += listaParametro.get(i)*parametro.get(i).getValorPorcentual();
        }

        resultado = notes/listaParametro.size();
        promedioFinConPorcent.setText(String.valueOf(resultado));

        if(resultado>=7){
            PasaNoPasa.setText("APROBADO");
        }else if (resultado>5.6 && resultado<7)
        {
            PasaNoPasa.setText("SUPLETORIO");
        }else if(resultado<5.6){
            PasaNoPasa.setText("REPROBADO");
        }

        //ActualizarParametro(notes,resultado);


    }

}
