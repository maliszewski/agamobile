package br.com.agafarma.agamobile.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import br.com.agafarma.agamobile.Activity.TituloActivity;
import br.com.agafarma.agamobile.Model.MenuModel;
import br.com.agafarma.agamobile.Model.SessionModel;
import br.com.agafarma.agamobile.Model.TituloModel;
import br.com.agafarma.agamobile.R;

public class TituloAdapter extends RecyclerView.Adapter<TituloAdapter.MyViewHolder> {
    private Context mContext;
    private List<TituloModel> dataSet;
    private float scale;
    private int width, height, roundPixels;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView pTitulo;
        TextView pDataVencimento;
        TextView pValor;
        CardView pCard;

        public MyViewHolder(final View itemView) {
            super(itemView);
            this.pTitulo = (TextView) itemView.findViewById(R.id.boleto_titulo_parcela);
            this.pDataVencimento = (TextView) itemView.findViewById(R.id.boleto_data_vencimento);
            this.pValor = (TextView) itemView.findViewById(R.id.boleto_valor);
            this.pCard = (CardView) itemView.findViewById(R.id.card_view);

        }
    }

    public TituloAdapter(Context c, List<TituloModel> data) {
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
                .inflate(R.layout.boleto_lista_item, parent, false);


        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView pTitulo = holder.pTitulo;
        TextView pDataVencimento = holder.pDataVencimento;
        TextView pValor = holder.pValor;
        CardView pCard = holder.pCard;

        String titulo = "";
        if (dataSet.get(listPosition).Parcela != null && !dataSet.get(listPosition).Parcela.isEmpty())
            titulo = String.format("%.0f", dataSet.get(listPosition).Titulo) + "/" + dataSet.get(listPosition).Parcela;
        else
            titulo = String.format("%.0f", dataSet.get(listPosition).Titulo);

        if (dataSet.get(listPosition).TipoEmpresa == 3 )
            titulo = SessionModel.Loja.RazaoSocial;

        pTitulo.setText(titulo);

        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
        pDataVencimento.setText(sdf.format(dataSet.get(listPosition).DataVencimento));
        pValor.setText(String.format("%.2f", dataSet.get(listPosition).Saldo));

        pCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TituloActivity.titulo = dataSet.get(listPosition);
                Intent intent = new Intent(view.getContext(), TituloActivity.class);
                view.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
