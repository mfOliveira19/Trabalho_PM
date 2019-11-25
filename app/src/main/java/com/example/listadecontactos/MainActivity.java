package com.example.listadecontactos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.listadecontactos.Utils.Utils;
import com.example.listadecontactos.adapters.CustomArrayAdapter;
import com.example.listadecontactos.adapters.MyCursorAdapter;
import com.example.listadecontactos.db.Contrato;
import com.example.listadecontactos.db.DB;
import com.example.listadecontactos.entities.Contacto;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mProximity;
    private static final int SENSOR_SENSITIVITY = 4;
    private int REQUEST_CODE_OP_1 = 1;
    private int REQUEST_CODE_OP_2 = 2;
    DB mDbHelper;
    SQLiteDatabase db;
    ListView lista;
    Cursor c, c_contactos;
    MyCursorAdapter madapter;
    Spinner spin;
    int iduser;
    EditText pnome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iduser = getIntent().getIntExtra("ID", -1);
        //Toast.makeText(MainActivity.this, "" + iduser, Toast.LENGTH_SHORT).show();
        mDbHelper = new DB(this);
        db = mDbHelper.getReadableDatabase();
        lista = (ListView)findViewById(R.id.lista);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        preencheLista();
        registerForContextMenu(lista);
        spin = ((Spinner)findViewById(R.id.spinner));
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, VerActivity.class);

                c.moveToPosition(i);
                intent.putExtra(Utils.PARAM_NOME, c.getString(c.getColumnIndex(Contrato.Contacto.COLUMN_NOME)));
                intent.putExtra(Utils.PARAM_APELIDO, c.getString(c.getColumnIndex(Contrato.Contacto.COLUMN_APELIDO)));
                intent.putExtra(Utils.PARAM_NUMERO, c.getInt(c.getColumnIndex(Contrato.Contacto.COLUMN_NUMERO)));
                intent.putExtra(Utils.PARAM_EMAIL, c.getString(c.getColumnIndex(Contrato.Contacto.COLUMN_EMAIL)));
                intent.putExtra(Utils.PARAM_MORADA, c.getString(c.getColumnIndex(Contrato.Contacto.COLUMN_MORADA)));
                intent.putExtra(Utils.PARAM_IDADE, c.getInt(c.getColumnIndex(Contrato.Contacto.COLUMN_IDADE)));
                startActivity(intent);
            }

        });
    }


    public final void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public final void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_PROXIMITY) {

            if(event.values[0] >= -SENSOR_SENSITIVITY && event.values[0] <= SENSOR_SENSITIVITY) {
                //Perto
                preencheListaSensor();

            } else {
                //Longe
                preencheLista();

            }

        }
    }


    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mProximity,  SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }


    private void getCursorPesquisaNome(String nomep) {
        String sql = "select " + Contrato.Contacto.TABLE_NAME + "." +
                Contrato.Contacto._ID + "," +
                Contrato.Contacto.COLUMN_NOME + "," +
                Contrato.Contacto.COLUMN_APELIDO + "," +
                Contrato.Contacto.COLUMN_NUMERO + "," +
                Contrato.Contacto.COLUMN_EMAIL + "," +
                Contrato.Contacto.COLUMN_MORADA + "," +
                Contrato.Contacto.COLUMN_IDADE + " FROM " +
                Contrato.Contacto.TABLE_NAME +
                " WHERE " + Contrato.Contacto.COLUMN_ID_USER + "=" + iduser +
                " AND " + Contrato.Contacto.COLUMN_NOME + "='" + nomep + "'";
        c = db.rawQuery(sql, null);

    }


    private void getCursorOrdenaIdade() {
        String sql = "select " + Contrato.Contacto.TABLE_NAME + "." +
                Contrato.Contacto._ID + "," +
                Contrato.Contacto.COLUMN_NOME + "," +
                Contrato.Contacto.COLUMN_APELIDO + "," +
                Contrato.Contacto.COLUMN_NUMERO + "," +
                Contrato.Contacto.COLUMN_EMAIL + "," +
                Contrato.Contacto.COLUMN_MORADA + "," +
                Contrato.Contacto.COLUMN_IDADE + " FROM " +
                Contrato.Contacto.TABLE_NAME +
                " WHERE " + Contrato.Contacto.COLUMN_ID_USER + "=" + iduser +
                " ORDER BY " + Contrato.Contacto.COLUMN_IDADE;
        c = db.rawQuery(sql, null);

    }

    private void getCursorSensor() {
        String sql = "select " + Contrato.Contacto.TABLE_NAME + "." +
                Contrato.Contacto._ID + "," +
                Contrato.Contacto.COLUMN_NOME + "," +
                Contrato.Contacto.COLUMN_APELIDO + "," +
                Contrato.Contacto.COLUMN_NUMERO + "," +
                Contrato.Contacto.COLUMN_EMAIL + "," +
                Contrato.Contacto.COLUMN_MORADA + "," +
                Contrato.Contacto.COLUMN_IDADE + " FROM " +
                Contrato.Contacto.TABLE_NAME +
                " WHERE " + Contrato.Contacto.COLUMN_ID_USER + "=" + iduser +
                " ORDER BY " + Contrato.Contacto._ID + " DESC " + " LIMIT 10 ";
        c = db.rawQuery(sql, null);

    }

    private void preencheListaSensor(){

        getCursorSensor();
        madapter = new MyCursorAdapter(MainActivity.this, c);
        lista.setAdapter(madapter);
    }

    private void getCursorOrdenaNome() {
        String sql = "select " + Contrato.Contacto.TABLE_NAME + "." +
                Contrato.Contacto._ID + "," +
                Contrato.Contacto.COLUMN_NOME + "," +
                Contrato.Contacto.COLUMN_APELIDO + "," +
                Contrato.Contacto.COLUMN_NUMERO + "," +
                Contrato.Contacto.COLUMN_EMAIL + "," +
                Contrato.Contacto.COLUMN_MORADA + "," +
                Contrato.Contacto.COLUMN_IDADE + " FROM " +
                Contrato.Contacto.TABLE_NAME +
                " WHERE " + Contrato.Contacto.COLUMN_ID_USER + "=" + iduser +
                " ORDER BY " + Contrato.Contacto.COLUMN_NOME;
        c = db.rawQuery(sql, null);

    }

    private void preencheListaPesquisaNome(String nomep){

        getCursorPesquisaNome(nomep);
        madapter = new MyCursorAdapter(MainActivity.this, c);
        lista.setAdapter(madapter);
    }

    private void preencheListaOrdenaIdade(){

        getCursorOrdenaIdade();
        madapter = new MyCursorAdapter(MainActivity.this, c);
        lista.setAdapter(madapter);
    }

    private void preencheListaOrdenaNome(){

        getCursorOrdenaNome();
        madapter = new MyCursorAdapter(MainActivity.this, c);
        lista.setAdapter(madapter);
    }


    private void getCursor() {
        String sql = "select " + Contrato.Contacto.TABLE_NAME + "." +
                Contrato.Contacto._ID + "," +
                Contrato.Contacto.COLUMN_NOME + "," +
                Contrato.Contacto.COLUMN_APELIDO + "," +
                Contrato.Contacto.COLUMN_NUMERO + "," +
                Contrato.Contacto.COLUMN_EMAIL + "," +
                Contrato.Contacto.COLUMN_MORADA + "," +
                Contrato.Contacto.COLUMN_IDADE + " FROM " +
                Contrato.Contacto.TABLE_NAME +
                " WHERE " + Contrato.Contacto.COLUMN_ID_USER + "=" + iduser;
        c = db.rawQuery(sql, null);

    }

    private void preencheLista(){

        getCursor();
        madapter = new MyCursorAdapter(MainActivity.this, c);
        lista.setAdapter(madapter);
    }

    private void deleteFromBD(int id) {
        db.delete(Contrato.Contacto.TABLE_NAME, Contrato.Contacto._ID + " = ?", new String[]{id+""});
        preencheLista();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_ativ_1 ,menu);
        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spin = (Spinner) item.getActionView();
        String[] spinnerlista = {getString(R.string.ordenacriacao), getString(R.string.ordenanome), getString(R.string.ordernaridade)};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerlista);
        spin.setAdapter(adapter);


        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if(position == 0) {
                    //Toast.makeText(MainActivity.this, selectedItem, Toast.LENGTH_SHORT).show();
                    preencheLista();
                }

                if(position == 1) {
                    //Toast.makeText(MainActivity.this, selectedItem, Toast.LENGTH_SHORT).show();
                    preencheListaOrdenaNome();
                }

                if(position == 2) {
                    //Toast.makeText(MainActivity.this, selectedItem, Toast.LENGTH_SHORT).show();
                    preencheListaOrdenaIdade();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return true;
    }

    @Override
     public boolean onOptionsItemSelected(MenuItem item) {
        Context mContext = this;
        switch(item.getItemId()) {
            case R.id.add:
                Intent i = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(i, REQUEST_CODE_OP_1);
                return true;
            case R.id.voltar:
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setCancelable(true);
                builder.setMessage(R.string.confirmalogout);
                builder.setPositiveButton(R.string.confirmar,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                intent.putExtra("logout", true);
                                startActivity(intent);
                                finish();
                            }
                        });
                builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            case R.id.pesquisanome:
                pnome = new EditText(this);

                AlertDialog.Builder builder2 = new AlertDialog.Builder(mContext);
                builder2.setCancelable(true);
                builder2.setMessage("Introduza o nome a pesquisar");
                builder2.setView(pnome);
                builder2.setPositiveButton(R.string.confirmar,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(!pnome.getText().toString().isEmpty()) {
                                    preencheListaPesquisaNome(pnome.getText().toString());
                                }
                            }
                        });
                builder2.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog2 = builder2.create();
                dialog2.show();
                return true;

            case R.id.refresh:
                preencheLista();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_OP_1) {

            if (resultCode == RESULT_OK) {

                ContentValues cv = new ContentValues();
                cv.put(Contrato.Contacto.COLUMN_NOME, data.getStringExtra(Utils.PARAM_NOME));
                cv.put(Contrato.Contacto.COLUMN_APELIDO, data.getStringExtra(Utils.PARAM_APELIDO));
                cv.put(Contrato.Contacto.COLUMN_NUMERO, data.getIntExtra(Utils.PARAM_NUMERO, -1));
                cv.put(Contrato.Contacto.COLUMN_EMAIL, data.getStringExtra(Utils.PARAM_EMAIL));
                cv.put(Contrato.Contacto.COLUMN_MORADA, data.getStringExtra(Utils.PARAM_MORADA));
                cv.put(Contrato.Contacto.COLUMN_IDADE, data.getIntExtra(Utils.PARAM_IDADE, -1));
                cv.put(Contrato.Contacto.COLUMN_ID_USER, iduser);
                db.insert(Contrato.Contacto.TABLE_NAME, null, cv);
                preencheLista();
            }
        }

        if (requestCode == REQUEST_CODE_OP_2) {

            if (resultCode == RESULT_OK) {
                ContentValues cv = new ContentValues();
                cv.put(Contrato.Contacto.COLUMN_NOME, data.getStringExtra(Utils.PARAM_NOME));
                cv.put(Contrato.Contacto.COLUMN_APELIDO, data.getStringExtra(Utils.PARAM_APELIDO));
                cv.put(Contrato.Contacto.COLUMN_NUMERO, data.getIntExtra(Utils.PARAM_NUMERO, -1));
                cv.put(Contrato.Contacto.COLUMN_EMAIL, data.getStringExtra(Utils.PARAM_EMAIL));
                cv.put(Contrato.Contacto.COLUMN_MORADA, data.getStringExtra(Utils.PARAM_MORADA));
                cv.put(Contrato.Contacto.COLUMN_IDADE, data.getIntExtra(Utils.PARAM_IDADE, -1));
                cv.put(Contrato.Contacto.COLUMN_ID_USER, iduser);
                int id = data.getIntExtra(Utils.PARAM_INDEX, -1);
                db.update(Contrato.Contacto.TABLE_NAME, cv, Contrato.Contacto._ID + " = ?", new String[]{id+""});
                preencheLista();
            }

        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cont_1, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Context mContext = this;

        switch (item.getItemId()) {
            case R.id.editar:
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                int itemPosition = info.position;
                c.moveToPosition(itemPosition);
                int id_contacto = c.getInt(c.getColumnIndex(Contrato.Contacto._ID));
                intent.putExtra(Utils.PARAM_NOME, c.getString(c.getColumnIndex(Contrato.Contacto.COLUMN_NOME)));
                intent.putExtra(Utils.PARAM_APELIDO, c.getString(c.getColumnIndex(Contrato.Contacto.COLUMN_APELIDO)));
                intent.putExtra(Utils.PARAM_NUMERO, c.getInt(c.getColumnIndex(Contrato.Contacto.COLUMN_NUMERO)));
                intent.putExtra(Utils.PARAM_EMAIL, c.getString(c.getColumnIndex(Contrato.Contacto.COLUMN_EMAIL)));
                intent.putExtra(Utils.PARAM_MORADA, c.getString(c.getColumnIndex(Contrato.Contacto.COLUMN_MORADA)));
                intent.putExtra(Utils.PARAM_IDADE, c.getInt(c.getColumnIndex(Contrato.Contacto.COLUMN_IDADE)));
                intent.putExtra(Utils.PARAM_INDEX, id_contacto);
                startActivityForResult(intent, REQUEST_CODE_OP_2);
                return true;
            case R.id.remover:
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setCancelable(true);
                builder.setMessage(R.string.certeza);
                builder.setPositiveButton(R.string.confirmar,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int itemPosition = info.position;
                                c.moveToPosition(itemPosition);
                                int id_contacto = c.getInt(c.getColumnIndex(Contrato.Contacto._ID));
                                deleteFromBD(id_contacto);
                            }
                        });
                builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();



                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }





}
