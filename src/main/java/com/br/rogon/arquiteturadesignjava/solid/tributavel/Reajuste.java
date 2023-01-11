package com.br.rogon.arquiteturadesignjava.solid.tributavel;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface Reajuste {
    
    BigDecimal valor();
    LocalDate data();
}
