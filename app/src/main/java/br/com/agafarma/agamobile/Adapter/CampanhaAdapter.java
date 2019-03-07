package br.com.agafarma.agamobile.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

import br.com.agafarma.agamobile.Activity.AvisoActivity;
import br.com.agafarma.agamobile.Model.AvisoModel;
import br.com.agafarma.agamobile.Model.CampanhaModel;
import br.com.agafarma.agamobile.R;
import br.com.agafarma.agamobile.Util.Autorizacao;
import br.com.agafarma.agamobile.Util.Image;
import pl.droidsonroids.gif.GifTextView;

public class CampanhaAdapter extends RecyclerView.Adapter<CampanhaAdapter.MyViewHolder> {
    private Context mContext;
    private List<CampanhaModel> dataSet;
    private float scale;
    private int width, height, roundPixels;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView pImage;
        TextView pTitulo;
        TextView pDecricao;
        CardView pCard;
        ImageLoader pImageLoader;
        GifTextView pCarregando;
        public MyViewHolder(final View itemView) {
            super(itemView);
            this.pImage = (ImageView) itemView.findViewById(R.id.campanha_foto);
            this.pTitulo = (TextView) itemView.findViewById(R.id.campanha_titulo);
            this.pDecricao = (TextView) itemView.findViewById(R.id.campanha_descricao);
            this.pCard = (CardView) itemView.findViewById(R.id.card_view);
            this.pImageLoader = ImageLoader.getInstance();
            this.pCarregando= (GifTextView) itemView.findViewById(R.id.carregando);
        }
    }

    public CampanhaAdapter(Context c, List<CampanhaModel> data) {
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
                .inflate(R.layout.campanha_lista_item, parent, false);


        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        ImageView pImage = holder.pImage;
        GifTextView pCarregando = holder.pCarregando;
        TextView pTitulo = holder.pTitulo;
        TextView pDecricao = holder.pDecricao;
        CardView pCard = holder.pCard;
        String urlFoto = (dataSet.get(listPosition).UrlImage != null && dataSet.get(listPosition).UrlImage.length() > 0) ? Autorizacao.BuscaURLPortal() +
                dataSet.get(listPosition).UrlImage : "";

        holder.pImageLoader.loadImage(urlFoto.trim(), new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                pCarregando.setVisibility(View.INVISIBLE);
                pImage.setImageBitmap(loadedImage);
            }

            @Override
            public void onLoadingStarted(String imageUri, View view) {
                pCarregando.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            }

        });

        pTitulo.setText(dataSet.get(listPosition).Titulo + dataSet.get(listPosition).Apelido);
        pDecricao.setText(dataSet.get(listPosition).Descricao);

        pCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Bundle bundle = new Bundle();
                //Intent intent = new Intent(view.getContext(), CampanhaActivity.class);
                //bundle.putString("idCampanha", String.valueOf(dataSet.get(listPosition).IdCampanha));
                //bundle.putString("tipo", String.valueOf(dataSet.get(listPosition).IdCampanha));
                //intent.putExtras(bundle);
                //view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
