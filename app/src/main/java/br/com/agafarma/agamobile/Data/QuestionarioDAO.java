package br.com.agafarma.agamobile.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import br.com.agafarma.agamobile.Activity.ListaQuestionarioActivity;
import br.com.agafarma.agamobile.Model.MenuModel;
import br.com.agafarma.agamobile.Model.QuestionarioModel;
import br.com.agafarma.agamobile.Model.QuestionarioPerguntaModel;
import br.com.agafarma.agamobile.Model.QuestionarioPerguntaRespostaModel;
import br.com.agafarma.agamobile.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuestionarioDAO {
    private static final String DEBUG_TAG = "QuestionarioDAO";
    private SQLiteDatabase bd;
    private Context context;

    public QuestionarioDAO() {
    }

    public QuestionarioDAO(Context paramContext) {
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

    public void Atualizar(QuestionarioModel paramQuestionarioModel) {

        ContentValues values = new ContentValues();
        values.put(DB.colunaQuestionario[0], Integer.valueOf(paramQuestionarioModel.IdQuestionario));
        values.put(DB.colunaQuestionario[1], Integer.valueOf(paramQuestionarioModel.IdFormulario));
        values.put(DB.colunaQuestionario[2], paramQuestionarioModel.Titulo);
        values.put(DB.colunaQuestionario[3], paramQuestionarioModel.Descricao);
        values.put(DB.colunaQuestionario[4], Integer.valueOf(paramQuestionarioModel.Status));
        values.put(DB.colunaQuestionario[5], Integer.valueOf(paramQuestionarioModel.IdLoja));
        values.put(DB.colunaQuestionario[6], Integer.valueOf(paramQuestionarioModel.Responsavel));
        values.put(DB.colunaQuestionario[7], Long.valueOf(paramQuestionarioModel.DataInicio.getTime()));
        values.put(DB.colunaQuestionario[8], Long.valueOf(paramQuestionarioModel.DataTermino.getTime()));
        values.put(DB.colunaQuestionario[9], Long.valueOf(paramQuestionarioModel.DataInicio.getTime()));
        values.put(DB.colunaQuestionario[10], Integer.valueOf(paramQuestionarioModel.IdUsuario));
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("");
        localStringBuilder.append(paramQuestionarioModel.IdQuestionario);

        Abrir();
        bd.update("questionario", values, "idquestionario = ?", new String[]{localStringBuilder.toString()});
        Fechar();

        Log.i(DEBUG_TAG, "Atualizar OK");
    }

    public List<MenuModel> BuscaTipoFormularioEnviado() {
        List<MenuModel> result = new ArrayList();

        StringBuilder sql = new StringBuilder();
        sql.append(" select  q.idformulariotipo,  " +
                "q.formulariotipotitulo,  " +
                "count(1) quantidade  " +
                " from questionario q  " +
                " where status = 2 " +
                " group by q.idformulariotipo, q.formulariotipotitulo ");
        Abrir();
        Cursor cursor = this.bd.rawQuery(sql.toString(), null);
        Fechar();

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                MenuModel m = new MenuModel();
                m.IdMenu = cursor.getInt(0);
                m.Titulo = cursor.getString(1);
                m.HintQt = cursor.getInt(2);
                m.Cls = ListaQuestionarioActivity.class;
                m.IdImage = R.drawable.ic_menu_questionario;
                m.Transporte = "idFormularioTipo";
                result.add(m);
            } while (cursor.moveToNext());
        }


        Log.i("QuestionarioRespostaDAO", "Buscar OK");
        return result;
    }


    public List<QuestionarioModel> Buscar(QuestionarioModel paramQuestionarioModel)
            throws ParseException {

        ArrayList localArrayList = new ArrayList();
        String where = " 1 = 1 ";
        if (paramQuestionarioModel.IdQuestionario > 0) {
            where += " and idquestionario = " + paramQuestionarioModel.IdQuestionario;
        }
        if (paramQuestionarioModel.Status > 0) {
            where += " and status = " + paramQuestionarioModel.Status;
        }
        if (paramQuestionarioModel.IdFormularioTipo > 0) {
            where += " and idformulariotipo = " + paramQuestionarioModel.IdFormularioTipo;
        }
        Abrir();
        Cursor cursor = bd.query("questionario", DB.colunaQuestionario, where, null, null, null, null);
        Fechar();

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                QuestionarioModel l = new QuestionarioModel();
                l.IdQuestionario = cursor.getInt(0);
                l.IdFormulario = cursor.getInt(1);
                l.Titulo = cursor.getString(2);
                l.Descricao = cursor.getString(3);
                l.Status = cursor.getInt(4);
                l.IdLoja = cursor.getInt(5);
                l.Responsavel = cursor.getInt(6);
                l.DataInicio.setTime(cursor.getLong(7));
                l.DataTermino.setTime(cursor.getLong(8));
                l.DataCadastro.setTime(cursor.getLong(9));
                l.IdUsuario = cursor.getInt(10);
                l.IdFormularioTipo = cursor.getInt(11);
                l.FormularioTipoTitulo = cursor.getString(12);
                localArrayList.add(l);
            } while (cursor.moveToNext());
        }

        Log.i(DEBUG_TAG, "Buscar OK");
        return localArrayList;
    }

    public QuestionarioModel Carregar(QuestionarioModel paramQuestionarioModel)
            throws ParseException {
        QuestionarioPerguntaModel localObject = new QuestionarioPerguntaModel();
        paramQuestionarioModel = (QuestionarioModel) Buscar(paramQuestionarioModel).get(0);
        localObject.IdQuestionario = paramQuestionarioModel.IdQuestionario;

        paramQuestionarioModel.ListaQuestionarioPergunta = new QuestionarioPerguntaDAO(context).Buscar((QuestionarioPerguntaModel) localObject);

        for (QuestionarioPerguntaModel questionarioPerguntaModel : paramQuestionarioModel.ListaQuestionarioPergunta) {
            QuestionarioPerguntaRespostaModel localQuestionarioPerguntaRespostaModel = new QuestionarioPerguntaRespostaModel();
            localQuestionarioPerguntaRespostaModel.IdQuestionarioPergunta = questionarioPerguntaModel.IdQuestionarioPergunta;
            questionarioPerguntaModel.ListaQuestionarioPerguntaResposta = new QuestionarioPerguntaRespostaDAO(context).Buscar(localQuestionarioPerguntaRespostaModel);
        }

        Log.i(DEBUG_TAG, "Carregar OK");
        return paramQuestionarioModel;
    }

    public void Deletar(Context paramContext, QuestionarioModel paramQuestionarioModel) throws ParseException {
        context = paramContext;
        String where = "idquestionario = " + paramQuestionarioModel.IdQuestionario;
        QuestionarioPerguntaModel qP = new QuestionarioPerguntaModel();
        qP.IdQuestionario = paramQuestionarioModel.IdQuestionario;
        List<QuestionarioPerguntaModel> listaQuestionarioPergunta = new QuestionarioPerguntaDAO(context).Buscar(qP);
        Abrir();
        bd.delete("questionarioresposta", where, null);
        Abrir();
        bd.delete("questionariorespostafoto", where, null);
        Abrir();
        for (QuestionarioPerguntaModel questionarioPerguntaModel : listaQuestionarioPergunta) {
            String whereQPR = "idquestionariopergunta = " + questionarioPerguntaModel.IdQuestionarioPergunta;
            Abrir();
            bd.delete("questionarioperguntaresposta", whereQPR.toString(), null);
        }
        Abrir();
        bd.delete("questionariopergunta", where.toString(), null);
        Abrir();
        bd.delete("questionario", where, null);
        Fechar();
        Log.i(DEBUG_TAG, "Deletar OK");
    }

    public void Deletar(QuestionarioModel paramQuestionarioModel) throws ParseException {
        String where = "idquestionario = " + paramQuestionarioModel.IdQuestionario;
        QuestionarioPerguntaModel qP = new QuestionarioPerguntaModel();
        qP.IdQuestionario = paramQuestionarioModel.IdQuestionario;
        List<QuestionarioPerguntaModel> listaQuestionarioPergunta = new QuestionarioPerguntaDAO(context).Buscar(qP);
        Abrir();
        bd.delete("questionarioresposta", where, null);
        Abrir();
        bd.delete("questionariorespostafoto", where, null);

        for (QuestionarioPerguntaModel questionarioPerguntaModel : listaQuestionarioPergunta) {
            String whereQPR = "idquestionariopergunta = " + questionarioPerguntaModel.IdQuestionarioPergunta;
            Abrir();
            bd.delete("questionarioperguntaresposta", whereQPR.toString(), null);
        }
        Abrir();
        bd.delete("questionariopergunta", where.toString(), null);
        Abrir();
        bd.delete("questionario", where, null);
        Log.i(DEBUG_TAG, "Deletar OK");
    }

    public void Deletar(int idQuestionario) throws ParseException {
        String where = "idquestionario = " + idQuestionario;
        QuestionarioPerguntaModel qP = new QuestionarioPerguntaModel();
        qP.IdQuestionario = idQuestionario;
        List<QuestionarioPerguntaModel> listaQuestionarioPergunta = new QuestionarioPerguntaDAO(context).Buscar(qP);
        Abrir();
        bd.delete("questionarioresposta", where, null);
        Abrir();
        bd.delete("questionariorespostafoto", where, null);
        for (QuestionarioPerguntaModel questionarioPerguntaModel : listaQuestionarioPergunta) {
            String whereQPR = "idquestionariopergunta = " + questionarioPerguntaModel.IdQuestionarioPergunta;
            Abrir();
            bd.delete("questionarioperguntaresposta", whereQPR.toString(), null);
        }
        Abrir();
        bd.delete("questionariopergunta", where.toString(), null);
        Abrir();
        bd.delete("questionario", where, null);
        Fechar();
        Log.i(DEBUG_TAG, "Deletar OK");
    }


    public void Inserir(QuestionarioModel paramQuestionarioModel) {

        ContentValues values = new ContentValues();
        values.put(DB.colunaQuestionario[0], Integer.valueOf(paramQuestionarioModel.IdQuestionario));
        values.put(DB.colunaQuestionario[1], Integer.valueOf(paramQuestionarioModel.IdFormulario));
        values.put(DB.colunaQuestionario[2], paramQuestionarioModel.Titulo);
        values.put(DB.colunaQuestionario[3], paramQuestionarioModel.Descricao);
        values.put(DB.colunaQuestionario[4], Integer.valueOf(paramQuestionarioModel.Status));
        values.put(DB.colunaQuestionario[5], Integer.valueOf(paramQuestionarioModel.IdLoja));
        values.put(DB.colunaQuestionario[6], Integer.valueOf(paramQuestionarioModel.Responsavel));
        values.put(DB.colunaQuestionario[7], Long.valueOf(paramQuestionarioModel.DataInicio.getTime()));
        values.put(DB.colunaQuestionario[8], Long.valueOf(paramQuestionarioModel.DataTermino.getTime()));
        values.put(DB.colunaQuestionario[9], Long.valueOf(paramQuestionarioModel.DataInicio.getTime()));
        values.put(DB.colunaQuestionario[10], Integer.valueOf(paramQuestionarioModel.IdUsuario));
        values.put(DB.colunaQuestionario[11], Integer.valueOf(paramQuestionarioModel.IdFormularioTipo));
        values.put(DB.colunaQuestionario[12], paramQuestionarioModel.FormularioTipoTitulo);

        Abrir();
        bd.insert("questionario", null, values);
        Fechar();
        Log.i(DEBUG_TAG, "Inserir OK");
    }

    public void Sincroniza(Context paramContext, List<QuestionarioModel> paramListQuestionarioModel)
            throws ParseException {
        context = paramContext;

        for (QuestionarioModel questionarioModel : paramListQuestionarioModel) {
            Abrir();
            boolean qExistente = Buscar(questionarioModel).size() > 0;
            Fechar();
            Abrir();

            if (qExistente) {
                Deletar(questionarioModel);
            }
            Inserir(questionarioModel);
            Fechar();

            for (QuestionarioPerguntaModel questionarioPerguntaModel : questionarioModel.ListaQuestionarioPergunta) {
                new QuestionarioPerguntaDAO(paramContext).Inserir(questionarioPerguntaModel);
                for (QuestionarioPerguntaRespostaModel questionarioPerguntaRespostaModel : questionarioPerguntaModel.ListaQuestionarioPerguntaResposta) {
                    new QuestionarioPerguntaRespostaDAO(paramContext).Inserir(questionarioPerguntaRespostaModel);
                }
            }

        }

        Log.i(DEBUG_TAG, "Sincroniza OK");
    }

    public void LimpaQuestionariosVencidos() throws ParseException {
        List<QuestionarioModel> listaQuestionario = Buscar(new QuestionarioModel());
        Date data = new Date();
        for (QuestionarioModel q : listaQuestionario) {
            if (data.getDate() > q.DataTermino.getDate()) {
                Abrir();
                Deletar(q);
            }
        }
    }
}
