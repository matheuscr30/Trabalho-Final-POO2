package com.matheus.magazinehenrique.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.matheus.magazinehenrique.R;
import com.matheus.magazinehenrique.config.ConfiguracaoFirebase;
import com.matheus.magazinehenrique.tools.Preferencias;

import org.w3c.dom.Text;

import java.util.function.LongToIntFunction;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private TextInputLayout inputLayoutEmail;
    private TextInputLayout inputLayoutSenha;
    private Button botaoLogin;
    private TextView botaoEsqueciSenha;
    private TextView botaoCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputLayoutEmail = (TextInputLayout)findViewById(R.id.inputLayoutEmailLogin);
        inputLayoutSenha = (TextInputLayout)findViewById(R.id.inputLayoutSenhaLogin);
        botaoLogin = (Button)findViewById(R.id.botaoLogin);
        botaoEsqueciSenha = (TextView)findViewById(R.id.textEsqueciSenhaLogin);
        botaoCadastrar = (TextView)findViewById(R.id.textCadastrarLogin);

        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        databaseReference = ConfiguracaoFirebase.getDatabaseReference();
        verificarUsuarioLogado();

        botaoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logarUsuario();
            }
        });

        botaoEsqueciSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {abrirEsqueciSenhaActivity();
            }
        });

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCadastroActivity();
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
                            //Toast.makeText(LoginActivity.this, "Login realizado com sucesso!!", Toast.LENGTH_SHORT).show();
                            verificarUsuarioLogado();
                        } else {
                            Toast.makeText(LoginActivity.this, "Email/Senha incorretos", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    void verificarUsuarioLogado(){
        if(firebaseAuth.getCurrentUser() != null){
            databaseReference = databaseReference.child("cpfs/"+firebaseAuth.getCurrentUser().getUid());
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Preferencias preferencias = new Preferencias(LoginActivity.this);
                    preferencias.salvarDados(dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            abrirMainActivity();
        }
    }

    void abrirMainActivity(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    void abrirCadastroActivity(){
        Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(intent);
    }

    void abrirEsqueciSenhaActivity(){
        Intent intent = new Intent(LoginActivity.this, EsqueciSenhaActivity.class);
        startActivity(intent);
    }
}
