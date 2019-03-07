package br.com.agafarma.agamobile.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import br.com.agafarma.agamobile.Model.AvisoModel;
import br.com.agafarma.agamobile.Model.QuestionarioPerguntaRespostaModel;
import br.com.agafarma.agamobile.R;

public class RespostaAdapter extends RecyclerView.Adapter<RespostaAdapter.MyViewHolder> {
    private Context mContext;
    private List<QuestionarioPerguntaRespostaModel> dataSet;
    private float scale;
    private int width, height, roundPixels;
    private int tipoResposta;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView pTitulo;
        TextView pDecricao;
        RadioButton pRadio;
        CheckBox pCheck;

        public MyViewHolder(final View itemView) {
            super(itemView);
            this.pTitulo = (TextView) itemView.findViewById(R.id.resposta_titulo);
            this.pDecricao = (TextView) itemView.findViewById(R.id.resposta_descricao);
            this.pRadio = (RadioButton) itemView.findViewById(R.id.resposta_radio);
            this.pCheck = (CheckBox) itemView.findViewById(R.id.resposta_check);
        }
    }

    public RespostaAdapter(Context c, List<QuestionarioPerguntaRespostaModel> data, int tipoResposta) {
        this.dataSet = data;
        this.tipoResposta = tipoResposta;
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
                .inflate(R.layout.resposta_lista_item, parent, false);


        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView pTitulo = holder.pTitulo;
        TextView pDecricao = holder.pDecricao;
        RadioButton pRadio = holder.pRadio;
        CheckBox pCheck = holder.pCheck;
        pTitulo.setText(dataSet.get(listPosition).Titulo);
        pDecricao.setText(dataSet.get(listPosition).Descricao);

        pRadio.setTag(R.id.IdQuestionarioPerguntaResposta, dataSet.get(listPosition).IdQuestionarioPerguntaResposta);
        pRadio.setTag("radio");
        pCheck.setTag(R.id.IdQuestionarioPerguntaResposta, dataSet.get(listPosition).IdQuestionarioPerguntaResposta);
        pCheck.setTag("check");


        if (this.tipoResposta == 1) {
            pCheck.setVisibility(View.INVISIBLE);
            pRadio.setVisibility(View.VISIBLE);
        } else {
            pCheck.setVisibility(View.VISIBLE);
            pRadio.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
