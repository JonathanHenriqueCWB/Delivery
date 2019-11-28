package android.bestfood.com.br.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.bestfood.com.br.R;
import android.bestfood.com.br.helper.ConfiguracaoFirebase;
import android.bestfood.com.br.helper.UsuarioFirebase;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class AutenticacaoActivity extends AppCompatActivity {

    private Button botaoAcessar;
    private EditText campoEmail, campoSenha;
    private Switch tipoAcesso, tipoUsuario;
    private LinearLayout linearTipoUsuario;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticacao);


        //Escondendo Actionbar
        getSupportActionBar().hide();
        //Chamando método inicializarComponentes
        inicializarComponentes();
        //instanciando autenticação
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        //Verefica usuario logado
        verificarUsuarioLogado();
        //Verefica o switch para saber se cadastro e referente a usuario ou empresa
        tipoAcesso.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){//Empresa
                    linearTipoUsuario.setVisibility(View.VISIBLE);
                }else{//Usuario
                    linearTipoUsuario.setVisibility(View.GONE);
                }
            }
        });


        //Autenticação login
        botaoAcessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //RECUPERA O EMAIL E SENHA
                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();

                if( !email.isEmpty()){
                    if( !senha.isEmpty()){
                        /*VEREFICA LOGIN SE ESTÁ MARCADO COMO LOGAR/CADASTRAR*/

                        //CASO ESEJA MARCADA IRA CADASTRAR
                        if( tipoAcesso.isChecked()){
                            autenticacao.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if(task.isSuccessful()){

                                        Toast.makeText(AutenticacaoActivity.this,
                                                "Cadastro Realizado com sucesso",
                                                Toast.LENGTH_SHORT).show();

                                        String tipoUsuario = getTipoUsuario();
                                        UsuarioFirebase.atualizarTipoUsuario(tipoUsuario);
                                        abrirTelaPrincipal(tipoUsuario);

                                    }else{
                                        String erroExecao ="";
                                        try {
                                            throw task.getException();
                                        }catch (FirebaseAuthWeakPasswordException e) {
                                            erroExecao = "Digite uma senha mais forte";
                                        }catch (FirebaseAuthInvalidCredentialsException e) {
                                            erroExecao = "Digite um email valido";
                                        }catch (FirebaseAuthUserCollisionException e){
                                            erroExecao = "Conta já cadastrada";
                                        } catch (Exception e) {
                                            erroExecao = "ao cadastrar usuario: " + e.getMessage();
                                            e.printStackTrace();
                                        }

                                        Toast.makeText(AutenticacaoActivity.this,
                                                "Erro" + erroExecao,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        //CASO ESTEJA DESMARCADO IRA LOGAR
                        }else{

                            autenticacao.signInWithEmailAndPassword(
                                    email, senha
                            ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(AutenticacaoActivity.this,
                                                "Logado com sucesso",
                                                Toast.LENGTH_SHORT).show();
                                        String tipoUsuario = task.getResult().getUser().getDisplayName();
                                        abrirTelaPrincipal(tipoUsuario);
                                    }else{
                                        Toast.makeText(AutenticacaoActivity.this,
                                                "Erro ao fazer login",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    }else{
                        Toast.makeText(AutenticacaoActivity.this,
                                "Preencha a senha",
                                Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(AutenticacaoActivity.this,
                            "Preencha o email",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getTipoUsuario(){
        return tipoUsuario.isChecked() ? "E" : "U";
    }
    //MÉTODO PARA VERIFICAR SE USUARIO NÃO ESTÁ LOGADO
    private void verificarUsuarioLogado(){
        FirebaseUser usuarioAtual = autenticacao.getCurrentUser();
        if(usuarioAtual != null){
            String tipoUsuario = usuarioAtual.getDisplayName();
            abrirTelaPrincipal(tipoUsuario);
        }
    }
    //MÉTODO PARA ABRIR A TELA DO MENU PRINCIPAL (HomeActivity)
    private void abrirTelaPrincipal(String tipoUsuario){
        if(tipoUsuario.equals("E")){ //Empresa
            startActivity(new Intent(getApplicationContext(),EmpresaActivity.class));
        }else{//Usuario
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        }
    }
    //INICIALIZA TODOS OS COMPONENTES DO CAMPO Activit_autenticacao
    private void inicializarComponentes(){
        campoEmail = findViewById(R.id.editCadastroEmail);
        campoSenha = findViewById(R.id.editCadastroSenha);
        botaoAcessar = findViewById(R.id.buttonAcesso);
        tipoAcesso = findViewById(R.id.switchAcesso);
        tipoUsuario = findViewById(R.id.switchTipoUsuario);
        //Pega linear do tipo de cadastro
        linearTipoUsuario = findViewById(R.id.LinearTipoUsuario);
    }

}
