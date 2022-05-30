package com.javeriana.lepsiapp.Holder;

import android.view.View;
import android.widget.TextView;
import com.javeriana.lepsiapp.R;

import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.rxjava3.annotations.NonNull;

public class arreglocontViewHolder extends RecyclerView.ViewHolder {
    private TextView idcont_histo;
    private TextView idsensor_hist;
    private TextView idfecha_histo;

    public TextView getIdcont_histo() {return idcont_histo;}
    public void setIdcont_histo(TextView idcont_histo) {this.idcont_histo = idcont_histo;}

    public TextView getIdsensor_hist() {return idsensor_hist;}
    public void setIdsensor_hist(TextView idsensor_hist) {this.idsensor_hist = idsensor_hist;}

    public TextView getIdfecha_histo() {return idfecha_histo;}
    public void setIdfecha_histo(TextView idfecha_histo) {this.idfecha_histo = idfecha_histo;}

    public arreglocontViewHolder(@NonNull  View itemView) {
        super(itemView);
        idcont_histo=itemView.findViewById(R.id.idcont_histo);
        idsensor_hist=itemView.findViewById(R.id.idsensor_hist);
        idfecha_histo=itemView.findViewById(R.id.idfecha_histo);
    }

}
