package com.br.rogon.designpatternsi.imposto;

import java.math.BigDecimal;

import com.br.rogon.designpatternsi.orcamento.Orcamento;

public class CalculadoraDeImpostos {
    public BigDecimal calcular(Orcamento orcamento, Imposto imposto){
        return imposto.calcular(orcamento);
    }
}
