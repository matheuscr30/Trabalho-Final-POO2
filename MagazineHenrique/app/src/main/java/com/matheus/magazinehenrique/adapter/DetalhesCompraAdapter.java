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
import com.matheus.magazinehenrique.model.Compra;
import com.matheus.magazinehenrique.model.Produto;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by matheus on 22/12/17.
 */

public class DetalhesCompraAdapter extends RecyclerView.Adapter<DetalhesCompraAdapter.DetalhesCompraViewHolder>{
    private ArrayList<Produto> produtos;
    private Context context;
    private Compra compra;

    public DetalhesCompraAdapter(ArrayList<Produto> produtos, Context context, Compra compra) {
        this.produtos = produtos;
        this.context = context;
        this.compra = compra;
    }

    @Override
    public DetalhesCompraAdapter.DetalhesCompraViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout_detalhes_compra, parent, false);
        DetalhesCompraAdapter.DetalhesCompraViewHolder dcvh =
                new DetalhesCompraAdapter.DetalhesCompraViewHolder(v, context, produtos);
        return dcvh;
    }

    @Override
    public void onBindViewHolder(final DetalhesCompraAdapter.DetalhesCompraViewHolder holder, int position) {
        holder.nome.setText(produtos.get(position).getNome());
        holder.tamanho.setText(produtos.get(position).getTamanho());
        holder.preco.setText("R$ " + produtos.get(position).getPreco());

        int quantidadeInt = compra.getQtdProdutos().get(position);
        holder.quantidade.setText(String.valueOf(quantidadeInt));

        double preco = Double.parseDouble(produtos.get(position).getPreco());
        double totalDouble = preco*quantidadeInt;
        String totalString = String.format(Locale.CANADA, "%.2f", totalDouble);
        holder.total.setText("R$ " + totalString);

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

    public static class DetalhesCompraViewHolder extends RecyclerView.ViewHolder{

        private Context context;
        private ArrayList<Produto>produtos;

        public TextView nome;
        public TextView tamanho;
        public TextView preco;
        public TextView quantidade;
        public TextView total;
        public ImageView fotoProduto;

        public DetalhesCompraViewHolder(View itemView, Context context, ArrayList<Produto>produtos){
            super(itemView);

            this.context = context;
            this.produtos = produtos;

            nome = itemView.findViewById(R.id.tv_nomeProduto);
            tamanho = itemView.findViewById(R.id.tv_tamanhoProduto);
            preco = itemView.findViewById(R.id.tv_precoProduto);
            quantidade = itemView.findViewById(R.id.tv_quantidadeProduto);
            total = itemView.findViewById(R.id.tv_totalProduto);
            fotoProduto = itemView.findViewById(R.id.imageViewDetalhesCompra);
        }
    }
}
