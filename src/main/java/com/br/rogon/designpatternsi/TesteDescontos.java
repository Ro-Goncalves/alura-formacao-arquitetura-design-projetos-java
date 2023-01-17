package com.br.rogon.designpatternsi;

import java.math.BigDecimal;

import com.br.rogon.designpatternsi.descontos.CalculadoraDescontos;
import com.br.rogon.designpatternsi.orcamento.Orcamento;

public class TesteDescontos {
    public static void main(String[] args) {
        Orcamento orcamento = new Orcamento(new BigDecimal("200"), 6);
        Orcamento orcamento2 = new Orcamento(new BigDecimal("1000"), 1);

        CalculadoraDescontos cDescontos = new CalculadoraDescontos();
        System.out.println(cDescontos.calcular(orcamento));
        System.out.println(cDescontos.calcular(orcamento2));
    }
}
