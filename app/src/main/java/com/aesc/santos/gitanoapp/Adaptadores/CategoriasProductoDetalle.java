package com.aesc.santos.gitanoapp.Adaptadores;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.Replaceable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aesc.santos.gitanoapp.Entidades.ProductosVo;
import com.aesc.santos.gitanoapp.R;
import com.aesc.santos.gitanoapp.Utilidades.Utilidades;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Android on 2/5/2018.
 */

public class CategoriasProductoDetalle extends RecyclerView.Adapter<CategoriasProductoDetalle.ProductosViewHolder> implements View.OnClickListener {

    private static final String TAG = "CategoriasProductoDetal";

    ArrayList<ProductosVo> listaProductos;
    private Context context;
    private View.OnClickListener listener;

    //Control de Fragment a cargar.
    private boolean producto_o_favorito;

    public CategoriasProductoDetalle(ArrayList<ProductosVo> listaProductos, Context context, boolean producto_o_favorito) {
        this.listaProductos = listaProductos;
        this.context = context;
        this.listener = listener;
        this.producto_o_favorito = producto_o_favorito;
    }//


    @Override
    public ProductosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_produtos_detalle, parent,false);
        view.setOnClickListener(this);
        return new ProductosViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ProductosViewHolder holder, final int position) {


        holder.txtNombre.setText(listaProductos.get(position).getNombre());
        holder.txtDescripcion.setText(listaProductos.get(position).getDescripcion());
        holder.txtPrecio.setText(listaProductos.get(position).getPrecio() + " Puntos");

        Picasso.with(context).load(listaProductos.get(position).getImage_url()).fit().error(R.mipmap.ic_launcher).into(holder.foto);
        holder.meEncanta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String clave = listaProductos.get(position).getNombre();//clave sharedpreferences

                //region INSTANCIA SHAREDPREFERENCES

                SharedPreferences sharedPreferences = context.getSharedPreferences(Utilidades.SHARED_NAME, context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();


                //endregion
                //region IF Fragmento Productos
                if (producto_o_favorito == true) {
                    try {
                        //region ARRAY VALORES CARDVIEW
                        String[] valores = new String[]{
                                clave,
                                listaProductos.get(position).getPrecio(),
                                listaProductos.get(position).getImage_url()};
                        //endregion

                        //region Junta los valores en un solo CSV singular
                        String valorConjunto = "";
                        for (int i = 0; i < valores.length; i++) {
                            if (i == 0)
                                valorConjunto += valores[i];
                            else
                                valorConjunto += "," + valores[i];
                        }
                        //endregion

                        String cadena = sharedPreferences.getString(Utilidades.SHARED_LIST_NAMES, "") + clave + ",";
                        editor.remove(Utilidades.SHARED_LIST_NAMES);
                        editor.commit();

                        editor.putString(Utilidades.SHARED_LIST_NAMES, cadena);
                        editor.commit();
                        //-----------------------------WATCH JOSH ----------------------------------------//
                        editor.putString(clave,valorConjunto);
                        editor.commit();
                        Toast.makeText(context, "Agregado a favoritos!!", Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        System.out.println(e.getMessage());

                    }
                }
                //endregion
                //region ELSE IF FRAGMENTO FAVORITOS
                else if (producto_o_favorito == false) {
                    try {

                        Utilidades utilidades = new Utilidades();

                        String cadena = sharedPreferences.getString(Utilidades.SHARED_LIST_NAMES,"");

                       String[] sharedList =  utilidades.separar(cadena);

                        String nuevoShared = "";
                        for (String i:sharedList) {
                            if (i.equals(clave)) i = "";
                            else nuevoShared += i+",";
                        }

                        editor.remove(Utilidades.SHARED_LIST_NAMES);
                        editor.commit();
                        editor.putString(Utilidades.SHARED_LIST_NAMES,nuevoShared);

                        editor.remove(clave);
                        editor.commit();
                        Toast.makeText(context, "Eliminado de Favoritos!", Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }

                }
                //endregion

            }
        });

    }

    @Override
    public void onViewAttachedToWindow(ProductosViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            animateCircularReveal(holder.itemView);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void animateCircularReveal(View view){
        int centerX = 0;
        int centerY = 0;
        int startRadius = 0;
        int endRadius = Math.max(view.getWidth(), view.getHeight());
        Animator animaton = ViewAnimationUtils.createCircularReveal(view,centerX,centerY,startRadius,endRadius);
        view.setVisibility(View.VISIBLE);
        animaton.start();
    }


    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener!= null){
            listener.onClick(view);
        }
    }

    public class ProductosViewHolder extends RecyclerView.ViewHolder {

        TextView txtNombre, txtDescripcion, txtPrecio;
        ImageView foto, meEncanta;

        public ProductosViewHolder(View itemView) {
            super(itemView);

            txtNombre = itemView.findViewById(R.id.tvNombre);
            txtDescripcion = itemView.findViewById(R.id.tvDescripcion);
            txtPrecio = itemView.findViewById(R.id.precio);
            foto = itemView.findViewById(R.id.idImage);
            meEncanta = itemView.findViewById(R.id.favoritosCora);
        }
    }
}
