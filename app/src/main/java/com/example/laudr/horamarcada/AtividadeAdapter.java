package com.example.laudr.horamarcada;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class AtividadeAdapter extends BaseAdapter {

    private Context ctx;
    private List<Atividade> atividades;

    public AtividadeAdapter(Context ctx, List<Atividade> atividades) {
        this.ctx = ctx;
        this.atividades = atividades;
    }

    @Override
    public int getCount() {
        return atividades.size();
    }

    @Override
    public Object getItem(int position) {
        return atividades.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Atividade atividade = atividades.get(position);

        View linha = LayoutInflater.from(ctx).inflate(R.layout.item_atividade, null);

        TextView txtEquipe2 = linha.findViewById(R.id.txtEquipe);
        TextView txtAtividade2 = linha.findViewById(R.id.txtAtividade);

        txtEquipe2.setText(atividade.getNome());
        txtAtividade2.setText(atividade.getDescricao());


        return linha;
    }
}
