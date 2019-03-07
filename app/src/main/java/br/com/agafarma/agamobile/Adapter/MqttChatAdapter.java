package br.com.agafarma.agamobile.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.agafarma.agamobile.Activity.AvisoActivity;
import br.com.agafarma.agamobile.Activity.MqttMensageActivity;
import br.com.agafarma.agamobile.Model.MqttMensagemModel;
import br.com.agafarma.agamobile.R;

public class MqttChatAdapter extends BaseAdapter {

    private final List<MqttMensagemModel> data = new ArrayList<>();
    Context mContex;

    public MqttChatAdapter(MqttMensageActivity context, List<MqttMensagemModel> listaMqttMensagemModel) {
        data.addAll(listaMqttMensagemModel);
        mContex = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public MqttMensagemModel getItem(int i) {
        if (data.size() > 0)
            return data.get(i);
        else
            return null;
    }

    @Override
    public long getItemId(int posicao) {
        return posicao;
    }

    @Override
    public View getView(int posicao, View view, ViewGroup viewGroup) {

        LayoutInflater mInflater = (LayoutInflater) mContex.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        MqttMensagemModel mqttMensagem = getItem(posicao);


        if (mqttMensagem.Status == 1 || mqttMensagem.Status == 2) {
            view = mInflater.inflate(R.layout.mqtt_mensage_lista_item_esquerda, null);

        } else {
            view = mInflater.inflate(R.layout.mqtt_mensage_lista_item_direta, null);
        }

        TextView txtMensagem = (TextView) view.findViewById(R.id.txt_mensagem);
        TextView txtUsuario = (TextView) view.findViewById(R.id.txt_nome_usuario);

        if (mqttMensagem.Status == 1 || mqttMensagem.Status == 2) {
            txtUsuario.setText(mqttMensagem.NomeUsuario);
        } else {
            txtUsuario.setText("Eu");
        }

        txtMensagem.setText(mqttMensagem.Mensagem);

        return view;
    }

    public void Adicionar(MqttMensagemModel msg) {
        data.add(msg);
    }

    public void Adicionar(List<MqttMensagemModel> listaMsg) {
        data.addAll(listaMsg);
    }

    public void Limpar() {
        if (data.size() > 0)
            data.clear();
    }
}
