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
import com.blimas.organizze.helper.Base64Custom;
import com.blimas.organizze.helper.DateCustom;
import com.blimas.organizze.model.Movimentacao;
import com.blimas.organizze.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class DespesaActivity extends AppCompatActivity {

    private TextInputEditText   campoData,
                                campoCategoria,
                                campoDescricao;
    private EditText campoValor;
    private FloatingActionButton fabSalvar;

    private Movimentacao movimentacao;

    private DatabaseReference dbRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth authUser = ConfiguracaoFirebase.getAuth();

    private Double despesaTotal;
    private Double despesaAtualizada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesa);

        campoValor = findViewById(R.id.valorDespesa);

        campoData = findViewById(R.id.dataDespesa);
        campoCategoria  = findViewById(R.id.categoriaDespesa);
        campoDescricao = findViewById(R.id.descricaoDespesa);

        //preenche o campo data com a data atual
        campoData.setText(DateCustom.dataAtual());

        recuperaDespesaTotal();

        fabSalvar = findViewById(R.id.fabSalvar);

        fabSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarDespesa(view);
            }
        });
    }

    public void salvarDespesa(View view){

        if (validaCamposDespesa()){
            movimentacao = new Movimentacao();
            String dataEscolhida = campoData.getText().toString();
            Double despesa = Double.parseDouble(campoValor.getText().toString());

            movimentacao.setValor(despesa);
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setDescricao(campoCategoria.getText().toString());
            movimentacao.setData(campoData.getText().toString());
            movimentacao.setTipo("despesa");

            despesaAtualizada = despesaTotal + despesa;
            atualizaDespesa(despesaAtualizada);

            movimentacao.salvar(dataEscolhida);
        }
    }

    public Boolean validaCamposDespesa(){
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
                        Toast.makeText(DespesaActivity.this, "Descricao não foi preenchida!", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }else {
                    Toast.makeText(DespesaActivity.this, "Categoria não foi preenchida!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else {
                Toast.makeText(DespesaActivity.this, "Data não foi preenchida!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else {
            Toast.makeText(DespesaActivity.this, "Valor não foi preenchido!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void recuperaDespesaTotal(){
        String user = authUser.getCurrentUser().getEmail();
        String idUser = Base64Custom.encodeBase64(user);

        DatabaseReference userRef = dbRef.child("usuarios")
                .child(idUser);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario user = dataSnapshot.getValue(Usuario.class);
                despesaTotal = user.getDespesaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void atualizaDespesa(Double despesa){
        String user = authUser.getCurrentUser().getEmail();
        String idUser = Base64Custom.encodeBase64(user);
        DatabaseReference userRef = dbRef.child("usuarios")
                .child(idUser);

        userRef.child("despesaTotal").setValue(despesa);
    }
}
