package br.com.agafarma.agamobile.Data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {

    private static final String NOME_BD = "AgaMobile";
    private static final int VERSAO_BD = 30;
    public static final String[] colunaLogin = {"idusuario", "apelido", "cpfcnpj", "email", "senha", "lojaid", "administrativo"};
    public static final String[] colunaQuestionario = {"idquestionario", "idformulario", "titulo", "descricao", "status", "loja_id", "responsavel", "data_inicio", "data_termino", "data_cadastro", "idusuario", "idformulariotipo", "formulariotipotitulo"};
    public static final String[] colunaQuestionarioPergunta = {"idquestionariopergunta", "idquestionario", "titulo", "descricao", "status", "tiporesposta", "tipocalculo", "peso", "ordem", "data_cadastro", "idusuario", "tipoperguntarespostafoto"};
    public static final String[] colunaQuestionarioPerguntaResposta = {"idquestionariopergresp", "idquestionariopergunta", "titulo", "descricao", "status", "nota", "ordem", "data_cadastro", "idusuario"};
    public static final String[] colunaQuestionarioResposta = {"idquestionarioresposta", "idquestionario", "idquestionariopergunta", "idquestionariopergresp", "texto", "data_cadastro", "idusuario"};
    public static final String[] colunaAviso = {"idaviso", "titulo", "descricao", "tipo", "data_cadastro", "idusuario", "data_notificacao"};
    public static final String[] colunaQuestionarioRespostaFoto = {"idquestionariorespostafoto", "idquestionario", "idquestionariopergunta", "imagem", "data_cadastro", "idusuario", "uriimagem"};
    public static final String[] colunaMqttMensagem = {"idmqttMensagem", "idreferencia", "tipo", "link", "titulo", "mensagem", "idUsuario", "nomeusuario", "status", "data_recebido", "data_leitura", "data_resposta", "topico", "idgrupomqtt", "nomegrupomqtt"};
    public static final String[] colunaContato = {"idcontato", "topico", "nome", "tipo", "idusuario", "idgrupomqtt"};

    private static DB sInstance;

    public static synchronized DB getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DB(context.getApplicationContext());
        }

        return sInstance;
    }

    private DB(Context paramContext) {
        super(paramContext, NOME_BD, null, VERSAO_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" create table login( idusuario numeric, " +
                "apelido   text,    " +
                "cpfcnpj   text,    " +
                "email     text,    " +
                "senha     text,    " +
                "lojaid    numeric, " +
                "administrativo text);");
        db.execSQL(" create table questionarioperguntaresposta( idquestionariopergresp    numeric, " +
                "idquestionariopergunta    numeric, " +
                "titulo                    text   , " +
                "descricao                 text   , " +
                "status                    numeric, " +
                "nota                      numeric, " +
                "ordem                     numeric, " +
                "data_cadastro             REAL   , " +
                "idusuario                 numeric );");
        db.execSQL(" create table questionariopergunta(  idquestionariopergunta   numeric,  " +
                "idquestionario           numeric,  " +
                "titulo                   text,     " +
                "descricao                text,     " +
                "status                   numeric,  " +
                "tiporesposta             numeric,  " +
                "tipocalculo              numeric,  " +
                "peso                     numeric,  " +
                "ordem                    numeric,  " +
                "data_cadastro            REAL,     " +
                "idusuario                numeric,  " +
                "tipoperguntarespostafoto numeric );");
        db.execSQL(" create table questionario(  idquestionario numeric,  " +
                "idformulario           numeric,    " +
                "titulo                 text,       " +
                "descricao              text,       " +
                "status                 numeric,    " +
                "loja_id                numeric,    " +
                "responsavel            numeric,    " +
                "data_inicio            REAL ,      " +
                "data_termino           REAL ,      " +
                "data_cadastro          REAL ,      " +
                "idusuario              numeric,    " +
                "idformulariotipo       numeric,    " +
                "formulariotipotitulo   numeric );  ");
        db.execSQL(" create table questionarioresposta(  idquestionarioresposta  integer primary key autoincrement,  " +
                "idquestionario               numeric,  " +
                "idquestionariopergunta       numeric,  " +
                "idquestionariopergresp       numeric,  " +
                "texto                        text,     " +
                "data_cadastro                REAL,     " +
                "idusuario                    numeric); ");

        db.execSQL(" create table aviso(  idaviso  numeric,  " +
                "titulo            text   ,  " +
                "tipo              numeric,  " +
                "descricao         text   ,  " +
                "data_cadastro     REAL   ,  " +
                "idusuario         numeric,  " +
                "data_notificacao  REAL );   ");


        db.execSQL(" create table questionariorespostafoto( idquestionariorespostafoto   integer primary key autoincrement,  " +
                "idquestionario               numeric,  " +
                "idquestionariopergunta       numeric,  " +
                "imagem                       blob,     " +
                "data_cadastro                REAL,     " +
                "idusuario                    numeric,  " +
                "uriimagem                    text);    ");

        db.execSQL(" create table mqttmensagem( idmqttMensagem   integer primary key autoincrement,  " +
                "idreferencia     numeric,  " +
                "tipo             numeric,  " +
                "link             text,     " +
                "titulo           text,     " +
                "mensagem         text,     " +
                "idUsuario        numeric,  " +
                "nomeusuario      text,     " +
                "status           numeric,  " +
                "data_recebido    REAL,     " +
                "data_leitura     REAL,     " +
                "data_resposta    REAL,     " +
                "topico           text,     " +
                "idgrupomqtt      numeric,  " +
                "nomegrupomqtt    text  );  ");

        db.execSQL(" create table contato( idcontato  numeric, " +
                "topico           text,     " +
                "nome             text,     " +
                "tipo             numeric,  " +
                "idusuario        numeric , " +
                "idgrupomqtt      numeric );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2) {
        paramSQLiteDatabase.execSQL("drop table if exists login;");
        paramSQLiteDatabase.execSQL("drop table if exists questionario;");
        paramSQLiteDatabase.execSQL("drop table if exists questionariopergunta;");
        paramSQLiteDatabase.execSQL("drop table if exists questionarioperguntaresposta;");
        paramSQLiteDatabase.execSQL("drop table if exists questionarioresposta;");
        paramSQLiteDatabase.execSQL("drop table if exists questionariorespostafoto;");

        paramSQLiteDatabase.execSQL("drop table if exists aviso;");
        paramSQLiteDatabase.execSQL("drop table if exists mqttmensagem;");
        paramSQLiteDatabase.execSQL("drop table if exists contato;");

        onCreate(paramSQLiteDatabase);
    }
}
