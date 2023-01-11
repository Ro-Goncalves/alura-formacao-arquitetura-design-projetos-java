# SOLID Com Java: Princípios Da Programação Orientada A Objetos

E aqui vamos ao meu primeiro contato com as coisas elevadas da programação. Gostei muito de estudar SOLID, me respondeu várias perguntas e trouxe outras tantas, mais trouxe do que respondeu. Acho que estou em um bom caminho.

A cada passo que eu dou, me adentrando nos cantos mais obscuros desse universo, fico fascinado com tudo o que vejo, espero um dia chegar à porta daqueles que são realmente grandes.

## Orientação a Objetos

O SOLID (**S**ingle Responsibility Principle; **O**pen Closed Principle; **L**iskov Substitution Principle, **I**nterface Segregation Principle, **D**ependency Inversion Principle) possui uma relação interessante com a programação orientada a objetos. Nelas ouvimos sempre os mesmos princípios: **Coesão, encapsulamento e acoplamento**. É interessante começar o estudo por esses pontos.

**COESÃO:**

Eu traduziria isso como: **faça somente uma coisa e muito bem.** Já o dicionário nós dá a seguinte definição

>União Harmônica entra uma coisa e outra.

Traduzindo para o nosso mundo, seria algo como ter uma classe que possui atributos e métodos que fazem sentido estarem nela, que representam coisas comuns a ela. Faz sentido uma classe **Funcionario** abrir uma conexão com o banco para gravar seus dados? Ou conter em seus atributos os dados de endereço?

Nesse segundo caso eu lembro das aulas de banco de dados onde usávamos o **Ns** da vida, aquelas normalizações lembram? Enfim, lá já não podemos usar endereço em cadastro de pessoas, vamos continuar com esse conceito aqui.

O primeiro caso creio ser auto evidente, caso não tenha sido, imagine ter que reescrever o código de conexão com o banco toda vez que precisar salvar algo nele, **tenso**.

Um sintoma que indica uma classe não-coesa seria o seu tamanho, quanto maior ela for, maiores são as chances de que ela esteja fazendo algo que não deva.

**ENCAPSULAMENTO:**

Quanto mais escondido melhor. Bem, além da classe fazer muito bem somente uma coisa, ela deve ocultar a implementação dessa coisa de que a está usando. O dicionário define encapsular da seguinte forma:

>Incluir ou proteger alguma coisa em uma cápsula.

Em tradução livre para o mundo de desenvolvimento: proteger, blindar uma classe contra manipulações externas. E aqui não estamos querendo dizer deixar os atributos privados e escrever *getters e setters*. Imagina um classe de conexão com o bando de dados com o método `setBanco(String nomeBanco)`, pode fazer sentido em algum lugar, não para a classe **Funcionario**.

O encapsulamento, dentre outras coisas, visa proteger as regras de negócio, a classe que está utilizando-se de outra não deve saber o porquê ela fez algo, só que ela o fez, e além disso ter visibilidade somente aquilo que faz sentido para ela.

**ACOPLAMENTO:**

Só para não fugir do padrão, segundo o dicionário:

>Ação de acoplar. Agrupamento aos pares.

Por obvio que acoplamento nem sempre é ruim, se uma classe sabe fazer somente o que lhe cabe, de onde vem as demais funcionalidades que ele precisa? De outras classes. O ideal é controlar o acoplamento, geralmente quanto menos melhor. Uma classe não deve conhecer detalhes de mais de outra, por isso do encapsulamento, se boa parte estiver escondida o acoplamento será baixo.

Uma forma de perceber o alto acoplamento são os problemas causados quando alteramos uma classe, meio que todas as outras ficam sabendo, pois irão quebrar. Um exemplo seria, uma classe retorna uma `List` com valores da fatura, para saber o total da fatura outra classe faz um `for` para calcular. Até ai "tranks", mas,**** se a primeira classe não retorna mais uma `List` e sim um `HashMap`, meio que deu ruim. Seria muito interessante se existisse o método `getValorFatura()` nela.

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

À princípio pode parecer que está tudo certo, afinal salário é algo do funcionário e calcular seu reajustes deve ser obrigação da classe **Funcionario**. Mas, veja, a primeira coisa que vimos foi a **coesão**, *uma classe deve saber fazer muito bem uma coisa*, a coisa que essa classe deveria saber fazer é conter as informações do funcionário, e não ficar validando regras de negócio.

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

Achei algo interessante nessa aula, uma classe deveria ter apenas um único motivo para mudar. Esse motivo deve estar alinhado com o ser dela. Creio que isso resume muito bem o **S**, até porque isso está de acordo com o ser das coisas, olhando por um lado filosófico, para que algo tenha uma forma ele deve se limitar, ter limites bem definidos. Esse é o ponto, uma classe deve ter limites que definem o que ela é, **Funcionario**: "eu sou uma classe que contém as informações de cadastro do funcionário", só isso, **ReajusteService**: "eu sou uma classe que controla o reajuste salarial", só isso. Veja como agora temos uma forma, a classe é bem definida, essa, talvez, seja uma forma mais fácil de pensar no **S**.

## Reduzindo o Acoplamento

Agora o código vai evoluir bastante, iremos implementar uma nova regra de reajuste, será uma que avaliará quando o funcionário recebeu o último reajuste, ele não pode receber reajustes caso tenha recebido um a menos de 6 meses.

Antes de sair escrevendo código, vamos relembrar um ponto que comentamos:

>Uma classe deve ter um único motivo para ser alterada.

Criar uma regra de reajustes em **ReajusteService**, fere essa regra? Aparentemente não. Segue esse raciocínio: Ao fazer isso a classe **ReajusteService** poderá ser alterada quando: mudar a regra para o percentual máximo de reajuste ou, quando alterar a restrição de 6 meses. Viu, dois motivos.

E é por isso que criarei duas classes, uma para cada regra. E irei além ainda. Parece ser intuitivo que para validar qualquer regra de reajuste, a classes que a fará, deve receber um funcionário e o valor do aumento. Ora, é evidente que podemos criar uma interface que define quais métodos as classes de reajuste devem ter, e é por aqui que começaremos. Cabendo as classes de regras a materialização de tal.

Nossa interface será bem simples, seria o que podemos chamar de [interface funcional](https://br.sensedia.com/post/functional-interfaces-java-8), entenderemos o porque daqui a pouco.

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

A primeira coisa que podemos notar é o atributo `List<ValidacaoReajuste> validacoes`, iremos usar as vantagens do polimorfismo. Ao instanciar essa classe ela receberá uma lista com as validações que deve realizar. Olhemos ao método `reajustarSalarioDoFuncionario`, olha que legal o que dá para fazer, lembra que criamos uma interface funcional? Bem, ela permite que façamos algo assim: `this.validacoes.forEach(v -> v.validar(funcionario, aumento));` usar uma *lambda*, "toper".
Se tudo der certo, o funcionário receberá o reajuste.

Olha o que ganhamos com essa brincadeira toda, ao implementar uma nova regra para reajuste não precisaremos mexer no código existente, somente criar uma nova classe de validação implementando a interface **ValidacaoReajuste**, e incluí-la na `List` que será passada à **ReajusteService**. Somente eu que estou impressionado aqui?

E acabamos de ver o **O**, a classe ficou extensível sem adicionar código. A cada nova implementação não precisamos abrir a classe, somente criar outra, com isso não mexeremos em código existente.

Segue o **O** de forma mais técnica:

>Entidades de software devem estar abertas para extensão, porém fechadas para modificação quanto menos mexer em uma classe melhor.

## Herança Indesejada

E vamos avançando nas letras como um quebra-gelo no ártico, é a hora do **L**, um bom **L**, não um **L** escuso. Para tal, iremos criar uma regra de negócio, a promoção, quem não gosta de promoção, se o funcionário bater a meta, ele passará de cargo da seguinte forma **ASSISTENTE -> ANALISTA -> ESPECIALISTA -> GERENTE**.

Primeira coisa a se fazer? Criar uma classe de serviço que controlará isso para nós, **PromocaoService**. Veja, da para perceber que o *GERENTE* não tem promoção, e que a regra é *se bater a meta*, logo um booleano. Podemos criar algo parecido com:

```java
public void promover(Funcionario funcionario, boolean metaBatida){
    Cargo cargoAtual = funcionario.getDadosPessoais().getCargo();
    if(Cargo.GERENTE == cargoAtual){
        throw new ValidacaoException("Gerentes não podem ser promovidos!");
    }

    if(metaBatida){
        Cargo novoCargo = cargoAtual.getProximoCargo();
        funcionario.promover(novoCargo);
    } else {
        throw new ValidacaoException("Funcionário não bateu a meta!");
    }
}
```

Existem algumas coisas novas aqui, vamos por partes que chegaremos lá. Primeiro, quem dirá qual será o próximo cargo será o *enun* **Cargo**. Ao criarmos o método abstrato `getProximoCargo()`, o teremos que implementar em todos os atributos.

```java
public enum Cargo {

    ASSISTENTE {
        @Override
        public Cargo getProximoCargo() {
            return ANALISTA;
        }
    },
    ANALISTA {
        @Override
        public Cargo getProximoCargo() {
            return ESPECIALISTA;
        }
    },
    ESPECIALISTA {
        @Override
        public Cargo getProximoCargo() {
            return GERENTE;
        }
    },
    GERENTE {
        @Override
        public Cargo getProximoCargo() {
            return GERENTE;
        }
    };

    public abstract Cargo getProximoCargo();
}
```

Depois, basta criar o método `promover` no funcionário.

```java
public void promover(Cargo novoCargo) {
    this.dadosPessoais.setCargo(novoCargo);
}
```

Os mais expertos notaram que existe um método novo em **Funcionario** o `getDadosPessoais()`, acalme-te, vamos chegar nele, e será agora.

Bem, a nova regra foi implementada sem muita dor de cabeça, porém, vamos colocar duas colheres de caos no bolo, agora teremos a classe **Terceirizado**, ele terá basicamente os mesmo atributos do **Funcionario**, afinal ele meio que é um. Ficaremos tentados a fazer o seguinte:

```java
public class Terceirizado extends Funcionario{

    public Terceirizado(String nome, String cpf, Cargo cargo, BigDecimal salario) {
        super(nome, cpf, cargo, salario);
    }
}
```

Uma consequência disso é podemos fazer:

```java
Terceirizado t1 = new Terceirizado("Rodrigo", "123123", "ANALISTA", 5000);
t1.atualizarSalario(7000);
```

A pergunta é, eu posso aumentar o salário de um terceiro? Por obvio que não, logo nesse caso a herança mais atrapalha do que ajuda.

Veja só fizemos isso porque alguns atributos e métodos são comuns as duas, uma outra forma de resolver seria criar a classe **DadosPessoais** e dentro de **Terceiros e Funcionarios** ter um atributo que referência essa classe.

```java
public class DadosPessoais {
    
    private String nome;
    private String cpf;
    private Cargo cargo;
    private BigDecimal salario;

    public DadosPessoais(String nome, String cpf, Cargo cargo, BigDecimal salario) {        
        this.nome = nome;
        this.cpf = cpf;
        this.cargo = cargo;
        this.salario = salario;
    }
    //Os roles que faltam
}

public class Funcionario {

    private DadosPessoais dadosPessoais;
    private LocalDate dataUltimoReajuste;

    public Funcionario(String nome, String cpf, Cargo cargo, BigDecimal salario) {
        dadosPessoais = new DadosPessoais(nome, cpf, cargo, salario);
    }
    //Os roles que faltam
}

public class Terceirizado {

    DadosPessoais dadosPessoais;
    private String empresa;

    public Terceirizado(String nome, String cpf, Cargo cargo, BigDecimal salario) {
        this.dadosPessoais = new DadosPessoais(nome, cpf, cargo, salario);
    }
    //Os roles que faltam
}
```

Agora temos privilégio a algo chamado composição, removendo assim a herança de métodos que não fazem sentido a classe **Terceirizado**.

O **L**, Liskov Substitution Principle, diz exatamente isso: **Se algo parece bosta, tem cheiro de bosta, gosto de bosta, mas não se comporta como uma bosta, ele não é um bosta**. Formalmente:

>Se q(x) é uma propriedade demonstrável dos objetos x de tipo T, então q(y) deve ser verdadeiro para objetos y do tipo S, onde S é um subtipo de T".

## Trabalhando Com Abstrações

Em nossas andanças, já implementamos uma outra letra, a **D** *Dependency Inversion Principle*. é um tanto quanto interessante essa parte, pois devemos depender de interfaces e não de classes concretas.

Bem em uma análise superficial fica fácil de identificar o porquê, as classes concretas tentem a sofre mais mudanças do que uma interface. Por exemplo, ao criar um objeto que representará um `ArrayList` e interessante que o seu tipo seja `List`, pois se em algum momente a classe que retorna o `ArrayList` retornar um outro tipo de `List`, a classe que a usou não sofrerá com tal mudança.

Alcançamos esse objetivo quando criamos interfaces que definirão como as implementações devem se comportar.

É interessante notar que, ao tentar aplicar o **O**, meio que, acabamos implementando o **S** e o **D**

Bem vindo ao fim das letras, o fake-fim ao quadrado, do nosso estudo sobre S.O.L.I.D.. **I**, *Interface Segregation Principle*, menos é mais ao contrário: *"muitas interfaces específicas são melhores do que uma interface geral"*. Meio que o mesmo do **S** das classes, só que em interfaces.

Se uma classe ao estender uma interface recebe tem que implementar métodos que não fazem sentido, tem algo de errado aí. Só não vamos sair criando trocentas interfaces com um método cada, não é disso que estamos falando.

## Referências

[Normalizando um banco de dados por meio das 3 principais formas](https://spaceprogrammer.com/bd/normalizando-um-banco-de-dados-por-meio-das-3-principais-formas/)

[Princípios S.O.L.I.D: o que são e porque projetos devem utilizá-los](https://mari-azevedo.medium.com/princ%C3%ADpios-s-o-l-i-d-o-que-s%C3%A3o-e-porque-projetos-devem-utiliz%C3%A1-los-bf496b82b299)

[Julio Falbo - Minha Visão De Desenvolvedor Sobre Domain Driven Design DDD](https://julio-falbo.medium.com/minha-vis%C3%A3o-de-desenvolvedor-sobre-domain-driven-design-ddd-841afbe2fbc7)
