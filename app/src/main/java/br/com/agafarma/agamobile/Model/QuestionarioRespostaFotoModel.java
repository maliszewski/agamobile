package br.com.agafarma.agamobile.Model;

import android.graphics.Bitmap;

import java.sql.Blob;
import java.util.Date;

import br.com.agafarma.agamobile.Util.Image;

public class QuestionarioRespostaFotoModel {
    public Date DataCadastro;
    public int IdQuestionario;
    public int IdQuestionarioPergunta;
    public int IdQuestionarioRespostaFoto;
    public Bitmap Imagem;
    public String UriImagem;
    public int IdUsuario;


    public QuestionarioRespostaFotoModel()
    {
        this.DataCadastro = new Date();
    }
}
