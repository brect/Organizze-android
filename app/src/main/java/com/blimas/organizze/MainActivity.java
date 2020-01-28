package com.blimas.organizze;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blimas.organizze.activity.CadastroActivity;
import com.blimas.organizze.activity.LoginActivity;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

public class MainActivity extends IntroActivity {


    private TextView btnLogar;
    private Button btnCadastrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        btnLogar = findViewById(R.id.btnLogar);
        btnCadastrar = findViewById(R.id.btnCadastrar);

        configuraFragmentsSlide();


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


//    public void efetuarCadastro(){
//        Intent intent = new Intent(getApplicationContext(), CadastroActivity.class);
//        startActivity(intent);
//    }

//    public void efetuarLogin(){
//        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//        startActivity(intent);
//    }

}
