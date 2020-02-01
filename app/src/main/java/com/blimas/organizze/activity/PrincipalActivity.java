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

import android.util.Log;
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
    private DatabaseReference movimentacaoRef;

    private ValueEventListener valueEventListenerUsuario;
    private ValueEventListener valueEventListenerMovimentacoes;

    private RecyclerView recyclerView;
    private AdapterMovimentacao adapterMovimentacao;
    private List<Movimentacao> movimentacoes = new ArrayList<>();
    private String mesAnoSelecionado;

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

        configuraCalendarView();

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

        //Configurar adapter
        adapterMovimentacao = new AdapterMovimentacao(movimentacoes,this);

        //Configurar RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter( adapterMovimentacao );
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperaResumo();
        recuperarMovimentacoes();
    }

    @Override
    protected void onStop() {
        super.onStop();
        userRef.removeEventListener(valueEventListenerUsuario);
        movimentacaoRef.removeEventListener(valueEventListenerMovimentacoes);
    }

    public void recuperarMovimentacoes(){

        String emailUsuario = auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.encodeBase64( emailUsuario );
        movimentacaoRef = dbRef.child("movimentacao")
                .child( idUsuario )
                .child( mesAnoSelecionado );

        Log.i("dadosRetorno", "recuperarMovimentacoes: " + mesAnoSelecionado);

        valueEventListenerMovimentacoes = movimentacaoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                movimentacoes.clear();
                for (DataSnapshot dados: dataSnapshot.getChildren() ){

                    Movimentacao movimentacao = dados.getValue( Movimentacao.class );
                    movimentacoes.add( movimentacao );

                }
                adapterMovimentacao.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

                DecimalFormat decimalFormat = new DecimalFormat("0.##");
                String resultadoFormatado = decimalFormat.format( resumoUsuario );

                textoSaudacao.setText("Olá, " + user.getNome() );
                textoValorSaldo.setText( "R$ " + resultadoFormatado );


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

        CharSequence meses[] = {"Janeiro","Fevereiro", "Março","Abril","Maio","Junho","Julho","Agosto","Setembro","Outubro","Novembro","Dezembro"};
        calendarView.setTitleMonths( meses );

        CalendarDay dataAtual = calendarView.getCurrentDate();
        String mesSelecionado = String.format("%02d", (dataAtual.getMonth() + 1) );
        mesAnoSelecionado = String.valueOf( mesSelecionado + "" + dataAtual.getYear() );

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                String mesSelecionado = String.format("%02d", (date.getMonth() + 1) );
                mesAnoSelecionado = String.valueOf( mesSelecionado + "" + date.getYear() );

                movimentacaoRef.removeEventListener( valueEventListenerMovimentacoes );
                recuperarMovimentacoes();
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
