package com.br.rogon.designpatternsi.descontos;

import java.math.BigDecimal;

import com.br.rogon.designpatternsi.orcamento.Orcamento;

public class DescontoQuantidadeItens extends Desconto{    
    public DescontoQuantidadeItens(Desconto proximo) {
        super(proximo);
    }

    public BigDecimal calcular(Orcamento orcamento){
        if(orcamento.getQuantidadeItens().compareTo(5) >= 0){
            return orcamento.getValor().multiply(new BigDecimal("0.1"));
        }        
        return proximo.calcular(orcamento);
    }
}
