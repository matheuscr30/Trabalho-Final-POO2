package com.matheus.magazinehenrique.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.matheus.magazinehenrique.R;
import com.matheus.magazinehenrique.config.ConfiguracaoFirebase;
import com.matheus.magazinehenrique.dao.CarrinhoDAO;
import com.matheus.magazinehenrique.model.Carrinho;
import com.matheus.magazinehenrique.model.Produto;
import com.matheus.magazinehenrique.tools.Preferencias;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DetalhesActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView nomeProduto;
    private TextView precoProduto;
    private TextView corProduto;
    private TextView tamanhoProduto;
    private TextView descricaoProduto;
    private TextView generoProduto;
    private FloatingActionButton fabAdicionarCarrinho;
    private DatabaseReference databaseReference;

    private ImageView fotoProduto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        fotoProduto = (ImageView) findViewById(R.id.imageViewProdutoDetalhes);
        nomeProduto = (TextView) findViewById(R.id.tv_nomeProduto);
        precoProduto = (TextView) findViewById(R.id.tv_precoProduto);
        corProduto = (TextView) findViewById(R.id.tv_corProduto);
        tamanhoProduto = (TextView) findViewById(R.id.tv_tamanhoProduto);
        descricaoProduto = (TextView) findViewById(R.id.tv_descricaoProduto);
        generoProduto = (TextView) findViewById(R.id.tv_generoProduto);
        fabAdicionarCarrinho = (FloatingActionButton) findViewById(R.id.fabAdicionarCarrinho);

        toolbar = (Toolbar) findViewById(R.id.toolbarDetalhes);
        toolbar.setTitle("Detalhes");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseReference = ConfiguracaoFirebase.getDatabaseReference();

        Bundle bundle = getIntent().getExtras();
        final Produto produto = (Produto) bundle.getSerializable("produto");

        populateCard(produto);

        Preferencias preferencias = new Preferencias(DetalhesActivity.this);
        final String cpfUsuario = preferencias.getCPF();
        fabAdicionarCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference aux = databaseReference.child("carrinhos/" + cpfUsuario);
                aux.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Carrinho carrinho = dataSnapshot.getValue(Carrinho.class);
                        if(carrinho == null){
                            carrinho = new Carrinho();
                            carrinho.setIdProdutos(new ArrayList<String>());
                            carrinho.setQtdProdutos(new ArrayList<Integer>());
                            carrinho.addProduto(produto.getReferencia());
                        } else {
                            carrinho.addProduto(produto.getReferencia());
                        }

                        CarrinhoDAO carrinhoDAO = new CarrinhoDAO();
                        if(carrinhoDAO.atualizarCarrinho(carrinho, cpfUsuario)){
                            Snackbar.make(fotoProduto, "Produto adicionado ao Carrinho", Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    public void populateCard(Produto produto) {
        StorageReference storageReference = ConfiguracaoFirebase.getFirebaseStorage()
                .child("images/" + produto.getReferencia() + ".jpg");

        Glide.with(DetalhesActivity.this)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .into(fotoProduto);

        nomeProduto.setText(produto.getNome());
        precoProduto.setText("R$ " + produto.getPreco());
        corProduto.setText(produto.getCor());
        tamanhoProduto.setText(produto.getTamanho());
        descricaoProduto.setText(produto.getDescricao());
        generoProduto.setText(produto.getGenero());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.opcoes_menu_detalhes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_carrinho:
                abrirCarrinhoActivity();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void abrirCarrinhoActivity() {
        Intent intent = new Intent(DetalhesActivity.this, CarrinhoActivity.class);
        startActivity(intent);
    }
}
