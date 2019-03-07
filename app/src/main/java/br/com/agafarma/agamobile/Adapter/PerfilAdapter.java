package br.com.agafarma.agamobile.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.agafarma.agamobile.Activity.LoginActivity;
import br.com.agafarma.agamobile.Activity.PerfilActivity;
import br.com.agafarma.agamobile.Data.LoginDAO;
import br.com.agafarma.agamobile.Model.MenuModel;
import br.com.agafarma.agamobile.R;

public class PerfilAdapter extends RecyclerView.Adapter<PerfilAdapter.MyViewHolder> {
    private Context mContext;
    private List<MenuModel> dataSet;
    private float scale;
    private int width, height, roundPixels;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView pImage;
        TextView pTitulo;
        TextView pHint;
        CardView pCard;
        Integer pMenu;
        Class<?> pCls;

        public MyViewHolder(final View itemView) {
            super(itemView);
            this.pTitulo = (TextView) itemView.findViewById(R.id.menu_titulo);
            this.pImage = (ImageView) itemView.findViewById(R.id.menu_image);
            this.pCard = (CardView) itemView.findViewById(R.id.card_view);
            this.pHint = (TextView) itemView.findViewById(R.id.menu_hint);

        }
    }

    public PerfilAdapter(Context c, List<MenuModel> data) {
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
                .inflate(R.layout.menu_lista_item, parent, false);


        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView pTitulo = holder.pTitulo;
        ImageView pImage = holder.pImage;
        CardView pCard = holder.pCard;
        Integer pMenu = holder.pMenu;
        Class<?> pCls = holder.pCls;
        TextView pHint = holder.pHint;

        pTitulo.setText(dataSet.get(listPosition).Titulo);
        pImage.setImageDrawable(this.mContext.getDrawable(dataSet.get(listPosition).IdImage));
        pImage.setColorFilter(this.mContext.getResources().getColor(R.color.menuIcone));

        pMenu = dataSet.get(listPosition).IdMenu;
        pCls = dataSet.get(listPosition).Cls;
        pHint.setText(String.valueOf(dataSet.get(listPosition).HintQt));
        if (dataSet.get(listPosition).HintQt == 0)
            pHint.setVisibility(View.INVISIBLE);


        Class<?> finalPCls = pCls;
        pCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), finalPCls);
                view.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
