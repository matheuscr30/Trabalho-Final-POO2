package com.matheus.magazinehenrique.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.matheus.magazinehenrique.R;
import com.matheus.magazinehenrique.adapter.CarrinhoAdapter;
import com.matheus.magazinehenrique.adapter.DetalhesCompraAdapter;
import com.matheus.magazinehenrique.config.ConfiguracaoFirebase;
import com.matheus.magazinehenrique.dao.CarrinhoDAO;
import com.matheus.magazinehenrique.dao.ProdutoDAO;
import com.matheus.magazinehenrique.model.Carrinho;
import com.matheus.magazinehenrique.model.Compra;
import com.matheus.magazinehenrique.model.Produto;
import com.matheus.magazinehenrique.tools.Preferencias;
import com.matheus.magazinehenrique.tools.SimpleCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DetalhesCompraActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TextView idCompra;
    private TextView dataCompra;
    private TextView totalCompra;

    private DetalhesCompraAdapter adapter;
    private Compra compra;
    private ArrayList<Produto>produtos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_compra);

        produtos = new ArrayList<>();

        idCompra = (TextView)findViewById(R.id.tv_idCompra);
        dataCompra = (TextView)findViewById(R.id.tv_dataCompra);
        totalCompra = (TextView)findViewById(R.id.tv_totalCompra);

        toolbar = (Toolbar) findViewById(R.id.toolbarDetalhesCompra);
        toolbar.setTitle("Detalhes");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        compra = (Compra)bundle.getSerializable("compra");

        idCompra.setText(compra.getChave());
        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy");
        String strDt = simpleDate.format(compra.getData());
        dataCompra.setText(strDt);
        totalCompra.setText("R$ " + compra.getValorTotal());

        configRecyclerView();
    }

    public void configRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.rvDetalhesCompra);
        LinearLayoutManager llm = new LinearLayoutManager(DetalhesCompraActivity.this);
        recyclerView.setLayoutManager(llm);
        adapter = new DetalhesCompraAdapter(produtos, DetalhesCompraActivity.this, compra);
        recyclerView.setAdapter(adapter);

        ProdutoDAO produtoDAO = new ProdutoDAO();
        produtoDAO.buscarProdutos(new SimpleCallback<ArrayList<Produto>>() {
            @Override
            public void callback(ArrayList<Produto> data) {
                produtos.clear();
                ArrayList<String>idProdutosAux = compra.getIdProdutos();
                for(Produto produto : data){
                    int indice = idProdutosAux.indexOf(produto.getReferencia());
                    if(indice != -1)
                        produtos.add(produto);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}
