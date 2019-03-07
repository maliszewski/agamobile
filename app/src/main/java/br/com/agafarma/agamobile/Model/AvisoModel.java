package br.com.agafarma.agamobile.Model;

import java.util.ArrayList;
import java.util.Date;

import br.com.agafarma.agamobile.Util.DataHora;

public class AvisoModel {
    public int IdAviso;
    public String Titulo;
    public int Tipo;
    public String Descricao;
    public Date DataCadastro;
    public int IdUsuario;
    public Date DataNotificacao;

    public AvisoModel()
    {
        this.DataNotificacao = new Date();
        this.DataCadastro = new Date();
    }

}
