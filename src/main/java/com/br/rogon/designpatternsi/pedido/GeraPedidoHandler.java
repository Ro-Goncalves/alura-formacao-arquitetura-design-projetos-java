package com.br.rogon.designpatternsi.pedido;

import java.time.LocalDateTime;

import com.br.rogon.designpatternsi.orcamento.Orcamento;

public class GeraPedidoHandler {
    
    //Construtos com injeção de dependências.
    
    public void execute(GeraPedido dados){
        Orcamento orcamento = new Orcamento(dados.getValorOrcamento(), dados.getQuantidadeItens()); 
        Pedido pedido = new Pedido(dados.getCliente(), LocalDateTime.now(), orcamento);

        System.out.println("Salvar Pedido no Banco de Dados.");
        System.out.println("Enviar e-mail com dados donovo pedido.");
    }
}
