package br.com.uno;

import java.util.ArrayList;

public class JogoUno {
    private Baralho baralho;
    private final ArrayList<Carta> monteDescarte = new ArrayList<>();
    private Jogador pessoa;
    private Jogador computador;
    private Carta cartaDaMesa;
    private Carta.Cor corAtual;
    private boolean vezDaPessoa;
    private boolean terminou;
    private String mensagem;

    public JogoUno() {
        novoJogo();
    }

    public void novoJogo() {
        baralho = new Baralho();
        monteDescarte.clear();
        pessoa = new Jogador("Você", false);
        computador = new Jogador("CPU", true);
        vezDaPessoa = true;
        terminou = false;
        mensagem = "Sua vez! Escolha uma carta que combine com a mesa.";

        for (int i = 0; i < 7; i++) {
            pessoa.receberCarta(comprarDoBaralho());
            computador.receberCarta(comprarDoBaralho());
        }

        cartaDaMesa = comprarDoBaralho();
        while (cartaDaMesa != null && cartaDaMesa.getTipo() != Carta.Tipo.NUMERO) {
            monteDescarte.add(cartaDaMesa);
            cartaDaMesa = comprarDoBaralho();
        }
        monteDescarte.add(cartaDaMesa);
        corAtual = cartaDaMesa.getCor();
    }

    public Jogador getPessoa() {
        return pessoa;
    }

    public Jogador getComputador() {
        return computador;
    }

    public Carta getCartaDaMesa() {
        return cartaDaMesa;
    }

    public Carta.Cor getCorAtual() {
        return corAtual;
    }

    public String getMensagem() {
        return mensagem;
    }

    public boolean ehVezDaPessoa() {
        return vezDaPessoa && !terminou;
    }

    public boolean terminou() {
        return terminou;
    }

    public int getQuantidadeNoBaralho() {
        return baralho.quantidadeCartas();
    }

    public boolean podeJogar(Carta carta) {
        if (carta == null || cartaDaMesa == null) {
            return false;
        }

        return carta.getTipo() == Carta.Tipo.CORINGA
                || carta.getCor() == corAtual
                || (carta.getTipo() == cartaDaMesa.getTipo()
                    && carta.getTipo() != Carta.Tipo.NUMERO)
                || (carta.getTipo() == Carta.Tipo.NUMERO
                    && cartaDaMesa.getTipo() == Carta.Tipo.NUMERO
                    && carta.getNumero() == cartaDaMesa.getNumero());
    }

    public boolean jogarCartaDaPessoa(int posicao, Carta.Cor corEscolhida) {
        if (!ehVezDaPessoa() || posicao < 0 || posicao >= pessoa.quantidadeCartas()) {
            return false;
        }

        Carta carta = pessoa.getMao().get(posicao);
        if (!podeJogar(carta)) {
            mensagem = "Carta inválida! Combine a cor ou o número.";
            return false;
        }

        carta = pessoa.jogarCarta(posicao);
        colocarNaMesa(carta);

        if (pessoa.estaSemCartas()) {
            terminou = true;
            mensagem = "PARABÉNS! Você venceu!";
            return true;
        }

        if (carta.getTipo() == Carta.Tipo.CORINGA) {
            corAtual = (corEscolhida == null || corEscolhida == Carta.Cor.CORINGA)
                    ? Carta.Cor.VERMELHO : corEscolhida;
        }

        if (carta.getTipo() == Carta.Tipo.MAIS_DOIS) {
            comprarPara(computador, 2);
            vezDaPessoa = true;
            mensagem = "Você jogou +2! A CPU comprou duas cartas. Jogue novamente.";
        } else if (carta.getTipo() == Carta.Tipo.BLOQUEIO
                || carta.getTipo() == Carta.Tipo.INVERTER) {
            vezDaPessoa = true;
            mensagem = "Você pulou a vez da CPU! Jogue novamente.";
        } else {
            vezDaPessoa = false;
            mensagem = "Sua carta foi jogada. Agora é a vez da CPU.";
        }
        return true;
    }

    public void comprarCartaParaPessoa() {
        if (!ehVezDaPessoa()) {
            return;
        }

        Carta carta = comprarDoBaralho();
        if (carta == null) {
            mensagem = "O baralho acabou. Tente jogar uma carta da sua mão.";
            return;
        }

        pessoa.receberCarta(carta);
        vezDaPessoa = false;
        mensagem = "Você comprou uma carta. Agora é a vez da CPU.";
    }

    public void jogarVezDaCpu() {
        if (terminou || vezDaPessoa) {
            return;
        }

        int posicaoEscolhida = -1;
        for (int i = 0; i < computador.quantidadeCartas(); i++) {
            if (podeJogar(computador.getMao().get(i))) {
                posicaoEscolhida = i;
                break;
            }
        }

        if (posicaoEscolhida == -1) {
            Carta cartaComprada = comprarDoBaralho();
            computador.receberCarta(cartaComprada);

            if (cartaComprada != null && podeJogar(cartaComprada)) {
                posicaoEscolhida = computador.quantidadeCartas() - 1;
            } else {
                vezDaPessoa = true;
                mensagem = "A CPU comprou uma carta e passou a vez. Sua vez!";
                return;
            }
        }

        Carta carta = computador.jogarCarta(posicaoEscolhida);
        colocarNaMesa(carta);

        if (computador.estaSemCartas()) {
            terminou = true;
            mensagem = "Fim de jogo! A CPU venceu.";
            return;
        }

        if (carta.getTipo() == Carta.Tipo.CORINGA) {
            corAtual = escolherCorDaCpu();
        }

        if (carta.getTipo() == Carta.Tipo.MAIS_DOIS) {
            comprarPara(pessoa, 2);
            vezDaPessoa = false;
            mensagem = "A CPU jogou +2! Você comprou duas cartas e perdeu a vez.";
        } else if (carta.getTipo() == Carta.Tipo.BLOQUEIO
                || carta.getTipo() == Carta.Tipo.INVERTER) {
            vezDaPessoa = false;
            mensagem = "A CPU bloqueou sua vez e jogará novamente.";
        } else {
            vezDaPessoa = true;
            mensagem = "Sua vez! Escolha uma carta.";
        }
    }

    private void colocarNaMesa(Carta carta) {
        cartaDaMesa = carta;
        monteDescarte.add(carta);
        if (carta.getTipo() != Carta.Tipo.CORINGA) {
            corAtual = carta.getCor();
        }
    }

    private Carta.Cor escolherCorDaCpu() {
        int[] quantidadePorCor = new int[4];

        for (Carta carta : computador.getMao()) {
            if (carta.getCor() != Carta.Cor.CORINGA) {
                quantidadePorCor[carta.getCor().ordinal()]++;
            }
        }

        Carta.Cor[] cores = {
            Carta.Cor.VERMELHO, Carta.Cor.AZUL,
            Carta.Cor.VERDE, Carta.Cor.AMARELO
        };

        Carta.Cor melhorCor = cores[0];
        for (Carta.Cor cor : cores) {
            if (quantidadePorCor[cor.ordinal()] > quantidadePorCor[melhorCor.ordinal()]) {
                melhorCor = cor;
            }
        }
        return melhorCor;
    }

    private void comprarPara(Jogador jogador, int quantidade) {
        for (int i = 0; i < quantidade; i++) {
            jogador.receberCarta(comprarDoBaralho());
        }
    }

    private Carta comprarDoBaralho() {
        if (baralho.estaVazio()) {
            if (monteDescarte.size() <= 1) {
                return null;
            }

            Carta cartaDoTopo = monteDescarte.remove(monteDescarte.size() - 1);
            ArrayList<Carta> cartasParaEmbaralhar = new ArrayList<>(monteDescarte);
            monteDescarte.clear();
            monteDescarte.add(cartaDoTopo);
            baralho.devolverCartas(cartasParaEmbaralhar);
        }
        return baralho.comprarCarta();
    }
}
