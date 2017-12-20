package com.matheus.magazinehenrique.dao;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.matheus.magazinehenrique.config.ConfiguracaoFirebase;
import com.matheus.magazinehenrique.model.Cliente;

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
}