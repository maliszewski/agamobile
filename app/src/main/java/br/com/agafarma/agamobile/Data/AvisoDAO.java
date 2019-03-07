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

import br.com.agafarma.agamobile.Model.AvisoModel;
import br.com.agafarma.agamobile.Model.QuestionarioModel;
import br.com.agafarma.agamobile.Util.DataHora;

public class AvisoDAO {
    private static final String DEBUG_TAG = "AvisoDAO";
    private SQLiteDatabase bd;
    private Context context;

    public AvisoDAO(Context paramContext) {
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


    public List<AvisoModel> Buscar(AvisoModel avisoModel)
            throws ParseException {

        ArrayList localArrayList = new ArrayList();
        String where = " where 1 = 1 ";
        if (avisoModel.Tipo > 0)
            where += " and tipo = " + avisoModel.Tipo;
        if (avisoModel.IdAviso > 0)
            where += " and idaviso = " + avisoModel.IdAviso;

        StringBuilder sql = new StringBuilder();
        sql.append(" select  idaviso, titulo, tipo, descricao, data_cadastro, idusuario, data_notificacao from aviso ");
        sql.append(where);
        Abrir();
        Cursor cursor = this.bd.rawQuery(sql.toString(), null);
        Fechar();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                AvisoModel l = new AvisoModel();
                l.IdAviso = cursor.getInt(0);
                l.Titulo = cursor.getString(1);
                l.Tipo = cursor.getInt(2);
                l.Descricao = cursor.getString(3);
                l.DataCadastro.setTime(cursor.getLong(4));
                l.IdUsuario = cursor.getInt(5);
                l.DataNotificacao.setTime(cursor.getLong(6));

                localArrayList.add(l);
            } while (cursor.moveToNext());
        }
        Log.i(DEBUG_TAG, "Buscar OK");
        return localArrayList;
    }


    public List<AvisoModel> BuscarNotificacao() throws ParseException {
        ArrayList localArrayList = new ArrayList();
        Date data = new Date();
        String where = " where tipo = 2 and  data_notificacao < " + data.getTime();
        String order = " order by data_notificacao desc";
        StringBuilder sql = new StringBuilder();
        sql.append(" select  idaviso, titulo, tipo, descricao, data_cadastro, idusuario, data_notificacao from aviso ");
        sql.append(where + order);
        Abrir();
        Cursor cursor = this.bd.rawQuery(sql.toString(), null);
        Fechar();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                AvisoModel l = new AvisoModel();
                l.IdAviso = cursor.getInt(0);
                l.Titulo = cursor.getString(1);
                l.Tipo = cursor.getInt(2);
                l.Descricao = cursor.getString(3);
                l.DataCadastro.setTime(cursor.getLong(4));
                l.IdUsuario = cursor.getInt(5);
                l.DataNotificacao.setTime(cursor.getLong(6));

                localArrayList.add(l);
            } while (cursor.moveToNext());
        }
        Log.i("AvisoDAO", "Buscar OK");
        return localArrayList;

    }

    public void Deletar(int idAviso) {
        String where = "idAviso = " + idAviso;
        Abrir();
        bd.delete("Aviso", where, null);
        Fechar();
        Log.i(DEBUG_TAG, "Deletar OK");
    }

    public void Inserir(AvisoModel avisoModel) {
        ContentValues value = new ContentValues();
        value.put(DB.colunaAviso[0], Integer.valueOf(avisoModel.IdAviso));
        value.put(DB.colunaAviso[1], avisoModel.Titulo);
        value.put(DB.colunaAviso[2], avisoModel.Descricao);
        value.put(DB.colunaAviso[3], Integer.valueOf(avisoModel.Tipo));
        value.put(DB.colunaAviso[4], Long.valueOf(avisoModel.DataCadastro.getTime()));
        value.put(DB.colunaAviso[5], Integer.valueOf(avisoModel.IdUsuario));
        value.put(DB.colunaAviso[6], Long.valueOf(avisoModel.DataNotificacao.getTime()));

        Abrir();
        this.bd.insert("aviso", null, value);
        Fechar();
    }

    public void Atualizar(AvisoModel avisoModel) {

        ContentValues value = new ContentValues();
        value.put(DB.colunaAviso[0], Integer.valueOf(avisoModel.IdAviso));
        value.put(DB.colunaAviso[1], avisoModel.Titulo);
        value.put(DB.colunaAviso[2], avisoModel.Descricao);
        value.put(DB.colunaAviso[3], Integer.valueOf(avisoModel.Tipo));
        value.put(DB.colunaAviso[4], Long.valueOf(avisoModel.DataCadastro.getTime()));
        value.put(DB.colunaAviso[5], Integer.valueOf(avisoModel.IdUsuario));
        value.put(DB.colunaAviso[6], Long.valueOf(avisoModel.DataNotificacao.getTime()));
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("");
        localStringBuilder.append(avisoModel.IdAviso);
        Abrir();
        bd.update("aviso", value, "idaviso = ?", new String[]{localStringBuilder.toString()});
        Fechar();
        Log.i(DEBUG_TAG, "Atualizar OK");
    }

}
