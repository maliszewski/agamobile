package br.com.agafarma.agamobile.Model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class MqttMensagemModel {

    public int IdMqttMensagem;
    public int IdReferencia;
    // 1 = Chat; 2 = Grupo; 3 = Alertas
    public int Tipo;
    public String Link;
    public String Titulo;
    public String Mensagem;
    public int IdUsuario;
    public String NomeUsuario;

    //Grupo de conversa
    public int IdGrupoMqtt;
    public String NomeGrupoMqtt;

    // 1 = Recebido; 2 = Visualizado; 3 = Enviar; 4 = Enviado
    public int Status;
    public Date DataRecebido;
    public Date DataLeitura;
    public Date DataResposta;
    public String Topico;


    public MqttMensagemModel(JSONObject obj, String topico) {
        if (obj != null) {
            try {

                Tipo = obj.getInt("Tipo");
                if (obj.has("IdReferencia"))
                    IdReferencia = obj.getInt("IdReferencia");
                if (obj.has("Link"))
                    Link = obj.getString("Link");
                if (obj.has("Titulo"))
                    Titulo = obj.getString("Titulo");

                if (obj.has("Mensagem"))
                    Mensagem = obj.getString("Mensagem");
                if (obj.has("IdUsuario"))
                    IdUsuario = obj.getInt("IdUsuario");
                if (obj.has("NomeUsuario"))
                    NomeUsuario = obj.getString("NomeUsuario");

                if (obj.has("IdGrupoMqtt"))
                    IdGrupoMqtt = obj.getInt("IdGrupoMqtt");
                if (obj.has("NomeGrupoMqtt"))
                    NomeGrupoMqtt = obj.getString("NomeGrupoMqtt");


                Status = 1;
                DataRecebido = new Date();
                DataLeitura = new Date();
                DataResposta = new Date();
                IdMqttMensagem = 0;
                Topico = topico;

            } catch (JSONException e) {
                Log.i("MqttMensagemModel", "Erro ou Deserializar");
                e.printStackTrace();
            }
        }
    }

    public MqttMensagemModel(){
        DataRecebido = new Date();
        DataLeitura = new Date();
        DataResposta = new Date();

    }
}
