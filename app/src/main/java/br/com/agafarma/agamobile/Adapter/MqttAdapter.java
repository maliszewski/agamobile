package br.com.agafarma.agamobile.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.util.List;

import br.com.agafarma.agamobile.Activity.AvisoActivity;
import br.com.agafarma.agamobile.Activity.MqttMensageActivity;
import br.com.agafarma.agamobile.Data.ContatoDAO;
import br.com.agafarma.agamobile.Model.AvisoModel;
import br.com.agafarma.agamobile.Model.ContatoChatModel;
import br.com.agafarma.agamobile.Model.MqttMensagemModel;
import br.com.agafarma.agamobile.R;

public class MqttAdapter extends RecyclerView.Adapter<MqttAdapter.MyViewHolder> {
    private Context mContext;
    private List<MqttMensagemModel> dataSet;
    private float scale;
    private int width, height, roundPixels;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView pNomeUsuario;
        TextView pDecricao;
        CardView pCard;

        public MyViewHolder(final View itemView) {
            super(itemView);
            this.pNomeUsuario = (TextView) itemView.findViewById(R.id.mqtt_nome_usuario);
            this.pDecricao = (TextView) itemView.findViewById(R.id.mqtt_descricao);
            this.pCard = (CardView) itemView.findViewById(R.id.card_view);
        }
    }

    public MqttAdapter(Context c, List<MqttMensagemModel> data) {
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
                .inflate(R.layout.mqtt_lista_item, parent, false);


        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView pNomeUsuario = holder.pNomeUsuario;
        TextView pDecricao = holder.pDecricao;
        CardView pCard = holder.pCard;
        int idUsuario = 0;
        int idGrupoMqtt = 0;

        String nomeUsuario = "";
        if (dataSet.get(listPosition).Tipo == 2)
            nomeUsuario = dataSet.get(listPosition).NomeGrupoMqtt;
        else
            nomeUsuario = dataSet.get(listPosition).NomeUsuario;

        if (nomeUsuario == null || nomeUsuario == "" || nomeUsuario.equals("null")){
            if (dataSet.get(listPosition).Tipo == 2)
                idGrupoMqtt = dataSet.get(listPosition).IdGrupoMqtt;
            else
                idUsuario = dataSet.get(listPosition).IdUsuario;

            ContatoChatModel contatoFiltro = new ContatoChatModel();
            contatoFiltro.IdGrupoMqtt = idGrupoMqtt;
            contatoFiltro.IdUsuario = idUsuario;
            try {
                List<ContatoChatModel> listaContato = new ContatoDAO(mContext).Buscar(contatoFiltro);
                if (listaContato.size() > 0)
                    nomeUsuario = listaContato.get(0).Nome;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        pNomeUsuario.setText(nomeUsuario);

        pDecricao.setText(dataSet.get(listPosition).Mensagem);

        if (dataSet.get(listPosition).Status == 1) {
            pDecricao.setTypeface(null, Typeface.BOLD);
            pDecricao.setTextColor(Color.BLACK);
        } else {
            pDecricao.setTypeface(null, Typeface.NORMAL);
            pDecricao.setTextColor(Color.parseColor("#ff766b67"));
        }

        String finalNomeUsuario = nomeUsuario;
        pCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(view.getContext(), MqttMensageActivity.class);
                bundle.putString("idUsuario", String.valueOf(dataSet.get(listPosition).IdUsuario));
                bundle.putString("nomeUsuario", String.valueOf(dataSet.get(listPosition).NomeUsuario));
                bundle.putString("idGrupoMqtt", String.valueOf(dataSet.get(listPosition).IdGrupoMqtt));
                bundle.putString("nomeGrupoMqtt", finalNomeUsuario);
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
