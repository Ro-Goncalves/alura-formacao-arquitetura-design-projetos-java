package com.br.rogon.designpatternsi.orcamento;

import java.math.BigDecimal;

import com.br.rogon.designpatternsi.orcamento.situacao.EmAnalise;
import com.br.rogon.designpatternsi.orcamento.situacao.SituacaoOrcamento;

public class Orcamento {
    private BigDecimal valor;
    private Integer quantidadeItens;
    private SituacaoOrcamento situacao;

    public Orcamento(BigDecimal valor, Integer quantidadeItens) {
        this.valor = valor;
        this.quantidadeItens = quantidadeItens;
        this.situacao = new EmAnalise();
    }

    public void aplicarDescontoExtra(){
        BigDecimal valorDoDescontoExtra = this.situacao.calcularValorDescontoExtra(this);
        this.valor = this.valor.subtract(valorDoDescontoExtra);
    }

    public void aprova(){
        this.situacao.aprovar(this);
    }

    public void reprovar(){
        this.situacao.reprovar(this);
    }

    public void finalizar(){
        this.situacao.finalizar(this);
    }

    public Integer getQuantidadeItens() {
        return this.quantidadeItens;
    }

    public void setSituacao(SituacaoOrcamento situacaoOrcamento) {
        this.situacao = situacaoOrcamento;
    }

    public SituacaoOrcamento getSituacao() {
        return situacao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public void setQuantidadeItens(Integer quantidadeItens) {
        this.quantidadeItens = quantidadeItens;
    }

    @Override
    public String toString() {
        return "Orcamento [valor=" + valor + ", quantidadeItens=" + quantidadeItens + "]";
    }
}
