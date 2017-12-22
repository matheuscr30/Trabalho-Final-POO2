package com.matheus.magazinehenrique.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.matheus.magazinehenrique.R;
import com.matheus.magazinehenrique.activity.MainActivity;
import com.matheus.magazinehenrique.adapter.CategoriasAdapter;
import com.matheus.magazinehenrique.config.ConfiguracaoFirebase;
import com.matheus.magazinehenrique.dao.CategoriaDAO;
import com.matheus.magazinehenrique.model.Categoria;
import com.matheus.magazinehenrique.tools.SimpleCallback;

import java.util.ArrayList;

/**
 * Created by matheus on 15/12/17.
 */

public class CategoriasFragment extends Fragment{
    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Categoria> categorias;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListenerCategorias;

    public CategoriasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Instanciar Array
        categorias = new ArrayList<>();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categorias, container, false);

        //Monta ListView e Adapter
        listView = (ListView)view.findViewById(R.id.lv_categorias);
        adapter = new CategoriasAdapter(getActivity(), categorias);
        listView.setAdapter(adapter);

        /*CategoriaDAO categoriaDAO = new CategoriaDAO();
        categoriaDAO.buscarCategorias(new SimpleCallback<ArrayList<Categoria>>() {
            @Override
            public void callback(ArrayList<Categoria> data) {
                categorias.clear();
                categorias = data;
                adapter.notifyDataSetChanged();
            }
        });*/

        databaseReference = ConfiguracaoFirebase.getDatabaseReference()
                .child("categorias");

        //Listener para recuperar contatos
        valueEventListenerCategorias = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Limpar lista
                categorias.clear();

                //Listar Contatos
                for(DataSnapshot dados : dataSnapshot.getChildren() ){

                    Categoria categoria = dados.getValue( Categoria.class );
                    categorias.add(categoria);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(valueEventListenerCategorias);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Categoria categoria = categorias.get(i);
                getActivity().getIntent().putExtra("categoria", categoria);
                ((MainActivity)getActivity()).mudaViewPagerPadrao();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener( valueEventListenerCategorias );
        Log.i("ValueEventListener", "onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReference.removeEventListener( valueEventListenerCategorias );
        Log.i("ValueEventListener", "onStop");
    }
}
