package com.br.rogon.designpatternsi.imposto;

import java.math.BigDecimal;

import com.br.rogon.designpatternsi.orcamento.Orcamento;

public class ISS implements Imposto{
    @Override
    public BigDecimal calcular(Orcamento orcamento){
        return orcamento.getValor().multiply(new BigDecimal("0.06")); 
    }
}
