package com.br.rogon.arquiteturadesignjava.solid.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Funcionario {

	private DadosPessoais dadosPessoais;
	private LocalDate dataUltimoReajuste;

	public Funcionario(String nome, String cpf, Cargo cargo, BigDecimal salario) {
		dadosPessoais = new DadosPessoais(nome, cpf, cargo, salario);
	}

	public void atualizarSalario(BigDecimal novoSalario) {
		this.dadosPessoais.setSalario(novoSalario);
		this.dataUltimoReajuste = LocalDate.now();
	}

	public LocalDate getDataUltimoReajuste() {
		return dataUltimoReajuste;
	}

	public void setDataUltimoReajuste(LocalDate dataUltimoReajuste) {
		this.dataUltimoReajuste = dataUltimoReajuste;
	}

    public void promover(Cargo novoCargo) {
		this.dadosPessoais.setCargo(novoCargo);
    }

	public DadosPessoais getDadosPessoais() {
		return dadosPessoais;
	}
}

