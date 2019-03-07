package br.com.agafarma.agamobile.Data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import br.com.agafarma.agamobile.Model.QuestionarioModel;
import br.com.agafarma.agamobile.Model.QuestionarioPerguntaModel;
import br.com.agafarma.agamobile.Model.QuestionarioRespostaFotoModel;
import br.com.agafarma.agamobile.Model.QuestionarioRespostaModel;
import br.com.agafarma.agamobile.Util.Image;

public class QuestionarioRespostaFotoDAO {
    private static final String DEBUG_TAG = "FotoDAO";
    private SQLiteDatabase bd;
    private Context context;

    public QuestionarioRespostaFotoDAO(Context paramContext) {
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


    public List<QuestionarioRespostaFotoModel> Buscar(QuestionarioModel questionarioModel) throws ParseException {

        QuestionarioRespostaFotoModel questionarioRespostaFotoModel = new QuestionarioRespostaFotoModel();
        questionarioRespostaFotoModel.IdQuestionario = questionarioModel.IdQuestionario;


        List<QuestionarioRespostaFotoModel> result = Buscar(questionarioRespostaFotoModel);
        return result;
    }

    public List<QuestionarioRespostaFotoModel> Buscar(QuestionarioPerguntaModel questionarioPerguntaModel) throws ParseException {

        QuestionarioRespostaFotoModel questionarioRespostaFotoModel = new QuestionarioRespostaFotoModel();
        questionarioRespostaFotoModel.IdQuestionario = questionarioPerguntaModel.IdQuestionario;
        questionarioRespostaFotoModel.IdQuestionarioPergunta = questionarioPerguntaModel.IdQuestionarioPergunta;


        List<QuestionarioRespostaFotoModel> result = Buscar(questionarioRespostaFotoModel);
        return result;
    }

    public List<QuestionarioRespostaFotoModel> Buscar(QuestionarioRespostaFotoModel questionarioRespostaFotoModel)
            throws ParseException {

        StringBuilder sql = new StringBuilder();
        sql.append(" select " +
                "q.idquestionariorespostafoto, " +
                "q.idquestionario, " +
                "q.idquestionariopergunta, " +
                "q.imagem, " +
                "q.data_cadastro, " +
                "q.idusuario," +
                "q.uriimagem " +
                " from questionariorespostafoto q " +
                " where ");


        String where = " 1 = 1 ";
        if (questionarioRespostaFotoModel.IdQuestionarioPergunta > 0)
            where += " and q.idquestionariopergunta = " + questionarioRespostaFotoModel.IdQuestionarioPergunta;

        if (questionarioRespostaFotoModel.IdQuestionario > 0)
            where += " and q.idquestionario = " + questionarioRespostaFotoModel.IdQuestionario;

        sql.append(where);

        Log.i(DEBUG_TAG, sql.toString());
        List<QuestionarioRespostaFotoModel> listaQuestionarioRespostaFotoModel = new ArrayList();
        Abrir();
        //Cursor cursor = this.bd.query("questionariorespostafoto", DB.colunaQuestionarioRespostaFoto, where, null, null, null, null);
        Cursor cursor = this.bd.rawQuery(sql.toString(), null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                QuestionarioRespostaFotoModel l = new QuestionarioRespostaFotoModel();
                l.IdQuestionarioRespostaFoto = cursor.getInt(0);
                l.IdQuestionario = cursor.getInt(1);
                l.IdQuestionarioPergunta = cursor.getInt(2);
                l.Imagem = cursor.getBlob(3) != null ? BitmapFactory.decodeByteArray(cursor.getBlob(3), 0, cursor.getBlob(3).length) : null;
                l.DataCadastro.setTime(cursor.getLong(4));
                l.IdUsuario = cursor.getInt(5);
                l.UriImagem = cursor.getString(6);
                listaQuestionarioRespostaFotoModel.add(l);
            } while (cursor.moveToNext());
        }
        Fechar();
        Log.i(DEBUG_TAG, "Buscar OK");
        return listaQuestionarioRespostaFotoModel;
    }

    public void Deletar(QuestionarioModel questionarioModel) {
        QuestionarioRespostaFotoModel questionarioRespostaFotoModel = new QuestionarioRespostaFotoModel();
        questionarioRespostaFotoModel.IdQuestionario = questionarioModel.IdQuestionario;
        Deletar(questionarioRespostaFotoModel);
    }

    public void Deletar(QuestionarioRespostaFotoModel questionarioRespostaFotoModel) {
        String where = " 1=1 ";
        if (questionarioRespostaFotoModel.IdQuestionarioRespostaFoto > 0)
            where += " and idquestionariorespostafoto = " + questionarioRespostaFotoModel.IdQuestionarioRespostaFoto;
        if (questionarioRespostaFotoModel.IdQuestionarioPergunta > 0)
            where += " and idquestionariopergunta = " + questionarioRespostaFotoModel.IdQuestionarioPergunta;
        if (questionarioRespostaFotoModel.IdQuestionario > 0)
            where += " and idquestionario = " + questionarioRespostaFotoModel.IdQuestionario;
        Abrir();
        bd.delete("questionariorespostafoto", where, null);
        Fechar();

        Log.i(DEBUG_TAG, "Deletar OK");
    }

    public void Inserir(QuestionarioRespostaFotoModel questionarioRespostaFotoModel) {
        ContentValues value = new ContentValues();
        value.put(DB.colunaQuestionarioRespostaFoto[1], Integer.valueOf(questionarioRespostaFotoModel.IdQuestionario));
        value.put(DB.colunaQuestionarioRespostaFoto[2], Integer.valueOf(questionarioRespostaFotoModel.IdQuestionarioPergunta));
        value.put(DB.colunaQuestionarioRespostaFoto[3], Image.GetBitmapAsByteArray(questionarioRespostaFotoModel.Imagem));
        value.put(DB.colunaQuestionarioRespostaFoto[4], Long.valueOf(questionarioRespostaFotoModel.DataCadastro.getTime()));
        value.put(DB.colunaQuestionarioRespostaFoto[5], Integer.valueOf(questionarioRespostaFotoModel.IdUsuario));
        value.put(DB.colunaQuestionarioRespostaFoto[6], questionarioRespostaFotoModel.UriImagem);

        Abrir();
        this.bd.insert("questionariorespostafoto", null, value);
        Fechar();
        Log.i(DEBUG_TAG, "Inserir OK");
    }
}