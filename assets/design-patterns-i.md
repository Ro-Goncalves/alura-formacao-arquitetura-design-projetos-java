# Design Patterns em Java I: Boas Práticas de Programação

Existem 23 padrões, divididos em criacionais, estruturais e comportamentais

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

## Template Method

## State

## Command

## Observer
