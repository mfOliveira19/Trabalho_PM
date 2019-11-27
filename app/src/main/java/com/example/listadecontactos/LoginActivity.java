package com.example.listadecontactos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.listadecontactos.Utils.Utils;
import com.example.listadecontactos.db.Contrato;
import com.example.listadecontactos.db.DB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {
    //DB mDbHelper;
    //SQLiteDatabase db;
    private int REQUEST_CODE_OP_1 = 1;
    Context mContext = this;
    CheckBox checkBox;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String PREF_NAME = "login";
    private static final String KEY_REMEMBER = "remember";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_ID = "user_id";
    private EditText et1, et2;
    String prefix_url = "https://inactive-mosses.000webhostapp.com/myslim/api/";
    String id;
    int id_int = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //mDbHelper = new DB(this);
        //db = mDbHelper.getReadableDatabase();
        Button btn1 = (Button)findViewById(R.id.button1);
        Button btn2 = (Button)findViewById(R.id.button2);
        et1 = (EditText)findViewById(R.id.user);
        et2 = (EditText)findViewById(R.id.password);
        final String[] columns = {Contrato.User.COLUMN_USERNAME, Contrato.User.COLUMN_PASSWORD};
        checkBox = (CheckBox)findViewById(R.id.checkBox);
        boolean logout = getIntent().getBooleanExtra("logout", false);
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if(sharedPreferences.getBoolean(KEY_REMEMBER, false)) {
            checkBox.setChecked(true);
        }
        else {
            checkBox.setChecked(false);
        }

        et1.setText(sharedPreferences.getString(KEY_USERNAME,""));
        et2.setText(sharedPreferences.getString(KEY_PASSWORD,""));
        if(logout == true) {
            checkBox.setChecked(false);
            editor.putBoolean(KEY_REMEMBER, false);
            et1.setText("");
            et2.setText("");
        }

        if(checkBox.isChecked()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            sharedPreferences.getInt(KEY_ID, -1);
            intent.putExtra("ID", sharedPreferences.getInt(KEY_ID, -1));
            startActivity(intent);
            // db.close();
            finish();
        }

        managePrefs();

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et1.getText().toString();
                String password = et2.getText().toString();
                String pwc = md5(password);
                //String[] cValues = {username, pwc};
                //Cursor cursor = db.query(Contrato.User.TABLE_NAME, columns, "USERNAME=? AND PASSWORD=?", cValues, null, null, null);


                if (username.isEmpty() || password.isEmpty()) {
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
                    // String teste = Login(username, password);
                    // Toast.makeText(LoginActivity.this, teste, Toast.LENGTH_SHORT).show();
                    Login(username, password);
                    managePrefs();
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistarActivity.class);
                startActivityForResult(intent, REQUEST_CODE_OP_1);
            }
        });


    }

    /*@Override
    protected void onResume() {
        super.onResume();
        checkBox.setChecked(false);
        managePrefs();
        Toast.makeText(LoginActivity.this, "OnResume", Toast.LENGTH_SHORT).show();
    }*/
    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private final void managePrefs(){
        if(checkBox.isChecked()){
            String username = et1.getText().toString();
            String password = et2.getText().toString();
            String pwc = md5(password);
            editor.putString(KEY_USERNAME, username);
            editor.putString(KEY_PASSWORD, pwc);
            //Cursor c = db.rawQuery("SELECT * FROM "+ Contrato.User.TABLE_NAME + " WHERE " + Contrato.User.COLUMN_USERNAME + "= ?", new String[]{username});

            /*if (c.moveToFirst()) {
               int id = c.getInt(c.getColumnIndex(Contrato.User._ID));
               editor.putInt(KEY_ID, id);
            }*/
            editor.putInt(KEY_ID, id_int);
            editor.putBoolean(KEY_REMEMBER, true);
            editor.apply();
        } else {
            editor.putBoolean(KEY_REMEMBER, false);
            editor.remove(KEY_PASSWORD);
            editor.remove(KEY_USERNAME);
            editor.apply();
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_OP_1) {

            if (resultCode == RESULT_OK) {
                String username = data.getStringExtra("username");
                String password = data.getStringExtra("password");
                //ContentValues cv = new ContentValues();
                //String pwc = md5(password);
                Registar(username, password);
                //cv.put(Contrato.User.COLUMN_USERNAME, username);
                //cv.put(Contrato.User.COLUMN_PASSWORD, pwc);
                //db.insert(Contrato.User.TABLE_NAME, null, cv);
            }
        }



    }

    private void Login(String username, String password) {

        String url = prefix_url + "users/" + username + "/" + password;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean status = response.getBoolean("status");
                            if(status) {
                                Toast.makeText(LoginActivity.this, ""+ status, Toast.LENGTH_SHORT).show();
                                //id = response.optString("id");

                                JSONObject obj = response.getJSONObject(("DATA"));
                                id = obj.optString("id");
                                //Toast.makeText(LoginActivity.this, "" + id, Toast.LENGTH_SHORT).show();
                                id_int = Integer.parseInt(id);
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("ID", id_int);
                                managePrefs();

                                startActivity(intent);

                            }

                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                builder.setCancelable(true);
                                builder.setMessage(R.string.loginerro);
                                builder.setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        });


                                AlertDialog dialog = builder.create();
                                dialog.show();




                                Toast.makeText(LoginActivity.this, "" + status, Toast.LENGTH_SHORT).show();
                                id = null;
                            }
                        }
                        catch (JSONException ex) {
                            Log.d("login", "" + ex);
                        }

                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
        //Toast.makeText(LoginActivity.this, "" + id, Toast.LENGTH_SHORT).show();
    }


    public void Registar(String username, String password) {
        String url = "https://inactive-mosses.000webhostapp.com/myslim/api/users";

        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("username", username);
        jsonParams.put("password", password);
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,

                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                // Toast.makeText(LoginActivity.this,response.getString("MSG") , Toast.LENGTH_SHORT).show();
                            } else {
                                // Toast.makeText(LoginActivity.this,response.getString("MSG"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException ex) {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(postRequest);
    }
}

