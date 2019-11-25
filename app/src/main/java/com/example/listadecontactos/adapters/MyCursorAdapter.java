package com.example.listadecontactos.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.listadecontactos.R;
import com.example.listadecontactos.db.Contrato;


public class MyCursorAdapter extends CursorAdapter {
    private Context mContext;
    private int id;
    private Cursor mCursor;

    public MyCursorAdapter (Context context, Cursor c){
        super(context, c, 0);
        mContext = context;
        mCursor = c;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.layout_linha, parent, false);
    }

    @Override
    public void bindView(View mView, Context context, Cursor cursor) {
        TextView text1 = (TextView)mView.findViewById(R.id.nome);
        TextView text2 = (TextView)mView.findViewById(R.id.numero);
        text1.setText(mCursor.getString(cursor.getColumnIndexOrThrow(Contrato.Contacto.COLUMN_NOME)) + " " + mCursor.getString(cursor.getColumnIndexOrThrow(Contrato.Contacto.COLUMN_APELIDO)));
        text2.setText(String.valueOf(mCursor.getString(cursor.getColumnIndexOrThrow(Contrato.Contacto.COLUMN_NUMERO))));
    }

}
