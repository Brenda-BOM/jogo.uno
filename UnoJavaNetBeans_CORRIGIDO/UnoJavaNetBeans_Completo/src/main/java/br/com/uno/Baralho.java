package br.com.uno;

import java.util.ArrayList;
import java.util.Collections;

public class Baralho {
    private final ArrayList<Carta> cartas = new ArrayList<>();

    public Baralho() {
        criarCartas();
        embaralhar();
    }

    private void criarCartas() {
        Carta.Cor[] cores = {
            Carta.Cor.VERMELHO, Carta.Cor.AZUL,
            Carta.Cor.VERDE, Carta.Cor.AMARELO
        };

        for (Carta.Cor cor : cores) {
            cartas.add(new Carta(cor, 0));

            for (int numero = 1; numero <= 9; numero++) {
                cartas.add(new Carta(cor, numero));
                cartas.add(new Carta(cor, numero));
            }

            for (int repeticao = 0; repeticao < 2; repeticao++) {
                cartas.add(new Carta(cor, Carta.Tipo.MAIS_DOIS));
                cartas.add(new Carta(cor, Carta.Tipo.BLOQUEIO));
                cartas.add(new Carta(cor, Carta.Tipo.INVERTER));
            }
        }

        for (int repeticao = 0; repeticao < 4; repeticao++) {
            cartas.add(new Carta(Carta.Cor.CORINGA, Carta.Tipo.CORINGA));
        }
    }

    public void embaralhar() {
        Collections.shuffle(cartas);
    }

    public Carta comprarCarta() {
        if (cartas.isEmpty()) {
            return null;
        }
        return cartas.remove(cartas.size() - 1);
    }

    public boolean estaVazio() {
        return cartas.isEmpty();
    }

    public int quantidadeCartas() {
        return cartas.size();
    }

    public void devolverCartas(ArrayList<Carta> cartasUsadas) {
        cartas.addAll(cartasUsadas);
        embaralhar();
    }
}
