package com.matheus.magazinehenrique.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.matheus.magazinehenrique.R;
import com.matheus.magazinehenrique.config.ConfiguracaoFirebase;

import java.util.function.LongToIntFunction;

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

        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        verificarUsuarioLogado();

        botaoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logarUsuario();
            }
        });
    }

    void logarUsuario(){

        boolean flag = true;
        String email = inputLayoutEmail.getEditText().getText().toString();
        String senha = inputLayoutSenha.getEditText().getText().toString();
        if(email.equals("")){
            inputLayoutEmail.setError("Preenchimento Obrigatório");
            flag = false;
        }
        if(senha.equals("")){
            inputLayoutSenha.setError("Preenchimento Obrigatório");
            flag = false;
        }

        if(!flag) return;

        firebaseAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(LoginActivity.this, "Login realizado com sucesso!!", Toast.LENGTH_SHORT).show();
                            verificarUsuarioLogado();
                        } else {
                            Toast.makeText(LoginActivity.this, "Email/Senha incorretos", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    void verificarUsuarioLogado(){
        if(firebaseAuth.getCurrentUser() != null){
            abrirMainActivity();
        }
    }

    void abrirMainActivity(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
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
