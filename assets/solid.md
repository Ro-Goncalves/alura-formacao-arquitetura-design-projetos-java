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

## Referências

[Normalizando um banco de dados por meio das 3 principais formas](https://spaceprogrammer.com/bd/normalizando-um-banco-de-dados-por-meio-das-3-principais-formas/)
[Princípios S.O.L.I.D: o que são e porque projetos devem utilizá-los](https://mari-azevedo.medium.com/princ%C3%ADpios-s-o-l-i-d-o-que-s%C3%A3o-e-porque-projetos-devem-utiliz%C3%A1-los-bf496b82b299)
