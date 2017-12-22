package com.matheus.magazinehenrique.dao;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.matheus.magazinehenrique.config.ConfiguracaoFirebase;
import com.matheus.magazinehenrique.model.Cliente;
import com.matheus.magazinehenrique.model.Compra;
import com.matheus.magazinehenrique.tools.SimpleCallback;

import java.util.ArrayList;

/**
 * Created by matheus on 20/12/17.
 */

public class CompraDAO {
    private DatabaseReference databaseReference;

    public CompraDAO() {
        this.databaseReference = ConfiguracaoFirebase.getDatabaseReference();
    }

    public String salvarCompra(Compra compra, String cpfUsuario){
        try{
            DatabaseReference databaseReferenceAux = databaseReference.child("compras")
                    .child(cpfUsuario);
            String key = databaseReferenceAux.push().getKey();
            compra.setChave(key);
            databaseReferenceAux = databaseReference.child("compras").child(cpfUsuario)
                    .child(key);
            databaseReferenceAux.setValue(compra);

            return key;
        } catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    public void buscarCompras(String cpfUsuario, @NonNull final SimpleCallback<ArrayList<Compra>> finishedCallback){
        try{
            DatabaseReference aux = databaseReference.child("compras")
                    .child(cpfUsuario);

            aux.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<Compra>compras = new ArrayList<Compra>();
                    for(DataSnapshot dados : dataSnapshot.getChildren()) {
                        Compra compra = dados.getValue(Compra.class);
                        compras.add(compra);
                    }
                    finishedCallback.callback(compras);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e){
            e.getMessage();
        }
    }
}
