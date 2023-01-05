# SOLID Com Java: Principios Da Programação Orientada A Objetos

## Orientação a Objetos

O SOLID (**S**ingle Responsibility Principle; **O**pen Closed Principle; **L**iskov Substitution Principle, **I**nterface Segregation Principle, **D**ependency Inversion Principle) possui uma relação interessante com a programação orientada a objetos. Nelas ouvimos sempre os mesmos principios: **Coesão, encapsulamento e acoplamento**. É interessante começar o estudo por esses pontos.

**COESÃO:**

Eu traduziria isso como: **faça somente uma coisa e muito bem.** Já o dicionário nós daria a seguinte definição

>União Harmônica entra uma coisa e outra.

Traduzindo tudo isso para o nosso mundo seria algo como ter uma classe que possui atributos e métodos que fazem sentido estarem nela, que representam coisas comuns à ela. Faz sentido uma classe `Funcionario` abrir uma conexão com o banco para gravar seus dados? Ou conter em seus atributos os dados de endereço?

Nesse segundo caso eu lembro das aulas de banco de dados onde usavamos o **Ns** da vida, aquelas normalizações lembra? Enfim, lá já não podemos usar endereço em cadastro de pessoas [1], vamos continuar com esse conceito aqui.

O primeiro caso creio ser autoevidente, caso não tenha cido, imagine ter que reescever o código de conexão com o banco toda vez que precisar salvar algo nele, **tenso**.

Um sintoma que indica uma classe não-coesa seria o seu tamanho, quanto maior ela for, maiores são as chances de que ela esteja fazendo algo que não deva.

**ENCAPSULAMENTO:**

Quanto mais escondido melhor. Bem, além da classe fazer muito bem somente um coisa, ela deve ocultar a implementação dessa coisa de que a está usando. O dicionário efine encapsular da seguinte forma:

>Incluir ou proteger alguma coisa em uma cápsula.

Em tradução livre para o mundo de desenvolvimento: proteger, blidar uma classe contra manipulações externas. E aqui não estamos querendo dizer deixar o atributos privados e escrever *getters e setters*. Imagina um classe de conexão com o bando de dados com o método `setBanco(String nomeBanco)`, pode fazer sentido em algum lugar, não para a classe **Funcionario**.

O encapsulamento, dentre outras coisas, visa proteger as regras de negócio, a classe que está utilizando-se de outra não deve saber o porque ela fez algo, só que ela fez, e além disso ter visibilidade somente aquilo que faz sentido para ela.

**ACOPLAMENTO:**

Só para não fugir do padrão, segundo o dicionário:

>Ação de acoplar. Agrupamento ao pares.

Por obvio que acoplamento nem sempre é ruim, se uma classe sabe fazer somente o que lhe cabe, de onde vem as demais funcionalidades que ele precisa? De outras classes. O ideal é controlar o acoplamento, via de regra quanto menos melhor. Uma classe não deve conhecer detalhes de mais de outra, por isso do escapsulamntos, se boa parte estiver escondida o acoplamento será baixo.

Uma forma de perceber o alto acoplamento são os problemas causados quando alteramos uma classe, meio que todas as outras ficam sabendo, pois irão quebrar. Um exemplo seria, uma classe retorna uma `List` com valores da fatura, para saber o total da fatura outra classe faz um `for` para calcular. Até ai "tranks", mas a primeira classe não retorna mais uma `List` e sim um `HashMap`, meio que deu ruim. Seria muito interessante se existisse o método `getValorFatura()` nela.

## Melhorando a Coesão

Para iniciar imagine, que dentre outras coisas, nossa classe **Funcionario** contenha a seguinte validação:

```java
public void reajustarSalario(BigDecimal aumento) {
    BigDecimal percentualReajuste = aumento.divide(salario, RoundingMode.HALF_UP);
    if (percentualReajuste.compareTo(new BigDecimal("0.4")) > 0) {
        throw new ValidacaoException("Reajuste nao pode ser superior a 40% do salario!");
    }
    this.salario = this.salario.add(aumento);
    this.dataUltimoReajuste = LocalDate.now();
}
```

À principio pode parecer que está tudo certo, afinal salário é algo do funcionário e calcular seu reajustes deve ser obrigação da classe **Funcionario**. Mas veja, a primeira coisa que vimos foi a **coesão**, *uma classe deve saber fazer muito bem uma coisa*, a coisa que essa classe deveria saber fazer é conter as informações do funcionário, e não ficar validando regras de negócio.

Vamos refatorar o role; podemos usar a **Extrair Classe**, de método à classe, e criar a classe **ReajusteService**.

```java
public class ReajusteService {
public void reajustarSalarioDoFuncionario(Funcionario funcionario, BigDecimal aumento){
    BigDecimal salarioAtual = funcionario.getSalario();
    BigDecimal percentualReajuste = aumento.divide(salarioAtual, RoundingMode.HALF_UP);
    if (percentualReajuste.compareTo(new BigDecimal("0.4")) > 0) {
        throw new ValidacaoException("Reajuste nao pode ser superior a 40% do salario!");
    }
    BigDecimal salarioReajustado = salarioAtual.add(aumento);
    funcionario.atualizarSalario(salarioReajustado);
}
}
```

De tal forma que nossa classe **Funcionario** passaria a ter o método:

```java
public void atualizarSalario(BigDecimal novoSalario) {
    this.salario = novoSalario;
    this.dataUltimoReajuste = LocalDate.now();
}
```

>Para mim ainda ficou faltando alguma coisa nesse processo, como estou no começo do curso darei o braço a torcer.

De qualquer forma, já começou a ficar melhor do que antes. Agora essa parte do código está mais clara e de fácil entendimento.

E chegamos a segunda aula desse módulo, realmente ficou faltando algo, será resolvido quando avançarmos nas letras. Acabamos de aplicar o **S**, Single Responsibility Principle, só porque você pode não signifia que deva. Só porque a classe funcionário pode calcular o seu reajuste não quer dizer que ela deva.

Achei algo interresante nessa aula, uma classe deveria ter apenas um único motivo para mudar. Esse motivo deve estar alinhado com o ser dela. Creio que isso resume muito bem o **S**, até porque isso está de acordo com o ser da coisas, olhando por um lado filosófico, para que algo tenha uma forma ela deve se limitar, ter limiter bem definidos. Esse é o ponto, uma classe deve ter limites que definem o que ela é, **Funcionario**: "eu sou uma classe que contém as informações de cadastro do funcionário", só isso, **ReajusteService**: "eu sou uma classe que controla o reajuste salárial", só isso. Veja como agora temos uma forma, a classe é bem definida, essa, talvez, seja uma forma mais fácil de pensar no **S**.

## Reduzindo o Acoplamento

Agora o código vai evoluir bastante, iremos implementar uma nova regra de reajuste, será uma que avaliará o quando o funcionário recebeu o último reajuste, ele não pode receber reajustes caso tenha recebido um a menos de 6 meses.

Antes de sair escrevendo código, vamos relembrar um ponto que comentamos:

>Uma classe deve ter um único motivo para ser alterada.

Criar uma nova regra de reajustes em **ReajusteService**, fere essa regra? Aparentemente não. Segue esse raciocinio: Ao fazer isso a classe **ReajusteService** poderá ser alterada quando: mudar a regra para o percentual máximo de reajuste ou quando alterar a restrição de 6 meses. Viu dois motivos.

E é por isso que criarei duas classes, uma para cada regra. E irei além ainda. Parece ser intuitivo que para validar qualquer regra de reajuste, a classes que a fará, deve receber um funcionario e o valor do aumento. Ora, é evidente que podemos criar uma interface que define quais métodos as classes de reajustes devem ter, e é por aqui que começaremos. Cabendo as classes de regras a materialização de tal.

Nossa inteface será bem simple, seria o que podemos chamar de [inteface funcional](https://br.sensedia.com/post/functional-interfaces-java-8), entenderemos o porque daqui à pouco.

```java
public interface ValidacaoReajuste {    
    void validar(Funcionario funcionario, BigDecimal aumento);
}
```

Basta criar as classes com a regra de cada caso implementando nossa interface.

```java
public class ValidacaoPercentualReajuste implements ValidacaoReajuste {
 
    @Override
    public void validar(Funcionario funcionario, BigDecimal aumento){
        BigDecimal salarioAtual = funcionario.getSalario();
        BigDecimal percentualReajuste = aumento.divide(salarioAtual, RoundingMode.HALF_UP);
        if (percentualReajuste.compareTo(new BigDecimal("0.4")) > 0) {
            throw new ValidacaoException("Reajuste nao pode ser superior a 40% do salario!");
        }
    }
}

public class ValidacaoPeridiocidadeEntreReajustes implements ValidacaoReajuste {
    
    @Override
    public void validar(Funcionario funcionario, BigDecimal aumento){
        LocalDate dataUltimoReajuste = funcionario.getDataUltimoReajuste();
        LocalDate dataAtual = LocalDate.now();
        //ChronoUnit gostei desse cara aqui, não tinha visto antes, quero estudar mais sobre ele
        long mesesDesdoUltimoReajuste = ChronoUnit.MONTHS.between(dataUltimoReajuste, dataAtual);
        if(mesesDesdoUltimoReajuste < 6){
            throw new ValidacaoException("Intervalo entre reajustes menor do que 6 meses.");
        }
    }
}
```

E a mágica vai acontecer agora, mostrarei a **ReajusteService**, depois comento.

```java
public class ReajusteService {

    private List<ValidacaoReajuste> validacoes;
    
    public ReajusteService(List<ValidacaoReajuste> validacoes) {
        this.validacoes = validacoes;
    }
    
    public void reajustarSalarioDoFuncionario(Funcionario funcionario, BigDecimal aumento){        
        this.validacoes.forEach(v -> v.validar(funcionario, aumento));

        BigDecimal salarioReajustado = funcionario.getSalario().add(aumento);
        funcionario.atualizarSalario(salarioReajustado);
    }
}
```

A primeira coisa que podemos notar é o atributo `List<ValidacaoReajuste> validacoes`, iremos usar as vantagens do polimorfismo. Ao instânciar essa classe ela receberá uma lista com as validações que deve realizar. Olhemos ao método `reajustarSalarioDoFuncionario`, olha que legal o que da para fazer, lembra que criamos uma interface funcional? Bem, ela permite que façamos algo assim: `this.validacoes.forEach(v -> v.validar(funcionario, aumento));` usar uma *lambda*, "toper".
Se tudo der certo, o funcionário receberá o reajuste.

Olha o que ganhamos com essa brincadeira toda, ao implementar uma nova regra para reajuste não precisaremos mexer no código existente, somente criar uma nova classe de validação impementando a interface **ValidacaoReajuste**, e incluí-la na `List` que será passada à **ReajusteService**. Somente eu que estou impressionado aqui?

E acabamos de ver o **O**, a classe ficou extencivel sem adicionar código. A cada nova implemntação não precisamos abrir a classe, somente criar outra, com isso não mexeremos em código existente.

Segue o **O** de forma mais técnica:

>Entidades de software devem estar abertas par aextensão, porém fechadas para modificação quanto menos mexer em uma classe melhor.

## Referências

[Normalizando um banco de dados por meio das 3 principais formas](https://spaceprogrammer.com/bd/normalizando-um-banco-de-dados-por-meio-das-3-principais-formas/)
[Princípios S.O.L.I.D: o que são e porque projetos devem utilizá-los](https://mari-azevedo.medium.com/princ%C3%ADpios-s-o-l-i-d-o-que-s%C3%A3o-e-porque-projetos-devem-utiliz%C3%A1-los-bf496b82b299)
