package com.br.rogon.designpatternsi;

import java.math.BigDecimal;

import com.br.rogon.designpatternsi.imposto.CalculadoraDeImpostos;
import com.br.rogon.designpatternsi.imposto.ISS;
import com.br.rogon.designpatternsi.orcamento.Orcamento;

public class TesteImpostos {
    public static void main(String[] args) {
        Orcamento orcamento = new Orcamento(new BigDecimal("100"), 0);
        CalculadoraDeImpostos calc = new CalculadoraDeImpostos();

        System.out.println(calc.calcular(orcamento, new ISS()));
    }
    
}
