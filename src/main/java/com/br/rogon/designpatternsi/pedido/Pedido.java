package com.br.rogon.designpatternsi.pedido;

import java.time.LocalDateTime;

import com.br.rogon.designpatternsi.orcamento.Orcamento;

public class Pedido {
    
    private String cliente;
    private LocalDateTime data;
    private Orcamento orcamento;

    public Pedido(String cliente, LocalDateTime data, Orcamento orcamento) {
        this.cliente = cliente;
        this.data = data;
        this.orcamento = orcamento;
    }

    public String getCliente() {
        return cliente;
    }

    public LocalDateTime getData() {
        return data;
    }

    public Orcamento getOrcamento() {
        return orcamento;
    }

    @Override
    public String toString() {
        return "Pedido [cliente=" + cliente + ", data=" + data + ", orcamento=" + orcamento + "]";
    }    
}
