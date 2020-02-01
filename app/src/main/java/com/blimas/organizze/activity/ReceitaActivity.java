package com.blimas.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.blimas.organizze.R;
import com.blimas.organizze.config.ConfiguracaoFirebase;
import com.blimas.organizze.helper.Base64Custom;
import com.blimas.organizze.helper.DateCustom;
import com.blimas.organizze.model.Movimentacao;
import com.blimas.organizze.model.Usuario;
import com.blimas.organizze.util.MonetaryMask;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class ReceitaActivity extends AppCompatActivity {


    private TextInputEditText campoData,
            campoCategoria,
            campoDescricao;

    private EditText campoValor;
    private FloatingActionButton fabSalvar;

    private Movimentacao movimentacao;

    private DatabaseReference dbRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth authUser = ConfiguracaoFirebase.getAuth();

    private Double receitaTotal;
    private Double receitaAtualizada;

    private MonetaryMask moneyMask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receita);

        campoValor = findViewById(R.id.valorReceita);
//        Locale mLocale = new Locale("pt", "BR");
//        campoValor.addTextChangedListener(new MonetaryMask(campoValor, mLocale));

        campoData = findViewById(R.id.dataReceita);
        campoCategoria  = findViewById(R.id.categoriaReceita);
        campoDescricao = findViewById(R.id.descricaoReceita);

        //preenche o campo data com a data atual
        campoData.setText(DateCustom.dataAtual());

        recuperaReceitaTotal();

        fabSalvar = findViewById(R.id.fabSalvar);

        fabSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarReceita(view);
            }
        });
    }

    private void salvarReceita(View view) {

        if (validaCamposReceita()){
            movimentacao = new Movimentacao();
            String dataEscolhida = campoData.getText().toString();
            Double receita = Double.parseDouble(campoValor.getText().toString());

            movimentacao.setValor(receita);
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setDescricao(campoCategoria.getText().toString());
            movimentacao.setData(campoData.getText().toString());
            movimentacao.setTipo("receita");

            receitaAtualizada = receitaTotal + receita;
            atualizaReceita(receitaAtualizada);

            movimentacao.salvar(dataEscolhida);

            finish();
        }

    }

    private void atualizaReceita(Double receita) {
        String user = authUser.getCurrentUser().getEmail();
        String idUser = Base64Custom.encodeBase64(user);
        DatabaseReference userRef = dbRef.child("usuarios")
                .child(idUser);

        userRef.child("receitaTotal").setValue(receita);
    }

    private boolean validaCamposReceita() {
        String textoValor = campoValor.getText().toString();
        String textoData = campoData.getText().toString();
        String textoCategoria  = campoCategoria.getText().toString();
        String textoDescricao = campoCategoria.getText().toString();

        if (!textoValor.isEmpty()){
            if (!textoData.isEmpty()){
                if (!textoCategoria.isEmpty()){
                    if (!textoDescricao.isEmpty()){
                        return true;
                    }else {
                        Toast.makeText(ReceitaActivity.this, "Descricao não foi preenchida!", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }else {
                    Toast.makeText(ReceitaActivity.this, "Categoria não foi preenchida!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else {
                Toast.makeText(ReceitaActivity.this, "Data não foi preenchida!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else {
            Toast.makeText(ReceitaActivity.this, "Valor não foi preenchido!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void recuperaReceitaTotal() {
        String user = authUser.getCurrentUser().getEmail();
        String idUser = Base64Custom.encodeBase64(user);

        DatabaseReference userRef = dbRef.child("usuarios")
                .child(idUser);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario user = dataSnapshot.getValue(Usuario.class);
                receitaTotal = user.getReceitaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
