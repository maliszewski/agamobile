package br.com.agafarma.agamobile.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.agafarma.agamobile.Model.ContatoChatModel;

public class ContatoDAO {
    private static final String DEBUG_TAG = "ContatoDAO";
    private SQLiteDatabase bd;
    private Context context;

    public ContatoDAO(Context paramContext) {
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


    public List<ContatoChatModel> Buscar(ContatoChatModel contatoChatModel)
            throws ParseException {

        String where = " where 1 = 1 ";

        if (contatoChatModel.Tipo > 0)
            where += " and tipo = " + contatoChatModel.Tipo;

        if (contatoChatModel.IdGrupoMqtt > 0)
            where += " and idgrupomqtt = " + contatoChatModel.IdGrupoMqtt;

        if (contatoChatModel.IdUsuario > 0)
            where += " and idusuario = " + contatoChatModel.IdUsuario;

        if (contatoChatModel.IdContato > 0)
            where += " and idcontato = " + contatoChatModel.IdContato;

        if (contatoChatModel.Nome != null && contatoChatModel.Nome.length() > 2)
            where += " and nome like '%" + contatoChatModel.Nome + "%'";

        List<ContatoChatModel> listaContato = Buscar(where);

        return listaContato;
    }

    private List<ContatoChatModel> Buscar(String where) {
        String orderby = " order by tipo desc, nome ";
        List<ContatoChatModel> listaContato = new ArrayList();
        StringBuilder sql = new StringBuilder();
        sql.append(" select  idcontato, topico, nome, tipo, idusuario, idgrupomqtt from contato ");
        sql.append(where);
        sql.append(orderby);
        Abrir();
        Cursor cursor = this.bd.rawQuery(sql.toString(), null);
        Fechar();

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                ContatoChatModel l = new ContatoChatModel();
                l.IdContato = cursor.getInt(0);
                l.Topico = cursor.getString(1);
                l.Nome = cursor.getString(2);
                l.Tipo = cursor.getInt(3);
                l.IdUsuario = cursor.getInt(4);
                l.IdGrupoMqtt = cursor.getInt(5);

                listaContato.add(l);
            } while (cursor.moveToNext());
        }

        Log.i(DEBUG_TAG, "Buscar OK");
        return listaContato;

    }

    public void Deletar() {
        Abrir();
        bd.delete("Contato", null, null);
        Fechar();
        Log.i(DEBUG_TAG, "Deletar OK");
    }

    public void Inserir(ContatoChatModel contatoChatModel) {
        ContentValues value = new ContentValues();
        value.put(DB.colunaContato[0], Integer.valueOf(contatoChatModel.IdContato));
        value.put(DB.colunaContato[1], contatoChatModel.Topico);
        value.put(DB.colunaContato[2], contatoChatModel.Nome);
        value.put(DB.colunaContato[3], Integer.valueOf(contatoChatModel.Tipo));
        value.put(DB.colunaContato[4], Integer.valueOf(contatoChatModel.IdUsuario));
        value.put(DB.colunaContato[5], Integer.valueOf(contatoChatModel.IdGrupoMqtt));
        Abrir();
        this.bd.insert("contato", null, value);
        Fechar();

        Log.i(DEBUG_TAG, "Inserir OK");
    }

    public List<ContatoChatModel> BuscarGrupo() {
        String where = " where idgrupomqtt > 0 ";

        List<ContatoChatModel> listaContato = Buscar(where);
        return listaContato;
    }
}
