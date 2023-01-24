package com.br.rogon.designpatternsi.pedido;

import java.time.LocalDateTime;
import java.util.List;

import com.br.rogon.designpatternsi.orcamento.Orcamento;
import com.br.rogon.designpatternsi.pedido.acao.AcoeAposGerarPedido;

public class GeraPedidoHandler {
    
    private List<AcoeAposGerarPedido> acoes;
    
    public GeraPedidoHandler(List<AcoeAposGerarPedido> acoes) {
        this.acoes = acoes;
    }

    public void execute(GeraPedido dados){
        Orcamento orcamento = new Orcamento(dados.getValorOrcamento(), dados.getQuantidadeItens()); 
        Pedido pedido = new Pedido(dados.getCliente(), LocalDateTime.now(), orcamento);

        acoes.forEach(a -> a.executarAcao(pedido));
        
    }
}
