package br.com.agafarma.agamobile.Model;

import java.util.Date;

public class TituloModel {
    public int Sequencia;
    public double Titulo;
    public String Parcela;
    // 1 Cooporfarma 2 Agafarma
    public int Empresa;
    public Date DataVencimento;
    public Date DataLimiteDesconto;
    public Date DataEmissao;
    public double Saldo;
    // Cooporfar/Agafarma = 1 : AgaCard = 2; AgaCredi = 3
    public int TipoEmpresa;

}
