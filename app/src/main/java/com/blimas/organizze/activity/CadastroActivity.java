package com.blimas.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blimas.organizze.R;
import com.blimas.organizze.config.ConfiguracaoFirebase;
import com.blimas.organizze.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CadastroActivity extends AppCompatActivity {

    private EditText campoNome, campoEmail, campoPw;
    private Button btnCadastrar;

    private FirebaseAuth auth;

    private Usuario user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);


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

                if(!textoNome.isEmpty() || textoNome == null &&
                        !textoEmail.isEmpty() || textoEmail == null  &&
                        textoPw.isEmpty() || textoPw == null ){

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
                    Toast.makeText(CadastroActivity.this, "Sucesso ao cadastrar usuário", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(CadastroActivity.this, "Erro ao cadastrar usuário", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
