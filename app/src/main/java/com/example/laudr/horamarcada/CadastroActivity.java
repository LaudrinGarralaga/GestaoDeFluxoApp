package com.example.laudr.horamarcada;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.regex.Pattern;

public class CadastroActivity extends AppCompatActivity {

    private EditText edtNomeCad, edtEmailCad, edtSenhaCad, edtConfirmaCad;
    private Button btnCadastrarCad;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        edtNomeCad = findViewById(R.id.edtNomeCad);
        edtEmailCad = findViewById(R.id.edtEmailCad);
        edtSenhaCad = findViewById(R.id.edtSenhaCad);
        edtConfirmaCad = findViewById(R.id.edtConfirmaCad);
        btnCadastrarCad = findViewById(R.id.btnCadastrarCad);

        btnCadastrarCad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String nome = edtNomeCad.getText().toString();
                final String email = edtEmailCad.getText().toString();
                String senha = edtSenhaCad.getText().toString();
                String confirma = edtConfirmaCad.getText().toString();

                String url = "http://192.168.0.107/cadastra_usuarios.php";

                if (confirma.equals(senha)) {
                    if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || confirma.isEmpty()) {
                        final AlertDialog.Builder alerta = new AlertDialog.Builder(CadastroActivity.this);
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
                    } else if (!email.matches(emailPattern)) {
                        final AlertDialog.Builder alerta = new AlertDialog.Builder(CadastroActivity.this);
                        alerta.setTitle("Aviso");
                        alerta.setMessage("Insira um email válido.");
                        alerta.setCancelable(false);
                        alerta.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        AlertDialog alertDialog = alerta.create();
                        alertDialog.show();
                    } else if (senha.length() <= 5) {
                        final AlertDialog.Builder alerta = new AlertDialog.Builder(CadastroActivity.this);
                        alerta.setTitle("Aviso");
                        alerta.setMessage("Senha deve maior que 5.");
                        alerta.setCancelable(false);
                        alerta.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        AlertDialog alertDialog = alerta.create();
                        alertDialog.show();
                    } else if (nome.length() <= 2) {
                            final AlertDialog.Builder alerta = new AlertDialog.Builder(CadastroActivity.this);
                            alerta.setTitle("Aviso");
                            alerta.setMessage("Nome de ser maior que 2.");
                            alerta.setCancelable(false);
                            alerta.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            AlertDialog alertDialog = alerta.create();
                            alertDialog.show();
                    }else {
                        Ion.with(CadastroActivity.this)
                                .load(url)
                                .setBodyParameter("nome_app", nome)
                                .setBodyParameter("email_app", email)
                                .setBodyParameter("senha_app", senha)
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {
                                        try {
                                            String RETORNO = result.get("CADASTRO").getAsString();
                                            if (RETORNO.equals("EMAIL_ERROR")) {
                                                final AlertDialog.Builder alerta = new AlertDialog.Builder(CadastroActivity.this);
                                                alerta.setTitle("Aviso");
                                                alerta.setMessage("Email já cadastrado.");
                                                alerta.setCancelable(false);
                                                alerta.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                    }
                                                });
                                                AlertDialog alertDialog = alerta.create();
                                                alertDialog.show();
                                            } else if (RETORNO.equals("SUCESSO")) {

                                                final ProgressDialog progressDialog = new ProgressDialog(CadastroActivity.this);
                                                progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
                                                progressDialog.setCancelable(false);
                                                progressDialog.setTitle("Registrando");
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
                                                        CadastroActivity.this.runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Intent abrePrincipal = new Intent(CadastroActivity.this, MainActivity.class);
                                                                startActivity(abrePrincipal);
                                                            }
                                                        });

                                                    }
                                                });
                                                t.start();
                                                progressDialog.show();

                                                int id = result.get("ID").getAsInt();
                                                SharedPreferences.Editor pref = getSharedPreferences("info", MODE_PRIVATE).edit();

                                                pref.putString(encrypt("nome"), encrypt(nome));
                                                pref.putString(encrypt("email"), encrypt(email));
                                                pref.putInt("id", id);

                                                pref.commit();

                                            } else {
                                                Toast.makeText(CadastroActivity.this, "Ocorreu um erro!", Toast.LENGTH_LONG).show();
                                            }
                                        } catch (NullPointerException e1) {
                                            final AlertDialog.Builder alerta = new AlertDialog.Builder(CadastroActivity.this);
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
                } else {
                    final AlertDialog.Builder alerta = new AlertDialog.Builder(CadastroActivity.this);
                    alerta.setTitle("Aviso");
                    alerta.setMessage("As senha não conferem");
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
    public String encrypt(String palavra){
        return Base64.encodeToString(palavra.getBytes(), Base64 .DEFAULT);
    }
}
