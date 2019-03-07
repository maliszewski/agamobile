package br.com.agafarma.agamobile.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuestionarioModel {
    public Date DataCadastro;
    public Date DataInicio;
    public Date DataTermino;
    public String Descricao;
    public int IdFormulario;
    public int IdLoja;
    public int IdQuestionario;
    public int IdUsuario;
    public List<QuestionarioPerguntaModel> ListaQuestionarioPergunta;
    public int Responsavel;
    public int Status;
    public String Titulo;
    public int IdFormularioTipo;
    public String FormularioTipoTitulo;

    public QuestionarioModel()
    {
        this.DataInicio = new Date();
        this.DataTermino = new Date();
        this.DataCadastro = new Date();
        this.ListaQuestionarioPergunta = new ArrayList();
    }
}
