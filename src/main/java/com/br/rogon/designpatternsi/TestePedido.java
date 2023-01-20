package com.br.rogon.designpatternsi;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.br.rogon.designpatternsi.orcamento.Orcamento;
import com.br.rogon.designpatternsi.pedido.GeraPedido;
import com.br.rogon.designpatternsi.pedido.GeraPedidoHandler;
import com.br.rogon.designpatternsi.pedido.Pedido;

public class TestePedido {
    public static void main(String[] args) {
        String cliente = "Rodrigo";
        BigDecimal valorOrcamento = new BigDecimal("600.00");
        Integer quantidadeItens = 4;

        GeraPedido gerador = new GeraPedido(cliente, valorOrcamento, quantidadeItens);
        GeraPedidoHandler handler = new GeraPedidoHandler(/* Dependências */);
        handler.execute(gerador);   
    }
}
