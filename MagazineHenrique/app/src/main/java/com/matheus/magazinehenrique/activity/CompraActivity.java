package com.matheus.magazinehenrique.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.matheus.magazinehenrique.R;
import com.matheus.magazinehenrique.adapter.CarrinhoAdapter;
import com.matheus.magazinehenrique.adapter.CompraAdapter;
import com.matheus.magazinehenrique.config.ConfiguracaoFirebase;
import com.matheus.magazinehenrique.fragment.ProdutosFragment;
import com.matheus.magazinehenrique.model.Carrinho;
import com.matheus.magazinehenrique.model.Compra;
import com.matheus.magazinehenrique.model.Produto;
import com.matheus.magazinehenrique.tools.Preferencias;

import java.util.ArrayList;

public class CompraActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private ArrayList<Compra>compras;
    private CompraAdapter adapter;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListenerCompras;

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private TextView nomeDrawerLayout;
    private TextView emailDrawerLayout;
    private NavigationView navigationView;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compra);

        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();

        compras = new ArrayList<>();

        navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        configRecyclerView();

        toolbar = (Toolbar)findViewById(R.id.toolbarCompra);
        toolbar.setTitle("Minhas Compras");
        setSupportActionBar(toolbar);

        configNavigationDrawer();
    }

    public void configRecyclerView(){
        recyclerView = (RecyclerView) findViewById(R.id.rvCompra);
        LinearLayoutManager llm = new LinearLayoutManager(CompraActivity.this);
        recyclerView.setLayoutManager(llm);
        adapter = new CompraAdapter(compras, CompraActivity.this);
        recyclerView.setAdapter(adapter);

        //Listener para recuperar contatos
        Preferencias preferencias = new Preferencias(CompraActivity.this);
        String cpfUsuario = preferencias.getCPF();
        databaseReference = ConfiguracaoFirebase.getDatabaseReference();
        DatabaseReference aux = databaseReference.child("compras/" + cpfUsuario + "/");
        valueEventListenerCompras = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Limpar lista
                compras.clear();

                for(DataSnapshot dados : dataSnapshot.getChildren()) {
                    Compra compra = dados.getValue(Compra.class);
                    compras.add(compra);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        aux.addValueEventListener(valueEventListenerCompras);
    }

    public void configNavigationDrawer(){
        navigationView.setCheckedItem(R.id.nav_item_inicio);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayoutCompra);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.openNavigation, R.string.closeDrawerLayout);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        nomeDrawerLayout = navigationView.getHeaderView(0).findViewById(R.id.tNome);
        emailDrawerLayout = navigationView.getHeaderView(0).findViewById(R.id.tEmail);

        FirebaseUser user = firebaseAuth.getCurrentUser();
        nomeDrawerLayout.setText(user.getDisplayName());
        emailDrawerLayout.setText(user.getEmail());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.opcoes_menu_compra, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_carrinho:
                abrirCarrinhoActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_item_inicio:
                abrirMainActivity();
                break;
            case R.id.nav_item_pedidos:
                drawer.closeDrawers();
                break;
            case R.id.nav_item_carrinho:
                abrirCarrinhoActivity();
                break;
            case R.id.nav_item_sair:
                deslogarUsuario();
                break;
        }
        return true;
    }

    public void abrirMainActivity(){
        finish();
    }

    public void abrirCarrinhoActivity(){
        Intent intent = new Intent(CompraActivity.this, CarrinhoActivity.class);
        startActivity(intent);
    }

    public void deslogarUsuario(){
        ConfiguracaoFirebase.getFirebaseAuth().signOut();
        Intent intent = new Intent(CompraActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayoutCompra);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
