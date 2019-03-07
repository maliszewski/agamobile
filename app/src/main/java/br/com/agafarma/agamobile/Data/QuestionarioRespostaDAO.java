package br.com.agafarma.agamobile.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import br.com.agafarma.agamobile.Model.QuestionarioModel;
import br.com.agafarma.agamobile.Model.QuestionarioRespostaModel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuestionarioRespostaDAO {
    private static final String DEBUG_TAG = "QuestionarioRespostaDAO";
    private SQLiteDatabase bd;
    private Context context;

    public QuestionarioRespostaDAO(Context paramContext) {
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


    public List<QuestionarioRespostaModel> Buscar(QuestionarioModel paramQuestionarioModel)
            throws ParseException {

        ArrayList localArrayList = new ArrayList();
        String where = " where 1 = 1 ";
        if (paramQuestionarioModel.Status > 0)
            where += " and q.status = " + paramQuestionarioModel.Status;
        if (paramQuestionarioModel.IdQuestionario > 0)
            where += " and idquestionario = " + paramQuestionarioModel.IdQuestionario;

        StringBuilder sql = new StringBuilder();
        sql.append(" select    qr.idquestionarioresposta,  " +
                "qr.idquestionario,  " +
                "qr.idquestionariopergunta,  " +
                "qr.idquestionariopergresp,  " +
                "qr.texto,  " +
                "qr.data_cadastro,  " +
                "qr.idusuario " +
                "from questionarioresposta qr  " +
                "inner join questionario q on q.idquestionario = qr.idquestionario ");
        sql.append(where);
        Abrir();
        Cursor cursor = this.bd.rawQuery(sql.toString(), null);
        Fechar();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                QuestionarioRespostaModel l = new QuestionarioRespostaModel();
                l.IdQuestionarioResposta = cursor.getInt(0);
                l.IdQuestionario = cursor.getInt(1);
                l.IdQuestionarioPergunta = cursor.getInt(2);
                l.IdQuestionarioPerguntaResposta = cursor.getInt(3);
                l.Texto = cursor.getString(4);
                l.DataCadastro.setTime(cursor.getLong(5));
                l.IdUsuario = cursor.getInt(6);
                localArrayList.add(l);
            } while (cursor.moveToNext());
        }

        Log.i(DEBUG_TAG, "Buscar OK");
        return localArrayList;
    }

    public List<QuestionarioRespostaModel> Buscar(QuestionarioRespostaModel paramQuestionarioRespostaModel)
            throws ParseException {

        String where = " 1 = 1 ";
        if (paramQuestionarioRespostaModel.IdQuestionarioPergunta > 0) {
            StringBuilder w = new StringBuilder();
            w.append(" 1 = 1 ");
            w.append(" and Idquestionariopergunta = ");
            w.append(paramQuestionarioRespostaModel.IdQuestionarioPergunta);
            where = w.toString();
        }
        String where2 = where;
        if (paramQuestionarioRespostaModel.IdQuestionario > 0) {
            StringBuilder w = new StringBuilder();
            w.append(where);
            w.append(" and idquestionario = ");
            w.append(paramQuestionarioRespostaModel.IdQuestionario);
            where2 = w.toString();
        }
        List<QuestionarioRespostaModel> listaQuestionarioRespostaModel = new ArrayList();
        Abrir();
        Cursor cursor = this.bd.query("questionarioresposta", DB.colunaQuestionarioResposta, where2, null, null, null, null);
        Fechar();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                QuestionarioRespostaModel l = new QuestionarioRespostaModel();
                l.IdQuestionarioResposta = cursor.getInt(0);
                l.IdQuestionario = cursor.getInt(1);
                l.IdQuestionarioPergunta = cursor.getInt(2);
                l.IdQuestionarioPerguntaResposta = cursor.getInt(3);
                l.Texto = cursor.getString(4);
                l.DataCadastro.setTime(cursor.getLong(5));
                l.IdUsuario = cursor.getInt(6);
                listaQuestionarioRespostaModel.add(l);
            } while (cursor.moveToNext());
        }

        Log.i(DEBUG_TAG, "Buscar OK");
        return listaQuestionarioRespostaModel;
    }

    public void Deletar(int idQuestionario) {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("idquestionario = ");
        localStringBuilder.append(idQuestionario);
        Abrir();
        bd.delete("questionarioresposta", localStringBuilder.toString(), null);
        Fechar();
        Log.i(DEBUG_TAG, "Deletar OK");
    }

    public void Inserir(QuestionarioRespostaModel paramQuestionarioRespostaModel) {
        ContentValues value = new ContentValues();
        value.put(DB.colunaQuestionarioResposta[1], Integer.valueOf(paramQuestionarioRespostaModel.IdQuestionario));
        value.put(DB.colunaQuestionarioResposta[2], Integer.valueOf(paramQuestionarioRespostaModel.IdQuestionarioPergunta));
        value.put(DB.colunaQuestionarioResposta[3], Integer.valueOf(paramQuestionarioRespostaModel.IdQuestionarioPerguntaResposta));
        value.put(DB.colunaQuestionarioResposta[4], paramQuestionarioRespostaModel.Texto);
        value.put(DB.colunaQuestionarioResposta[5], Long.valueOf(paramQuestionarioRespostaModel.DataCadastro.getTime()));
        value.put(DB.colunaQuestionarioResposta[6], Integer.valueOf(paramQuestionarioRespostaModel.IdUsuario));
        Abrir();
        this.bd.insert("questionarioresposta", null, value);
        Fechar();
        Log.i(DEBUG_TAG, "Inserir OK");
    }
}