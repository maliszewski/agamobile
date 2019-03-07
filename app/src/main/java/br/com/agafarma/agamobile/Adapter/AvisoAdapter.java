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

import br.com.agafarma.agamobile.Activity.AvisoActivity;
import br.com.agafarma.agamobile.Model.AvisoModel;
import br.com.agafarma.agamobile.R;

public class AvisoAdapter extends RecyclerView.Adapter<AvisoAdapter.MyViewHolder> {
    private Context mContext;
    private List<AvisoModel> dataSet;
    private float scale;
    private int width, height, roundPixels;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView pTitulo;
        TextView pDecricao;
        CardView pCard;

        public MyViewHolder(final View itemView) {
            super(itemView);
            this.pTitulo = (TextView) itemView.findViewById(R.id.aviso_titulo);
            this.pDecricao = (TextView) itemView.findViewById(R.id.aviso_descricao);
            this.pCard = (CardView) itemView.findViewById(R.id.card_view);
        }
    }

    public AvisoAdapter(Context c, List<AvisoModel> data) {
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
                .inflate(R.layout.aviso_lista_item, parent, false);


        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView pTitulo = holder.pTitulo;
        TextView pDecricao = holder.pDecricao;
        CardView pCard = holder.pCard;

        pTitulo.setText(dataSet.get(listPosition).Titulo);
        pDecricao.setText(dataSet.get(listPosition).Descricao);

       pCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Bundle bundle = new Bundle();
               Intent intent = new Intent(view.getContext(), AvisoActivity.class);
               bundle.putString("idAviso", String.valueOf(dataSet.get(listPosition).IdAviso));
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
