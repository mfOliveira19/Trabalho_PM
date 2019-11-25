package com.example.listadecontactos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.listadecontactos.Utils.Utils;

import java.util.regex.Pattern;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
    }

    public static final Pattern EMAIL
            = Pattern.compile(
                    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

     @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_ativ_2 ,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Context mContext = this;
        switch(item.getItemId()) {

            case R.id.cancelar:
                finish();
                return true;
            case R.id.check:
                EditText edit_nome = (EditText)findViewById(R.id.edit_nome);
                EditText edit_apelido = (EditText)findViewById(R.id.edit_apelido);
                EditText edit_numero = (EditText)findViewById(R.id.edit_numero);
                EditText edit_email = (EditText)findViewById(R.id.edit_email);
                EditText edit_morada = (EditText)findViewById(R.id.edit_morada);
                EditText edit_idade = (EditText)findViewById(R.id.edit_idade);
                String nome = edit_numero.getText().toString();
                String apelido = edit_apelido.getText().toString();
                String email = edit_email.getText().toString();
                String morada = edit_morada.getText().toString();
                String numero = edit_numero.getText().toString();
                String idade = edit_idade.getText().toString();

                    if (nome.matches("") || apelido.matches("") || email.matches("") || morada.matches("") || numero.matches("") || idade.matches("")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setCancelable(true);
                        builder.setMessage(R.string.alertapreenche);
                        builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });


                        AlertDialog dialog = builder.create();
                        dialog.show();



                        return true;
                    }

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setCancelable(true);
                    builder.setMessage(R.string.emailinvalido);
                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });


                    AlertDialog dialog = builder.create();
                    dialog.show();



                    return true;
                }

                    Intent output = new Intent();
                    output.putExtra(Utils.PARAM_NOME, edit_nome.getText().toString());
                    output.putExtra(Utils.PARAM_APELIDO, edit_apelido.getText().toString());
                    output.putExtra(Utils.PARAM_NUMERO, Integer.parseInt(edit_numero.getText().toString()));
                    output.putExtra(Utils.PARAM_EMAIL, edit_email.getText().toString());
                    output.putExtra(Utils.PARAM_MORADA, edit_morada.getText().toString());
                    output.putExtra(Utils.PARAM_IDADE, Integer.parseInt(edit_idade.getText().toString()));
                    setResult(RESULT_OK, output);
                    finish();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
