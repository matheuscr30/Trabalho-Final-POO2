package com.matheus.magazinehenrique.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.matheus.magazinehenrique.cupons.Cupom;
import com.matheus.magazinehenrique.R;
import com.matheus.magazinehenrique.config.ConfiguracaoFirebase;
import com.matheus.magazinehenrique.cupons.CupomAnoNovo;
import com.matheus.magazinehenrique.cupons.CupomBermuda;
import com.matheus.magazinehenrique.cupons.CupomCalca;
import com.matheus.magazinehenrique.cupons.CupomCamisa;
import com.matheus.magazinehenrique.cupons.CupomCamiseta;
import com.matheus.magazinehenrique.cupons.CupomNatal;
import com.matheus.magazinehenrique.cupons.CupomSaia;
import com.matheus.magazinehenrique.cupons.CupomVestido;
import com.matheus.magazinehenrique.cupons.IdCupom;
import com.matheus.magazinehenrique.dao.CarrinhoDAO;
import com.matheus.magazinehenrique.dao.CompraDAO;
import com.matheus.magazinehenrique.fretes.Frete;
import com.matheus.magazinehenrique.fretes.FretePAC;
import com.matheus.magazinehenrique.fretes.FreteSedex;
import com.matheus.magazinehenrique.model.Cartao;
import com.matheus.magazinehenrique.model.Cliente;
import com.matheus.magazinehenrique.model.Compra;
import com.matheus.magazinehenrique.pedidos.Pedido;
import com.matheus.magazinehenrique.pedidos.PedidoBoleto;
import com.matheus.magazinehenrique.pedidos.PedidoCartao;
import com.matheus.magazinehenrique.tools.Preferencias;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class PagamentoActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private Preferencias preferencias;

    private Toolbar toolbar;
    private TextView nomeCliente;
    private TextView enderecoCliente;
    private TextView cidadeCliente;
    private TextView telefoneCliente;
    private TextView cpfCliente;
    private TextView btnAlterarEndereco;
    private TextView opcaoPagamento;
    private TextView btnAlterarPagamento;
    private TextView opcaoTransporte;
    private TextView btnAlterarTransporte;
    private TextView subTotalResumo;
    private TextView freteResumo;
    private TextView cupomResumo;
    private TextView totalResumo;
    private TextView totalCompra;
    private Button btnPagar;
    private int numParcelas;

    private ArrayList<String>idProdutos;
    private ArrayList<Integer>qtdProdutos;
    private Cartao cartao;
    private Cupom cupom;
    private Frete frete;
    private Pedido pedido;
    private String precoSubtotalGlobal;
    private double precoTotalGlobal;
    private String cupomStringGlobal;

    //PAC - 15
    //SEDEX - 30

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamento);
        preferencias = new Preferencias(PagamentoActivity.this);

        cupom = new CupomAnoNovo();
        cupom.setNext(new CupomBermuda());
        cupom.setNext(new CupomCalca());
        cupom.setNext(new CupomCamisa());
        cupom.setNext(new CupomCamiseta());
        cupom.setNext(new CupomNatal());
        cupom.setNext(new CupomSaia());
        cupom.setNext(new CupomVestido());

        nomeCliente = (TextView)findViewById(R.id.tv_nomePagamento);
        enderecoCliente = (TextView)findViewById(R.id.tv_enderecoPagamento);
        cidadeCliente = (TextView)findViewById(R.id.tv_cidadePagamento);
        telefoneCliente = (TextView)findViewById(R.id.tv_telefonePagamento);
        cpfCliente = (TextView)findViewById(R.id.tv_cpfPagamento);
        btnAlterarEndereco = (TextView)findViewById(R.id.btnAlterarEntrega);
        opcaoPagamento = (TextView)findViewById(R.id.tv_metodoPagamento);
        btnAlterarPagamento = (TextView)findViewById(R.id.btnAlterarPagamento);
        opcaoTransporte = (TextView)findViewById(R.id.tv_opcaoFrete);
        btnAlterarTransporte = (TextView)findViewById(R.id.btnAlterarTransporte);
        subTotalResumo = (TextView)findViewById(R.id.tv_subTotalPagamento);
        freteResumo = (TextView)findViewById(R.id.tv_fretePagamento);
        cupomResumo = (TextView)findViewById(R.id.tv_cupomPagamento);
        totalResumo = (TextView)findViewById(R.id.tv_totalPagamento);
        totalCompra = (TextView)findViewById(R.id.precoTotalPagamento);
        btnPagar = (Button)findViewById(R.id.btnPagarAgora);

        btnAlterarEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abreDialogEntrega();
            }
        });

        btnAlterarPagamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {abreDialogPagamento();
            }
        });

        btnAlterarTransporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {abreDialogTransporte();
            }
        });

        toolbar = (Toolbar)findViewById(R.id.toolbarPagamento);
        toolbar.setTitle("Pagamento");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseReference = ConfiguracaoFirebase.getDatabaseReference();

        Bundle bundle = getIntent().getExtras();
        idProdutos = bundle.getStringArrayList("idProdutos");
        qtdProdutos = bundle.getIntegerArrayList("qtdProdutos");
        String cupom = bundle.getString("cupom");
        String preco = bundle.getString("preco");
        String cpfUsuario = preferencias.getCPF();
        cupomStringGlobal = cupom;
        precoSubtotalGlobal = preco;

        configPagina(cpfUsuario);

        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abreConfirmacaoActivity();
            }
        });
    }

    void configPagina(String cpfUsuario){

        DatabaseReference databaseReferenceAux =
                databaseReference.child("usuarios/" + cpfUsuario);

        databaseReferenceAux.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Cliente cliente = dataSnapshot.getValue(Cliente.class);

                nomeCliente.setText(cliente.getNome());
                enderecoCliente.setText(cliente.getEndereco() + ", " + cliente.getNumero());
                cidadeCliente.setText(cliente.getCidade() + ", " + cliente.getEstado());
                telefoneCliente.setText(cliente.getTelefone());
                cpfCliente.setText(cliente.getCPF());
                subTotalResumo.setText("R$ " + precoSubtotalGlobal);

                Calculate();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void Calculate(){
        String transporte = opcaoTransporte.getText().toString();
        if(transporte.equals("PAC")){
            frete = new FretePAC();
        } else {
            frete = new FreteSedex();
        }

        int numParcelas = 0;
        if(opcaoPagamento.getText().toString().equals("Boleto")){
            pedido = new PedidoBoleto(cupom, frete);
        } else {
            if(cartao != null)
                numParcelas = cartao.getNumParcelas();
            pedido = new PedidoCartao(cupom, frete);
        }

        boolean flag = true;
        IdCupom idCupom = null;
        try{
            idCupom = IdCupom.valueOf(cupomStringGlobal);
        } catch (Exception e){
            e.printStackTrace();
            flag = false;
        }

        double precoDouble = Double.parseDouble(precoSubtotalGlobal), precoTotal = precoDouble, precoFrete;
        pedido.processar(idCupom, precoDouble, numParcelas);

        precoTotal = pedido.getPrecoTotal();
        precoFrete = pedido.calcularFrete();

        String precoFreteString = String.format(Locale.CANADA, "%.2f", precoFrete);
        String precoTotalString = String.format(Locale.CANADA, "%.2f", precoTotal);
        cupomResumo.setText(flag ? cupomStringGlobal : "Nenhum cupom disponível");
        freteResumo.setText("R$ " + precoFreteString);
        totalResumo.setText("R$ " + precoTotalString);
        totalCompra.setText("R$ " + precoTotalString);

        precoTotalGlobal = precoTotal;
    }

    public void abreDialogEntrega(){
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(PagamentoActivity.this);


      //  mBottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View sheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_entrega, null);

        ImageView ivFecharEntrega = sheetView.findViewById(R.id.ivFecharEntrega);
        TextInputEditText cep = sheetView.findViewById(R.id.editTextCEPEntrega);
        final TextInputEditText cidade = sheetView.findViewById(R.id.editTextCidadeEntrega);
        final TextInputEditText estado = sheetView.findViewById(R.id.editTextEstadoEntrega);
        final TextInputEditText endereco = sheetView.findViewById(R.id.editTextEnderecoEntrega);
        final TextInputEditText numero = sheetView.findViewById(R.id.editTextNumeroEntrega);
        Button btnConfirmar = sheetView.findViewById(R.id.btnConfirmarEntrega);

        ivFecharEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {mBottomSheetDialog.dismiss();}
        });

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enderecoCliente.setText(endereco.getText().toString()
                + ", " + numero.getText().toString());
                cidadeCliente.setText(cidade.getText().toString()
                + ", " + estado.getText().toString());
                mBottomSheetDialog.dismiss();
            }
        });

        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();

        mBottomSheetDialog.setCancelable(false);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) sheetView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    mBottomSheetDialog.dismiss();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        };

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        View parent = (View) sheetView.getParent();
        parent.setFitsSystemWindows(true);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(parent);
        sheetView.measure(0, 0);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenHeight = displaymetrics.heightPixels;
        bottomSheetBehavior.setPeekHeight(screenHeight);

        if (params.getBehavior() instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior)params.getBehavior()).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        Double papa = screenHeight*0.6;
        params.height = papa.intValue();
        parent.setLayoutParams(params);
    }

    public void abreDialogPagamentoComCartao(final BottomSheetDialog dialogPrincipal){

        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(PagamentoActivity.this);

        mBottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View sheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_informacoes_cartao, null);

        ImageView ivFecharInformacoesCartao = sheetView.findViewById(R.id.ivFecharInformacoesCartao);
        Button btnConfirmar = sheetView.findViewById(R.id.btnConfirmarInformacoesCartao);
        final TextInputEditText numeroCartao = sheetView.findViewById(R.id.editTextNumeroCartao);
        final TextInputEditText mes = sheetView.findViewById(R.id.editTextMesCartao);
        final TextInputEditText ano = sheetView.findViewById(R.id.editTextAnoCartao);
        final TextInputEditText cvCartao = sheetView.findViewById(R.id.editTextCVCartao);
        final TextInputEditText nome = sheetView.findViewById(R.id.editTextNomeCartao);
        final TextInputEditText sobrenome = sheetView.findViewById(R.id.editTextSobrenomeCartao);
        final TextInputEditText cpf = sheetView.findViewById(R.id.editTextCPFCartao);

        ivFecharInformacoesCartao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {mBottomSheetDialog.dismiss();
            }
        });

        String[] items = new String[] {"1 Parcela sem Juros", "2 Parcelas sem Juros",
                "3 Parcelas sem Juros", "4 Parcelas sem Juros"};
        Spinner spinner = sheetView.findViewById(R.id.spinnerInformacoesCartao);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                numParcelas = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!numeroCartao.getText().toString().isEmpty() && !mes.getText().toString().isEmpty() &&
                        !ano.getText().toString().isEmpty() && !cvCartao.getText().toString().isEmpty() &&
                        !nome.getText().toString().isEmpty() && !sobrenome.getText().toString().isEmpty() &&
                        !cpf.getText().toString().isEmpty()){

                    cartao = new Cartao(numeroCartao.getText().toString(),
                            Integer.parseInt(mes.getText().toString()),
                            Integer.parseInt(ano.getText().toString()),
                            Integer.parseInt(cvCartao.getText().toString()),
                            nome.getText().toString(), sobrenome.getText().toString(),
                            cpf.getText().toString(), numParcelas);
                    mBottomSheetDialog.dismiss();
                    dialogPrincipal.dismiss();
                    Toast.makeText(PagamentoActivity.this, "Cartão Adicionado", Toast.LENGTH_SHORT).show();
                } else {
                    mBottomSheetDialog.dismiss();
                    dialogPrincipal.dismiss();
                    opcaoPagamento.setText("Boleto");
                    Toast.makeText(PagamentoActivity.this, "Informações Inválidas", Toast.LENGTH_SHORT).show();
                }
                Calculate();
            }
        });

        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();

        mBottomSheetDialog.setCancelable(false);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) sheetView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    mBottomSheetDialog.dismiss();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        };

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        View parent = (View) sheetView.getParent();
        parent.setFitsSystemWindows(true);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(parent);
        sheetView.measure(0, 0);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenHeight = displaymetrics.heightPixels;
        bottomSheetBehavior.setPeekHeight(screenHeight);

        if (params.getBehavior() instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior)params.getBehavior()).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        params.height = screenHeight;
        parent.setLayoutParams(params);
    }

    public void abreDialogPagamento(){
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(PagamentoActivity.this);
        View sheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_pagamento, null);

        ImageView ivFecharPagamento = sheetView.findViewById(R.id.ivFecharPagamento);
        RadioGroup rgPagamento = sheetView.findViewById(R.id.rgPagamento);

        rgPagamento.check( opcaoPagamento.getText().toString().equals("Boleto") ? R.id.rbBoleto : R.id.rbCartao );

        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();

        ivFecharPagamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {mBottomSheetDialog.dismiss();
            }
        });

        rgPagamento.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if(i == R.id.rbBoleto){
                    opcaoPagamento.setText("Boleto");
                    mBottomSheetDialog.dismiss();
                }
                else if(i == R.id.rbCartao) {
                    opcaoPagamento.setText("Cartão de Crédito");
                    abreDialogPagamentoComCartao(mBottomSheetDialog);
                }
                Calculate();
            }
        });
    }

    public void abreDialogTransporte(){
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(PagamentoActivity.this);
        View sheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_transporte, null);

        ImageView ivFecharTransporte = sheetView.findViewById(R.id.ivFecharTransporte);
        RadioGroup rgTransporte = sheetView.findViewById(R.id.rgTransporte);

        rgTransporte.check( opcaoTransporte.getText().toString().equals("PAC") ? R.id.rbPAC : R.id.rbSEDEX );

        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();

        ivFecharTransporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {mBottomSheetDialog.dismiss();
            }
        });

        rgTransporte.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if(i == R.id.rbPAC){
                    opcaoTransporte.setText("PAC");
                }
                else if(i == R.id.rbSEDEX){
                    opcaoTransporte.setText("SEDEX");
                }
                Calculate();
                mBottomSheetDialog.dismiss();
            }
        });
    }

    public void abreConfirmacaoActivity(){
        CompraDAO compraDAO = new CompraDAO();
        CarrinhoDAO carrinhoDAO = new CarrinhoDAO();

        Compra compra = new Compra();
        compra.setData(Calendar.getInstance().getTime());
        compra.setValorTotal(totalCompra.getText().toString().substring(3));
        compra.setIdProdutos(idProdutos);
        compra.setQtdProdutos(qtdProdutos);

        String cpfUsuario = preferencias.getCPF();
        String key = compraDAO.salvarCompra(compra, cpfUsuario);
        carrinhoDAO.removeCarrinho(cpfUsuario);

        if(key == null)
            Toast.makeText(PagamentoActivity.this, "Erro no Sistema", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(PagamentoActivity.this, ConfirmacaoActivity.class);
        intent.putExtra("key", key);
        startActivity(intent);

        Intent intent2 = new Intent();
        setResult(Activity.RESULT_OK,intent2);
        finish();
    }
}
