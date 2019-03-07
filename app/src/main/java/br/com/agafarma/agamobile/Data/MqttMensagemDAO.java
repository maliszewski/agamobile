package br.com.agafarma.agamobile.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.agafarma.agamobile.Model.MenuModel;
import br.com.agafarma.agamobile.Model.MqttMensagemModel;
import br.com.agafarma.agamobile.Model.QuestionarioModel;
import br.com.agafarma.agamobile.Model.UsuarioModel;

public class MqttMensagemDAO {
    private static final String DEBUG_TAG = "MqttMensagemDAO";
    private SQLiteDatabase bd;
    private Context context;

    public MqttMensagemDAO(Context paramContext) {
        context = paramContext;
        Abrir();
    }

    private void Abrir() {

        if (bd == null || !bd.isOpen()) {
            bd = DB.getInstance(context).getWritableDatabase();
            bd.enableWriteAheadLogging();
        }
    }

    private void Fechar() {
        if (bd == null && bd.isOpen()) {
            bd.close();
        }
    }

    public List<MqttMensagemModel> ListaIdGrupoUsuario() {

        StringBuilder sql = new StringBuilder();
        sql.append(" select m.idgrupomqtt, m.idusuario, m.tipo " +
                " from mqttmensagem m " +
                " where m.tipo != 3 and ((m.tipo = 2 and m.idgrupomqtt > 0) or (m.tipo = 1 and m.idusuario > 0))" +
                " group by m.idgrupomqtt, m.idusuario, m.tipo " +
                " order by m.tipo desc  ");

        Log.i(DEBUG_TAG, sql.toString());
        Abrir();
        Cursor localCursor = this.bd.rawQuery(sql.toString(), null);
        Fechar();
        List<MqttMensagemModel> resultado = new ArrayList<>();
        if (localCursor.getCount() > 0) {
            localCursor.moveToFirst();
            do {
                MqttMensagemModel mqttM = new MqttMensagemModel();
                mqttM.IdGrupoMqtt = localCursor.getInt(0);
                mqttM.IdUsuario = localCursor.getInt(1);
                mqttM.Tipo = localCursor.getInt(2);

                resultado.add(mqttM);

            } while (localCursor.moveToNext());
        }
        Log.i(DEBUG_TAG, "Buscar OK");
        return resultado;
    }

    public List<MqttMensagemModel> BuscarAgrupado() {

        StringBuilder sql = new StringBuilder();
        sql.append(" select m.tipo, m.idgrupomqtt, m.nomegrupomqtt, m.idusuario, " +
                " m.nomeusuario, " +
                " (select mm.mensagem from mqttmensagem mm where (m.tipo = 1 and mm.tipo = m.tipo and mm.idusuario = m.idusuario) or (m.tipo = 2 and mm.tipo = m.tipo and mm.idgrupomqtt = m.idgrupomqtt) or (m.tipo = 3 and mm.tipo = m.tipo and mm.idusuario = m.idusuario) " +
                " order by idmqttmensagem desc limit 1) mensagem," +
                " '' titulo, " +
                " m.topico, " +
                " m.status " +
                " from mqttmensagem m " +
                " group by m.tipo, m.topico " +
                " order by m.idmqttmensagem desc  ");

        Log.i(DEBUG_TAG, sql.toString());
        Abrir();
        Cursor localCursor = this.bd.rawQuery(sql.toString(), null);
        Fechar();
        List<MqttMensagemModel> resultado = new ArrayList<>();
        if (localCursor.getCount() > 0) {
            localCursor.moveToFirst();
            do {
                MqttMensagemModel mqttM = new MqttMensagemModel();
                mqttM.Tipo = localCursor.getInt(0);
                mqttM.IdGrupoMqtt = localCursor.getInt(1);
                mqttM.NomeGrupoMqtt = localCursor.getString(2);

                mqttM.IdUsuario = localCursor.getInt(3);
                mqttM.NomeUsuario = localCursor.getString(4);
                mqttM.Mensagem = localCursor.getString(5);
                mqttM.Titulo = localCursor.getString(6);
                mqttM.Topico = localCursor.getString(7);
                mqttM.Status = localCursor.getInt(8);

                resultado.add(mqttM);

            } while (localCursor.moveToNext());
        }
        Log.i(DEBUG_TAG, "Buscar OK");
        return resultado;
    }

    public List<MqttMensagemModel> Buscar(MqttMensagemModel paramMqttMensagemModel, boolean orderStatus) {
        List<MqttMensagemModel> result = new ArrayList();
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from mqttmensagem where 1=1 ");

        if (paramMqttMensagemModel.Status > 0)
            sql.append(" and status = " + paramMqttMensagemModel.Status);
        if (paramMqttMensagemModel.Tipo > 0)
            sql.append(" and tipo = " + paramMqttMensagemModel.Tipo);
        if (paramMqttMensagemModel.IdUsuario > 0)
            sql.append(" and idusuario = " + paramMqttMensagemModel.IdUsuario);
        if (paramMqttMensagemModel.IdGrupoMqtt > 0)
            sql.append(" and idgrupomqtt = " + paramMqttMensagemModel.IdGrupoMqtt);

        if (orderStatus)
            sql.append(" order by status asc, data_recebido desc, data_resposta desc ");
        else
            sql.append(" order by idmqttmensagem asc ");

        Log.i(DEBUG_TAG, sql.toString());
        Abrir();
        Cursor localCursor = this.bd.rawQuery(sql.toString(), null);
        Fechar();
        if (localCursor.getCount() > 0) {
            localCursor.moveToFirst();
            do {
                MqttMensagemModel mqttM = new MqttMensagemModel();
                mqttM.IdMqttMensagem = localCursor.getInt(0);
                mqttM.IdReferencia = localCursor.getInt(1);
                mqttM.Tipo = localCursor.getInt(2);
                mqttM.Link = localCursor.getString(3);
                mqttM.Titulo = localCursor.getString(4);
                mqttM.Mensagem = localCursor.getString(5);
                mqttM.IdUsuario = localCursor.getInt(6);
                mqttM.NomeUsuario = localCursor.getString(7);
                mqttM.Status = localCursor.getInt(8);
                mqttM.DataRecebido.setTime(localCursor.getLong(9));
                mqttM.DataLeitura.setTime(localCursor.getLong(10));
                mqttM.DataResposta.setTime(localCursor.getLong(11));
                mqttM.Topico = localCursor.getString(12);
                mqttM.IdGrupoMqtt = localCursor.getInt(13);
                mqttM.NomeGrupoMqtt = localCursor.getString(14);

                result.add(mqttM);

            } while (localCursor.moveToNext());
        }

        Log.i(DEBUG_TAG, "Buscar OK");
        return result;
    }

    public void Deletar(MqttMensagemModel mqttMensagemModel) {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("1 = 1");

        if (mqttMensagemModel.IdMqttMensagem > 0)
            localStringBuilder.append(" and idmqttMensagem = " + mqttMensagemModel.IdMqttMensagem);
        if (mqttMensagemModel.Tipo > 0)
            localStringBuilder.append(" and tipo = " + mqttMensagemModel.Tipo);
        if (mqttMensagemModel.IdUsuario > 0)
            localStringBuilder.append(" and idusuario = " + mqttMensagemModel.IdUsuario);
        if (mqttMensagemModel.IdGrupoMqtt > 0)
            localStringBuilder.append(" and idgrupoMqtt = " + mqttMensagemModel.IdGrupoMqtt);

        Abrir();
        bd.delete("mqttmensagem", localStringBuilder.toString(), null);
        Fechar();
        Log.i(DEBUG_TAG, "Deletar OK");
    }

    public MqttMensagemModel Inserir(MqttMensagemModel paramMqttMensagemModel) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DB.colunaMqttMensagem[1], Integer.valueOf(paramMqttMensagemModel.IdReferencia));
        contentValues.put(DB.colunaMqttMensagem[2], Integer.valueOf(paramMqttMensagemModel.Tipo));
        contentValues.put(DB.colunaMqttMensagem[3], paramMqttMensagemModel.Link);
        contentValues.put(DB.colunaMqttMensagem[4], paramMqttMensagemModel.Titulo);
        contentValues.put(DB.colunaMqttMensagem[5], paramMqttMensagemModel.Mensagem);
        contentValues.put(DB.colunaMqttMensagem[6], Integer.valueOf(paramMqttMensagemModel.IdUsuario));
        contentValues.put(DB.colunaMqttMensagem[7], paramMqttMensagemModel.NomeUsuario);
        contentValues.put(DB.colunaMqttMensagem[8], Integer.valueOf(paramMqttMensagemModel.Status));

        contentValues.put(DB.colunaMqttMensagem[9], Long.valueOf(paramMqttMensagemModel.DataRecebido.getTime()));
        contentValues.put(DB.colunaMqttMensagem[10], Long.valueOf(paramMqttMensagemModel.DataLeitura.getTime()));
        contentValues.put(DB.colunaMqttMensagem[11], Long.valueOf(paramMqttMensagemModel.DataResposta.getTime()));
        contentValues.put(DB.colunaMqttMensagem[12], paramMqttMensagemModel.Topico);
        contentValues.put(DB.colunaMqttMensagem[13], Integer.valueOf(paramMqttMensagemModel.IdGrupoMqtt));
        contentValues.put(DB.colunaMqttMensagem[14], paramMqttMensagemModel.NomeGrupoMqtt);

        Abrir();
        int id = (int) this.bd.insert("mqttmensagem", null, (ContentValues) contentValues);
        Log.i(DEBUG_TAG, "Inserir OK");
        Fechar();
        paramMqttMensagemModel.IdMqttMensagem = id;
        return paramMqttMensagemModel;
    }

    public void Atualizar(MqttMensagemModel paramMqttMensagemModel) {

        ContentValues values = new ContentValues();
        values.put(DB.colunaMqttMensagem[0], Integer.valueOf(paramMqttMensagemModel.IdMqttMensagem));
        values.put(DB.colunaMqttMensagem[1], Integer.valueOf(paramMqttMensagemModel.IdReferencia));
        values.put(DB.colunaMqttMensagem[2], Integer.valueOf(paramMqttMensagemModel.Tipo));
        values.put(DB.colunaMqttMensagem[3], paramMqttMensagemModel.Link);
        values.put(DB.colunaMqttMensagem[4], paramMqttMensagemModel.Titulo);
        values.put(DB.colunaMqttMensagem[5], paramMqttMensagemModel.Mensagem);
        values.put(DB.colunaMqttMensagem[6], Integer.valueOf(paramMqttMensagemModel.IdUsuario));
        values.put(DB.colunaMqttMensagem[7], paramMqttMensagemModel.NomeUsuario);
        values.put(DB.colunaMqttMensagem[8], Integer.valueOf(paramMqttMensagemModel.Status));
        values.put(DB.colunaMqttMensagem[9], Long.valueOf(paramMqttMensagemModel.DataRecebido.getTime()));
        values.put(DB.colunaMqttMensagem[10], Long.valueOf(paramMqttMensagemModel.DataLeitura.getTime()));
        values.put(DB.colunaMqttMensagem[11], Long.valueOf(paramMqttMensagemModel.DataResposta.getTime()));
        values.put(DB.colunaMqttMensagem[12], paramMqttMensagemModel.Topico);


        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("");
        localStringBuilder.append(paramMqttMensagemModel.IdMqttMensagem);
        Abrir();
        bd.update("mqttmensagem", values, "idmqttMensagem = ?", new String[]{localStringBuilder.toString()});
        Fechar();
        Log.i(DEBUG_TAG, "Atualizar OK");
    }

}