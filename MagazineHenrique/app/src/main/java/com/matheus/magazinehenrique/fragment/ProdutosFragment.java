package com.matheus.magazinehenrique.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.matheus.magazinehenrique.R;
import com.matheus.magazinehenrique.activity.CarrinhoActivity;
import com.matheus.magazinehenrique.activity.MainActivity;
import com.matheus.magazinehenrique.adapter.CarrinhoAdapter;
import com.matheus.magazinehenrique.adapter.ProdutosAdapter;
import com.matheus.magazinehenrique.config.ConfiguracaoFirebase;
import com.matheus.magazinehenrique.dao.ProdutoDAO;
import com.matheus.magazinehenrique.model.Categoria;
import com.matheus.magazinehenrique.model.Produto;
import com.matheus.magazinehenrique.tools.SimpleCallback;

import java.util.ArrayList;

/**
 * Created by matheus on 15/12/17.
 */

public class ProdutosFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProdutosAdapter adapter;
    private ArrayList<Produto> produtos;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListenerProdutos;
    private ValueEventListener valueEventListenerProcura;
    private String categoriaAtual;
    private boolean carregou = false;

    public ProdutosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Instanciar Array
        produtos = new ArrayList<>();
        categoriaAtual = "Todos";

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_produtos, container, false);

        //Monta RecyclerView e Adapter
        recyclerView = (RecyclerView) view.findViewById(R.id.rvProdutos);
        GridLayoutManager glm = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(glm);
        adapter = new ProdutosAdapter(produtos, getActivity());
        recyclerView.setAdapter(adapter);

        databaseReference = ConfiguracaoFirebase.getDatabaseReference()
                .child("produtos");

        //Listener para recuperar contatos
        valueEventListenerProdutos = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Limpar lista
                produtos.clear();

                //Listar Contatos
                for (DataSnapshot dados : dataSnapshot.getChildren()) {

                    Produto produto = dados.getValue(Produto.class);

                    String genero = produto.getGenero();
                    if (genero.equals("masculino")) {
                        produto.setGenero("M" + genero.substring(1));
                    } else if (genero.equals("feminino")) {
                        produto.setGenero("F" + genero.substring(1));
                    }

                    String status = produto.getStatus();
                    produto.iniciaProduto();

                    produtos.add(produto);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        carregou = true;
        return view;
    }

    public void populaCategoria(String nomeCategoria) {
        if (nomeCategoria.equals("Todos")) {
            onStart();
        } else {
            Query query1 = databaseReference.orderByChild("tipo").equalTo(nomeCategoria);
            query1.addValueEventListener(valueEventListenerProdutos);
        }
    }

    //MESMO PROBLEMA DAS COMPRAS
    public void buscaPorNome(final String nome){
        /*final ProdutoDAO produtoDAO = new ProdutoDAO();
        produtoDAO.buscarProdutoPorNome(nome, new SimpleCallback<ArrayList<Produto>>() {
            @Override
            public void callback(ArrayList<Produto> data) {
                produtos.clear();
                produtos = data;
                adapter.notifyDataSetChanged();
            }
        });*/

        valueEventListenerProcura = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Limpar lista
                produtos.clear();

                //Listar Contatos
                for (DataSnapshot dados : dataSnapshot.getChildren()) {

                    Produto produto = dados.getValue(Produto.class);
                    String genero = produto.getGenero();
                    if (genero.equals("masculino")) {
                        produto.setGenero("M" + genero.substring(1));
                    } else if (genero.equals("feminino")) {
                        produto.setGenero("F" + genero.substring(1));
                    }

                    if(produto.getNome().contains(nome))
                        produtos.add(produto);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.addListenerForSingleValueEvent(valueEventListenerProcura);
    }

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (visible && isResumed()) {
            onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!getUserVisibleHint()) return;
        Bundle bundle = getActivity().getIntent().getExtras();

        try {
            Categoria categoria = (Categoria) bundle.getSerializable("categoria");
            if (!categoriaAtual.equals(categoria.getNome())) {
                populaCategoria(categoria.getNome());
                categoriaAtual = categoria.getNome();
            }
        } catch (Exception e) {
            Log.d("DEU RUIM", e.getMessage());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(valueEventListenerProdutos);
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReference.removeEventListener(valueEventListenerProdutos);
    }
}
