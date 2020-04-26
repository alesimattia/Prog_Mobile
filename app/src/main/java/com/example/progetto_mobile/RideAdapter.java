package com.example.progetto_mobile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RideAdapter extends RecyclerView.Adapter<RideAdapter.CViewHolder>{

    private ArrayList<Ride> corsa;

    public RideAdapter(ArrayList<Ride> corsa){
        this.corsa=corsa;
    }


            class CViewHolder extends RecyclerView.ViewHolder {
                TextView textView_tratta, textView_verso, textView_posti, textView_ora, textView_data;

                CViewHolder(@NonNull View itemView) {
                    super(itemView);
                    textView_tratta = itemView.findViewById(R.id.tratta);
                    textView_verso = itemView.findViewById(R.id.verso);
                    textView_posti = itemView.findViewById(R.id.posti);
                    textView_ora = itemView.findViewById(R.id.ora);
                    textView_data = itemView.findViewById(R.id.data);
                }
            }



    @NonNull
    @Override
    public CViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ride, parent,false);
        return new CViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CViewHolder holder, int position) {        //popoliamo gli elementi
        holder.textView_tratta.setText(corsa.get(position).getTratta());
        holder.textView_verso.setText(corsa.get(position).getVerso());
        holder.textView_posti.setText(corsa.get(position).getPosti());
        holder.textView_ora.setText(corsa.get(position).getOra());
        holder.textView_data.setText(corsa.get(position).getData());
    }

    @Override
    public int getItemCount() {  return corsa.size(); }




}
