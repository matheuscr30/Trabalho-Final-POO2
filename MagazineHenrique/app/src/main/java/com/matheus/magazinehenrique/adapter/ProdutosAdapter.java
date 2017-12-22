package com.matheus.magazinehenrique.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;
import com.matheus.magazinehenrique.R;
import com.matheus.magazinehenrique.activity.DetalhesProdutoActivity;
import com.matheus.magazinehenrique.config.ConfiguracaoFirebase;
import com.matheus.magazinehenrique.model.Produto;

import java.util.ArrayList;


/**
 * Created by matheus on 15/12/17.
 */

public class ProdutosAdapter extends RecyclerView.Adapter<ProdutosAdapter.ProdutosViewHolder> {

    private ArrayList<Produto> produtos;
    private Context context;

    public ProdutosAdapter(ArrayList<Produto> produtos, Context context) {
        this.produtos = produtos;
        this.context = context;
    }

    public static class ProdutosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public CardView cv;
        public TextView nome;
        public TextView preco;
        public ImageView fotoProduto;
        public Context context;
        public ArrayList<Produto>produtos;

        public ProdutosViewHolder(View itemView, Context context, ArrayList<Produto>produtos){
            super(itemView);

            this.context = context;
            this.produtos = produtos;
            cv = (CardView)itemView.findViewById(R.id.cvProdutos);
            nome = (TextView)itemView.findViewById(R.id.tv_nomeProduto);
            preco = (TextView)itemView.findViewById(R.id.tv_precoProduto);
            fotoProduto = (ImageView)itemView.findViewById(R.id.imageViewProdutos);

            cv.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, DetalhesProdutoActivity.class);
            intent.putExtra("produto", produtos.get(getAdapterPosition()));
            context.startActivity(intent);
        }
    }

    @Override
    public ProdutosAdapter.ProdutosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout_produtos, parent, false);
        ProdutosAdapter.ProdutosViewHolder pvh = new ProdutosAdapter.ProdutosViewHolder(v, context, produtos);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final ProdutosAdapter.ProdutosViewHolder holder, int position) {
        holder.nome.setText(produtos.get(position).getNome());
        holder.preco.setText("R$ " + produtos.get(position).getPreco());

        StorageReference storageReference = ConfiguracaoFirebase.getFirebaseStorage()
                .child("images/"+produtos.get(position).getReferencia()+".jpg");

        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .into(holder.fotoProduto);
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
