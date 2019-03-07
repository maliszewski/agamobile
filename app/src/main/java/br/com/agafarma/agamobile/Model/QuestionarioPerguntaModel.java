package br.com.agafarma.agamobile.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuestionarioPerguntaModel {
    public int TipoPerguntaRespostaFoto;
    public Date DataCadastro = new Date();
    public String Descricao;
    public int IdQuestionario;
    public int IdQuestionarioPergunta;
    public int IdUsuario;
    public List<QuestionarioPerguntaRespostaModel> ListaQuestionarioPerguntaResposta = new ArrayList();
    public int Ordem;
    public Double Peso;
    public int Status;
    public int TipoCalculo;
    public int TipoResposta;
    public String Titulo;

    public QuestionarioPerguntaModel() {}
}
