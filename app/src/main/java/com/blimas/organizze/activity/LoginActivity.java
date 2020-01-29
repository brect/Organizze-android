package com.blimas.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blimas.organizze.R;
import com.blimas.organizze.config.ConfiguracaoFirebase;
import com.blimas.organizze.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {


    private EditText campoEmail, campoSenha;
    private Button btnEfetuarLogin;

    private Usuario usuario;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        campoEmail = findViewById(R.id.inputEmail);
        campoSenha = findViewById(R.id.inputPass);

        btnEfetuarLogin = findViewById(R.id.btnEfetuarLogin);

        btnEfetuarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textoEmail = campoEmail.getText().toString();
                String textoPw = campoSenha.getText().toString();

                if(!textoEmail.isEmpty() || textoEmail == null  &&
                        textoPw.isEmpty() || textoPw == null ){

                    usuario = new Usuario();

                    usuario.setEmail( textoEmail );
                    usuario.setSenha( textoPw );

                    validarLogin();

                }else{
                    Toast.makeText(LoginActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void validarLogin() {

        auth = ConfiguracaoFirebase.getAuth();
        auth.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Sucesso ao fazer login", Toast.LENGTH_SHORT).show();
                }else{

                    String exception = "";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        exception = "E-mail e senha não correspondem a um usuário válido";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        exception = "Por favor, digite um e-mail válido";
                    }catch (Exception e){
                        Toast.makeText(LoginActivity.this, "Erro ao fazer login " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
        });

    }
}
