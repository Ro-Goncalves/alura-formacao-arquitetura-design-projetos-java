package com.br.rogon.designpatternsi.orcamento;

import java.math.BigDecimal;

public class Orcamento {
    private BigDecimal valor;
    private Integer quantidadeItens;

    public Orcamento(BigDecimal valor, Integer quantidadeItens) {
        this.valor = valor;
        this.quantidadeItens = quantidadeItens;
    }

    public BigDecimal getValor() {
        return this.valor;
    }

    public Integer getQuantidadeItens() {
        return this.quantidadeItens;
    }
}
