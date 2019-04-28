package com.example.laudr.horamarcada;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class LoginActivity  extends AppCompatActivity {

    private EditText edtLogin, edtSenha;
    private Button btnLogar;
    private TextView txtCadastrar;
    private ProgressDialog caixa;
    private String URL = "http://10.137.237.72/login_usuarios.php/";

    public void verificaDados(){
       SharedPreferences pref =  getSharedPreferences("info", MODE_PRIVATE);
       String emailEncrypt = pref.getString(encrypt("email"), "");

       String email = decrypt(emailEncrypt);

       if (!email.isEmpty()) {
           Intent abrePrincipal = new Intent(LoginActivity.this, MainActivity.class);
           startActivity(abrePrincipal);
       }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verificaDados();

        edtLogin = findViewById(R.id.edtLogin);
        edtSenha = findViewById(R.id.edtSenha);
        txtCadastrar = findViewById(R.id.txtCadastrar);
        btnLogar = findViewById(R.id.btnLogar);

        txtCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent abreCad = new Intent(LoginActivity.this, CadastroActivity.class);
                startActivity(abreCad);
            }
        });

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = edtLogin.getText().toString();
                String senha = edtSenha.getText().toString();

            if (login.isEmpty() || senha.isEmpty()) {
                final AlertDialog.Builder alerta = new AlertDialog.Builder(LoginActivity.this);
                alerta.setTitle("Aviso");
                alerta.setMessage("Todos os campos são obrigatórios.");
                alerta.setCancelable(false);
                alerta.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog = alerta.create();
                alertDialog.show();
                } else {
                    Ion.with(LoginActivity.this)
                            .load(URL)
                            .setBodyParameter("email_app", login)
                            .setBodyParameter("senha_app", senha)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    try {
                                        String RETORNO = result.get("LOGIN").getAsString();
                                        if (RETORNO.equals("ERRO")) {
                                            final AlertDialog.Builder alerta = new AlertDialog.Builder(LoginActivity.this);
                                            alerta.setTitle("Aviso");
                                            alerta.setMessage("Email ou senha incorretos.");
                                            alerta.setCancelable(false);
                                            alerta.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                }
                                            });
                                            AlertDialog alertDialog = alerta.create();
                                            alertDialog.show();
                                        } else if (RETORNO.equals("SUCESSO")) {
                                            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                                            progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
                                            progressDialog.setCancelable(false);
                                            progressDialog.setTitle("Autenticando");
                                            progressDialog.setMessage("Espere um pouco ....");
                                            progressDialog.setMax(100);
                                            progressDialog.setProgress(0);
                                            Thread t = new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    int progress = 0;
                                                    while (progress <= 100) {

                                                        try {
                                                            progressDialog.setProgress(progress);
                                                            progress++;
                                                            Thread.sleep(30);


                                                        } catch (Exception ex) {

                                                        }

                                                    }
                                                    progressDialog.dismiss();
                                                    LoginActivity.this.runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Intent abrePrincipal = new Intent(LoginActivity.this, MainActivity.class);
                                                            startActivity(abrePrincipal);
                                                            finish();

                                                        }
                                                    });

                                                }
                                            });
                                            t.start();
                                            progressDialog.show();

                                            String nome = result.get("NOME").getAsString();
                                            String email = result.get("EMAIL").getAsString();
                                            int id = result.get("ID").getAsInt();

                                            SharedPreferences.Editor pref = getSharedPreferences("info", MODE_PRIVATE).edit();

                                            pref.putString(encrypt("nome"), encrypt(nome));
                                            pref.putString(encrypt("email"), encrypt(email));
                                            pref.putInt("id", id);

                                            pref.commit();

                                        } else {
                                            final AlertDialog.Builder alerta = new AlertDialog.Builder(LoginActivity.this);
                                            alerta.setTitle("Erro");
                                            alerta.setMessage("Algo inesperado aconteceu.");
                                            alerta.setCancelable(false);
                                            alerta.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                }
                                            });
                                            AlertDialog alertDialog = alerta.create();
                                            alertDialog.show();
                                        }
                                    } catch (NullPointerException e1) {
                                        final AlertDialog.Builder alerta = new AlertDialog.Builder(LoginActivity.this);
                                        alerta.setTitle("Algo inesperado aconteceu");
                                        alerta.setMessage("Verifique sua conexão.");
                                        alerta.setCancelable(false);
                                        alerta.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });
                                        AlertDialog alertDialog = alerta.create();
                                        alertDialog.show();
                                    }
                                }
                            });
                }
            }
        });
    }

    public String encrypt(String palavra){
        return Base64.encodeToString(palavra.getBytes(), Base64.DEFAULT);
    }
    public String decrypt(String palavra) {
        return new String(Base64.decode(palavra, Base64.DEFAULT));
    }


}
