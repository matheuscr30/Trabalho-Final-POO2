package com.matheus.magazinehenrique.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.matheus.magazinehenrique.R;
import com.matheus.magazinehenrique.dao.ClienteDAO;
import com.matheus.magazinehenrique.tools.Mask;
import com.matheus.magazinehenrique.model.Cliente;
import com.matheus.magazinehenrique.tools.ValidaCPF;

import java.util.ArrayList;

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
    private TextInputLayout inputLayoutConfirmarSenha;
    private FloatingActionButton fabBotaoCadastro;

    private TextWatcher cpfMask;
    private TextWatcher telefoneMask;
    private TextWatcher cepMask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        toolbar = (Toolbar)findViewById(R.id.toolbarCadastro);
        spinnerIdade = (Spinner)findViewById(R.id.spinnerIdadeCadastro);
        radioGroupGenero = (RadioGroup)findViewById(R.id.radioGroupCadastro);
        inputLayoutNome = (TextInputLayout)findViewById(R.id.inputLayoutNomeCadastro);
        inputLayoutCPF = (TextInputLayout)findViewById(R.id.inputLayoutCPFCadastro);
        inputLayoutEmail = (TextInputLayout)findViewById(R.id.inputLayoutEmailCadastro);
        inputLayoutSenha = (TextInputLayout)findViewById(R.id.inputLayoutSenhaCadastro);
        inputLayoutConfirmarSenha = (TextInputLayout)findViewById(R.id.inputLayoutConfirmarSenhaCadastro);
        inputLayoutTelefone = (TextInputLayout)findViewById(R.id.inputLayoutTelefoneCadastro);
        inputLayoutCEP = (TextInputLayout)findViewById(R.id.inputLayoutCEPCadastro);
        inputLayoutEstado = (TextInputLayout)findViewById(R.id.inputLayoutEstadoCadastro);
        inputLayoutCidade = (TextInputLayout)findViewById(R.id.inputLayoutCidadeCadastro);
        inputLayoutEndereco = (TextInputLayout)findViewById(R.id.inputLayoutEnderecoCadastro);
        inputLayoutNumero = (TextInputLayout)findViewById(R.id.inputLayoutNumeroCadastro);
        fabBotaoCadastro = (FloatingActionButton)findViewById(R.id.fabCadastro);

        fabBotaoCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastraCliente();
            }
        });

        configuracaoTela();
    }

    private void cadastraCliente(){

        if(!validaDados()){
            return;
        }

        Cliente cliente = new Cliente();
        cliente.setNome(inputLayoutNome.getEditText().getText().toString());
        cliente.setCPF(Mask.unmask(inputLayoutCPF.getEditText().getText().toString()));
        cliente.setEmail(inputLayoutEmail.getEditText().getText().toString());
        cliente.setSenha(inputLayoutSenha.getEditText().getText().toString());
        cliente.setIdade(Integer.parseInt(spinnerIdade.getSelectedItem().toString()));

        int radioId = radioGroupGenero.getCheckedRadioButtonId();
        switch (radioId){
            case R.id.radioButtonMasculino:
                cliente.setSexo("M");
                break;
            case R.id.radioButtonFeminino:
                cliente.setSexo("F");
                break;
            default:
                cliente.setSexo("ERRO");
        }

        cliente.setTelefone(Mask.unmask(inputLayoutTelefone.getEditText().getText().toString()));
        cliente.setCEP(Mask.unmask(inputLayoutCEP.getEditText().getText().toString()));
        cliente.setEstado(inputLayoutEstado.getEditText().getText().toString());
        cliente.setCidade(inputLayoutCidade.getEditText().getText().toString());
        cliente.setEndereco(inputLayoutEndereco.getEditText().getText().toString());
        cliente.setNumero(Integer.parseInt(inputLayoutNumero.getEditText().getText().toString()));

        ClienteDAO clienteDAO = new ClienteDAO();
        if(clienteDAO.salvarCliente(cliente))
            Toast.makeText(CadastroActivity.this, "Cadastro Realizado com Sucesso", Toast.LENGTH_SHORT).show();
    }

    private boolean validaDados(){

        Boolean flag = true;

        if(inputLayoutNome.getEditText().getText().toString().equals("")) {
            inputLayoutNome.setError("Preenchimento Obrigatório");
            flag = false;
        }

        String cpf = Mask.unmask(inputLayoutCPF.getEditText().getText().toString());

        if(ValidaCPF.isCPF(cpf)) {
            inputLayoutCPF.setError("CPF Inválido");
            flag = false;
        }

        if(cpf.equals("")){
            inputLayoutCPF.setError("Preenchimento Obrigatório");
            flag = false;
        }

        if(inputLayoutEmail.getEditText().getText().toString().equals("")){
            inputLayoutEmail.setError("Preenchimento Obrigatório");
            flag = false;
        }

        if(inputLayoutSenha.getEditText().getText().toString().equals("")){
            inputLayoutSenha.setError("Preenchimento Obrigatório");
            flag = false;
        }

        if(inputLayoutConfirmarSenha.getEditText().getText().toString().equals("")){
            inputLayoutConfirmarSenha.setError("Preenchimento Obrigatório");
            flag = false;
        }

        if(inputLayoutSenha.getEditText().getText().toString() != inputLayoutConfirmarSenha.getEditText().getText().toString()){
            inputLayoutConfirmarSenha.setError("Senhas Diferentes");
            flag = false;
        }

        if(inputLayoutTelefone.getEditText().getText().toString().equals("")){
            inputLayoutTelefone.setError("Preenchimento Obrigatório");
            flag = false;
        }

        String cep = Mask.unmask(inputLayoutCEP.getEditText().getText().toString());

        if(cep.equals("")){
            inputLayoutCEP.setError("Preenchimento Obrigatório");
            flag = false;
        }

        if(inputLayoutEstado.getEditText().getText().toString().equals("")){
            inputLayoutEstado.setError("Preenchimento Obrigatório");
            flag = false;
        }

        if(inputLayoutCidade.getEditText().getText().toString().equals("")){
            inputLayoutCidade.setError("Preenchimento Obrigatório");
            flag = false;
        }

        if(inputLayoutEndereco.getEditText().getText().toString().equals("")){
            inputLayoutEndereco.setError("Preenchimento Obrigatório");
            flag = false;
        }

        if(inputLayoutNumero.getEditText().getText().toString().equals("")){
            inputLayoutNumero.setError("Preenchimento Obrigatório");
            flag = false;
        }

        return flag;
    }

    private void configuracaoTela(){
        toolbar.setTitle("Cadastro de Usuarios");
        toolbar.setTitleTextColor(getResources().getColor(R.color.corTituloToolbar));
        setSupportActionBar(toolbar);

        ArrayList<String> arraylist = new ArrayList<String>();
        arraylist.add("Idade");
        for(int i = 1; i < 130; i++)
            arraylist.add(String.valueOf(i));

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
}
