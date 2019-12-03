package android.bestfood.com.br.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.bestfood.com.br.R;
import android.bestfood.com.br.helper.UsuarioFirebase;
import android.bestfood.com.br.model.Produto;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class NovoProdutoEmpresaActivity extends AppCompatActivity {

    private EditText editProdutoNome, editProdutoDescricao, editProdutoTempo,
            editProdutoPreco;
    private String idUsuarioLogado;
    private DatabaseReference firebaseRef;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_produto_empresa);

        //Configuração da toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Novo produto");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inicializarComponentes();
        idUsuarioLogado = UsuarioFirebase.getIdUsuario();
    }

    public void validarDadosProduto(View view){
        String nome = editProdutoNome.getText().toString();
        String descricao = editProdutoDescricao.getText().toString();
        String preco = editProdutoPreco.getText().toString();

        if (!nome.isEmpty()){
            if (!descricao.isEmpty()){
                if (!preco.isEmpty()){

                    Produto produto = new Produto();
                    produto.setIdUsuario(idUsuarioLogado);
                    produto.setNome(nome);
                    produto.setDescricao(descricao);
                    produto.setPreco(Double.parseDouble(preco));

                    produto.salvar();
                    finish();
                    exibirMensagem("Produto salvo.");

                } else {
                    exibirMensagem("Digite um preço");
                }
            } else {
                exibirMensagem("Digite uma descrição");
            }
        } else {
            exibirMensagem("Digite um nome para a produto");
        }
    }

    private void exibirMensagem(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void inicializarComponentes(){
        editProdutoDescricao = findViewById(R.id.editProdutoDescricao);
        editProdutoNome = findViewById(R.id.editProdutoNome);
        editProdutoPreco = findViewById(R.id.editProdutoPreco);
    }
}
