package com.example.firebaseautenticacao;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginEmailActivity extends AppCompatActivity implements View.OnClickListener {

    private Button Button_cadastrar_ok, Button_Recuperar_senha;
    private EditText EditText_Email_Login, EditText_Senha_Login;

    private FirebaseAuth auth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginemail);

        EditText_Email_Login = ( EditText ) findViewById(R.id.editText_Email_Login);
        EditText_Senha_Login = ( EditText ) findViewById(R.id.editText_Senha_Login);

        Button_cadastrar_ok = ( Button ) findViewById(R.id.button_cadastrar_ok);
        Button_Recuperar_senha = ( Button ) findViewById(R.id.button_Recuperar_senha);

        Button_cadastrar_ok.setOnClickListener(this);
        Button_Recuperar_senha.setOnClickListener(this);

        //acesso aos recurso da tela do firebase
        auth = FirebaseAuth.getInstance();
    }


    //qualquel dos buttons acertados vira para cá!
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.button_cadastrar_ok:

                loginEmail();

                break;

            case R.id.button_Recuperar_senha:

                recuperaSenha();

                break;
        }
    }


    private void recuperaSenha(){

        String email = EditText_Email_Login.getText().toString().trim();

        if(email.isEmpty()){

            Toast.makeText(this, "Insira pelo menos o e-mail para recuperar a senha!!", Toast.LENGTH_LONG).show();

        }else {

            enviarEmail(email);
        }

    }

    private  void enviarEmail(String email){

        auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) { //essa variavel aVoid nao retorna o erro do firebase

                Toast.makeText(getBaseContext(), "Enviamos uma MSG para o e-mail  informado acima!", Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                String erro = e.toString();

                Util.opcoesErro(getBaseContext(),erro);

            }
        });
    }


    private void loginEmail() {

        String email = EditText_Email_Login.getText().toString().trim();
        String senha = EditText_Senha_Login.getText().toString().trim();

        if (email.isEmpty() || senha.isEmpty()) {

            Toast.makeText(getBaseContext(), "Insira os camppos obrigatorios!!", Toast.LENGTH_LONG).show();

        } else {

            if(Util.verificarInternet(this)){

                //getSystemService - só pode ser usado dentro de activity
                ConnectivityManager conexao = ( ConnectivityManager ) getSystemService(CONNECTIVITY_SERVICE);


                //verificar se usuario esta com internet no exato momento. se sim executa o metodo abaixo
                confirmarLoginSenha(email, senha);

            }else {

                Toast.makeText(getBaseContext(), "Erro - Verifique sua WIffi ou 3g esta Bugada!!!", Toast.LENGTH_LONG).show();
            }
        }
    }


//metodo com o dever de confirmar login e senha, e tratando seus devidos erros atraves de metodos criados expliado a frente
    private void confirmarLoginSenha(String email, String senha) {

        auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    // se login ok, ira direcionar para a activity principal
                    startActivity(new Intent(getBaseContext(), PrincipalActivity.class));

                    Toast.makeText(getBaseContext(), "Usuario Logado com scesso!!", Toast.LENGTH_LONG).show();

                    finish();  //para nao retornar para tela de login

                } else {

                    String resposta = task.getException().toString();
                    Util.opcoesErro(getBaseContext(), resposta);
                    // Toast.makeText(getBaseContext(), "Erro ao logar Usuario!!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
