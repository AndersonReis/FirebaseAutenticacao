package com.example.firebaseautenticacao;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button_login, button_cadastrar;

    private FirebaseAuth auth;
    private FirebaseUser user;   //get dados do usuario no firebase

    private FirebaseAuth.AuthStateListener authStateListener; //qual estado da nossa autenticação no momento, caso logado ou deslogado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_login = ( Button ) findViewById(R.id.button_login);
        button_cadastrar = ( Button ) findViewById(R.id.button_cadastrar);


        button_login.setOnClickListener(this);
        button_cadastrar.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();  // dando poderes a variavel auth

        estadoAutenticacao();

    }

    //metodo que fiscaliza ou/ ouve o estado da autenticação
    private void estadoAutenticacao(){

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null){

                    Toast.makeText(getBaseContext(), "Usuário "+ (( FirebaseUser ) user).getEmail() + " esta logado", Toast.LENGTH_LONG).show();

                }else {


                }

            }
        };

    }

    //Ao clicar tanto no button login ou cadastrar executara esse metodo abaixo, entao faremos um
    //switch para diferenciar os buttons
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.button_login:

                user = auth.getCurrentUser(); //usuario corrente ou atual conectado no app e authenticado no firebase

                if (user == null) {

                    startActivity(new Intent(this, LoginEmailActivity.class));

                } else {

                    startActivity(new Intent(this, PrincipalActivity.class));
                }

                break;

            case R.id.button_cadastrar:

                startActivity(new Intent(this, CadastrarActivity.class));

                break;

        }

    }

//-----------------------------------relativo  metodo estadoAutenticação()----------------------------------------------
    @Override
    protected void onStart() {
        super.onStart();

        //ouvinte para nossa variavel
        auth.addAuthStateListener(authStateListener);
    }

    //toda vez que o usuario abrir outro app e o nosso cair em stop entao será finalizado o login do mesmo
    @Override
    protected void onStop() {
        super.onStop();

        if(authStateListener != null){

            auth.removeAuthStateListener(authStateListener);
        }

    }
//-----------------------------------------------------------------------------------------------------------------------

}
