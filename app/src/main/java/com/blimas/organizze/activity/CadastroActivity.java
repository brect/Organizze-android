package com.blimas.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blimas.organizze.R;
import com.blimas.organizze.config.ConfiguracaoFirebase;
import com.blimas.organizze.helper.Base64Custom;
import com.blimas.organizze.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {

    private EditText campoNome, campoEmail, campoPw;
    private Button btnCadastrar;

    private FirebaseAuth auth;

    private Usuario user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

//        getSupportActionBar().setTitle("Cadastro");

        campoNome = findViewById(R.id.inputNome);
        campoEmail = findViewById(R.id.inputEmail);
        campoPw = findViewById(R.id.inputPass);

        btnCadastrar = findViewById(R.id.btnConfirmarCadastro);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textoNome = campoNome.getText().toString();
                String textoEmail = campoEmail.getText().toString();
                String textoPw = campoPw.getText().toString();
                /*
                * Valida se os campos foram preenchidos
                */

                if(!textoNome.isEmpty() || textoNome != null &&
                        !textoEmail.isEmpty() || textoEmail != null  &&
                        !textoPw.isEmpty() || textoPw != null ){

                    user = new Usuario();
                    user.setNome(textoNome);
                    user.setEmail(textoEmail);
                    user.setSenha(textoPw);

                    cadastrarUsuario();

                }else{
                    Toast.makeText(CadastroActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void cadastrarUsuario(){

        auth = ConfiguracaoFirebase.getAuth();

        auth.createUserWithEmailAndPassword(
                user.getEmail(), user.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    String idUser = Base64Custom.encodeBase64(user.getEmail());
                    user.setIdUser(idUser);
                    user.salvar();

                    finish();
//                    Toast.makeText(CadastroActivity.this, "Sucesso ao cadastrar usuário", Toast.LENGTH_SHORT).show();
                }else{


                    String excesao = "";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        excesao = "Digite uma senha mais forte";
                    }catch (FirebaseAuthEmailException e){
                        excesao = "Por favor, digite um e-mail válido";
                    }catch (FirebaseAuthUserCollisionException e){
                        excesao = "Esta conta já está cadastrada";
                    }catch (Exception e){
                        excesao = "Erro ao cadastrar usuário " + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroActivity.this, "Erro ao cadastrar usuário", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
