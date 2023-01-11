package com.br.rogon.arquiteturadesignjava.solid.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.br.rogon.arquiteturadesignjava.solid.exceptions.ValidacaoException;
import com.br.rogon.arquiteturadesignjava.solid.model.Funcionario;

public class ValidacaoPeridiocidadeEntreReajustes implements ValidacaoReajuste {
    
    @Override
    public void validar(Funcionario funcionario, BigDecimal aumento){
        LocalDate dataUltimoReajuste = funcionario.getDataUltimoReajuste();
        LocalDate dataAtual = LocalDate.now();
        long mesesDesdoUltimoReajuste = ChronoUnit.MONTHS.between(dataUltimoReajuste, dataAtual);
        if(mesesDesdoUltimoReajuste < 6){
            throw new ValidacaoException("Intervalo entre reajustes menor do que 6 meses.");
        }
    }
}
