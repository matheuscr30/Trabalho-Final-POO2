package com.matheus.magazinehenrique.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.matheus.magazinehenrique.R;

public class EsqueciSenhaActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueci_senha);

        toolbar = (Toolbar)findViewById(R.id.toolbarEsqueciMinhaSenha);
        toolbar.setTitle("Recuperação de Senha");
        toolbar.setTitleTextColor(getResources().getColor(R.color.corTituloToolbar));
        setSupportActionBar(toolbar);
    }
}
