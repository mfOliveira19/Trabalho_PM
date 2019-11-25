package com.example.listadecontactos.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.listadecontactos.R;
import com.example.listadecontactos.entities.Contacto;

import java.util.ArrayList;

public class CustomArrayAdapter extends ArrayAdapter<Contacto> {
    public CustomArrayAdapter(Context context, ArrayList<Contacto> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Contacto c = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_linha, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.nome)).setText(c.getNome() + " " + c.getApelido());
        ((TextView) convertView.findViewById(R.id.numero)).setText(String.valueOf(c.getNumero()));
        return convertView;
    }

}
