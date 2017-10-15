package com.matheus.magazinehenrique.activity;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.matheus.magazinehenrique.R;
import com.matheus.magazinehenrique.config.ConfiguracaoFirebase;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.toolbarMain);
        toolbar.setTitle("Summer EletroFashion");
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawerLayoutMain);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.openNavigation, R.string.closeDrawerLayout);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.opcoes_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.item_sair:
                deslogarUsuario();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void deslogarUsuario(){
        ConfiguracaoFirebase.getFirebaseAuth().signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}