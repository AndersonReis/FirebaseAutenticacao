package com.example.firebaseautenticacao;

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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CadastrarActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editText_Email_Cadastrar, editText_SenhaCadastro, editText_SenhaRepetir;
    Button button_CadastrarUsuario, button_Cancelar;

    private FirebaseAuth mauth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        editText_Email_Cadastrar = ( EditText ) findViewById(R.id.editText_Email_Cadastrar);
        editText_SenhaCadastro = ( EditText ) findViewById(R.id.editText_SenhaCadastro);
        editText_SenhaRepetir = ( EditText ) findViewById(R.id.editText_SenhaRepetir);

        button_CadastrarUsuario = ( Button ) findViewById(R.id.button_cadastrarUsuario);
        button_Cancelar = ( Button ) findViewById(R.id.button_Cancelar);

        button_CadastrarUsuario.setOnClickListener(this);
        button_Cancelar.setOnClickListener(this);

        mauth = FirebaseAuth.getInstance();
        //instanciando FirebaseAuth na variavel auth. significa que com essa variavel podemos ter acesso as opcoes da tela do firebase

    }

    //se clicar em qualquer um dos botoes vira pra cá, colocaremos um switch para diferenciar os butoes.
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.button_cadastrarUsuario:

                cadastrar();

                break;

            case R.id.button_Cancelar:

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();

                break;
        }
    }


    private void cadastrar() {

        //criado var. para buscar o que foi digitado na tela da activity na forma de string
        //metodo trim() - > elimina espaços na digitação do usuario
        String email = editText_Email_Cadastrar.getText().toString().trim();
        String senha = editText_SenhaCadastro.getText().toString().trim();
        String confirmarSenha = editText_SenhaRepetir.getText().toString().trim();


        //verificando se usuario deixou algum campo em branco
        if (senha.isEmpty() || email.isEmpty() || confirmarSenha.isEmpty()) {

            Toast.makeText(getBaseContext(), "Preencha os campos!!", Toast.LENGTH_LONG).show();


        } else {

            //estamos comparando a senha digitada com a o campo de confirmação de senha
            if (senha.contentEquals(confirmarSenha)) {

                if (Util.verificarInternet(this)) {

                    criarUsuario(email, senha);

                } else {

                    Toast.makeText(getBaseContext(), "Erro - Verifique se sua WIffi ou 3g esta funcionando!", Toast.LENGTH_LONG).show();

                }
            } else {

                Toast.makeText(getBaseContext(), "Senha Incongruentes!!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void criarUsuario(final String email, final String senha) {


        //criar usuario com email e password
        mauth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) { // task, nos retorna informaçoes do firebase sobre erro ocasionados durante criação de email e senha

                //boolean resultado = task.isSuccessful(); //retorna true ou false

                if (task.isSuccessful()) {
                    Toast.makeText(getBaseContext(), "Cadastro Realizado com sucesso!", Toast.LENGTH_LONG).show();
                    editText_Email_Cadastrar.setText("");
                    editText_SenhaCadastro.setText("");
                    editText_SenhaRepetir.setText("");

                } else {

                    //pegando o erro informado pelo firebase e colocando na variavel resposta
                    String resposta = task.getException().toString();
                    Util.opcoesErro(getBaseContext(), resposta);

                }
            }
        });
    }

}


