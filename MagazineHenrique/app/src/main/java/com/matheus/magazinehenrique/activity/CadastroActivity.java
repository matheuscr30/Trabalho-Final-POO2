package com.matheus.magazinehenrique.activity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.api.Response;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.matheus.magazinehenrique.R;
import com.matheus.magazinehenrique.config.ConfiguracaoFirebase;
import com.matheus.magazinehenrique.dao.ClienteDAO;
import com.matheus.magazinehenrique.tools.Mask;
import com.matheus.magazinehenrique.model.Cliente;
import com.matheus.magazinehenrique.tools.RepoCEP;
import com.matheus.magazinehenrique.tools.ValidaCPF;
import com.matheus.magazinehenrique.tools.ViaCepServices;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.lang.Object.*;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CadastroActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Spinner spinnerIdade;
    private RadioGroup radioGroupGenero;
    private TextInputLayout inputLayoutNome;
    private TextInputLayout inputLayoutCPF;
    private TextInputLayout inputLayoutEmail;
    private TextInputLayout inputLayoutSenha;
    private TextInputLayout inputLayoutTelefone;
    private TextInputLayout inputLayoutCEP;
    private TextInputLayout inputLayoutEstado;
    private TextInputLayout inputLayoutCidade;
    private TextInputLayout inputLayoutEndereco;
    private TextInputLayout inputLayoutNumero;
    private TextInputLayout inputLayoutComplemento;
    private TextInputLayout inputLayoutConfirmarSenha;
    private FloatingActionButton fabBotaoCadastro;

    private TextWatcher cpfMask;
    private TextWatcher telefoneMask;
    private TextWatcher cepMask;

    private FirebaseAuth firebaseAuth;
    private Cliente cliente;

    private boolean flagCPF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();

        toolbar = (Toolbar) findViewById(R.id.toolbarCadastro);
        spinnerIdade = (Spinner) findViewById(R.id.spinnerIdadeCadastro);
        radioGroupGenero = (RadioGroup) findViewById(R.id.radioGroupCadastro);
        inputLayoutNome = (TextInputLayout) findViewById(R.id.inputLayoutNomeCadastro);
        inputLayoutCPF = (TextInputLayout) findViewById(R.id.inputLayoutCPFCadastro);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.inputLayoutEmailCadastro);
        inputLayoutSenha = (TextInputLayout) findViewById(R.id.inputLayoutSenhaCadastro);
        inputLayoutConfirmarSenha = (TextInputLayout) findViewById(R.id.inputLayoutConfirmarSenhaCadastro);
        inputLayoutTelefone = (TextInputLayout) findViewById(R.id.inputLayoutTelefoneCadastro);
        inputLayoutCEP = (TextInputLayout) findViewById(R.id.inputLayoutCEPCadastro);
        inputLayoutEstado = (TextInputLayout) findViewById(R.id.inputLayoutEstadoCadastro);
        inputLayoutCidade = (TextInputLayout) findViewById(R.id.inputLayoutCidadeCadastro);
        inputLayoutEndereco = (TextInputLayout) findViewById(R.id.inputLayoutEnderecoCadastro);
        inputLayoutNumero = (TextInputLayout) findViewById(R.id.inputLayoutNumeroCadastro);
        inputLayoutComplemento = (TextInputLayout) findViewById(R.id.inputLayoutComplementoCadastro);
        fabBotaoCadastro = (FloatingActionButton) findViewById(R.id.fabCadastro);

        configuracaoTela();

        fabBotaoCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastraCliente();
            }
        });

        inputLayoutCEP.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus && validaCep()) chamaViaCep();
            }
        });
    }

    private boolean validaCep() {
        if (inputLayoutCEP.getEditText().getText().toString().equals("") ||
                inputLayoutCEP.getEditText().getText().toString().length() != 9)
            return false;
        return true;
    }

    private void chamaViaCep() {
        final ProgressDialog progressDialog = new ProgressDialog(CadastroActivity.this);
        progressDialog.setTitle("Carregando");
        progressDialog.setMessage("Aguarde um pouco...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://viacep.com.br")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ViaCepServices viaCepServices = retrofit.create(ViaCepServices.class);
        String cep = Mask.unmask(inputLayoutCEP.getEditText().getText().toString());
        Log.i("RUIM", cep);
        Call<RepoCEP> repos = viaCepServices.
                listRepos(cep);
        repos.enqueue(new Callback<RepoCEP>() {
            @Override
            public void onResponse(Call<RepoCEP> call, retrofit2.Response<RepoCEP> response) {

                RepoCEP repoResponse = response.body();

                if(repoResponse.getCidade() == null){
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    Snackbar.make(inputLayoutCPF, "CEP não encontrado", Snackbar.LENGTH_SHORT).show();
                }

                inputLayoutEstado.getEditText().setText(repoResponse.getEstado());
                inputLayoutCidade.getEditText().setText(repoResponse.getCidade());
                inputLayoutEndereco.getEditText().setText(repoResponse.getRua());
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<RepoCEP> call, Throwable t) {
                t.printStackTrace();
                progressDialog.dismiss();

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                Toast.makeText(CadastroActivity.this, "CEP nao encontrado", Toast.LENGTH_LONG).show();
                Snackbar.make(inputLayoutCPF, "CEP não encontrado", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void cadastraCliente() {

        if (!validaDados()) {
            return;
        }

        cliente = new Cliente();
        cliente.setNome(inputLayoutNome.getEditText().getText().toString());
        cliente.setCPF(Mask.unmask(inputLayoutCPF.getEditText().getText().toString()));
        cliente.setEmail(inputLayoutEmail.getEditText().getText().toString());
        cliente.setSenha(inputLayoutSenha.getEditText().getText().toString());
        cliente.setIdade(Integer.parseInt(spinnerIdade.getSelectedItem().toString()));

        int radioId = radioGroupGenero.getCheckedRadioButtonId();
        switch (radioId) {
            case R.id.radioButtonMasculino:
                cliente.setSexo("M");
                break;
            case R.id.radioButtonFeminino:
                cliente.setSexo("F");
                break;
        }

        cliente.setTelefone(Mask.unmask(inputLayoutTelefone.getEditText().getText().toString()));
        cliente.setCEP(Mask.unmask(inputLayoutCEP.getEditText().getText().toString()));
        cliente.setEstado(inputLayoutEstado.getEditText().getText().toString());
        cliente.setCidade(inputLayoutCidade.getEditText().getText().toString());
        cliente.setEndereco(inputLayoutEndereco.getEditText().getText().toString());
        cliente.setComplemento(inputLayoutComplemento.getEditText().getText().toString());
        cliente.setNumero(Integer.parseInt(inputLayoutNumero.getEditText().getText().toString()));

        final ProgressDialog progressDialog = new ProgressDialog(CadastroActivity.this);
        progressDialog.setTitle("Carregando");
        progressDialog.setMessage("Aguarde um pouco...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        //Verifica se ja existe o CPF
        String cpf = Mask.unmask(inputLayoutCPF.getEditText().getText().toString());
        ConfiguracaoFirebase.getDatabaseReference()
                .child("usuarios").child(cpf)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            inputLayoutCPF.setErrorEnabled(true);
                            inputLayoutCPF.setError("CPF já Cadastrado");
                            Toast.makeText(CadastroActivity.this, "Erro ao efetuar o cadastro!", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        } else {

                            /* Create the Firebase User */
                            firebaseAuth.createUserWithEmailAndPassword(cliente.getEmail(), cliente.getSenha())
                                    .addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                            if (task.isSuccessful()) {

                                                /* Change Display Name */
                                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                        .setDisplayName(cliente.getNome()).build();
                                                firebaseAuth.getCurrentUser().updateProfile(profileUpdates);

                                                ClienteDAO clienteDAO = new ClienteDAO();
                                                if (clienteDAO.salvarCliente(cliente, firebaseAuth.getCurrentUser().getUid())) {
                                                    Toast.makeText(CadastroActivity.this, "Cadastro Realizado com Sucesso", Toast.LENGTH_SHORT).show();
                                                    abrirLoginActivity();
                                                }

                                            } else {

                                                try {
                                                    Log.i("EXCECAO:", task.getException().toString());
                                                    throw task.getException();
                                                } catch (FirebaseAuthWeakPasswordException e) {
                                                    inputLayoutSenha.setErrorEnabled(true);
                                                    inputLayoutSenha.setError("Digite uma senha mais forte, contendo mais caracteres e com letras e números!");
                                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                                    inputLayoutEmail.setErrorEnabled(true);
                                                    inputLayoutEmail.setError("O e-mail digitado é inválido, digite um novo e-mail!");
                                                } catch (FirebaseAuthUserCollisionException e) {
                                                    inputLayoutEmail.setErrorEnabled(true);
                                                    inputLayoutEmail.setError("Esse e-mail já está em uso!");
                                                } catch (Exception e) {
                                                    Toast.makeText(CadastroActivity.this, "Erro ao efetuar o cadastro!", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                            progressDialog.dismiss();
                                        }
                                    });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        flagCPF = false;
                        progressDialog.dismiss();
                    }
                });
    }

    private boolean validaDados() {

        Boolean flag = true;

        if (inputLayoutNome.getEditText().getText().toString().equals("")) {
            inputLayoutNome.setErrorEnabled(true);
            inputLayoutNome.setError("Preenchimento Obrigatório");
            flag = false;
        } else {
            inputLayoutNome.setErrorEnabled(false);
        }

        String cpf = Mask.unmask(inputLayoutCPF.getEditText().getText().toString());

        if (!ValidaCPF.isCPF(cpf)) {
            inputLayoutCPF.setErrorEnabled(true);
            inputLayoutCPF.setError("CPF Inválido");
            flag = false;
        } else {
            inputLayoutCPF.setErrorEnabled(false);
        }

        if (inputLayoutEmail.getEditText().getText().toString().equals("")) {
            inputLayoutEmail.setErrorEnabled(true);
            inputLayoutEmail.setError("Preenchimento Obrigatório");
            flag = false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        if (inputLayoutSenha.getEditText().getText().toString().equals("")) {
            inputLayoutSenha.setErrorEnabled(true);
            inputLayoutSenha.setError("Preenchimento Obrigatório");
            flag = false;
        } else {
            inputLayoutSenha.setErrorEnabled(false);
        }

        if (inputLayoutConfirmarSenha.getEditText().getText().toString().equals("")) {
            inputLayoutConfirmarSenha.setErrorEnabled(true);
            inputLayoutConfirmarSenha.setError("Preenchimento Obrigatório");
            flag = false;
        } else {
            inputLayoutConfirmarSenha.setErrorEnabled(false);
        }

        if (!inputLayoutSenha.getEditText().getText().toString().equals(inputLayoutConfirmarSenha.getEditText().getText().toString())) {
            inputLayoutConfirmarSenha.setErrorEnabled(true);
            inputLayoutConfirmarSenha.setError("Senhas Diferentes");
            flag = false;
        } else {
            inputLayoutConfirmarSenha.setErrorEnabled(false);
        }

        if (spinnerIdade.getSelectedItem().toString().equals("Idade")) {
            Snackbar.make(spinnerIdade, "Não esqueça de selecionar sua idade", Snackbar.LENGTH_SHORT).show();
            flag = false;
        }

        if (inputLayoutTelefone.getEditText().getText().toString().equals("")) {
            inputLayoutTelefone.setErrorEnabled(true);
            inputLayoutTelefone.setError("Preenchimento Obrigatório");
            flag = false;
        } else {
            inputLayoutTelefone.setErrorEnabled(false);
        }

        String cep = Mask.unmask(inputLayoutCEP.getEditText().getText().toString());

        if (cep.equals("")) {
            inputLayoutCEP.setErrorEnabled(true);
            inputLayoutCEP.setError("Preenchimento Obrigatório");
            flag = false;
        } else {
            inputLayoutCEP.setErrorEnabled(false);
        }

        if (inputLayoutEstado.getEditText().getText().toString().equals("")) {
            inputLayoutEstado.setErrorEnabled(true);
            inputLayoutEstado.setError("Preenchimento Obrigatório");
            flag = false;
        } else {
            inputLayoutEstado.setErrorEnabled(false);
        }

        if (inputLayoutCidade.getEditText().getText().toString().equals("")) {
            inputLayoutCidade.setErrorEnabled(true);
            inputLayoutCidade.setError("Preenchimento Obrigatório");
            flag = false;
        } else {
            inputLayoutCidade.setErrorEnabled(false);
        }

        if (inputLayoutEndereco.getEditText().getText().toString().equals("")) {
            inputLayoutEndereco.setErrorEnabled(true);
            inputLayoutEndereco.setError("Preenchimento Obrigatório");
            flag = false;
        } else {
            inputLayoutEndereco.setErrorEnabled(false);
        }

        if (inputLayoutNumero.getEditText().getText().toString().equals("")) {
            inputLayoutNumero.setErrorEnabled(true);
            inputLayoutNumero.setError("Preenchimento Obrigatório");
            flag = false;
        } else {
            inputLayoutNumero.setErrorEnabled(false);
        }

        return flag;
    }

    private void configuracaoTela() {
        toolbar.setTitle("Cadastro de Usuarios");
        toolbar.setTitleTextColor(getResources().getColor(R.color.corTituloToolbar));
        setSupportActionBar(toolbar);

        ArrayList<String> arraylist = new ArrayList<String>();
        arraylist.add("Idade");
        for (int i = 18; i < 130; i++)
            arraylist.add(String.valueOf(i));

        radioGroupGenero.check(R.id.radioButtonMasculino);

        cpfMask = Mask.insert("###.###.###-##", inputLayoutCPF.getEditText());
        inputLayoutCPF.getEditText().addTextChangedListener(cpfMask);

        telefoneMask = Mask.insert("(##)#####-####", inputLayoutTelefone.getEditText());
        inputLayoutTelefone.getEditText().addTextChangedListener(telefoneMask);

        cepMask = Mask.insert("#####-###", inputLayoutCEP.getEditText());
        inputLayoutCEP.getEditText().addTextChangedListener(cepMask);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arraylist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIdade.setAdapter(adapter);
    }

    private void abrirLoginActivity() {
        Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
