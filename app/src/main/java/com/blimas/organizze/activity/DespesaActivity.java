package com.blimas.organizze.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.blimas.organizze.R;
import com.blimas.organizze.helper.DateCustom;
import com.google.android.material.textfield.TextInputEditText;

public class DespesaActivity extends AppCompatActivity {

    private TextInputEditText   campoData,
                                campoCategoria,
                                campoDescricao;
    private EditText campoValor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesa);

        campoData = findViewById(R.id.dataDespesa);
        campoCategoria  = findViewById(R.id.categoriaDespesa);
        campoDescricao = findViewById(R.id.descricaoDespesa);
        campoValor = findViewById(R.id.valorDespesa);

        //preenche o campo data com a data atual
        campoData.setText(DateCustom.dataAtual());

    }
}
