package com.matheus.magazinehenrique.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.matheus.magazinehenrique.R;
import com.matheus.magazinehenrique.model.Categoria;

import java.util.ArrayList;

/**
 * Created by matheus on 15/12/17.
 */

public class CategoriasAdapter extends ArrayAdapter<Categoria> {
    private ArrayList<Categoria> categorias;
    private Context context;

    public CategoriasAdapter(@NonNull Context c, @NonNull ArrayList<Categoria> objects) {
        super(c, 0, objects);
        this.context = c;
        this.categorias = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;

        if(categorias != null){

            //Inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Monta view a partir do xml
            view = inflater.inflate(R.layout.lista_categoria, parent, false);

            //Recupera elemento para exibicao
            TextView nome = (TextView)view.findViewById(R.id.tv_nome);
            ImageView icone = (ImageView)view.findViewById(R.id.imageViewCategorias);

            Categoria categoria = categorias.get(position);
            nome.setText(categoria.getNome());

            int drawable;
            switch (categoria.getNome_icone()){
                case "icone_bermudas":
                    drawable = R.drawable.icon_bermudas;
                    break;

                case "icone_calcas":
                    drawable = R.drawable.icon_calcas;
                    break;

                case "icone_camisas":
                    drawable = R.drawable.icon_camisas;
                    break;

                case "icone_camisetas":
                    drawable = R.drawable.icon_camisetas;
                    break;

                case "icone_saias":
                    drawable = R.drawable.icon_saias;
                    break;

                case "icone_vestidos":
                    drawable = R.drawable.icon_vestidos;
                    break;
                case "icone_todos":
                    drawable = R.drawable.icon_todos;
                    break;
                default:
                    drawable = R.drawable.ic_local_play;
            }
            icone.setImageResource(drawable);
        }

        return view;
    }
}
