package com.matheus.magazinehenrique.dao;

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

    public boolean salvarCliente(Cliente cliente){
        try{
            databaseReference = databaseReference.child("users")
                    .child(cliente.getEmail());
            databaseReference.setValue(cliente);
            return true;
        } catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

    public boolean excluirCliente(Cliente cliente){
        try{
            databaseReference = databaseReference.child("users")
                    .child(cliente.getEmail());
            databaseReference.removeValue();
            return true;
        } catch (Exception e){
            System.out.println(e);
            return false;
        }
    }
}