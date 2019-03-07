package br.com.agafarma.agamobile.Model;

public class CampanhaModel {
    public int IdCampanhaExterna;
    public int IdCampanhaInterna;
    public int IdCampanhaSubInterna;
    public String Titulo;
    public String Apelido;
    public String Descricao;
    public String UrlImage;

    // Campanha Interna = 1, CampanhaSIPPA = 2
    public String Tipo;
    public String Editavel;
    public int IdCircular;
    public int Ordem;
    public int NumeroPedidoCompulsorio;
    public int NumeroPedido;

}
