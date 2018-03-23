package com.example.tivi.appinicio.Adaptador;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tivi.appinicio.AddNota.InsertNote;
import com.example.tivi.appinicio.BaseDatos.baseDatos;
import com.example.tivi.appinicio.Modelo.Materia;
import com.example.tivi.appinicio.Modelo.Parametro;
import com.example.tivi.appinicio.R;
import com.example.tivi.appinicio.Relacionando.Relacion_Mater_Alum;

import java.util.List;

/**
 * Created by Tivi on 20/03/2018.
 */


//Un RecyclerView se apoyará también en un adaptador para
// trabajar con nuestros datos, en este caso un adaptador
// que herede de la clase RecyclerView.Adapter.
public class AdapterParametros extends RecyclerView.Adapter<AdapterParametros.ViewHolder>
{
    private List<Parametro> ListadoParametros;
    private Context mContext;
    private RecyclerView mRecyclerV;



    // Proporcionar una referencia a las vistas para cada elemento de datos
    // Los elementos de datos complejos pueden necesitar más de una vista por artículo, y
    // proporciona acceso a todas las vistas para un elemento de datos en un titular de vista
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nombreParametro;

        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            nombreParametro = (TextView) v.findViewById(R.id.txt_item_parametro_nombre);
        }
    }

    //constructor
    public AdapterParametros(List<Parametro> myDataset, Context context, RecyclerView recyclerView) {
        ListadoParametros = myDataset;
        mContext = context;
        mRecyclerV = recyclerView;
    }

    // Crear nuevas vistas (invocadas por el administrador de diseño)
    @Override
    public AdapterParametros.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //CREAMOS UNA NUEVA VISTA
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //INFLAMOS LA VISTA
        View v = inflater.inflate(R.layout.item_parametro, parent, false);
        // establecer el tamaño de la vista, los márgenes, los paddings y los parámetros de diseño
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Reemplazamos el contenido de una vista (invocada por el administrador de diseño)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - obtener elemento de su conjunto de datos en esta posición
        // - reemplaza el contenido de la vista con ese elemento

        final Parametro parametro = ListadoParametros.get(position);
        holder.nombreParametro.setText(parametro.getNombreParametro());



        //escuchar clic de diseño de vista única
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CREAMOS UN DIALOGO DE OPCIONES DE PARAMETROS
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Escoge una opcion");

                builder.setNeutralButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        android.support.v7.app.AlertDialog.Builder dialogo1 = new android.support.v7.app.AlertDialog.Builder(mContext);
                        dialogo1.setTitle("Importante");
                        dialogo1.setMessage("¿ Esta seguro de borrar este item ?");
                        dialogo1.setCancelable(false);
                        dialogo1.setPositiveButton("si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                baseDatos dbHelper = new baseDatos(mContext);
                                dbHelper.EliminarRegistroParametros(parametro.getIdParametro(), mContext);

                                ListadoParametros.remove(position);
                                mRecyclerV.removeViewAt(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, ListadoParametros.size());
                                notifyDataSetChanged();
                            }
                        });
                        dialogo1.setNegativeButton("no", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                dialogo1.dismiss();
                            }
                        });
                        dialogo1.show();
                    }
                });

                builder.setNegativeButton("Agregar nota", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Enviar_Datos_De_Parametro_A_Insertar_Notas(parametro.getIdParametro(), parametro.getNombreParametro(),parametro.getValorPorcentual(), parametro.getFk_Materia(), parametro.getNotaFinal(), parametro.getPromedioParametro());
                        //dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }
//ENVIAMOS DATOS DEL PARAMETRO EN UN INTENT PARA RECIBIR EN LA CLASE INSERTNOTE(INSERTAR NOTAS)
    private void Enviar_Datos_De_Parametro_A_Insertar_Notas(int materiaId, String NombreParam, double valPorcent, int fkMateria, double notaFin, double Promedio){
        Intent goToUpdate = new Intent(mContext, InsertNote.class);
        goToUpdate.putExtra("PARAMETRO_ID", materiaId);
        goToUpdate.putExtra("NOMBREARAM", NombreParam);
        goToUpdate.putExtra("VALORPORC", valPorcent);
        goToUpdate.putExtra("FKMATERIA", fkMateria);
        goToUpdate.putExtra("NOTAFINAL", notaFin);
        goToUpdate.putExtra("PROMEDIOFINALCONPORCENTAJE", Promedio);

        mContext.startActivity(goToUpdate);
    }
    // Devuelve el tamaño de tu conjunto de datos (invocado por el administrador de diseño)
    @Override
    public int getItemCount() {
        return ListadoParametros.size();
    }


}
