package br.com.agafarma.agamobile.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import br.com.agafarma.agamobile.Model.UsuarioModel;

public class LoginDAO {
    private static final String DEBUG_TAG = "LoginDAO";
    private SQLiteDatabase bd;
    private Context context;

    public LoginDAO(Context paramContext) {
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


    public UsuarioModel Buscar() {
        UsuarioModel localObject = new UsuarioModel();
        Abrir();
        Cursor localCursor = this.bd.query("login", DB.colunaLogin, null, null, null, null, null);
        Fechar();
        if (localCursor.getCount() > 0) {
            localCursor.moveToFirst();
            do {
                ((UsuarioModel) localObject).UsuarioId = localCursor.getInt(0);
                ((UsuarioModel) localObject).Apelido = localCursor.getString(1);
                ((UsuarioModel) localObject).CpfCnpj = localCursor.getString(2);
                ((UsuarioModel) localObject).Email = localCursor.getString(3);
                ((UsuarioModel) localObject).Senha = localCursor.getString(4);
                ((UsuarioModel) localObject).LojaId = localCursor.getInt(5);
                ((UsuarioModel) localObject).Administrativo = localCursor.getString(6);

            } while (localCursor.moveToNext());
        }
        Log.i("LoginDAO", "Buscar OK");
        return (UsuarioModel) localObject;
    }

    public void Deletar(UsuarioModel paramUsuarioModel) {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("idusuario = ");
        localStringBuilder.append(paramUsuarioModel.UsuarioId);
        Abrir();
        bd.delete("login", localStringBuilder.toString(), null);
        Fechar();
        Log.i("LoginDAO", "Deletar OK");
    }

    public void LimparApp() {
        Abrir();
        bd.delete("login", null, null);
        Abrir();
        bd.delete("questionarioperguntaresposta", null, null);
        Abrir();
        bd.delete("questionariopergunta", null, null);
        Abrir();
        bd.delete("questionario", null, null);
        Abrir();
        bd.delete("questionarioresposta", null, null);
        Abrir();
        bd.delete("questionariorespostafoto", null, null);
        Abrir();
        bd.delete("aviso", null, null);
        Abrir();
        bd.delete("mqttmensagem", null, null);
        Fechar();
        Log.i("LoginDAO", "Sair OK");
    }

    public void LogOff() {
        Abrir();
        bd.delete("login", null, null);
        Fechar();
        Log.i("LoginDAO", "Sair OK");
    }


    public void Inserir(UsuarioModel paramUsuarioModel) {
        ContentValues localObject = new ContentValues();
        localObject.put(DB.colunaLogin[0], Integer.valueOf(paramUsuarioModel.UsuarioId));
        localObject.put(DB.colunaLogin[1], paramUsuarioModel.Apelido);
        localObject.put(DB.colunaLogin[2], paramUsuarioModel.CpfCnpj);
        localObject.put(DB.colunaLogin[3], paramUsuarioModel.Email);
        localObject.put(DB.colunaLogin[4], paramUsuarioModel.Senha);
        localObject.put(DB.colunaLogin[5], paramUsuarioModel.LojaId);
        localObject.put(DB.colunaLogin[6], paramUsuarioModel.Administrativo);
        Abrir();
        this.bd.insert("login", null, (ContentValues) localObject);
        Fechar();
        Log.i("LoginDAO", "Inserir OK");
    }
}