package com.matheus.magazinehenrique.dao;

import com.google.firebase.database.DatabaseReference;
import com.matheus.magazinehenrique.config.ConfiguracaoFirebase;
import com.matheus.magazinehenrique.model.Cliente;
import com.matheus.magazinehenrique.model.Compra;

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
}
