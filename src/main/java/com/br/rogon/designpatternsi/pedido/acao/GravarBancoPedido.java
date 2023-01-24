package com.br.rogon.designpatternsi.pedido.acao;

import com.br.rogon.designpatternsi.pedido.Pedido;

public class GravarBancoPedido implements AcoeAposGerarPedido{
    @Override
    public void executarAcao(Pedido pedido){
        System.out.println("Salvar Pedido no Banco de Dados." + pedido.toString());
    }
}
