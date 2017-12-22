package com.matheus.magazinehenrique.dao;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.matheus.magazinehenrique.config.ConfiguracaoFirebase;
import com.matheus.magazinehenrique.model.Carrinho;
import com.matheus.magazinehenrique.model.Produto;
import com.matheus.magazinehenrique.tools.SimpleCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by matheus on 21/12/17.
 */

public class ProdutoDAO {
    private DatabaseReference databaseReference;

    public ProdutoDAO() {
        this.databaseReference = ConfiguracaoFirebase.getDatabaseReference();
    }

    public boolean atualizarProdutos(ArrayList<Produto>produtos){
        try{
            Map<String, Object> childUpdates = new HashMap<>();

            for(Produto produto : produtos){
                childUpdates.put("/produtos/" + produto.getReferencia(), produto);
            }

            Log.d("RUIM", childUpdates.toString());
            databaseReference.updateChildren(childUpdates);

            return true;
        } catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

    public void buscarProdutos(@NonNull final SimpleCallback<ArrayList<Produto>> finishedCallback){
        try{
            DatabaseReference aux = databaseReference.child("produtos");
            aux.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<Produto>produtos = new ArrayList<Produto>();

                    for (DataSnapshot dados : dataSnapshot.getChildren()) {

                        Produto produto = dados.getValue(Produto.class);
                        String genero = produto.getGenero();
                        if (genero.equals("masculino")) {
                            produto.setGenero("M" + genero.substring(1));
                        } else if (genero.equals("feminino")) {
                            produto.setGenero("F" + genero.substring(1));
                        }
                        produto.iniciaProduto();
                        produtos.add(produto);
                    }

                    finishedCallback.callback(produtos);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e){
            e.getMessage();
        }
    }

    public void buscarProdutoPorNome(final String nome, final @NonNull SimpleCallback<ArrayList<Produto>> finishedCallback){
        try{
            DatabaseReference aux = databaseReference.child("produtos");
            aux.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<Produto>produtos = new ArrayList<Produto>();

                    for (DataSnapshot dados : dataSnapshot.getChildren()) {

                        Produto produto = dados.getValue(Produto.class);
                        String genero = produto.getGenero();
                        if (genero.equals("masculino")) {
                            produto.setGenero("M" + genero.substring(1));
                        } else if (genero.equals("feminino")) {
                            produto.setGenero("F" + genero.substring(1));
                        }

                        produto.iniciaProduto();
                        if(produto.getNome().contains(nome))
                            produtos.add(produto);
                    }

                    finishedCallback.callback(produtos);
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
