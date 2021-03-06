package com.example.firebaseautenticacao;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class PrincipalActivity extends AppCompatActivity implements View.OnClickListener {

    Button button_Deslogar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        button_Deslogar = (Button) findViewById(R.id.button_deslogar);

        button_Deslogar.setOnClickListener(this);
    }


//ao clicar no botao deslogar, o usuario sera deslogado do firebase e do app
    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.button_deslogar:
                FirebaseAuth.getInstance().signOut();
                finish();
                break;
        }
    }
}
