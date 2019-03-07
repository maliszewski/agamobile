package br.com.agafarma.agamobile.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.agafarma.agamobile.Activity.PerguntaActivity;
import br.com.agafarma.agamobile.Model.QuestionarioModel;
import br.com.agafarma.agamobile.R;

public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.ViewHolder> {
    private Context mContext;
    private List<QuestionarioModel> dataSet;
    private float scale;
    private int width, height, roundPixels;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView pTitulo;
        TextView pDecricao;
        CardView pCard;

        public ViewHolder(final View itemView) {
            super(itemView);
            this.pTitulo = (TextView) itemView.findViewById(R.id.agenda_titulo);
            this.pDecricao = (TextView) itemView.findViewById(R.id.agenda_descricao);
            this.pCard = (CardView) itemView.findViewById(R.id.card_view);
        }
    }

    public AgendaAdapter(Context c, List<QuestionarioModel> data) {
        this.dataSet = data;
        mContext = c;

        scale = mContext.getResources().getDisplayMetrics().density;
        width = mContext.getResources().getDisplayMetrics().widthPixels - (int) (14 * scale + 0.5f);
        height = (width / 16) * 9;

        roundPixels = (int) (2 * scale + 0.5f);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.agenda_lista_item, parent, false);


        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition) {

        TextView pTitulo = holder.pTitulo;
        TextView pDecricao = holder.pDecricao;
        CardView pCard = holder.pCard;

        pTitulo.setText("AGA-" +dataSet.get(listPosition).IdLoja +" - " + dataSet.get(listPosition).Titulo);
        pDecricao.setText(dataSet.get(listPosition).Descricao);

       pCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Bundle bundle = new Bundle();
               Intent intent = new Intent(view.getContext(), PerguntaActivity.class);
               bundle.putString("idQuestionario", String.valueOf(dataSet.get(listPosition).IdQuestionario));
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
