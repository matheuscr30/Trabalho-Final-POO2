package com.matheus.magazinehenrique.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.StringLoader;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;
import com.matheus.magazinehenrique.R;
import com.matheus.magazinehenrique.activity.CarrinhoActivity;
import com.matheus.magazinehenrique.activity.DetalhesActivity;
import com.matheus.magazinehenrique.activity.MainActivity;
import com.matheus.magazinehenrique.config.ConfiguracaoFirebase;
import com.matheus.magazinehenrique.dao.CarrinhoDAO;
import com.matheus.magazinehenrique.model.Carrinho;
import com.matheus.magazinehenrique.model.Produto;
import com.matheus.magazinehenrique.tools.Preferencias;

import java.util.ArrayList;

/**
 * Created by matheus on 16/12/17.
 */

public class CarrinhoAdapter extends RecyclerView.Adapter<CarrinhoAdapter.CarrinhoViewHolder> {

    private ArrayList<Produto> produtos;
    private ArrayList<String> idProdutos;
    private ArrayList<Integer> qtdProdutosInt;
    private Context context;
    private CarrinhoDAO carrinhoDAO;

    public CarrinhoAdapter(ArrayList<Produto> produtos, ArrayList<Integer> qtdProdutosInt,
                           ArrayList<String> idProdutos, Context context) {
        this.produtos = produtos;
        this.qtdProdutosInt = qtdProdutosInt;
        this.idProdutos = idProdutos;
        this.context = context;
        this.carrinhoDAO = new CarrinhoDAO();
    }

    @Override
    public CarrinhoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout_carrinho, parent, false);
        CarrinhoViewHolder cvh = new CarrinhoViewHolder(v, context, this);
        return cvh;
    }

    @Override
    public void onBindViewHolder(CarrinhoViewHolder holder, int position) {
        holder.nome.setText(produtos.get(position).getNome());
        holder.tamanho.setText("Tam.: " + produtos.get(position).getTamanho());
        holder.preco.setText("R$ " + produtos.get(position).getPreco());
        holder.quantidade.setText(String.valueOf(qtdProdutosInt.get(position)));

        StorageReference storageReference = ConfiguracaoFirebase.getFirebaseStorage()
                .child("images/" + produtos.get(position).getReferencia() + ".jpg");

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

    public void aumentaQtdProduto(int position) {

        try {
            int num = qtdProdutosInt.get(position);
            if (num + 1 > produtos.get(position).getQuantidadeEstoque()) {
                Toast.makeText(context, "Não há produto suficiente em estoque", Toast.LENGTH_SHORT).show();
                return;
            }

            qtdProdutosInt.set(position, num + 1);

            Preferencias preferencias = new Preferencias(context);
            String cpfUsuario = preferencias.getCPF();

            Carrinho carrinho = new Carrinho();
            carrinho.setIdProdutos(idProdutos);
            carrinho.setQtdProdutos(qtdProdutosInt);

            carrinhoDAO.atualizarCarrinho(carrinho, cpfUsuario);

            ((CarrinhoActivity) context).calculaPrecoTotal(produtos, qtdProdutosInt);
            notifyDataSetChanged();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void abaixaQtdProduto(int position) {

        try {
            int num = qtdProdutosInt.get(position);
            if (num <= 1) {
                Toast.makeText(context, "Mínimo de 1 unidade", Toast.LENGTH_SHORT).show();
                return;
            }

            qtdProdutosInt.set(position, num - 1);

            Preferencias preferencias = new Preferencias(context);
            String cpfUsuario = preferencias.getCPF();

            Carrinho carrinho = new Carrinho();
            carrinho.setIdProdutos(idProdutos);
            carrinho.setQtdProdutos(qtdProdutosInt);

            carrinhoDAO.atualizarCarrinho(carrinho, cpfUsuario);

            ((CarrinhoActivity) context).calculaPrecoTotal(produtos, qtdProdutosInt);
            notifyDataSetChanged();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void deletaProduto(int position) {

        qtdProdutosInt.remove(position);
        idProdutos.remove(position);
        produtos.remove(position);

        Preferencias preferencias = new Preferencias(context);
        String cpfUsuario = preferencias.getCPF();

        Carrinho carrinho = new Carrinho();
        carrinho.setIdProdutos(idProdutos);
        carrinho.setQtdProdutos(qtdProdutosInt);

        carrinhoDAO.removeCarrinho(cpfUsuario);
        carrinhoDAO.salvarCarrinho(carrinho, cpfUsuario);

        ((CarrinhoActivity) context).calculaPrecoTotal(produtos, qtdProdutosInt);
        notifyDataSetChanged();
    }

    public void detalharProduto(int position) {
        Intent intent = new Intent(context, DetalhesActivity.class);
        intent.putExtra("produto", produtos.get(position));
        context.startActivity(intent);
    }

    public static class CarrinhoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public CardView cv;
        public TextView nome;
        public TextView tamanho;
        public TextView preco;
        public ImageView fotoProduto;
        public TextView quantidade;
        public LinearLayout linearLayoutCima;
        public ImageView btnMais;
        public ImageView btnMenos;
        public ImageView btnDelete;

        public Context context;
        private CarrinhoDAO carrinhoDAO;
        private CarrinhoAdapter adapter;

        public interface OnIncrementListener {
            void onNumberIncremented();
        }

        public CarrinhoViewHolder(View itemView, Context context, CarrinhoAdapter adapter) {
            super(itemView);

            carrinhoDAO = new CarrinhoDAO();

            this.context = context;
            this.adapter = adapter;

            cv = (CardView) itemView.findViewById(R.id.cvCarrinho);
            nome = (TextView) itemView.findViewById(R.id.tv_nomeProduto);
            tamanho = (TextView) itemView.findViewById(R.id.tv_tamanhoProduto);
            preco = (TextView) itemView.findViewById(R.id.tv_precoProduto);
            fotoProduto = (ImageView) itemView.findViewById(R.id.imageViewProdutoCarrinho);
            linearLayoutCima = (LinearLayout) itemView.findViewById(R.id.linearLayoutCimaCarrinho);
            quantidade = (TextView) itemView.findViewById(R.id.tv_quantidade);
            btnMais = (ImageView) itemView.findViewById(R.id.imageViewMais);
            btnMenos = (ImageView) itemView.findViewById(R.id.imageViewMenos);
            btnDelete = (ImageView) itemView.findViewById(R.id.imageViewDelete);

            btnMais.setOnClickListener(this);
            btnMenos.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
            linearLayoutCima.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imageViewMais:
                    adapter.aumentaQtdProduto(getAdapterPosition());
                    break;

                case R.id.imageViewMenos:
                    adapter.abaixaQtdProduto(getAdapterPosition());
                    break;

                case R.id.imageViewDelete:
                    adapter.deletaProduto(getAdapterPosition());
                    break;

                case R.id.linearLayoutCimaCarrinho:
                    adapter.detalharProduto(getAdapterPosition());
                    break;
            }
        }
    }
}
