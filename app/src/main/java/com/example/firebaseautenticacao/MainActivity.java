package com.example.firebaseautenticacao;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView  cardview_logingoogle;
    private Button button_login, button_cadastrar;

    private FirebaseAuth auth;
    private FirebaseUser user;   //get dados do usuario no firebase

    private FirebaseAuth.AuthStateListener authStateListener; //qual estado da nossa autenticação no momento, caso logado ou deslogado

    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_login = ( Button ) findViewById(R.id.button_login);
        button_cadastrar = ( Button ) findViewById(R.id.button_cadastrar);
        cardview_logingoogle = (CardView ) findViewById(R.id.cardlogingoogle);


        button_login.setOnClickListener(this);
        button_cadastrar.setOnClickListener(this);
        cardview_logingoogle.setOnClickListener(this);


        auth = FirebaseAuth.getInstance();  // dando poderes a variavel auth

        estadoAutenticacao();

        servicosGoogle();

    }

//---------------------------------------------------SERVIÇOES GOOGLE---------------------------------------------------------------------

    private void servicosGoogle(){

        //INICIALIZA OS SERVICOES DO GOOGLE NO APP
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

    }


    private  void signInGoogle(){

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if(account == null){

            Intent intent = googleSignInClient.getSignInIntent();
            //estamos com intenção de logar com a conta do google
            startActivityForResult(intent, 000);
            //inciando activity para obter resultado de link com o google

        }else {

            Toast.makeText(this, "já logado", Toast.LENGTH_LONG).show();
            startActivity(new Intent( getBaseContext(), PrincipalActivity.class ));



            //só caira dentro desse else se existir alguem logado com conta do google
        }

    }


    //nesse met. é que obtemos resposta do google da comunicação
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(resultCode == 000) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {

                GoogleSignInAccount account = task.getResult(ApiException.class);

                startActivity(new Intent( getBaseContext(), PrincipalActivity.class ));

            }catch (ApiException e){

                Toast.makeText(this, "Erro ao logar com login do google!", Toast.LENGTH_LONG).show();
            }


        }   else {


        }


    }

  //-----------------------------------------------------AUTENTICAÇÃO FIREBASE-------------------------------------------------------------
    //metodo que fiscaliza ou/ ouve o estado da autenticação
    private void estadoAutenticacao() {

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {

                    Toast.makeText(getBaseContext(), "Usuário " + (( FirebaseUser ) user).getEmail() + " esta logado", Toast.LENGTH_LONG).show();

                }
            }
        };
    }


//---------------------------------------------------TRATAMENTO DE ERRO -----------------------------------------------------------------

    //Ao clicar tanto no button login ou cadastrar executara esse metodo abaixo, entao faremos um
    //switch para diferenciar os buttons
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.cardlogingoogle:

                //fara todo procedimento para logarmos com conta do google
                signInGoogle();

                break;

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

        if (authStateListener != null) {

            auth.removeAuthStateListener(authStateListener);
        }

    }
//-----------------------------------------------------------------------------------------------------------------------

}
