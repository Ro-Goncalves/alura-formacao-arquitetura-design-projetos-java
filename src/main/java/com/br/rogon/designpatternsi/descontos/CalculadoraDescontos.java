package com.br.rogon.designpatternsi.descontos;

import java.math.BigDecimal;

import com.br.rogon.designpatternsi.orcamento.Orcamento;

public class CalculadoraDescontos {

    public BigDecimal calcular (Orcamento orcamento){    
        Desconto desconto = new DescontoQuantidadeItens(
            new DescontoValorOrcamento(
                new SemDesconto()
            )
        );
               
        return desconto.calcular(orcamento);
    }
}
