package com.example.laudr.horamarcada;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Base64;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView txtNome, txtEmail, txtID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtNome = findViewById(R.id.txtNome);
        txtEmail = findViewById(R.id.txtEmail);
        //txtID = findViewById(R.id.txtId);

        SharedPreferences pref = getSharedPreferences("info", MODE_PRIVATE);

        String nomeEncrytp = pref.getString(encrypt("nome"), "");
        String emailEncrytp = pref.getString(encrypt("email"), "");
        int id_user = pref.getInt("id",18) ;

        String nome = decrypt(nomeEncrytp);
        String email = decrypt(emailEncrytp);


         txtNome.setText("Seja bem vindo " + nome);
         txtEmail.setText("");
         //txtID.setText("ID: " + id);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            SharedPreferences.Editor pref = getSharedPreferences("info", MODE_PRIVATE).edit();
            pref.clear();
            pref.commit();
            finish();
            Intent abrePrincipal = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(abrePrincipal);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_iniciar) {
            Intent abreAfazer = new Intent(MainActivity.this, MainActivity.class);
            startActivity(abreAfazer);
        } else if (id == R.id.nav_afazer) {
            Intent abreAfazer = new Intent(MainActivity.this, AfazerActivity.class);
            startActivity(abreAfazer);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public String encrypt(String palavra){
        return Base64.encodeToString(palavra.getBytes(), Base64.DEFAULT);
    }
    public String decrypt(String palavra) {
        return new String(Base64.decode(palavra, Base64.DEFAULT));
    }

}
