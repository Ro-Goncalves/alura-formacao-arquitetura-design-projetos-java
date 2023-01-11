package com.br.rogon.arquiteturadesignjava.solid.service;

import java.math.BigDecimal;

import com.br.rogon.arquiteturadesignjava.solid.model.Funcionario;

public interface ValidacaoReajuste {    
    void validar(Funcionario funcionario, BigDecimal aumento);
}
