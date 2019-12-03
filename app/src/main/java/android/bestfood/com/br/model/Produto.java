package android.bestfood.com.br.model;

import android.bestfood.com.br.helper.ConfiguracaoFirebase;

import com.google.firebase.database.DatabaseReference;

public class Produto {
    private String idUsuario, idProduto, nome, descricao;
    private Double preco;

    public Produto() {
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference produtoRef = firebaseRef.child("produtos");

        setIdProduto(produtoRef.push().getKey());
    }

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void salvar(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference produtoRef = firebaseRef.child("produtos").child(getIdUsuario()).child(getIdProduto());

        produtoRef.setValue(this);
    }

    public void remover(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference produtoRef = firebaseRef.child("produtos").child(getIdUsuario()).child(getIdProduto());

        produtoRef.removeValue();
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }
}
