# Design Patterns em Java I: Boas Práticas de Programação

Devo dizer que é uma merca estudar esses padrões, na verdade é uma merda estudar qualquer coisa, explico.

Você fica achando que dá para aplicar o que aprendeu em tudo, todos os seus problemas se parecem com o padrão que está estudando no momento. Outro ponto, ao tentar aplicar vemos o qual difícil e fazer isso no mundo real, é nele que as coisas dão errado.

É claro que tudo isso deve-se a imaturidade, quanto mais estudar e aprender, mais saberei quando e como aplicar um *Pattern*.

A consequência imediata e a vontade de querer escrever um código melhor, de querer se aprimorar a mostrar que estamos nos tronando um profissional melhor.

É aqui que entramos na separação do joio do trigo, de um lado programadores, do outro amadores. Peça Graça a Deus, que ele observe cada momento de seus estudos, Ele pode iluminar nosso intelecto e fazer com que esse conhecimento entre em nossa cabeça dura.

## Strategy

Eeeii!! começou a brincadeira. Temos a classe **Orcamento**, e, como vivemos no Brasil, faz-se necessário calcular o imposto. A princípio iremos calcular o *ICMS* e o *ISS*. Começaremos assim, para facilitar um pouco nosso vida, criaremos um ***enun*** que terá os impostos.

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

Aplicando o *Strategy* nesse caso iremos definir classes que calculam o imposto, cada uma o seu, já começamos com o **S**. Sem contar que iremos criar a interface **Imposto**, toda classe que calculará o imposto deverá implementá-la. Pensem, isso deve lhe lembrar alguma letra, um **O**, mesclado com **D**. O **D** é auto evidente, estamos dependendo de uma interface e não de classes concretas. O **O** ficará mais claro à frente.

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

Olha que maravilha, sabe os 200 tipos de impostos do Brasil, basta criar 200 classes que implementam a interface **Imposto**, no final do dia tudo fica certo. Agora vamos rescrever nossa classe que calcula o imposto.

```java
public class CalculadoraDeImpostos {
    public BigDecimal calcular(Orcamento orcamento, Imposto imposto){
        return imposto.calcular(orcamento);
    }
}
```

Sinto cheiro de **O** no ar. Nossa classe calculadora está fechada, e pode calcular qualquer tipo de imposto. **SOD** aplicado com sucesso. Claro que posso estar errado em minha analise, afinal acabei de estudar o SOLID.

Isso é o **Strategy**, reduzir os ***ifs*** e ***cases*** abstraindo suas lógicas em classes. Mas ele não é somente isso, vamos à outra fonte.

Podemos definir o *Strategy* da seguinte forma:

>Padrão comportamental que permite definir uma família de algoritmos, tornando seus objetos intercambiáveis.

Conseguimos ver isso muito bem no exemplo, separamos nossa regra que calcula o imposto, e para qualquer cálculo para passar uma delas para a classe **CalculadoraDeImpostos**. Não é só disso que vive o *Strategy*; como diria Cesar *divide et impera*, ou Napoleão *divide ut regnes*, como diria o afegão médio, dividir para conquistar, pelo que entendi é esse o espírito desse padrão. Em nosso exemplo, dividimos as regras de cálculo do imposto e duas classes, um outro que podemos citar são classes que vão recebendo mais e mais lógica, uma classe que calcula rotas, um dia ela calcula uma de carro, no outro a pé, no outros de bicicleta, e fácil ver que ela pode crescer muito.

Para cada coisa que queremos fazer criamos uma classe, chamaremos elas de *strategies*. A classe original é a *context* do role, nela temos um campo que referência uma *strategy*, que será usado para delegar o trabalho.

Quem define qual *strategy* será utilizada é o *client*, ele que tem o poder. O *context* não sabe muito sobre as regras, ele se liga a elas através duma interface, pense nele como um **roteador**, recebe e distribui.

Caso não tenha ficado claro, a ***Context*** possui uma referência a interface das *strategies*, um método **set** que diz a ela qual *strategy* usar, e seus demais métodos. A interface ***Strategy***, não apegue-se ao nome, define qual deve ser o comportamento das *strategies*. As ***Concrete Strategies*** implementam a interface. A ***Context*** chama o método de execução da ***Concrete Strategy*** que está vinculado a ela, sem saber o que acontece por baixo dos panos. Por fim, a ***Client*** cria o objeto ***Concrete Strategy*** que precisa, a passa para o `context`, e executa os métodos necessários, se quiser trocar de `concrete strategy` é só passar o novo para `context` usando o **set**.

## Chain of Responsability

Jordan B. Peterson, em seu livro 12 regras para a vida, diz que existe caos e ordem no mundo, que tudo tende ao caos e nós devemos trazer a ordem, tendo em vista que devemos nós manter sempre em cima do muro, onde de um lado temos o caos e do outro a ordem.

No episódio anterior colocamos ordem no coreto; nesse, o caos. Além de impostos nosso sistema passará a dar descontos, a princípio por quantidade de itens e valor do pedido. A fim de concluir esse objetivo criamos a classe **CalculadoraDescontos**, com os famosos ***ifs***.

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

Olhando para esse novo cenário, somos levados a tentar usar o padrão **Strategy**, porém essa seria uma estratégia errada, uma vez que não sabemos qual desconto deve ser aplicado. Nesse caso a vacina será outra, *uma já testada e comprovada*.

E temos aqui um caso típico para o uso do *Chain of Responsability*, que é um padrão comportamental que permite passa a solicitação ao longo de uma cadeia, cada classe irá decidir se processa o passa para frente.

>Podemos usar esse padrão sempre que identificamos que as execuções de rotinas devem ser feitas em sequência.

Cada rotina dessa será transformada em um objeto independente, o ***handler***, em nosso exemplo, cada regra de desconto virará uma classe. Cada **Handler** criado deverá conter a referência ao próximo, possibilitando que todos os **Handlers** possam ser executados.

>Cada **Handler** pode decidir não passar a execução para frente, interrompendo o fluxo.

Um outro exemplo são os eventos na janela de um programa, ao clicar em um botão ele navega por todos os elementos, aquele que quiser pode processar o evento. Caso tenha ficado meio *desclaro*, temos que tomar cuidado com a palavra **obscuro** agora, imagine uma árvore, com suas raízes ela puxa a água do solo, essa água vai passando pela raiz, troco, galho e folhas, ela pode ficar parada em qualquer parte desse caminho, ou sofrer alguma transformação química e ir para o próximo estágio. É a mesma coisa aqui, o cliente definirá qual caminho que a requisição deve passar, e a faz passar por ele, nesse caminho alguém poderá executar alguma rotina e decidir se a requisição deve ou não seguir para frente.

Chega disso, creio que agora ficou *desescuro*, caso não, leia a referência.

Chega de metáforas e vamos ao mundo real, primeiro de tudo o **D**, devemos criar uma interface que será comum a todos os **Concret Handlers**, pode ter somente um método para lidar com a requisição. Algumas vezes possui um método para definir o próximo **Concret Handler**, chamaremos essa interface de **Handler**. Uma classe opcional chamada **BaseHandler**, ela funciona como um **boilerplate** possuindo algum código padrão a todos os **Concret Handlers**, normalmente possui um campo que referência o próximo **Concret Handler**, também pode funcionar como um **Default Handler**, passando a execução ao próximo **Concret Handler** caso ele exista. Os **Concret Handlers**, são independentes e imutáveis, todos os dados necessários são passados no construtor, a essa altura do campeonato creio não precisar falar o que ele faz. **Client**, esse é o maestro, ele decidirá qual desse ser a corrente e qual o evento que deverá ser processado por ela, uma observação aqui: Mesmo tendo uma cadeia montada o evento pode iniciar sua jornada no meio dele, não no início.

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

Agora sim, vamos extrair os códigos dos ***ifs*** e os transformar em **Concret Handler**

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

Às vezes, mesmo quando tudo parece calmo e tranquilo, podemos melhorar aquilo que já está bom. É o que faremos com o código anterior, ele está bom, mas dá para melhorar um tanto mais.

Seremos apresentados ao *Template Method*, ele é exatamente o que o nome diz, *template*. Agora vamos explicar o desenho.

> Esse é um padrão comportamental que define o esqueleto de um algoritmo na superclasse, as etapas podem ser sobrescritas pelas subclasses, não causando a modificação do algoritmo.

Ou mais simplista, é a forma e bolo que deverá ser colocada no formo, que após um tempo sairá com o bolo assado. Esse é o algoritmo, o bolo que estará na forma, pouco importa, a receita pode subscrevê-lo sem prejudicar o resultado, o bolo sairá assado no final, só não conseguimos garantir um bolo gostoso e fofinho.

Vamos agora á um exemplo na Matriz, terminando o capítulo anterior com três **Concrets Handlers**, se você notar todos eles possuem o mesmo ***if*** com basicamente a mesma lógica, se tal condição for satisfeita, faça tal coisa, senão, faz essa outra aqui. Temos um padrão se repedindo aqui.

No objeto de estudo diz que devemos quebrar esse algoritmo em uma série de etapas, as transformas em métodos, e coloca-las em um único local. Nele as etapas podem ser **abstratas**, ou possuir alguma implementação. As classes concretas, devem implementar os métodos abstratos e se quiser sobrescrever os implementados. É obvio que o método padrão, aquele que executará a lógica, não deve ser sobrescrito.

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

Veja que no fundo, só melhoramos nossa classe **Desconto**. Nesse exemplo não temos métodos padrões, mas imagine que um dos passos seja abrir um arquivo, o mesmo arquivo sempre, ele poderia ser implementado direto no *template*. Existe um outro tipo de método os ***hooks***, ela seria uma etapa com o corpo vazio, geralmente colocada entre pontos cruciais do algoritmo. O algoritmo funciona mesmo sem eles serem implementados.

Veja, nosso *template* para assar um bolo, não impede que ele seja assado à banho maria. Ela é uma etapa sem corpo, se não for implementada o bolo será assado da forma "normal", se for será assado na forma da implementação.

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

Os códigos duplicados foram-se embora. Lindo de mais.

>Esse padrão me respondeu a uma pergunta que sempre fiz: *Mas eu tenho que ficar duplicando esse pedaço de código, mesmo que seja um if pequeno? *

## State

Nem sempre os requisitos ficam tanto tempo se se alterar, essas merdas sempre mudam, então, vamos de novidade. Nosso orçamento terá algumas situações agora, a saber: *Aprovado, EmAnalise, Finalizado, Reprovado*, cada uma delas definirá para qual situação o orçamento pode ir, bem como um novo desconto, sempre tem aquela pessoa que chora por um pouco mais de desconto.

Uma coisa que devo colocar em minha cabeça e você também:

>Comece simples, resolva o problema, depois identifique o que pode ser melhorado. Às vezes é fácil identificar um padrão, às vezes não.

Sendo assim, vamos ao simples.

```java
public class Orcamento {
    private BigDecimal valor;
    private Integer quantidadeItens;
    //Situação como String, já vimos que não é o ideal, deixa estar
    private String situacao;

    public Orcamento(BigDecimal valor, Integer quantidadeItens) {
        this.valor = valor;
        this.quantidadeItens = quantidadeItens;
    }

    //Calcula o choro
    public void aplicarDescontoExtra(){
        BigDecimal valorDoDescontoExtra = BigDecimal.ZERO;
        //Ifs, estamos os odiando agora, mas espere.
        if(situacao.equalsIgnoreCase("EM_ANALISE")){
            //Um pouco mais de desconto para ver se aprova
            valorDoDescontoExtra = new BigDecimal("0.05");
        } else if (situacao.equalsIgnoreCase("APROVADO")){
            //Depois que aprovou, um pouco menos, só para pagar com PIX
            valorDoDescontoExtra = new BigDecimal("0.02");
        }

        this.valor = this.valor.subtract(valorDoDescontoExtra);
    }

    //Troca situação para aprovado
    public void aprovar(){
        this.situacao = "APROVADO";
    }
    //(Troca demais situações, e o resto dos métodos)...
}
```

Parece um **Strategy**, mas não é. Esse, cria uma família de algoritmos que podem ser intercambiáveis entre si, ele resolve os problemas em que existem várias soluções semelhantes, calcule a rota a pé, calcule a rota para um carro, calcule a rota para um navio, é sempre calcule a rota para algo. Agora temos uma alteração de estado para um mesmo objeto.

Em cenários como esse usamos o **State**, ele é mais um padrão comportamental, permite que o objeto altere seu estado, como se alterasse de classe.

Ele está relacionado ao conceito de *Máquina de Estado Finito*, aqui eu irei copiar a referência, ficou muito bom o que ele escreveu.

>"A ideia principal é que, em qualquer dado momento, há um número *finito* de *estados* que um programa possa estar. Dentro de qualquer estado único, o programa se comporta de forma diferente, e o programa pode ser trocado de um estado para outro instantaneamente. Contudo, dependendo do estado atual, o programa pode ou não trocar para outros estados. Essas regras de troca, chamadas *transições*, também são finitas e pré-determinadas." ***[REFACTORING GURU - State](https://refactoring.guru/pt-br/design-patterns/state)***

Esse é exatamente o nosso caso, o orçamento passa por diversas transformações ao longo do tempo, e em cada estado ele deve se comportar um pouco diferente. E a solução o famigerado **S**; cada macaco no seu galho; cada estado em sua classe, e o objeto original tem uma referência a ela. Primeira pergunta, tenho 15 estados, devo ter 15 referências em minha classe original? não "mane", aqui é **POO** e **D**, basta criar uma boa abstração para os estados, e fazer com que todos herdem ele, pode ser uma interface ou classe abstrata, tudo depende do freguês.

Tudo parece claro, mas quando vamos fazer sozinhos algo de errado, isso quer dizer que não absorvemos o conteúdo de forma adequada. Então, uma receita de bolo.

O **Context** armazena uma referência a um **Concrete State**, delegando a ele todo o trabalho específico do estado. Essa comunicação é feita através de uma *interface* ou *abstract class*. Ele expõe um método para definir o novo estado.

A **Interface State** ou **Abstract class State**, declara os métodos dos estados, cuidado com o **L**.

**Concrete State**, é o estado em si, dependendo do caso podemos ter classes abstratas de suporte, imagina que dois estados podem fazer mais ou menos a mesma coisa. Eles podem possuir uma referência ao **Context**. Tanto o **Concrete State** e o **Context** podem fazer transição de estado.

Show, dito isso, vamos mexer o bolo.

Primeiro nossa abstração. Criaremos a classe `SituacaoOrcamento`, abstrata, e não interface, porque queremos algumas implementações padrão, e podemos ou não implementar o método na classe concreta.

```java
public abstract class SituacaoOrcamento {

    //Podemos não querer dar desconto. Comportamento de cada estado
    public BigDecimal calcularValorDescontoExtra(Orcamento orcamento){
        return BigDecimal.ZERO;
    }

    /*
     * Métodos que fazerm a transição de estado. 
     * Caso não sobrescrito retornará um erro.
    */
    public void aprovar(Orcamento orcamento){
        throw new DomainException("Orçamento não pode ser aprovado!");
    }

    public void reprovar(Orcamento orcamento) {
        throw new DomainException("Orçamento não pode ser reprovado!");
    }

    public void finalizar(Orcamento orcamento) {
        throw new DomainException("Orçamento não pode ser finalizado!");
    }
}
```

Os **Concrets States**.

```java
public class EmAnalise extends SituacaoOrcamento{
    /*
     * O início.
     * O orçamento pode ser aprovado ou reprovado.
    */
    @Override
    public BigDecimal calcularValorDescontoExtra(Orcamento orcamento){
        return orcamento.getValor().multiply(new BigDecimal("0.05"));
    }

    @Override
    public void aprovar(Orcamento orcamento){
        orcamento.setSituacao(new Aprovado());
    }

    @Override
    public void reprovar(Orcamento orcamento){
        orcamento.setSituacao(new Reprovado());
    }
}

public class Aprovado extends SituacaoOrcamento{
    /*
     * Não implementaremos todos os métodos, herdará o padrão.
     * Se estou aprovado, só posso finalizar
    */
    @Override
    public BigDecimal calcularValorDescontoExtra(Orcamento orcamento){
        return orcamento.getValor().multiply(new BigDecimal("0.02"));
    }

    @Override
    public void finalizar(Orcamento orcamento){
        orcamento.setSituacao(new Finalizado());
    }
}

public class Reprovado extends SituacaoOrcamento{
    /*
     * Se estou reprovado, só posso ser finalizado
    */
    @Override
    public void finalizar(Orcamento orcamento){
        orcamento.setSituacao(new Finalizado());
    }
}

public class Finalizado extends SituacaoOrcamento{
   //Se estou finalizado, não posso fazer mais nada.
}
```

E agora a nossa classe **Context**

```java
public class Orcamento {
    private BigDecimal valor;
    private Integer quantidadeItens;
    //Não é mais uma String, grande evolução
    private SituacaoOrcamento situacao;

    public Orcamento(BigDecimal valor, Integer quantidadeItens) {
        this.valor = valor;
        this.quantidadeItens = quantidadeItens;
        //Estado inicial do role
        this.situacao = new EmAnalise();
    }

    //Delega o calculo de desconto para o estado.
    public void aplicarDescontoExtra(){
        BigDecimal valorDoDescontoExtra = this.situacao.calcularValorDescontoExtra(this);
        this.valor = this.valor.subtract(valorDoDescontoExtra);
    }

    /*
     * As transições tão serão delegadas.
    */
    public void aprova(){
        this.situacao.aprovar(this);
    }

    public void reprovar(){
        this.situacao.reprovar(this);
    }

    public void finalizar(){
        this.situacao.finalizar(this);
    }
    //...
}
```

Top, por hoje é isso aí.

## Command

Agora o bicho vai pegar, esse padrão está sendo um pouco complicado para entender. Se Deus quiser, até eu terminar a descrição dele irei entender, e quem estiver lendo também.

Em resumo, podemos dizer que esse padrão transforma o pedido em um objeto que contém toda a informação sobre ele. Com isso, podemos configurar os pedidos de várias formas, colocá-los em fila, serializa e deserializar o pedido, enfim, qualquer coisa que você faria com um objeto.

Eu sou muito pouco criativo, e inexperiente também, para criar um exemplo por mim mesmo. Por isso, se eu for insuficiente recomendo a leitura da referência: ***[REFACTORING GURU - Command](https://refactoring.guru/pt-br/design-patterns/command)***.

Dito isso, imagine que você tem alguma classe, muito bonita e bem-produzida, que executa alguma rotina, ela é usada em várias subclasses, que executam rotinas mais específicas. Quem saber isso pode ajudar, você tem uma receita base de bolo, farinha, ovo, leite, fermento, manteiga, certo, porém você pode fazer dezenas de variações com a mesma receita, você pode ter um bolo de maracujá, um de chocolate, um de cenoura, um de cenoura com laranja, um de laranja. Até aí parece OK, você tem uma subclasse para cada bolo, mas todas elas estão suscetíveis a alterações no classe bolo, se você trocar a farinha, mudar a proporção de alguma coisa, tudo pode dar errado. Criamos um código altamente dependente de nossa regra de negócio.

E ainda ficou pior do que o imaginado, veja temo bolo de laranja, bolo de cenoura, e bolo de cenoura com laranja; tivemos que duplicar código.

Esse é nosso problema, uma classe que serviu de base para várias outras, criando uma dependência grande delas com a regra de negócio, que pode mudar de uma hora para outra, sem contar que até duplicamos código dentro das subclasses.

É hora de pensar em uma solução. Hora de invocar a máxima: ***divide et impera***. Normalmente uma aplicação é dividia em camadas, o famigerado **MVC**, onde podemos separar a lógica de negócio da interface gráfica. Podemos descrever essa interação da seguinte forma: A GUI chama um método da camada de negócio passando os argumentos, **falamos que um objeto está enviando um *pedido* para outro.**.

Nosso objeto de estudo, sugere que esse pedido não seja enviado diretamente. Ele deve enviar à classe ***Command***, essa classe contém tudo o que é necessário para realizar o pedido e um método de execução.

Esse objeto **Command** funciona como um link entre a GUI e a lógica de negócio, é a **Command** que fará o serviço sujo. Você pode ter botões em locais diferentes que executam a mesma ação, salvar alguma coisa, todos eles podem utilizar o ***Save Comand***.

Já conseguimos ver uma melhora, indo além, cada **Command** deve implementar a mesma interface, ela geralmente contém o método de execução, que não recebe parâmetro algum. Isso resolve o problema de acoplamento. Agora nosso remetente tem uma referência à essa interface, temos várias **Command** que a implementam, logo, o comportamento do rementente pode ser alterado em tempo de execução, pasta conter o método `setCommand(InterfaceCommand command)`.

Vamos ao que falta, os parâmetros de execução. Ou nosso comando é pré-configurado, ou é capaz de obtê-los por si mesmo.

Vimos até aqui que podemos criar somente uma classe, sem um monte de filhas, essa classe contém apenas uma referência a uma interface command, essa referência pode receber toda sorte de command que queremos. Teremos tantos commands quando execuções que desejamos.

Agora em “tecniques”. Temos um **Invoker**, ou remetente, é ele que inicia o pedido. Como já dito, ele possuiu um campo que referência algum **Command**, ele possui o método `setCommand(command)` que é configurado pelo cliente, ou recebe pelo seu construtor. E um método de execução, `executeCommand()`, que a delegará ao **Command**.

A interface **Command**, só tem o método `execute()`, àquele que é executado pelo **Invoker**.

Os **Concrete Command**, quem realmente realizará o serviço sujo, o peão de obra, chão de fábrica. Ele pode delegar a execução do trabalho para uma das classes que controlam a regra de negócio, pode até ficar nele para facilitar o role, vai do freguês. Os parâmetros podem ser os campos desse objeto, e para que ele seja imutável, deve recebê-los pelo construtor.

**Reciver**, se nosso **Concrete Command** delegar a execução da lógica, será para os **Reciveres**, dessa forma o **Concrete Command** terá os detalhes do pedido, e o **Reciver** executará a lógica.

Sobra para nosso **Client** criar e configurar o **Concrete Command**, passa todos os parâmetros, inclusive a instância do **Reciver**, para o construtor do **Concrete Command**.

Ufa, chegamos ao fim, e tenho uma breve imagem em minha cabeça de como esse padrão funciona. Bora à aula da Alura.

Comecemos criando a classe que representará o pedido.

```java
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
}
```

Primeiro, em nosso cliente, descreveremos o que deve ser executado.

```java
public class TestePedido {
    public static void main(String[] args) {
        Orcamento orcamento = new Orcamento(new BigDecimal("600"), 4);
        String cliente = "Rodrigo Gonçalves";
        LocalDateTime data = LocalDateTime.now();

        //Criamos o pedido.
        Pedido pedido = new Pedido(cliente, data, orcamento);

        //Executamos alguma regra de négocio.
        System.out.println("Salvar Pedido no Banco de Dados.");
        System.out.println("Enviar e-mail com dados donovo pedido.");
    }
}
```

Nosso exemplo é um pouco diferente do que foi comentado, aqui toda a regra de negócio está no cliente, o que é pior inda. Para resolver isso, vamos criar uma classe que será a responsável por gerar o pedido e controlar a execução das rotinas.

```java
public class GeraPedido {
    
    private String cliente;
    private BigDecimal valorOrcamento;
    private Integer quantidadeItens;

    //Injeção de dependências: PedidoRepository, EmailService,... Polui.
    public GeraPedido(String cliente, BigDecimal valorOrcamento, Integer quantidadeItens) {
        this.cliente = cliente;
        this.valorOrcamento = valorOrcamento;
        this.quantidadeItens = quantidadeItens;
    }

    public void executa(){
        Orcamento orcamento = new Orcamento(this.valorOrcamento, this.quantidadeItens); 
        Pedido pedido = new Pedido(this.cliente, LocalDateTime.now(), orcamento);

        System.out.println("Salvar Pedido no Banco de Dados.");
        System.out.println("Enviar e-mail com dados donovo pedido.");
    }
}
```

Essa poderia ser uma das classes **Concretes Commandes**, só que ela não está tão legal assim. Ela faz muita coisa, tem os dados do pedido, recebe uma referência à classe que salvará o pedido e outra para uma que enviará o e-mail. “Tá”, vamos remover toda essa lógica dela, e deixá-la tratando somente dos parâmetros do pedido.

```java
public class GeraPedido {
    
    private String cliente;
    private BigDecimal valorOrcamento;
    private Integer quantidadeItens;

    public GeraPedido(String cliente, BigDecimal valorOrcamento, Integer quantidadeItens) {
        this.cliente = cliente;
        this.valorOrcamento = valorOrcamento;
        this.quantidadeItens = quantidadeItens;
    }

    public String getCliente() {
        return cliente;
    }

    public BigDecimal getValorOrcamento() {
        return valorOrcamento;
    }

    public Integer getQuantidadeItens() {
        return quantidadeItens;
    }   
}
```

E quem irá executar a lógica de negócio será outra classe. Note que ela consegue criar o **Orcamento**, o **Pedido**, e executar algumas rotinas, e possui somente o método execute.

```java
public class GeraPedidoHandler {
    
    //Construtos com injeção de dependências.
    
    public void execute(GeraPedido dados){
        Orcamento orcamento = new Orcamento(dados.getValorOrcamento(), dados.getQuantidadeItens()); 
        Pedido pedido = new Pedido(dados.getCliente(), LocalDateTime.now(), orcamento);

        System.out.println("Salvar Pedido no Banco de Dados.");
        System.out.println("Enviar e-mail com dados donovo pedido.");
    }
}
```

E o cliente, como fica? Muito complicado, veja só

```java
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
```

## Observer

E estamos chegando ao fim dessa breve jornada, muita coisa aconteceu até aqui. Muitas ideias foram surgindo. Muita vontade de colocar tudo o que aprendi aqui em meus projetos. Bora falar sobre **Observer**, creio que já usei esse role em algum outro treinamento.

Esse padrão cria um mecanismo de assinatura, onde vários objetos podem ser notificados de qualquer evento que acontece com o objeto que estão observando.

Você precisa se comunicar com uma API, para saber se um determinado usuário concluiu um determinado curso, faz um *robô* que fica perguntando toda hora à API se o curso foi concluído. Essa API é de uma escola, várias pessoas precisam da mesma informação, sobre outros alunos, ela resolver desenvolver um *robô* que comunica todo mundo sobre o estatus dos alunos matriculados.

Seu problema antes era o gasto de ter que ficar verificando toda hora se o aluno fez ou não o curso, agora você, e um monte de outras pessoas, recebem informações sobre todos os alunos. Os dois perderam.

Vamos definir ***publicador e assinantes***. **Publicador** são objetos que possuem um estado, e notifica outros objetos sobre a mudança desse estado. Os objetos que querem saber dessa alteração são os **Assinantes**.

Nosso padrão cria um mecanismo de assinatura no **Publicador**, uma lista de **Assinantes**, eles podem entrar ou sair a hora que quiser. Sempre que algo acontecer o **Publicador** notificará os **Assinantes** utilizando um método padrão a todos eles. Por isso que os **assinantes** devem implementar a mesma interface.

Outra coisa que pode ser feita é uma interface para os **publicadores**, isso desacopla nossos **assinantes**, eles ficam independentes.

Em nosso exemplo, o **Assinante** poderia dizer a escola que quer ser notificado sempre a nota de um determinado aluno sair, ela pode fazer isso para vários alunos, e pedir a nota dele para várias escolas.

Tudo parece complicado de mais, não tanto quanto o **Command**, bem vamos imaginar como as coisas devem acontecer. Teremos nosso **Publisher**, sim agora será com nomes em inglês, ele, como já dito, é responsável por notificar objetos interessados as alterações que ele sofreu, ou quando executa alguma rotina, ele possui um estrutura que permite a inscrição e remoção de **Subscribers**.

Quando algo de interessante acontece, o **Publisher** percorre a lista de **Subscribers** chamando o método de notificação, esse método é definido em uma interface. Na maioria das vezes, a interface contém um método de execução que recebe os parâmetros necessários para lidar com ela.

Os **Concrete Subscribers**, como esperado, implementa a interface **Subscriber**, e realizam o que desejarem. Eles precisam de algumas informações de contexto para lidar com a requisição, o **Publisher** é quem deve indicar esse contexto, em alguns casos, ela passa a sí mesma como argumento.

Ao **Client** basta instanciar o **Publisher**, os **Concrete Subscribers**, os registrar e esperar a mágica acontecer.

Como de praxe, vamos ao nosso sistema de pedidos. Lembre-se que nosso **GerarPedidoHandler** grava o pedido no banco e envia um e-mail, isso não está legal, afinal ele pode fazer mais coisas no futuro, e acabará crescendo demasiada e descontroladamente. Vamos transformá-lo em um **Publisher**.

Para isso, iniciamos com a criação da interface.

```java
public interface AcoeAposGerarPedido {
    public void executarAcao(Pedido pedido);
}
```

Tão simples quanto isso, agora as nossas ações, são duas até então.

```java
public class EnviarEmailPedido implements AcoeAposGerarPedido{
    @Override
    public void executarAcao(Pedido pedido){
        System.out.println("Enviar e-mail com dados do novo pedido." + pedido.toString());
    }
}

public class GravarBancoPedido implements AcoeAposGerarPedido{
    @Override
    public void executarAcao(Pedido pedido){
        System.out.println("Salvar Pedido no Banco de Dados." + pedido.toString());
    }
}
```

Agora basta fazer algumas alterações na classe **GerarPedidoHandler**.

```java
public class GeraPedidoHandler {
    
    //Lista vinculada a interface
    private List<AcoeAposGerarPedido> acoes;
    
    //Receber os Concret Subscribers no construtor
    public GeraPedidoHandler(List<AcoeAposGerarPedido> acoes) {
        this.acoes = acoes;
    }

    //Método que cria o medido
    public void execute(GeraPedido dados){
        Orcamento orcamento = new Orcamento(dados.getValorOrcamento(), dados.getQuantidadeItens()); 
        Pedido pedido = new Pedido(dados.getCliente(), LocalDateTime.now(), orcamento);

        //após a criaçao, notifica os Concret Subscribers
        acoes.forEach(a -> a.executarAcao(pedido));
        
    }
}
```

Parece ser complicado, mas não é tanto.

## Referências

[REFACTORING GURU - Strategy](https://refactoring.guru/pt-br/design-patterns/strategy)

[REFACTORING GURU - Chain of Responsability](https://refactoring.guru/pt-br/design-patterns/chain-of-responsibility)

[REFACTORING GURU - Template Method](https://refactoring.guru/design-patterns/template-method)

[REFACTORING GURU - State](https://refactoring.guru/pt-br/design-patterns/state)

[REFACTORING GURU - Command](https://refactoring.guru/pt-br/design-patterns/command)

[REFACTORING GURU - Observer](https://refactoring.guru/pt-br/design-patterns/observer)

[RICARDO KERSCHBAUMER - INTRODUÇÃO À MÁQUINA DE ESTADO FINITA EM LIGUAGEM C](https://professor.luzerna.ifc.edu.br/ricardo-kerschbaumer/wp-content/uploads/sites/43/2021/04/Maquinas-de-Estado.pdf)

[TUTORIALPOINT - DESIGN PATTERN](https://www.tutorialspoint.com/design_pattern/index.htm)

[BETHROBSON - HEAD FIRST DESIGN PATTERNS](https://github.com/bethrobson/Head-First-Design-Patterns/tree/master/src/headfirst/designpatterns)

[GEEKSFORGEEKS - INTRODUCTION TO PATTERN DESIGNING](https://www.geeksforgeeks.org/introduction-to-pattern-designing/?ref=leftbar-rightbar)

[DOVMIR - AWESOME DESIGN-PATTERNS](https://github.com/DovAmir/awesome-design-patterns)
