package com.blimas.organizze.activity;

import android.content.Intent;
import android.os.Bundle;

import com.blimas.organizze.adapter.AdapterMovimentacao;
import com.blimas.organizze.config.ConfiguracaoFirebase;
import com.blimas.organizze.helper.Base64Custom;
import com.blimas.organizze.model.Movimentacao;
import com.blimas.organizze.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.blimas.organizze.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

    private Intent intent;
    private com.github.clans.fab.FloatingActionButton addReceita, addDespesa;
    private MaterialCalendarView calendarView;
    private Menu deslogar;

    private TextView textoSaudacao, textoValorSaldo;

    private Double despesaTotal = 0.0;
    private Double receitaTotal = 0.0;
    private Double resumoUsuario = 0.0;

    private DatabaseReference dbRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth auth = ConfiguracaoFirebase.getAuth();
    private DatabaseReference userRef;

    private ValueEventListener valueEventListenerUsuario;

    private RecyclerView recyclerView;
    private AdapterMovimentacao adapterMovimentacao;
    private List<Movimentacao> movimentacoes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Organizze");
        setSupportActionBar(toolbar);

        deslogar = findViewById(R.id.menuSair);

        textoSaudacao = findViewById(R.id.textoSaudacao);
        textoValorSaldo = findViewById(R.id.textoValorSaldo);
        calendarView = findViewById(R.id.calendarView);
        recyclerView = findViewById(R.id.recyclerMovimentacoes);

        addDespesa = findViewById(R.id.menu_despesa);
        addDespesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionarDespesa(v);
                finish();
            }
        });
        addReceita = findViewById(R.id.menu_receita);
        addReceita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionarReceita(v);
                finish();
            }
        });

        configuraCalendarView();

        //Configura adapter recyclerView
        adapterMovimentacao = new AdapterMovimentacao(movimentacoes, this);

        //configura recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterMovimentacao);
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperaResumo();
    }

    @Override
    protected void onStop() {
        super.onStop();
        userRef.removeEventListener(valueEventListenerUsuario);
    }

    public void recuperaResumo(){
        String user = auth.getCurrentUser().getEmail();
        String idUser = Base64Custom.encodeBase64(user);

        userRef = dbRef.child("usuarios")
                .child(idUser);

        valueEventListenerUsuario = userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario user = dataSnapshot.getValue(Usuario.class);

                despesaTotal = user.getDespesaTotal();
                receitaTotal = user.getReceitaTotal();
                resumoUsuario = receitaTotal - despesaTotal;

                DecimalFormat decimalFormat = new DecimalFormat("0.00");
//                DecimalFormat decimalFormat = new DecimalFormat("0.##");
                String resultadoFormatado = decimalFormat.format(resumoUsuario);

                textoSaudacao.setText("Olá, " + user.getNome());
                textoValorSaldo.setText("R$ " + resultadoFormatado);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menuSair:
                deslogarUsuario();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void deslogarUsuario() {
        auth = ConfiguracaoFirebase.getAuth();
        auth.signOut();
        startActivity(new Intent(this, MainActivity.class));
    }

    private void configuraCalendarView() {

        CharSequence meses[] = {"Janeiro","Fevereiro","Marco","Abril","Maio","Junho","Julho","Agosto","Setembro","Outubro","Novembro","Dezembro"};
        calendarView.setTitleMonths(meses);

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

            }
        });
    }

    public void adicionarReceita(View view){
        intent = new Intent(this, ReceitaActivity.class);
        startActivity(intent);
    }

    public void adicionarDespesa(View view){
        intent = new Intent(this, DespesaActivity.class);
        startActivity(intent);
    }

}
