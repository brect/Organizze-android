package com.blimas.organizze.model;

import com.blimas.organizze.config.ConfiguracaoFirebase;
import com.blimas.organizze.helper.Base64Custom;
import com.blimas.organizze.helper.DateCustom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Movimentacao {

    private String data;
    private String categoria;
    private String descricao;
    private String tipo;
    private double valor;

    public Movimentacao() {
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public void salvar(String dataEscolhida){

        FirebaseAuth auth = ConfiguracaoFirebase.getAuth();
        String idUsuario = Base64Custom.encodeBase64(auth.getCurrentUser().getEmail());
        String mesAno = DateCustom.dataMes(dataEscolhida);

        DatabaseReference reference = ConfiguracaoFirebase.getFirebaseDatabase();
        reference.child("movimentacao")
                .child(idUsuario)
                .child(mesAno)
                .push()
                .setValue(this);
    }
}
