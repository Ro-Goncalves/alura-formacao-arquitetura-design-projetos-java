package com.br.rogon.designpatternsi.orcamento.situacao;

import java.math.BigDecimal;

import com.br.rogon.designpatternsi.orcamento.Orcamento;

public abstract class SituacaoOrcamento {

    public BigDecimal calcularValorDescontoExtra(Orcamento orcamento){
        return BigDecimal.ZERO;
    }

    public void aprovar(Orcamento orcamento){
        throw new DomainException("Orçamento não pode ser aprovado!");
    }

    public void reprovar(Orcamento orcamento) {
        throw new DomainException("Orçamento não pode ser reprovado!");
    }

    public void finalizar(Orcamento orcamento) {
        throw new DomainException("Orçamento não pode ser finalizado!");
    }
}
