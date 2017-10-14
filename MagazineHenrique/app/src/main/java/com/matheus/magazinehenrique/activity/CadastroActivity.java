package com.matheus.magazinehenrique.activity;

import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.matheus.magazinehenrique.R;

import java.util.ArrayList;

public class CadastroActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        toolbar = (Toolbar)findViewById(R.id.toolbarCadastro);
        toolbar.setTitle("Cadastro de Usuarios");
        toolbar.setTitleTextColor(getResources().getColor(R.color.corTituloToolbar));
        setSupportActionBar(toolbar);

        ArrayList<String> arraylist = new ArrayList<String>();
        arraylist.add("Idade");
        for(int i = 1; i < 130; i++)
            arraylist.add(String.valueOf(i));

        spinner = (Spinner)findViewById(R.id.spinnerCadastro);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arraylist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
