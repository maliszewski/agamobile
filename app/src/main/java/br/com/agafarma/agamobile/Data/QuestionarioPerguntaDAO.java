package br.com.agafarma.agamobile.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import br.com.agafarma.agamobile.Model.QuestionarioPerguntaModel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuestionarioPerguntaDAO {
    private static final String DEBUG_TAG = "QuestionarioPerguntaDAO";
    private SQLiteDatabase bd;
    private Context context;

    public QuestionarioPerguntaDAO(Context paramContext) {
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

    public List<QuestionarioPerguntaModel> Buscar(QuestionarioPerguntaModel paramQuestionarioPerguntaModel)
            throws ParseException {

        String where1 = " 1 = 1 ";
        if (paramQuestionarioPerguntaModel.IdQuestionarioPergunta > 0) {
            StringBuilder where = new StringBuilder();
            where.append(" 1 = 1 ");
            where.append(" and Idquestionariopergunta = ");
            where.append(paramQuestionarioPerguntaModel.IdQuestionarioPergunta);
            where1 = where.toString();
        }
        String where2 = where1;
        if (paramQuestionarioPerguntaModel.IdQuestionario > 0) {
            StringBuilder where = new StringBuilder();
            where.append((String) where2);
            where.append(" and idquestionario = ");
            where.append(paramQuestionarioPerguntaModel.IdQuestionario);
            where2 = ((StringBuilder) where).toString();
        }
        List<QuestionarioPerguntaModel> listQuestionarioPerguntaModel = new ArrayList();
        Abrir();
        Cursor cursor = this.bd.query("questionariopergunta", DB.colunaQuestionarioPergunta, where2, null, null, null, null);
        Fechar();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                QuestionarioPerguntaModel questionarioPerguntaModel = new QuestionarioPerguntaModel();
                questionarioPerguntaModel.IdQuestionarioPergunta = cursor.getInt(0);
                questionarioPerguntaModel.IdQuestionario = cursor.getInt(1);
                questionarioPerguntaModel.Titulo = cursor.getString(2);
                questionarioPerguntaModel.Descricao = cursor.getString(3);
                questionarioPerguntaModel.Status = cursor.getInt(4);
                questionarioPerguntaModel.TipoResposta = cursor.getInt(5);
                questionarioPerguntaModel.TipoCalculo = cursor.getInt(6);
                questionarioPerguntaModel.Peso = Double.valueOf(cursor.getDouble(7));
                questionarioPerguntaModel.Ordem = cursor.getInt(8);
                questionarioPerguntaModel.DataCadastro.setTime(cursor.getLong(9));
                questionarioPerguntaModel.IdUsuario = cursor.getInt(10);
                questionarioPerguntaModel.TipoPerguntaRespostaFoto = cursor.getInt(11);

                listQuestionarioPerguntaModel.add(questionarioPerguntaModel);
            } while (cursor.moveToNext());
        }
        Log.i("QuestionarioPerguntaDAO", "Buscar OK");
        return listQuestionarioPerguntaModel;
    }

    public void Inserir(QuestionarioPerguntaModel paramQuestionarioPerguntaModel) {

        ContentValues value = new ContentValues();
        value.put(DB.colunaQuestionarioPergunta[0], Integer.valueOf(paramQuestionarioPerguntaModel.IdQuestionarioPergunta));
        value.put(DB.colunaQuestionarioPergunta[1], Integer.valueOf(paramQuestionarioPerguntaModel.IdQuestionario));
        value.put(DB.colunaQuestionarioPergunta[2], paramQuestionarioPerguntaModel.Titulo);
        value.put(DB.colunaQuestionarioPergunta[3], paramQuestionarioPerguntaModel.Descricao);
        value.put(DB.colunaQuestionarioPergunta[4], Integer.valueOf(paramQuestionarioPerguntaModel.Status));
        value.put(DB.colunaQuestionarioPergunta[5], Integer.valueOf(paramQuestionarioPerguntaModel.TipoResposta));
        value.put(DB.colunaQuestionarioPergunta[6], Integer.valueOf(paramQuestionarioPerguntaModel.TipoCalculo));
        value.put(DB.colunaQuestionarioPergunta[7], paramQuestionarioPerguntaModel.Peso);
        value.put(DB.colunaQuestionarioPergunta[8], Integer.valueOf(paramQuestionarioPerguntaModel.Ordem));
        value.put(DB.colunaQuestionarioPergunta[9], Long.valueOf(paramQuestionarioPerguntaModel.DataCadastro.getTime()));
        value.put(DB.colunaQuestionarioPergunta[10], Integer.valueOf(paramQuestionarioPerguntaModel.IdUsuario));
        value.put(DB.colunaQuestionarioPergunta[11], Integer.valueOf(paramQuestionarioPerguntaModel.TipoPerguntaRespostaFoto));
        Abrir();
        this.bd.insert("questionariopergunta", null, value);
        Fechar();
        Log.i("QuestionarioPerguntaDAO", "Inserir OK");
    }
}