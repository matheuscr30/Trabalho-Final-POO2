package com.matheus.magazinehenrique.dao;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.matheus.magazinehenrique.config.ConfiguracaoFirebase;
import com.matheus.magazinehenrique.model.Cliente;
import com.matheus.magazinehenrique.tools.SimpleCallback;

/**
 * Created by matheus on 09/10/17.
 */

public class ClienteDAO {

    private DatabaseReference databaseReference;

    public ClienteDAO() {
        this.databaseReference = ConfiguracaoFirebase.getDatabaseReference();
    }

    public boolean salvarCliente(Cliente cliente, String uid){
        try{
            DatabaseReference databaseReferenceAux = databaseReference.child("usuarios")
                    .child(cliente.getCPF());
            databaseReferenceAux.setValue(cliente);

            DatabaseReference databaseReferenceAux2 = databaseReference.child("cpfs")
                    .child(uid);
            databaseReferenceAux2.setValue(cliente.getCPF());
            return true;
        } catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

    public boolean excluirCliente(Cliente cliente){
        try{
            databaseReference = databaseReference.child("usuarios")
                    .child(cliente.getCPF());
            databaseReference.removeValue();
            return true;
        } catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

    public void buscarCliente(String cpfUsuario, @NonNull final SimpleCallback<Cliente> finishedCallback){
        try{
            DatabaseReference aux = databaseReference.child("usuarios").child(cpfUsuario);
            aux.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Cliente cliente = dataSnapshot.getValue(Cliente.class);
                    finishedCallback.callback(cliente);
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