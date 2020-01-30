package com.blimas.organizze.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.blimas.organizze.R;
import com.blimas.organizze.activity.CadastroActivity;
import com.blimas.organizze.activity.LoginActivity;
import com.blimas.organizze.config.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

import java.util.zip.Inflater;

public class MainActivity extends IntroActivity {


    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        configuraFragmentsSlide();

    }

    @Override
    protected void onStart() {
        super.onStart();
        verificarUsuarioLogado();
    }

    private void configuraFragmentsSlide() {
        setButtonBackVisible(false);
        setButtonNextVisible(false);

        addSlide(new FragmentSlide
                .Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_1)
                .canGoBackward(false)
                .build());

        addSlide(new FragmentSlide
                .Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_2)
                .build());

        addSlide(new FragmentSlide
                .Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_3)
                .build());

        addSlide(new FragmentSlide
                .Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_4)
                .build());

        addSlide(new FragmentSlide
                .Builder()
                .background(android.R.color.white)
                .fragment(R.layout.into_cadastro)
                .canGoForward(false)
                .build());

    }


    public void efetuarCadastro(View view){
        Intent intent = new Intent(getApplicationContext(), CadastroActivity.class);
        startActivity(intent);
    }

    public void efetuarLogin(View view){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    public void verificarUsuarioLogado(){
        auth = ConfiguracaoFirebase.getAuth();
//        auth.signOut();
        if( auth.getCurrentUser() != null ){
            abrirTelaPrincipal();
        }
    }

    public void abrirTelaPrincipal(){
        startActivity(new Intent(this, PrincipalActivity.class));
    }
}
