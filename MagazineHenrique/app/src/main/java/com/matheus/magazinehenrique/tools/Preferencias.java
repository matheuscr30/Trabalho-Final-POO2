package com.matheus.magazinehenrique.tools;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by matheus on 22/04/17.
 */

public class Preferencias {

    private Context context;
    private SharedPreferences preferences;
    private final String NOME_ARQUIVO = "summereletrohits.preferencias";
    private final int MODE = 0;
    private SharedPreferences.Editor editor;

    private String CHAVE_IDENTIFICADOR = "cpfUsuarioLogado";

    public Preferencias(Context context){
        this.context = context;
        preferences = this.context.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferences.edit();
    }

    public void salvarDados(String cpfUsuario){
        editor.putString(CHAVE_IDENTIFICADOR, cpfUsuario);
        editor.commit();
    }

    public String getCPF(){
        return preferences.getString(CHAVE_IDENTIFICADOR, null);
    }
}
