package br.com.agafarma.agamobile.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.agafarma.agamobile.Activity.AvisoActivity;
import br.com.agafarma.agamobile.Model.AvisoModel;
import br.com.agafarma.agamobile.R;

public class FotoAdapter extends RecyclerView.Adapter<FotoAdapter.MyViewHolder> {
    private Context mContext;
    private List<String> dataSet;
    private float scale;
    private int width, height, roundPixels;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView pImage;
        CardView pCard;

        public MyViewHolder(final View itemView) {
            super(itemView);
            this.pImage = (ImageView) itemView.findViewById(R.id.foto_image);
            this.pCard = (CardView) itemView.findViewById(R.id.card_view);
        }
    }

    public FotoAdapter(Context c, List<String> data) {
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
                .inflate(R.layout.foto_lista_item, parent, false);


        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        ImageView pImage = holder.pImage;
        pImage.setImageURI(Uri.parse(dataSet.get(listPosition).toString()));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
