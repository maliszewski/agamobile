package br.com.agafarma.agamobile.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import br.com.agafarma.agamobile.Model.QuestionarioPerguntaRespostaModel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuestionarioPerguntaRespostaDAO {
    private static final String DEBUG_TAG = "QuestionarioPRDAO";
    private SQLiteDatabase bd;
    private Context context;

    public QuestionarioPerguntaRespostaDAO(Context paramContext) {
        context = paramContext;
        Abrir();
    }
    private void Abrir() {

        if (bd == null || !bd.isOpen()) {
            bd = DB.getInstance(context).getWritableDatabase();
            bd.enableWriteAheadLogging();
            Log.i(DEBUG_TAG, "Abriu OK");
        }
    }

    private void Fechar() {
        if (bd == null && bd.isOpen()) {
            bd.close();
        }
    }

    public List<QuestionarioPerguntaRespostaModel> Buscar(QuestionarioPerguntaRespostaModel paramQuestionarioPerguntaRespostaModel)
            throws ParseException {
        String where = " 1 = 1 ";
        if (paramQuestionarioPerguntaRespostaModel.IdQuestionarioPerguntaResposta > 0)
            where += " and IdQuestionarioPerguntaResposta = " + paramQuestionarioPerguntaRespostaModel.IdQuestionarioPerguntaResposta;

        if (paramQuestionarioPerguntaRespostaModel.IdQuestionarioPergunta > 0)
            where += " and IdQuestionarioPergunta = " + paramQuestionarioPerguntaRespostaModel.IdQuestionarioPergunta;

        List<QuestionarioPerguntaRespostaModel> questionarioPerguntaRespostaModel = new ArrayList();
        Abrir();
        Cursor cursor = this.bd.query("questionarioperguntaresposta", DB.colunaQuestionarioPerguntaResposta, where, null, null, null, null);
        Fechar();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                QuestionarioPerguntaRespostaModel l = new QuestionarioPerguntaRespostaModel();
                l.IdQuestionarioPerguntaResposta = cursor.getInt(0);
                l.IdQuestionarioPergunta = cursor.getInt(1);
                l.Titulo = cursor.getString(2);
                l.Descricao = cursor.getString(3);
                l.Status = cursor.getInt(4);
                l.Nota = Double.valueOf(cursor.getDouble(5));
                l.Ordem = cursor.getInt(6);
                l.DataCadastro.setTime(cursor.getLong(7));
                l.IdUsuario = cursor.getInt(8);
                questionarioPerguntaRespostaModel.add(l);
            } while (cursor.moveToNext());
        }
        Log.i(DEBUG_TAG, "Buscar OK");
        return questionarioPerguntaRespostaModel;
    }

    public void Inserir(QuestionarioPerguntaRespostaModel paramQuestionarioPerguntaRespostaModel) {
        ContentValues v = new ContentValues();
        v.put(DB.colunaQuestionarioPerguntaResposta[0], Integer.valueOf(paramQuestionarioPerguntaRespostaModel.IdQuestionarioPerguntaResposta));
        v.put(DB.colunaQuestionarioPerguntaResposta[1], Integer.valueOf(paramQuestionarioPerguntaRespostaModel.IdQuestionarioPergunta));
        v.put(DB.colunaQuestionarioPerguntaResposta[2], paramQuestionarioPerguntaRespostaModel.Titulo);
        v.put(DB.colunaQuestionarioPerguntaResposta[3], paramQuestionarioPerguntaRespostaModel.Descricao);
        v.put(DB.colunaQuestionarioPerguntaResposta[4], Integer.valueOf(paramQuestionarioPerguntaRespostaModel.Status));
        v.put(DB.colunaQuestionarioPerguntaResposta[5], paramQuestionarioPerguntaRespostaModel.Nota);
        v.put(DB.colunaQuestionarioPerguntaResposta[6], Integer.valueOf(paramQuestionarioPerguntaRespostaModel.Ordem));
        v.put(DB.colunaQuestionarioPerguntaResposta[7], Long.valueOf(paramQuestionarioPerguntaRespostaModel.DataCadastro.getTime()));
        v.put(DB.colunaQuestionarioPerguntaResposta[8], Integer.valueOf(paramQuestionarioPerguntaRespostaModel.IdUsuario));
        Abrir();
        this.bd.insert("questionarioperguntaresposta", null, v);
        Fechar();
        Log.i(DEBUG_TAG, "Inserir OK");
    }
}
