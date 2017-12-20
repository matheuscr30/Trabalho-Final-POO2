package com.matheus.magazinehenrique.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.matheus.magazinehenrique.R;

public class ConfirmacaoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView numPedido;
    private ImageView btnVoltarMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmacao);

        numPedido = (TextView)findViewById(R.id.tv_numPedidoConfirmacao);
        btnVoltarMain = (ImageView)findViewById(R.id.ivVoltarMain);

        toolbar = (Toolbar) findViewById(R.id.toolbarConfirmacao);
        toolbar.setTitle("Confirmação");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        numPedido.setText("Pedido " + bundle.getString("key") + " realizado");

        btnVoltarMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConfirmacaoActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}
