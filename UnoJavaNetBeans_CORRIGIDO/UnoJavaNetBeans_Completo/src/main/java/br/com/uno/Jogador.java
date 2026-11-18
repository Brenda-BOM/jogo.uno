package br.com.uno;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Jogador {
    private final String nome;
    private final boolean computador;
    private final ArrayList<Carta> mao = new ArrayList<>();

    public Jogador(String nome, boolean computador) {
        this.nome = nome;
        this.computador = computador;
    }

    public String getNome() {
        return nome;
    }

    public boolean ehComputador() {
        return computador;
    }

    public List<Carta> getMao() {
        return Collections.unmodifiableList(mao);
    }

    public int quantidadeCartas() {
        return mao.size();
    }

    public void receberCarta(Carta carta) {
        if (carta != null) {
            mao.add(carta);
        }
    }

    public Carta jogarCarta(int posicao) {
        if (posicao < 0 || posicao >= mao.size()) {
            return null;
        }
        return mao.remove(posicao);
    }

    public boolean estaSemCartas() {
        return mao.isEmpty();
    }
}
