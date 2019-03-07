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

import br.com.agafarma.agamobile.Activity.CircularActivity;
import br.com.agafarma.agamobile.Model.CircularModel;
import br.com.agafarma.agamobile.R;

public class CircularAdapter extends RecyclerView.Adapter<CircularAdapter.MyViewHolder> {
    private Context mContext;
    private List<CircularModel> dataSet;
    private float scale;
    private int width, height, roundPixels;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView pNumero;
        TextView pAssunto;
        CardView pCard;

        public MyViewHolder(final View itemView) {
            super(itemView);
            this.pNumero = (TextView) itemView.findViewById(R.id.circular_numero);
            this.pAssunto = (TextView) itemView.findViewById(R.id.circular_assunto);
            this.pCard = (CardView) itemView.findViewById(R.id.card_view);
        }
    }

    public CircularAdapter(Context c, List<CircularModel> data) {
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
                .inflate(R.layout.circular_lista_item, parent, false);


        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView pTitulo = holder.pNumero;
        TextView pDecricao = holder.pAssunto;
        CardView pCard = holder.pCard;

        pTitulo.setText(dataSet.get(listPosition).Numero + "/" + dataSet.get(listPosition).Ano);
        pDecricao.setText(dataSet.get(listPosition).Assunto);

        pCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(dataSet.get(listPosition).Anexo));
                //view.getContext().startActivity(browserIntent);

                Bundle bundle = new Bundle();
                Intent intent = new Intent(view.getContext(), CircularActivity.class);
                CircularActivity.circular = dataSet.get(listPosition);
                //bundle.putString("idCircular", String.valueOf(dataSet.get(listPosition).IdCircular));
                //intent.putExtras(bundle);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
