package android.bestfood.com.br.activity;

        import androidx.appcompat.app.AppCompatActivity;

        import android.bestfood.com.br.R;
        import android.content.Intent;
        import android.os.Bundle;
        import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Escondendo Actionbar
        getSupportActionBar().hide();

        //Mudara de activity depois um determinado tempo
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AbrirAutenticação();
            }
        }, 3000);
    }

    private void AbrirAutenticação(){
        Intent i = new Intent(MainActivity.this, AutenticacaoActivity.class);
        startActivity(i);
        finish(); //Fecha o main activity
    }
}
