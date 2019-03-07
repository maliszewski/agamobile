package br.com.agafarma.agamobile.Model;

import java.util.Date;

public class QuestionarioPerguntaRespostaModel {
    public Date DataCadastro;
    public String Descricao;
    public int IdQuestionarioPergunta;
    public int IdQuestionarioPerguntaResposta;
    public int IdUsuario;
    public Double Nota;
    public int Ordem;
    public int Status;
    public String Titulo;

    public QuestionarioPerguntaRespostaModel()
    {
        this.DataCadastro = new Date();
    }
}
