# Design Patterns em Java I: Boas Práticas de Programação

Existem 23 padrões, divididos em criacionais, estruturais e comportamentais

Esses padrões nos fazem pensar de forma difernete nosso código

## Strategy

Eeeii!! começou a brincadeira. Temos a classe **Orcamento**, e, como vivemos no Brasil, faz-se necessário calcular o imposto. A principio iremos calcular o *ICMS* e o *ISS*. Começamemos assim, para facilidar um pouco nosso vida, criaremos um ***enun*** que terá os impostos.

```java
public enum TipoImposto {
    ICMS,
    ISS;
}
```

Nada de mais. A classe **CalculadoraDeImpostos** terá o método `calcular(Orcamento orcamento, TipoImposto tipoImposto)`, como teremos vários tipos de impostos, podemos fazer um ***switch*** e implementar a regra para cada um.

```java
public class CalculadoraDeImpostos {
    public BigDecimal calcular(Orcamento orcamento, TipoImposto tipoImposto){
        switch (tipoImposto) {
            case ICMS:                
                return orcamento.getValor().multiply(new BigDecimal("0.1")); 
            case ISS:
                return orcamento.getValor().multiply(new BigDecimal("0.06"));
            default:
                return BigDecimal.ZERO;
        }
    }
}
```

*Mais aqui é Brasil "véi"*, só pensa o caralhada de impostos que pode ter, o quando essa classe pode crescer. Teremos um monte de ***cases*** ou ***ifs/elses***, ler isso seria um tormento. E é aqui que começamos a usar nossos padrões.

Aplicando o *Strategy* nesse caso iremos definir classes que calculam o imposto, cada uma o seu, já começamos com o **S**. Sem contar que iremos criar a interface **Imposto**, toda classe que calculará o imposto deverá implementá-la. Pensem, isso deve lhe lembrar alguma letra um **O**, mesclado com **D**. O **D** é auto evidente, estamos dependendo de uma interface e não de classes concretas. O **O** ficará mais claro à frente.

Nossa interface será bem simples:

```java
public interface Imposto {
    BigDecimal calcular(Orcamento orcamento);
}
```

As classes que a implementaram também

```java
public class ICMS implements Imposto{
    @Override
    public BigDecimal calcular(Orcamento orcamento){
        return orcamento.getValor().multiply(new BigDecimal("0.1")); 
    }
}

public class ISS implements Imposto{
    @Override
    public BigDecimal calcular(Orcamento orcamento){
        return orcamento.getValor().multiply(new BigDecimal("0.06")); 
    }
}
```

Olha que maravilha, sabe os 200 tipo de impostos do Brasil, basta criar 200 classes que implementam a interface **Imposto**, no final do dia tudo fica certo. Agora vamos rescrever nossa classe que calcula o imposto.

```java
public class CalculadoraDeImpostos {
    public BigDecimal calcular(Orcamento orcamento, Imposto imposto){
        return imposto.calcular(orcamento);
    }
}
```

Sinto cheiro de **O** no ar. Nossa classe calculadora está fechada, e pode calcular qualquer tipo de imposto. **SOD** aplicado com sucesso. Claro que possos estar errado em minha analise, afinal acabei de estudar o SOLID.

Isso é o **Strategy**, retuzir os ***ifs*** e ***cases*** abstratindo suas lógicas em classes. Mas ele não é sómente isso, vamos à outra fonte.

Podemos definir o *Strategy* da seguinte forma:

>Padrão comportalemntal que permite definir uma família de algoritmos, tornando seus objetos intercambiáveis.

Conseguimos ver isso muito bem no exemplo, separamos nossa regra que calcula o imposto, e para qualquer calculo para passar uma delas para a classe **CalculadoraDeImpostos**. Não é só disso que vive o *Strategy*; como diria Cesar *divide et impera*, ou Napoleão *divide ut regnes*, como diria o afegão médio, dividir para conquistar, élo que entendi é esse o espirito desse padrão. Em nosso exemplo, dividimos as regras de calculo do imposto e duas classes, um outro que podemos citar são classes que vão recebendo mais e mais lógica, uma classe que calcula rotas, um dia ela calcula uma de carro, no outro a pé, no outros de bicicleta, e fácil ver que ela pode crescer muito.

Para cada coisa que queremos fazer criamos uma classe, chamaremos elas de *strategies*. A classe original é a *context* do role, nela temos um campo que referência uma *strategy*, que será usado para delegar o tabalho.

Quem define qual *strategy* será utilizada é o *client*, ele que tem o poder. O *context* não sabe muito sobre as regras, ele se liga a elas através duma interface, pense nele como um **roteador**, recebe e distribui.

Caso não tenha ficado claro, a ***Context*** possui uma referência a inteface das *strategies*, um método **set** que diz a ela qual *strategy* usar, e seus demais métodos. A interface ***Strategy***, não apeque-se ao nome, define qual deve ser o comportamento das *strategies*. As ***Concrete Strategies*** implementam a inteface. A ***Context*** chama o método de execução da ***Concrete Strategy*** que está vinculado a ela, sem saber o que acontece por baixo dos panos. Por fim, a ***Client*** cria o objeto ***Concrete Strategy*** que precisa, a passa para o `context`, e executa os método necessários, se quiser trocar de `concrete strategy` é só passar o novo para `context` usando o **set**.

## Chain of Responsability

Jordan B. Peterson, em seu livro 12 regras para a vida, diz que existe caos e ordem no mundo, que tudo tende ao caos e nós devemos trazer a ordem, tendo em vista que devemos nós manter sempre em cima do muro, onde de uma lado temos o caos e do outro a ordem.

No epsódio anterior colocamos ordem no coreto; nesse, o caos. Além de impostos nosso sistema passará a dar descontos, a princípio por quantidade de itens e valor do pedido. A fim de concluir esse objetivo criamos a classe **CalculadoraDescontos**, com os famosos ***ifs***.

```java
public class CalculadoraDescontos {
    public BigDecimal calcular (Orcamento orcamento){
        if(orcamento.getQuantidadeItens().compareTo(5) >= 0){
            return orcamento.getValor().multiply(new BigDecimal("0.1"));
        }if (orcamento.getValor().compareTo(new BigDecimal("500")) > 0){
            return orcamento.getValor().multiply(new BigDecimal("0.1"));
        }
        return BigDecimal.ZERO;
    }
}
```

Olhando para esse novo cenário, somos levados a tentar usar o padrão **Strategy**, porém essa seria uma estrategia errada, uma vez que não sabemos qual desconto deve ser aplicado. Nesse caso a vacina será outra, *uma já testada e comprovada*.

E temos aqui um caso tipico para o uso do *Chain of Responsability*, que é um padrão comportamental que permite passa a solicitação ao longo de uma cadeia, cada classe irá decidir se processa o passa para frente.

>Podemos usar esse padrão sempre que identificamos que as execuções de rotinas devem ser feitas em sequência.

Cada rotina dessa será transformada em um objeto independente, o ***handler***, em nosso exemplo, cada regra de desconto virará uma classe. Cada **Handler** criado deverá conter a referência ao próximo, possibilitando que todos os **Handlers** possam ser executados.

>Cada **Handler** pode descidir não passar a execução para frente, interrompendo o fluxo.

Um outro exemplo são os eventos na janela de um programa, ao clicar em um botão ele navega por todos os elementos, aquele que quiser pode processar o evento. Caso tenha ficado meio *desclaro*, temos que tomar cuidado com a palavra **obscuro** agora, imagine uma árvore, com suas raizes ela puxa a água do solo, essa água vai passando pela raiz, troco, galho e folhas, ela pode ficar parada em qualquer parte desse caminho, ou sofrer alguma transformação quimica e ir para o próximo estágio. É a mesma coisa aqui, o cliente definirá qual caminho que a requisição deve passar, e a faz passar por ele, nesse caminho alguém poderá executar alguma rotina e decidir se a requisição deve ou não seguir para frente.

Chega disso, creio que agora ficou *desescuro*, caso não, leia a referência.

Chega de metáforas e vamos ao mundo real, primeiro de tudo o **D**, devemos criar uma interface que será comum a todos os **Concret Handlers**, pode ter somente um método para lidar com a requisição. Algumas vezes possui um método para definir o próximo **Concret Handler**, chamaremos essa interface de **Handler**. Uma classe opcional chamada **BaseHandler**, ela funciona como um **boilerplate** possuindo algum código padrão a todos os **Concret Handlers**, normalmente possui um campo que referencia o próximo **Concret Handler**, também pode funcionar como um **Default Handler**, passando a execução ao próximo **Concret Handler** caso ele exista. Os **Concret Handlers**, são independentes e imutáveis, todos os dados necessários são passados no construtor, a essa altura do campeonado creio não precisar falar o que ele faz. **Client**, esse é o maestro, ele decidirá qual desse ser a corrente e qual o evento que deverá ser processado por ela, uma observação aqui: Mesmo tendo uma cadeia montada o evento pode iniciar sua jornada no meio dele, não no inicio.

BláBláBlá de teoria, como melhorar a classe **CalculadoraDescontos**? Na aula o caminho seguido fui um pouco diferente, criamos uma classe abstrata, a **Desconto**, ela é quem fará a vez da interface.

```java
//Creio poder chamar essa classe de BaseHandler, pois ela possui o boilerplate das demais
public abstract class Desconto {

    //Refêrencia ao próximo Concret Handlers
    protected Desconto proximo;

    //Lembra, receber tudo pelo contrutor
    public Desconto(Desconto proximo) {
        this.proximo = proximo;
    }

    //Processamento que deverá ser implementado pela Concret Handlers
    public abstract BigDecimal calcular(Orcamento orcamento);
}
```

Agora sim, vamos extrair os códigos dos ***ifs*** e os tranformar em **Concret Handler**

```java
//Concret Handler do BaseHandler
public class DescontoValorOrcamento extends Desconto{
    //Recebendo o próximo Concret Handler da cadeia
    public DescontoValorOrcamento(Desconto proximo) {
        super(proximo);
    }

    //Processando a requisição, imutavelmente e independentemente
    @Override
    public BigDecimal calcular (Orcamento orcamento){
        //Verifica de deve processar
        if(orcamento.getValor().compareTo(new BigDecimal("500")) > 0){
            //Processando ...
            return orcamento.getValor().multiply(new BigDecimal("0.05"));
        }
        //Se não for processar, passa ao próximo Concret Handler
        return proximo.calcular(orcamento);
    }
}

//Concret Handler do BaseHandler
public class DescontoQuantidadeItens extends Desconto{   
    //Recebendo o próximo Concret Handler da cadeia 
    public DescontoQuantidadeItens(Desconto proximo) {
        super(proximo);
    }

    //Processando a requisição, imutavelmente e independentemente
    @Override
    public BigDecimal calcular(Orcamento orcamento){
        //Verifica de deve processar
        if(orcamento.getQuantidadeItens().compareTo(5) >= 0){
            //Processando ...
            return orcamento.getValor().multiply(new BigDecimal("0.1"));
        }
        //Se não for processar, passa ao próximo Concret Handler    
        return proximo.calcular(orcamento);
    }
}

//Concret Handler do BaseHandler, o curinga.
public class SemDesconto extends Desconto{

    //Deveria receber o próximo Concret Handler, nesse caso essa classe representa o fim da cadeia
    public SemDesconto() {
        super(null);
    }

    //Processando a requisição, imutavelmente e independentemente, é o fim da cadeio por isso ZERO
    @Override
    public BigDecimal calcular(Orcamento orcamento) {
        return BigDecimal.ZERO;
    }
    
}
```

Nossa calculadora será um tanto quando diferente

```java
//Não é um Concret Handler, está mais para um serviço
public class CalculadoraDescontos {

    //Não está sobrescrevendo nada, só definindo a chain
    public BigDecimal calcular (Orcamento orcamento){
        //Primeiro Concret Handler da chain
        Desconto desconto = new DescontoQuantidadeItens(
            //Primeiro Concret Handler recebe como parâmetro o segundo
            new DescontoValorOrcamento(
                //Segundo Concret Handler recebe como parâmetro o terceiro
                new SemDesconto()
            )
        );
        //Executa o processamento
        return desconto.calcular(orcamento);
    }
}
```

E por hora é isso, criamos o caos e trouxemos a ordem, agora ficaremos em cima do muro esperando o próximo caos.

## Template Method

Às vezes, mesmo quando tudo parece calmo e tranquilo, podemos melhorar aquilo que já está bom. É o que faremos com o código anterior, ele está bom, mas da para melhorar um tanto mais.

Seremos apresentados ao *Template Method*, ele é exatamente o que o nome diz, *template*. Agora vamos explicar o desenho.

> Esse é um padrão comportamental que define o esqueleto de um algoritimo na superclasse, as etapas podem ser sobrescritas pelas subclasses, não causando a modificação do algoritimo.

Ou mais simplista, é a forma e bolo que deverá ser colocada no formo, que após um tempo sairá com o bolo assado. Esse é o algoritimo, o bolo que estará na forma, pouco importa, a receita pode sobscreve-lo sem prejudicar o resultado, o bolo sairá assado no final, só não conseguimos garantir um bolo gostoso e fofinho.

Vamos agora á um exemplo na Matriz, terminando o capítulo anterior com três **Concrets Handlers**, se você notar todos ele possuem o mesmo ***if*** com basicamente a mesma lógica, se tal condição for satisfeita, faça tal coisa, senão, faz essa outra aqui. Temos um padrão se repedindo aqui.

No objeto de estudo diz que devemos quebrar esse algoritimo em uma série de etapas, as transformas em métodos, e coloca-las em um único local. Nele as etapas podem ser **abstratas**, ou possuir algum implementação. As classes concretas, devem implementar o métodos abstratos e se quiser sobescrever os implementados. É obvio que o método padrão, aquele que executará a lógica, não deve ser sobrescrito.

Então criaremos nossa classe padrão com as chamadas necessárias para que o bolo saia quentinho do forno.

```java
public abstract class Desconto {

    protected Desconto proximo;

    public Desconto(Desconto proximo) {
        this.proximo = proximo;
    }

    //Método que representa o algoritimo.
    //Note que ele é final, não poderá ser sobrescrito.
    public final BigDecimal calcular(Orcamento orcamento){
        if(deveAplicar(orcamento)){
            return efetuarCalculo(orcamento);
        }
        return proximo.calcular(orcamento);
    };

    //métodos que devem ser implementados para executar o algoritimo.
    protected abstract BigDecimal efetuarCalculo(Orcamento orcamento);
    protected abstract boolean deveAplicar(Orcamento orcamento);
}
```

Veja que no fundo, só melhoramos nossa classe **Desconto**. Nesse exemplo não temos métodos padrões, mas imagine que um dos passos seja abrir um arquivo, o mesmo arquivo sempre, ele poderia ser implementado direto no *template*. Existe um outro tipo de método os ***hooks***, ela seria uma etapa com o corpo vazio, geralmente colocada entre pontos cruciais do algoritimo. O algoritimo funciona mesmo sem eles serem implementados.

Veja, nosso *template* para assar um bolo, não impede que ele seja assado à banho maria. Ela é um etapa sem corpo, se não for implementada o bolo será assado da forma "normal", se for será assado na forma da implementação.

```java
public class DescontoQuantidadeItens extends Desconto{    
    public DescontoQuantidadeItens(Desconto proximo) {
        super(proximo);
    }

    //Sobrescrevendo a etapa
    @Override
    public BigDecimal efetuarCalculo(Orcamento orcamento){
        return orcamento.getValor().multiply(new BigDecimal("0.1"));  
    }

    //Sobrescrevendo a etapa
    @Override
    public boolean deveAplicar(Orcamento orcamento) {   
        return orcamento.getQuantidadeItens().compareTo(5) >= 0;
    }        
}

public class DescontoValorOrcamento extends Desconto{
    public DescontoValorOrcamento(Desconto proximo) {
        super(proximo);
    }

    //Sobrescrevendo a etapa
    @Override
    public BigDecimal efetuarCalculo (Orcamento orcamento){       
        return orcamento.getValor().multiply(new BigDecimal("0.05"));       
    }

    //Sobrescrevendo a etapa
    @Override
    public boolean deveAplicar(Orcamento orcamento) {
        return orcamento.getValor().compareTo(new BigDecimal("500")) > 0;
    }
}

public class SemDesconto extends Desconto{

    public SemDesconto() {
        super(null);
    }

    //Sobrescrevendo a etapa
    @Override
    public BigDecimal efetuarCalculo(Orcamento orcamento) {
        return BigDecimal.ZERO;
    }

    //Sobrescrevendo a etapa
    @Override
    public boolean deveAplicar(Orcamento orcamento) {
        return true;
    }    
}
```

Os código duplicados foram-se embora. Lindo de mais.

>Esse padrão me respondeu a uma pergunta que sempre fiz: *Mas eu tenho que ficar duplicando esse pedaço de código, mesmo que seja um if pequeno?*

## State

## Command

## Observer

## Referências

[REFACTORING GURU - Strategy](https://refactoring.guru/pt-br/design-patterns/strategy)

[REFACTORING GURU - Chain of Responsability](https://refactoring.guru/pt-br/design-patterns/chain-of-responsibility)

[REFACTORING GURU - Template Method](https://refactoring.guru/design-patterns/template-method)

[REFACTORING GURU - State](https://refactoring.guru/pt-br/design-patterns/state)

[REFACTORING GURU - Command](https://refactoring.guru/pt-br/design-patterns/command)

[REFACTORING GURU - Observer](https://refactoring.guru/pt-br/design-patterns/observer)
