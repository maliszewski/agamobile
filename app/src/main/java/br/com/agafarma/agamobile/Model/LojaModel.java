package br.com.agafarma.agamobile.Model;

import java.util.Date;

public class LojaModel {
    public int LojaId;
    public String RazaoSocial;
    public String NomeFantasia;
    public String Endereco;
    public String Bairro;
    public String Cidade;
    public String Ativa;
    public String Cnpj;
    public Date DataCadastro;
    public Date DataBaseCirculares;
    public int CodigoSis;
    public String Classificacao;
    public Double PotencialEtico;
    public Double ComprometimentoEtico;
    public Double PotencialSimilar;
    public String ComprometimentoSimilar;
    public int QuantidadeEncarte;
    public int QuantidadeExcedente;
    public int DDD;
    public String Fone;
    public String TeleEntrega;
    public String GetTeleEntrega() {
        String result = "Não";
        if (TeleEntrega.equals("S"))
            result = "Sim";
        return result;
    }
    public String TeleEntregaHorario;
    public String TeleEntregaFone;
    public String FarmaciaPopular;
    public String GetFarmaciaPopular() {
        String result = "Não";
        if (FarmaciaPopular.equals("S"))
            result = "Sim";
        return result;
    }

    public String Cep;
    public String Contato;
    public String RegiaoRbsId;
    public String Tipo;
    public String GetTipo() {
        String result = "Não identificado";
        if (Tipo.equals("SF"))
            result = "Socio Fundador";
        else if (Tipo.equals("F"))
            result = "Fundador";
        else if (Tipo.equals("S"))
            result = "Socio";
        return result;
    }

    public String CodigoSolbase;
    public String SituacaoCredito;
    public String GetSituacaoCredito() {
        String result = "Bloqueada";
        if (SituacaoCredito.equals("L"))
            result = "Liberada";
        return result;
    }
    public int PortadorSolbase;
    public int ClimaTempoId;
    public String CpfProprietario1;
    public String NomeProprietario1;
    public String Cooperada;
    public String GetCooperada() {
        String result = "Não";
        if (Cooperada.equals("S"))
            result = "Sim";
        return result;
    }

    public String InscricaoEstadual;
    public String IgnorarPotencialEncarte;
    public int CodigoTransportadoraSolbase;
    public String Estado;
    public int ClassificacaoLojaId;
    public String CodSantaCruz;
    public Double SaldoDevedor;
    public Double LimiteCredito;
    public String Email;
    public int QuantidadeFolhetoExcedente;
    public String EmNegociacao;
    public String GetEmNegociacao() {
        String result = "Não";
        if (EmNegociacao.equals("S"))
            result = "Sim";
        return result;
    }

    public int Regiao;
    public String SistemaERP;
    public Double Metragem;
    public Double MetragemVenda;
    public int NumeroFuncionario;
    public int PadraoLoja;
    public int Cluster;
    public int Tamanho;

    public LojaModel() {
        this.DataBaseCirculares = new Date();
        this.DataCadastro = new Date();
    }
}

