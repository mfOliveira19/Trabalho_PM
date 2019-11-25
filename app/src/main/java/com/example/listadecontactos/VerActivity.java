package com.example.listadecontactos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.listadecontactos.Utils.Utils;

public class VerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver);
        TextView nome = (TextView) findViewById(R.id.nome);
        // TextView apelido = (TextView) findViewById(R.id.apelido);
        TextView numero = (TextView) findViewById(R.id.numero);
        TextView email = (TextView) findViewById(R.id.email);
        TextView morada = (TextView) findViewById(R.id.morada);
        TextView idade = (TextView) findViewById(R.id.idade);
        nome.setText(getIntent().getStringExtra(Utils.PARAM_NOME)+ " " + getIntent().getStringExtra(Utils.PARAM_APELIDO));
      //  apelido.setText(getIntent().getStringExtra(Utils.PARAM_APELIDO));
        numero.setText(String.valueOf(getIntent().getIntExtra(Utils.PARAM_NUMERO, 0)));
        email.setText(getIntent().getStringExtra(Utils.PARAM_EMAIL));
        morada.setText(getIntent().getStringExtra(Utils.PARAM_MORADA));
        idade.setText(String.valueOf(getIntent().getIntExtra(Utils.PARAM_IDADE, 0)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_ativ_3 , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.voltar:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
