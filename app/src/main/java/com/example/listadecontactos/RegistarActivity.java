
package com.example.listadecontactos;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.listadecontactos.MySingleton;
import com.example.listadecontactos.Utils.Utils;
import com.example.listadecontactos.db.Contrato;
import com.example.listadecontactos.db.DB;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class RegistarActivity extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" + // pelo menos 1 numero
                    "(?=.*[a-z])" + // pelo menos 1 minuscula
                    "(?=\\S+$)" +   // sem espaços
                    ".{6,}" +       // pelo menos 6 caracteres
                    "$");
    //DB mDbHelper;
    //SQLiteDatabase db;
    Context mContext = this;
    private TextInputLayout u, p, cp;
    String prefix_url = "https://inactive-mosses.000webhostapp.com/myslim/api/";
    boolean existeuser = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar);

        //mDbHelper = new DB(this);
        //db = mDbHelper.getReadableDatabase();
        Button btn1 = (Button)findViewById(R.id.button1);
        u = findViewById(R.id.user);
        p = findViewById(R.id.password);
        cp = findViewById(R.id.cpassword);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = u.getEditText().getText().toString();
                String password = p.getEditText().getText().toString();
                String cpassword = cp.getEditText().getText().toString();
                if (username.isEmpty() || password.isEmpty() || cpassword.isEmpty() || !password.equals(cpassword)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setCancelable(true);

                    if(username.isEmpty()) {
                        builder.setMessage(R.string.preencheloginuser);
                    }

                    if(password.isEmpty()) {
                        builder.setMessage(R.string.preencheloginpass);
                    }

                    if(username.isEmpty() && password.isEmpty()) {
                        builder.setMessage(R.string.alertapreenche);
                    }

                    if(!password.equals(cpassword)) {
                        builder.setMessage(R.string.introduzapassword);
                    }

                    // builder.setMessage(R.string.alertapreenche);
                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });


                    AlertDialog dialog = builder.create();
                    dialog.show();


                }
                if(confirmInput() == true){
                    Registar(username, password);
                }
                //}
            }
        });
    }

    public void Registar(final String username, final String password) {
        String url = prefix_url + "users/usercheck/" + username;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean status = response.getBoolean("status");
                            //Toast.makeText(RegistarActivity.this, status, Toast.LENGTH_SHORT).show();
                            if(status) {
                                // Toast.makeText(RegistarActivity.this, "" + status, Toast.LENGTH_SHORT).show();
                                existeuser = true;
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                builder.setCancelable(true);
                                builder.setMessage(R.string.userexiste);
                                builder.setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        });


                                AlertDialog dialog = builder.create();
                                dialog.show();

                            }

                            else {
                                // Toast.makeText(RegistarActivity.this, "" + status, Toast.LENGTH_SHORT).show();
                                existeuser = false;
                                Intent output = new Intent();
                                output.putExtra(Utils.PARAM_USERNAME, username);
                                output.putExtra(Utils.PARAM_PASSWORD, password);
                                setResult(RESULT_OK, output);
                                finish();
                            }
                        }
                        catch (JSONException ex) {
                            Log.d("userexiste", "" + ex);
                        }

                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegistarActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
        //Toast.makeText(RegistarActivity.this, "" + existeuser, Toast.LENGTH_SHORT).show();
    }



    public boolean confirmInput() {
        if(!validateUsername() | !validatePassword() |!validatecPassword()){
            return false;
        } else {
            return true;
        }
    }

    private boolean validateUsername() {
        String username = u.getEditText().getText().toString().trim();

        if(username.isEmpty()) {
            u.setError(getText(R.string.preencheloginuser));
            return false;
        }
        else if (username.length() > 15) {
            u.setError(getText(R.string.usernamelong));
            return false;
        } else {
            u.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String password = p.getEditText().getText().toString().trim();

        if(password.isEmpty()) {
            p.setError(getText(R.string.preencheloginpass));
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()){
            p.setError(getText((R.string.passinvalida)));
            return false;
        }

        else {
            u.setError(null);
            return true;
        }
    }

    private boolean validatecPassword() {
        String cpassword = cp.getEditText().getText().toString().trim();
        String password = p.getEditText().getText().toString().trim();
        if(cpassword.isEmpty()) {
            cp.setError(getText(R.string.preencheloginpass));
            return false;
        } else if(cpassword.equals(password)) {
            u.setError(null);
            return true;
        } else {
            cp.setError(getText(R.string.confirmapassworderro));
            return false;
        }

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