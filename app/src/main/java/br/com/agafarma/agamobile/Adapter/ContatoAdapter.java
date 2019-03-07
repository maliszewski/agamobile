package br.com.agafarma.agamobile.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.agafarma.agamobile.Activity.MqttMensageActivity;
import br.com.agafarma.agamobile.Model.ContatoChatModel;
import br.com.agafarma.agamobile.R;

public class ContatoAdapter extends RecyclerView.Adapter<ContatoAdapter.MyViewHolder> {
    private Context mContext;
    private List<ContatoChatModel> dataSet;
    private float scale;
    private int width, height, roundPixels;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView pNome;
        CardView pCard;

        public MyViewHolder(final View itemView) {
            super(itemView);
            this.pNome = (TextView) itemView.findViewById(R.id.txt_contato);
            this.pCard = (CardView) itemView.findViewById(R.id.card_view);
        }
    }

    public ContatoAdapter(Context c, List<ContatoChatModel> data) {
        this.dataSet = data;
        mContext = c;

        scale = mContext.getResources().getDisplayMetrics().density;
        width = mContext.getResources().getDisplayMetrics().widthPixels - (int) (14 * scale + 0.5f);
        height = (width / 16) * 9;

        roundPixels = (int) (2 * scale + 0.5f);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contato_lista_item, parent, false);


        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView pNome = holder.pNome;
        CardView pCard = holder.pCard;

        pNome.setText(dataSet.get(listPosition).Nome);

        pCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                Intent intent = new Intent(view.getContext(), MqttMensageActivity.class);
                bundle.putString("idUsuario", String.valueOf(dataSet.get(listPosition).IdUsuario));
                bundle.putString("nomeUsuario", String.valueOf(dataSet.get(listPosition).Nome));
                bundle.putString("idGrupoMqtt", String.valueOf(dataSet.get(listPosition).IdGrupoMqtt));
                bundle.putString("nomeGrupoMqtt", String.valueOf(dataSet.get(listPosition).Nome));
                bundle.putString("tipo", String.valueOf(dataSet.get(listPosition).Tipo));
                bundle.putString("topico", String.valueOf(dataSet.get(listPosition).Topico));

                intent.putExtras(bundle);
                view.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
