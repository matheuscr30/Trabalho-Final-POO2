package com.matheus.magazinehenrique.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StreamDownloadTask;
import com.matheus.magazinehenrique.R;
import com.matheus.magazinehenrique.fragment.ProdutosFragment;
import com.matheus.magazinehenrique.model.Produto;
import com.matheus.magazinehenrique.slidingTabUtils.TabAdapter;
import com.matheus.magazinehenrique.config.ConfiguracaoFirebase;
import com.matheus.magazinehenrique.slidingTabUtils.SlidingTabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private FirebaseAuth firebaseAuth;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private TextView nomeDrawerLayout;
    private TextView emailDrawerLayout;
    private NavigationView navigationView;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private TabAdapter tabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();

        viewPager = (ViewPager)findViewById(R.id.vp_pagina);
        slidingTabLayout = (SlidingTabLayout)findViewById(R.id.stl_tabs);

        navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toolbar = (Toolbar)findViewById(R.id.toolbarMain);
        toolbar.setTitle("Summer EletroFashion");
        setSupportActionBar(toolbar);

        configNavigationDrawer();
        configSlidingTabLayout();

        viewPager.setCurrentItem(1);
    }

    public void configSlidingTabLayout(){
        //Configurar SlidingTabLayouts
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccent));

        //Configurar Adapter
        tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        slidingTabLayout.setViewPager(viewPager);
    }

    public void configNavigationDrawer(){
        navigationView.setCheckedItem(R.id.nav_item_inicio);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayoutMain);
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
        inflater.inflate(R.menu.opcoes_menu, menu);

        SearchView searchView =
                (SearchView) menu.findItem(R.id.app_bar_search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mudaViewPagerPadrao();
                ProdutosFragment fragment = (ProdutosFragment)tabAdapter.getCurrentFragment();
                fragment.buscaPorNome(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //Toast.makeText(MainActivity.this, s + "textchange",  Toast.LENGTH_SHORT).show();
                return false;
            }
        });

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
                drawer.closeDrawers();
                break;
            case R.id.nav_item_pedidos:
                drawer.closeDrawers();
                abrirCompraActivity();
                break;
            case R.id.nav_item_carrinho:
                drawer.closeDrawers();
                abrirCarrinhoActivity();
                break;
            case R.id.nav_item_sair:
                deslogarUsuario();
                break;
        }
        return true;
    }

    public void mudaViewPagerPadrao(){
        viewPager.setCurrentItem(1);
    }

    public void abrirCompraActivity(){
        Intent intent = new Intent(MainActivity.this, CompraActivity.class);
        startActivity(intent);
    }

    public void abrirCarrinhoActivity(){
        Intent intent = new Intent(MainActivity.this, CarrinhoActivity.class);
        startActivity(intent);
    }

    public void deslogarUsuario(){
        ConfiguracaoFirebase.getFirebaseAuth().signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mudaViewPagerPadrao();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayoutMain);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}