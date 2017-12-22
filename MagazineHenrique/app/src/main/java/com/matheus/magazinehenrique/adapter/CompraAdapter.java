package com.matheus.magazinehenrique.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.matheus.magazinehenrique.R;
import com.matheus.magazinehenrique.activity.DetalhesCompraActivity;
import com.matheus.magazinehenrique.model.Compra;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by matheus on 16/12/17.
 */

public class CompraAdapter extends RecyclerView.Adapter<CompraAdapter.CompraViewHolder> {

    private ArrayList<Compra>compras;
    private Context context;

    public CompraAdapter(ArrayList<Compra> compras, Context context) {
        this.compras = compras;
        this.context = context;
    }

    @Override
    public CompraViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout_compra, parent, false);
        CompraViewHolder cvh = new CompraViewHolder(v, context, this);
        return cvh;
    }

    @Override
    public void onBindViewHolder(CompraViewHolder holder, int position) {
        Compra compra = compras.get(position);
        holder.numPedido.setText("Pedido " + compra.getChave().substring(1));
        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy");
        String strDt = simpleDate.format(compra.getData());
        holder.data.setText(strDt);
        holder.total.setText("R$ " + compra.getValorTotal());
    }

    @Override
    public int getItemCount() {
        return compras.size();
    }

    void detalharCompra(int position){
        Intent intent = new Intent(context, DetalhesCompraActivity.class);
        intent.putExtra("compra", compras.get(position));
        context.startActivity(intent);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class CompraViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public CardView cv;
        public TextView data;
        public TextView total;
        public TextView numPedido;
        public TextView btnDetalhes;

        public Context context;
        private CompraAdapter adapter;

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnDetalhesCompra:
                    adapter.detalharCompra(getAdapterPosition());
                    break;
            }
        }

        public CompraViewHolder(View itemView, Context context, CompraAdapter adapter) {
            super(itemView);

            this.context = context;
            this.adapter = adapter;

            cv = itemView.findViewById(R.id.cvCompra);
            data = itemView.findViewById(R.id.tv_dataCompra);
            total = itemView.findViewById(R.id.tv_totalCompra);
            numPedido = itemView.findViewById(R.id.tv_numCompra);
            btnDetalhes = itemView.findViewById(R.id.btnDetalhesCompra);

            btnDetalhes.setOnClickListener(this);
        }
    }
}
