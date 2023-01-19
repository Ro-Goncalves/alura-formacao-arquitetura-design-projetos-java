package com.br.rogon.designpatternsi.orcamento.situacao;

import com.br.rogon.designpatternsi.orcamento.Orcamento;

public class Reprovado extends SituacaoOrcamento{

    public void finalizar(Orcamento orcamento){
        orcamento.setSituacao(new Finalizado());
    }
}
