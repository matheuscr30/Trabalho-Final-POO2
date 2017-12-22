package com.matheus.magazinehenrique.dao;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.matheus.magazinehenrique.config.ConfiguracaoFirebase;
import com.matheus.magazinehenrique.model.Carrinho;
import com.matheus.magazinehenrique.tools.SimpleCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by matheus on 16/12/17.
 */

public class CarrinhoDAO {

    private DatabaseReference databaseReference;
    private Carrinho carrinhoFinal;

    public CarrinhoDAO() {
        this.databaseReference = ConfiguracaoFirebase.getDatabaseReference();
    }

    public boolean salvarCarrinho(Carrinho carrinho, String cpfUsuario){
        try{
            DatabaseReference databaseReferenceAux = databaseReference.child("carrinhos")
                    .child(cpfUsuario);
            databaseReferenceAux.setValue(carrinho);

            return true;
        } catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

    public boolean atualizarCarrinho(Carrinho carrinho, String cpfUsuario){
        try{
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/carrinhos/" + cpfUsuario, carrinho);

            databaseReference.updateChildren(childUpdates);

            return true;
        } catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

    public boolean removeCarrinho(String cpfUsuario){
        try{
            DatabaseReference databaseReferenceAux = databaseReference.child("carrinhos")
                    .child(cpfUsuario);
            databaseReferenceAux.removeValue();
            return true;
        } catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

    public void buscarCarrinho(String cpfUsuario, @NonNull final SimpleCallback<Carrinho> finishedCallback){
        try{
            DatabaseReference aux = databaseReference.child("carrinhos").child(cpfUsuario);
            aux.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Carrinho carrinho = dataSnapshot.getValue(Carrinho.class);
                    finishedCallback.callback(carrinho);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e){

        }
    }
}
