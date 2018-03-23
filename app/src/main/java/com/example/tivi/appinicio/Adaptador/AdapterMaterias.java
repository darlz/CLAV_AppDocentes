package com.example.tivi.appinicio.Adaptador;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import com.example.tivi.appinicio.BaseDatos.baseDatos;
import com.example.tivi.appinicio.InsertParamtr;
import com.example.tivi.appinicio.Modelo.Materia;
import com.example.tivi.appinicio.R;
import com.example.tivi.appinicio.Relacionando.ListarRelacion;
import com.example.tivi.appinicio.Relacionando.Relacion_Mater_Alum;

import java.util.List;

/**
 * Created by Tivi on 18/03/2018.
 */

//Un RecyclerView se apoyará también en un adaptador para
// trabajar con nuestros datos, en este caso un adaptador
// que herede de la clase RecyclerView.Adapter.
public class AdapterMaterias extends RecyclerView.Adapter<AdapterMaterias.ViewHolder>
{

    private List<Materia> ListadoMaterias;
    private Context mContext;
    private RecyclerView mRecyclerV;


    // Proporcionar una referencia a las vistas para cada elemento de datos
    // Los elementos de datos complejos pueden necesitar más de una vista por artículo, y
    // proporciona acceso a todas las vistas para un elemento de datos en un titular de vista
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView materiaNameTxtV;


        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            materiaNameTxtV = (TextView) v.findViewById(R.id.txt_item_nombre_materia);
        }
    }

    //constructor
    public AdapterMaterias(List<Materia> myDataset, Context context, RecyclerView recyclerView) {
        ListadoMaterias = myDataset;
        mContext = context;
        mRecyclerV = recyclerView;
    }

    // Crear nuevas vistas (invocadas por el administrador de diseño)
    @Override
    public AdapterMaterias.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //CREAMOS UNA NUEVA VISTA
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //INFLAMOS LA VISTA
        View v = inflater.inflate(R.layout.items_materia, parent, false);
        // establecer el tamaño de la vista, los márgenes, los paddings y los parámetros de diseño
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Reemplazamos el contenido de una vista (invocada por el administrador de diseño)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - obtener elemento de su conjunto de datos en esta posición
        // - reemplaza el contenido de la vista con ese elemento

        final Materia materia = ListadoMaterias.get(position);
        holder.materiaNameTxtV.setText(materia.getNombreMateria());

        //escuchar clic de diseño de vista única
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Escoge una opcion");
                builder.setMessage("Actualizar o Eliminar Materia?");

                builder.setPositiveButton("Crear Parametro", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        addParam(materia.getIdMateria(), materia.getNombreMateria());

                        //go to update activity
                        // goToUpdateActivity(materia.getIdEstudiante());

                    }
                });
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
                                dbHelper.EliminarRegistroMaterias(materia.getIdMateria(), mContext);

                                ListadoMaterias.remove(position);
                                mRecyclerV.removeViewAt(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, ListadoMaterias.size());
                                notifyDataSetChanged();
                                dialogo1.dismiss();
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
                builder.setNegativeButton("Ver Alumnos", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addAlumno(materia.getIdMateria(), materia.getNombreMateria());
                        //dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });


    }

   /* private void goToUpdateActivity(int personId){
        Intent goToUpdate = new Intent(mContext, ActualizarDatosPersona.class);
        goToUpdate.putExtra("USER_ID", personId);
        mContext.startActivity(goToUpdate);
    }*/

   private void addAlumno(int materiaId, String name){
        Intent goToUpdate = new Intent(mContext, Relacion_Mater_Alum.class);
       goToUpdate.putExtra("MATERIA_ID", materiaId);
       goToUpdate.putExtra("MATERI_NAME", name);
        mContext.startActivity(goToUpdate);
    }

    private void addParam(int materiaId, String name){
        Intent goToUpdate = new Intent(mContext, InsertParamtr.class);
        goToUpdate.putExtra("MATERIA_ID", materiaId);
        goToUpdate.putExtra("MATERI_NAME", name);

        mContext.startActivity(goToUpdate);
    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return ListadoMaterias.size();
    }



}
