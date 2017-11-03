package com.matheus.magazinehenrique.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.matheus.magazinehenrique.R;
import com.matheus.magazinehenrique.config.ConfiguracaoFirebase;

public class EsqueciSenhaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextInputLayout inputLayoutEmail;
    private Button botaoEnviar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueci_senha);

        inputLayoutEmail = (TextInputLayout)findViewById(R.id.inputLayoutEmailEsqueciSenha);
        botaoEnviar = (Button)findViewById(R.id.button2);

        toolbar = (Toolbar)findViewById(R.id.toolbarEsqueciMinhaSenha);
        toolbar.setTitle("Recuperação de Senha");
        toolbar.setTitleTextColor(getResources().getColor(R.color.corTituloToolbar));
        setSupportActionBar(toolbar);

        botaoEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
                firebaseAuth
                        .sendPasswordResetEmail( inputLayoutEmail.getEditText().getText().toString() )
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if( task.isSuccessful() ){
                                    Toast.makeText(
                                            EsqueciSenhaActivity.this,
                                            "Recuperação de acesso iniciada. Email enviado.",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                    abrirLoginActivity();
                                }
                                else{
                                    Toast.makeText(
                                            EsqueciSenhaActivity.this,
                                            "Falhou! Tente novamente",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            }
                        });
            }
        });
    }

    private void abrirLoginActivity(){
        Intent intent = new Intent(EsqueciSenhaActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
