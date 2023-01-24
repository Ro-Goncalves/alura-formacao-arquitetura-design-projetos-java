package com.br.rogon.designpatternsi.imposto;

import java.math.BigDecimal;

import com.br.rogon.designpatternsi.orcamento.Orcamento;

public interface Imposto {
    BigDecimal calcular(Orcamento orcamento);
}
