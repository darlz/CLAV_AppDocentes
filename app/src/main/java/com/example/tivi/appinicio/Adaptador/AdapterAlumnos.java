package com.example.tivi.appinicio.Adaptador;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tivi.appinicio.BaseDatos.baseDatos;
import com.example.tivi.appinicio.Modelo.Estudiante;
import com.example.tivi.appinicio.R;

import java.util.List;

/**
 * Created by Tivi on 17/03/2018.
 */

//Un RecyclerView se apoyará también en un adaptador para
// trabajar con nuestros datos, en este caso un adaptador
// que herede de la clase RecyclerView.Adapter.
public class AdapterAlumnos extends RecyclerView.Adapter<AdapterAlumnos.ViewHolder>
{
    private List<Estudiante> ListaDeAlumnos;
    private Context mContext;
    private RecyclerView mRecyclerV;




    // Proporcionar una referencia a las vistas para cada elemento de datos
    // Los elementos de datos complejos pueden necesitar más de una vista por artículo, y
    // proporciona acceso a todas las vistas para un elemento de datos en un titular de vista
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nombreAlumno;

        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            nombreAlumno = (TextView) v.findViewById(R.id.txt_item_nombre);
        }
    }

    //constructor
    public AdapterAlumnos(List<Estudiante> myDataset, Context context, RecyclerView recyclerView) {
        ListaDeAlumnos = myDataset;
        mContext = context;
        mRecyclerV = recyclerView;
    }

    // Crear nuevas vistas (invocadas por el administrador de diseño)
    @Override
    public AdapterAlumnos.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //CREAMOS UNA NUEVA VISTA
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //INFLAMOS LA VISTA
        View v = inflater.inflate(R.layout.items_alumnos, parent, false);
        // establecer el tamaño de la vista, los márgenes, los paddings y los parámetros de diseño
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Reemplazamos el contenido de una vista (invocada por el administrador de diseño)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - obtener elemento de su conjunto de datos en esta posición
        // - reemplaza el contenido de la vista con ese elemento

        final Estudiante person = ListaDeAlumnos.get(position);
        holder.nombreAlumno.setText(person.getNombre()+"  "+ person.getApellido());

        //escuchar clic de diseño de vista única
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Elegir opción");

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
                                dbHelper.EliminarRegistroPersonas(person.getIdEstudiante(), mContext);

                                ListaDeAlumnos.remove(position);
                                mRecyclerV.removeViewAt(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, ListaDeAlumnos.size());
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

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }

    // Devuelve el tamaño de tu conjunto de datos (invocado por el administrador de diseño)
    @Override
    public int getItemCount() {
        return ListaDeAlumnos.size();
    }
}
