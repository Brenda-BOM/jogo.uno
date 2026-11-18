UNO SIMPLIFICADO CONTRA CPU - PROJETO JAVA PARA NETBEANS

REQUISITOS
- JDK 17 ou superior.
- NetBeans com suporte a projetos Maven.

COMO ABRIR
1. Baixe e extraia o arquivo ZIP.
2. Abra o NetBeans.
3. Clique em Arquivo > Abrir Projeto.
4. Selecione a pasta UnoJavaNetBeans_Completo (a pasta que contém pom.xml).
5. Clique em Executar (Run).

COMO JOGAR
- Você joga contra o computador (CPU).
- MOUSE: clique em uma carta para selecioná-la e jogá-la.
- TECLADO: use as setas ESQUERDA/DIREITA para escolher uma carta.
- ENTER: joga a carta selecionada.
- ESPAÇO: compra uma carta.
- Também há botões na tela para jogar e comprar.
- Só é possível jogar uma carta que combine com a cor ou o número/símbolo da carta da mesa.
- CORINGA: ao jogar, escolha uma das quatro cores.
- +2: o adversário compra duas cartas e perde a vez.
- BLOQUEIO: o adversário perde a vez.
- INVERTER: com apenas dois jogadores, o adversário perde a vez.
- Vence quem ficar sem cartas primeiro.

ORGANIZAÇÃO DAS CLASSES (CADA UMA EM SEU PRÓPRIO ARQUIVO)
- Principal.java: ponto de entrada do programa.
- Carta.java: representa uma carta, sua cor e seu tipo.
- Baralho.java: cria, embaralha e fornece cartas.
- Jogador.java: guarda e administra as cartas de cada jogador.
- JogoUno.java: contém as regras, turnos, compras e vitória.
- JanelaUno.java: janela gráfica, botões, mouse e teclado.

AS VARIÁVEIS, MÉTODOS E NOMES DAS CLASSES ESTÃO EM PORTUGUÊS PARA FACILITAR A LEITURA.

CORREÇÃO: a janela do jogo agora é exibida automaticamente ao executar Principal.java.
