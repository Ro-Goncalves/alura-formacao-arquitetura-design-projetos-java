package com.br.rogon.designpatternsi.pedido.acao;

import com.br.rogon.designpatternsi.pedido.Pedido;

public class EnviarEmailPedido implements AcoeAposGerarPedido{
    @Override
    public void executarAcao(Pedido pedido){
        System.out.println("Enviar e-mail com dados do novo pedido." + pedido.toString());
    }
}
