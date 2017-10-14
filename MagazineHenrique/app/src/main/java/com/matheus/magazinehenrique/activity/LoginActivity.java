package com.matheus.magazinehenrique.activity;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.matheus.magazinehenrique.R;
import com.matheus.magazinehenrique.config.ConfiguracaoFirebase;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private TextInputLayout inputLayoutEmail;
    private TextInputLayout inputLayoutSenha;
    private Button botaoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputLayoutEmail = (TextInputLayout)findViewById(R.id.inputLayoutEmailLogin);
        inputLayoutSenha = (TextInputLayout)findViewById(R.id.inputLayoutSenhaLogin);
        botaoLogin = (Button)findViewById(R.id.botaoLogin);

        verificarUsuarioLogado();
    }

    void verificarUsuarioLogado(){
        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        if(firebaseAuth.getCurrentUser() != null){
            abrirMainActivity();
        }
    }

    void abrirMainActivity(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    void abrirCadastroActivity(View v){
        Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(intent);
    }

    void abrirEsqueciSenhaActivity(View v){
        Intent intent = new Intent(LoginActivity.this, EsqueciSenhaActivity.class);
        startActivity(intent);
    }
}
