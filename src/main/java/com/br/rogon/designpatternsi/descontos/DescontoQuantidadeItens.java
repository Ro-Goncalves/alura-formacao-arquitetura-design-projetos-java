package com.br.rogon.designpatternsi.descontos;

import java.math.BigDecimal;

import com.br.rogon.designpatternsi.orcamento.Orcamento;

public class DescontoQuantidadeItens extends Desconto{    
    public DescontoQuantidadeItens(Desconto proximo) {
        super(proximo);
    }

    @Override
    public BigDecimal efetuarCalculo(Orcamento orcamento){
        return orcamento.getValor().multiply(new BigDecimal("0.1"));  
    }

    @Override
    public boolean deveAplicar(Orcamento orcamento) {   
        return orcamento.getQuantidadeItens().compareTo(5) >= 0;
    }        
}
