package br.com.agafarma.agamobile.Model;

import java.util.Date;

public class QuestionarioRespostaModel {
    public Date DataCadastro;
    public int IdQuestionario;
    public int IdQuestionarioPergunta;
    public int IdQuestionarioPerguntaResposta;
    public int IdQuestionarioResposta;
    public int IdUsuario;
    public String Texto;

    public QuestionarioRespostaModel()
    {
        this.DataCadastro = new Date();
    }
}
