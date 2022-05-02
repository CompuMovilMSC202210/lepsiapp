package com.javeriana.lepsiapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;


public class arreglocont extends CursorAdapter {
    public arreglocont(Context context, Cursor c, int flags) {

        super(context, c,flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.histograma_cam, viewGroup,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView idcont_histo = (TextView) view.findViewById(R.id.idcont_histo);
        TextView idfecha_histo = (TextView) view.findViewById(R.id.idfecha_histo);
        int idnum = cursor.getInt(0);
        String nombre = cursor.getString(1);
        idcont_histo.setText(String.valueOf(idnum));
        idfecha_histo.setText(nombre);

    }
}
