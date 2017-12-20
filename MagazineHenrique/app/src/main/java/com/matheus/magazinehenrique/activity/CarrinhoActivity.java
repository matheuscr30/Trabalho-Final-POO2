package com.matheus.magazinehenrique.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.matheus.magazinehenrique.R;
import com.matheus.magazinehenrique.adapter.CarrinhoAdapter;
import com.matheus.magazinehenrique.config.ConfiguracaoFirebase;
import com.matheus.magazinehenrique.model.Carrinho;
import com.matheus.magazinehenrique.model.Categoria;
import com.matheus.magazinehenrique.model.Produto;
import com.matheus.magazinehenrique.tools.Preferencias;

import java.util.ArrayList;
import java.util.Locale;

public class CarrinhoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListenerProdutos;
    private RecyclerView recyclerView;
    private EditText codigoCupom;
    private TextView precoTotal;
    private Button btnConfirmar;

    private ArrayList<Produto> produtos;
    private ArrayList<String> idProdutos;
    private ArrayList<Integer> qtdProdutosInt;
    private CarrinhoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        produtos = new ArrayList<>();
        idProdutos = new ArrayList<>();
        qtdProdutosInt = new ArrayList<>();

        codigoCupom = (EditText) findViewById(R.id.editTextCupom);
        precoTotal = (TextView) findViewById(R.id.tv_precoTotal);
        btnConfirmar = (Button) findViewById(R.id.buttonContinuar);

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(precoTotal.getText().toString().substring(3).equals("0.00"))
                    Toast.makeText(CarrinhoActivity.this, "Carrinho está vazio", Toast.LENGTH_SHORT).show();
                else
                    abrirPagamentoActivity();
            }
        });

        configRecyclerView();

        toolbar = (Toolbar) findViewById(R.id.toolbarCarrinho);
        toolbar.setTitle("Carrinho");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void configRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.rvCarrinho);
        LinearLayoutManager llm = new LinearLayoutManager(CarrinhoActivity.this);
        recyclerView.setLayoutManager(llm);
        adapter = new CarrinhoAdapter(produtos, qtdProdutosInt, idProdutos, CarrinhoActivity.this);
        recyclerView.setAdapter(adapter);

        //Listener para recuperar contatos
        Preferencias preferencias = new Preferencias(CarrinhoActivity.this);
        String cpfUsuario = preferencias.getCPF();
        databaseReference = ConfiguracaoFirebase.getDatabaseReference();
        DatabaseReference aux = databaseReference.child("carrinhos/" + cpfUsuario + "/");
        valueEventListenerProdutos = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Limpar lista
                produtos.clear();
                idProdutos.clear();
                qtdProdutosInt.clear();
                final Carrinho carrinho = dataSnapshot.getValue(Carrinho.class);

                DatabaseReference aux2 = databaseReference.child("produtos");
                aux2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Listar Produtos do Usuario
                        ArrayList<Produto> produtosAux = new ArrayList<>();
                        for (DataSnapshot dados : dataSnapshot.getChildren()) {

                            Produto produto = dados.getValue(Produto.class);
                            String genero = produto.getGenero();
                            if (genero.equals("masculino")) {
                                produto.setGenero("M" + genero.substring(1));
                            } else if (genero.equals("feminino")) {
                                produto.setGenero("F" + genero.substring(1));
                            }
                            produtosAux.add(produto);
                        }

                        if (carrinho != null) {
                            ArrayList<String> idProdutosCarrinho = carrinho.getIdProdutos();
                            ArrayList<Integer> qtdProdutosCarrinho = carrinho.getQtdProdutos();
                            for (Produto prod : produtosAux) {
                                int index = idProdutosCarrinho.indexOf(prod.getReferencia());
                                if (index != -1) {
                                    produtos.add(prod);
                                    idProdutos.add(prod.getReferencia());
                                    qtdProdutosInt.add(qtdProdutosCarrinho.get(index));
                                }
                            }
                            calculaPrecoTotal(produtos, qtdProdutosInt);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        aux.addValueEventListener(valueEventListenerProdutos);
    }

    public void calculaPrecoTotal(ArrayList<Produto>produtos, ArrayList<Integer>qtdProdutos){

        double total = 0;
        for(int i = 0; i < produtos.size(); i++){
            double preco = Double.parseDouble(produtos.get(i).getPreco());
            total += preco * qtdProdutos.get(i);
        }

        String resultado = String.format(Locale.CANADA, "%.2f", total);
        precoTotal.setText("R$ " + resultado);
    }

    public void abrirPagamentoActivity(){
        Intent intent = new Intent(CarrinhoActivity.this, PagamentoActivity.class);
        intent.putExtra("cupom", codigoCupom.getText().toString());
        intent.putExtra("preco", precoTotal.getText().toString().substring(3));
        intent.putStringArrayListExtra("idProdutos", idProdutos);
        intent.putIntegerArrayListExtra("qtdProdutos", qtdProdutosInt);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                finish();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Toast.makeText(CarrinhoActivity.this, "CANCELED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
