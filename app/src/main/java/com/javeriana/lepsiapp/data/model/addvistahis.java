package com.javeriana.lepsiapp.data.model;

//import static com.javeriana.lepsiapp.data.model.arreglocont.getEvento;
//import static com.javeriana.lepsiapp.data.model.arreglocont.getFecha;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.javeriana.lepsiapp.R;

import java.util.ArrayList;

public class addvistahis extends RecyclerView.Adapter<addvistahis.ViewHolderEvento> {
    private ArrayList<arreglocont> histList;

    public addvistahis(ArrayList<arreglocont> histList) {
        this.histList = histList;
    }

    @NonNull
    @Override
    public ViewHolderEvento onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.histograma_cam,null,false);
        return new ViewHolderEvento(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderEvento holder, int position) {
        holder.evento.setText(histList.get(position).getEvento());
        holder.fuente.setText(histList.get(position).getFuente());
        holder.fecha.setText(histList.get(position).getFecha());
    }

    @Override
    public int getItemCount() {
        return histList.size();
    }


    public class ViewHolderEvento extends RecyclerView.ViewHolder {
        TextView evento, fuente, fecha ;
        public ViewHolderEvento(@NonNull View itemView) {
            super(itemView);
            evento=(TextView) itemView.findViewById(R.id.idcont_histo);
            fuente=(TextView) itemView.findViewById(R.id.idsensor_hist);
            fecha=(TextView) itemView.findViewById(R.id.idfecha_histo);
        }
    }
}
