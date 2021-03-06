package com.matheus.magazinehenrique.dao;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.matheus.magazinehenrique.config.ConfiguracaoFirebase;
import com.matheus.magazinehenrique.model.Categoria;
import com.matheus.magazinehenrique.tools.SimpleCallback;

import java.util.ArrayList;

/**
 * Created by matheus on 16/12/17.
 */

public class CategoriaDAO {

    private DatabaseReference databaseReference;

    public CategoriaDAO() {
        this.databaseReference = ConfiguracaoFirebase.getDatabaseReference();
    }

    public void buscarCategorias(final SimpleCallback<ArrayList<Categoria>> finishedCallback){
        try {
            DatabaseReference aux = databaseReference.child("categorias");
            aux.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<Categoria>categorias = new ArrayList<Categoria>();

                    for(DataSnapshot dados : dataSnapshot.getChildren() ){
                        Categoria categoria = dados.getValue( Categoria.class );
                        categorias.add(categoria);
                    }

                    finishedCallback.callback(categorias);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

